package cz.antecky.fluidx

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import cz.antecky.fluidx.entities.Domain
import cz.antecky.fluidx.entities.Entity
import cz.antecky.fluidx.shaders.Shader
import cz.antecky.fluidx.shaders.ShaderManager
import java.nio.IntBuffer

interface IRenderer {
    val time: Float

    // screen width in pixels
    val width: Int
    val widthTexel: Float

    // screen height in pixels
    val height: Int
    val heightTexel: Float

    val projectionM: FloatArray
    val textureId: Int
    val frameBufferId: Int

    val maxTimestep: Float

    fun checkGlError()
}

class MyRenderer(private val context: Context) : GLSurfaceView.Renderer, IRenderer {
    companion object {
        // keep Courant number below 0.5
        const val GRID_SIZE = 64
        const val CONDUCTIVITY = 0.05f
        const val COURANT_NUMBER = 0.45f

        /*
        GL_EXT_color_buffer_half_float
        GL_OES_texture_half_float
        GL_OES_texture_half_float_linear
         */
        const val GL_HALF_FLOAT_OES = 0x8D61

        /*
        GL_NV_half_float
         */
        const val GL_HALF_FLOAT_NV = 0x140B
    }

    private lateinit var entities: Array<Entity>
    private val domain: Domain get() = entities[0] as Domain
    private var startMillis: Long = 0
    private var _width: Int = -1
    private var _height: Int = -1

    private var _projectionM: FloatArray = FloatArray(16)
    private var _textureId: Int = -1
    private var _frameBufferId: Int = -1

    override val time: Float get() = (SystemClock.uptimeMillis() - startMillis) / 1000.0f
    override val maxTimestep: Float
        get() = COURANT_NUMBER / CONDUCTIVITY / (1.0f / (widthTexel * widthTexel) + 1.0f / (heightTexel * heightTexel))

    override val width: Int get() = _width
    override val widthTexel: Float get() = 1.0f / GRID_SIZE

    override val height: Int get() = _height
    override val heightTexel: Float get() = 1.0f / GRID_SIZE

    override val projectionM: FloatArray get() = _projectionM
    override val textureId: Int get() = _textureId
    override val frameBufferId: Int get() = _frameBufferId


    private fun printGlExtensions() {
        val extensions = glGetString(GL_EXTENSIONS)
        Log.d(this::class.qualifiedName, "GL_EXTENSIONS: $extensions")
    }

    private fun halfFloatFormat(): Int {
        val extensions = glGetString(GL_EXTENSIONS)

        if (arrayOf(
                "ANDROID_EMU",
            ).all { extensions.contains(it) }
        ) {
            return GL_HALF_FLOAT_NV
        }

        if (arrayOf(
                "GL_EXT_color_buffer_half_float",
                "GL_OES_texture_half_float",
                "GL_OES_texture_half_float_linear",
            ).all { extensions.contains(it) }
        ) {
            return GL_HALF_FLOAT_OES
        }

        return GL_UNSIGNED_BYTE
    }

    private fun createTexture(): Int {
        glActiveTexture(GL_TEXTURE0)
        val textures: IntBuffer = IntBuffer.allocate(1)
        glGenTextures(1, textures)
        val textureId = textures[0]

        if (textureId < 0) {
            Log.e(this::class.qualifiedName, "glGenTextures: GL_INVALID_VALUE")
            throw RuntimeException()
        }

        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        val halfFloatFormat = halfFloatFormat()

        Log.d(this::class.qualifiedName, "halfFloatFormat: $halfFloatFormat")

        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, GRID_SIZE, GRID_SIZE, 0, GL_RGBA,
            halfFloatFormat, null
        )
        checkGlError()
        glBindTexture(GL_TEXTURE_2D, 0)

        return textureId
    }

    private fun createFrameBuffer(textureToAttach: Int): Int {
        val frameBuffers: IntBuffer = IntBuffer.allocate(1)

        glGenFramebuffers(1, frameBuffers)

        val frameBufferId = frameBuffers[0]

        if (frameBufferId < 0) {
            Log.e(this::class.qualifiedName, "glGenFramebuffers: GL_INVALID_VALUE")
            throw RuntimeException()
        } else {
            Log.d(this::class.qualifiedName, "glGenFramebuffers: $frameBufferId")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId)
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        glFramebufferTexture2D(
            GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D, textureToAttach, 0
        )

        val status = glCheckFramebufferStatus(GL_FRAMEBUFFER)

        if (status != GL_FRAMEBUFFER_COMPLETE) {
            Log.e(this::class.qualifiedName, "glCheckFramebufferStatus: != GL_FRAMEBUFFER_COMPLETE")
            throw RuntimeException()
        }
        checkGlError()

        glClear(GL_COLOR_BUFFER_BIT)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        return frameBufferId
    }

    override fun checkGlError() {
        val erno = glGetError()
        if (erno != GL_NO_ERROR) {
            Log.e(this::class.qualifiedName, "checkError: glGetError:$erno")
            throw RuntimeException()
        }
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        Log.d(this::class.qualifiedName, "onSurfaceCreated")
        printGlExtensions()
        Log.d(this::class.qualifiedName, "timestep: $maxTimestep")

        Matrix.orthoM(
            _projectionM, 0,
            0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f
        )

        ShaderManager.compileAll(this.context)
        checkGlError()

        this.entities = arrayOf(
            Domain(),
        )

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        startMillis = SystemClock.uptimeMillis()
    }

    override fun onDrawFrame(unused: GL10) {
        domain.solveTemperature(this)
        domain.draw(Shader.SCREEN, this)

        // Log.d(this::class.qualifiedName, "onDrawFrame: time:$time")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Log.d(this::class.qualifiedName, "onSurfaceChanged: w:$width h:$height")
        this._width = width
        this._height = height

        _textureId = createTexture()
        _frameBufferId = createFrameBuffer(_textureId)
    }

    fun onTouch(s: Float, t: Float) {
        domain.touch(s, t, this)

        //Log.d(this::class.qualifiedName, "onTouch: s:$s t:$t")
    }
}