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
import cz.antecky.fluidx.shaders.ShaderManager
import java.nio.IntBuffer

interface IRenderer {
    val time: Float

    // screen width in pixels
    val width: Float
    val widthTexel: Float

    // screen height in pixels
    val height: Float
    val heightTexel: Float

    val projectionM: FloatArray
    val textureId: Int

    fun checkGlError()
}

class Renderer(private val context: Context) : GLSurfaceView.Renderer, IRenderer {
    companion object {
        const val GRID_SIZE = 256

        /*
        GL_EXT_color_buffer_half_float
        GL_OES_texture_half_float
        GL_OES_texture_half_float_linear
         */
        const val GL_HALF_FLOAT_OES = 0x8D61
    }

    private lateinit var entities: Array<Entity>
    private var startMillis: Long = 0
    private var _width: Float = 0.0f
    private var _height: Float = 0.0f
    private var _projectionM: FloatArray = FloatArray(16)
    private var _textureId: Int = -1
    private var frameBufferId: Int = -1

    override val time: Float get() = (SystemClock.uptimeMillis() - startMillis) / 1000.0f

    override val width: Float get() = _width
    override val widthTexel: Float get() = _width / GRID_SIZE

    override val height: Float get() = _height
    override val heightTexel: Float get() = _height / GRID_SIZE

    override val projectionM: FloatArray get() = _projectionM
    override val textureId: Int get() = _textureId

    private fun printGlExtensions() {
        val extensions = glGetString(GL_EXTENSIONS)
        Log.d(this::class.qualifiedName, "GL_EXTENSIONS: $extensions")
    }

    private fun initTexture() {
        glActiveTexture(GL_TEXTURE0)
        val textures: IntBuffer = IntBuffer.allocate(1)
        glGenTextures(1, textures)
        _textureId = textures[0]

        if (_textureId < 0) {
            Log.e(this::class.qualifiedName, "glGenTextures: GL_INVALID_VALUE")
            throw RuntimeException()
        }

        glBindTexture(GL_TEXTURE_2D, _textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, GRID_SIZE, GRID_SIZE, 0, GL_RGBA,
            GL_HALF_FLOAT_OES, null
        );
    }

    private fun initFrameBuffer() {
        val frameBuffers: IntBuffer = IntBuffer.allocate(1)

        glGenFramebuffers(1, frameBuffers)

        frameBufferId = frameBuffers[0]

        if (frameBufferId < 0) {
            Log.e(this::class.qualifiedName, "glGenFramebuffers: GL_INVALID_VALUE")
            throw RuntimeException()
        }

        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId)

        glFramebufferTexture2D(
            GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D, _textureId, 0
        )

        val status = glCheckFramebufferStatus(GL_FRAMEBUFFER);

        if (status != GL_FRAMEBUFFER_COMPLETE) {
            Log.e(this::class.qualifiedName, "glCheckFramebufferStatus: != GL_FRAMEBUFFER_COMPLETE")
            throw RuntimeException()
        }
    }

    override fun checkGlError() {
        val erno = glGetError()
        if (erno != GL_NO_ERROR) {
            Log.e(this::class.qualifiedName, "checkError: glGetError:$erno")
            throw RuntimeException()
        }
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // printGlExtensions()

        Matrix.orthoM(
            _projectionM, 0,
            0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f
        )

        ShaderManager.compileAll(this.context)
        checkGlError()

        initTexture()
        checkGlError()

        initFrameBuffer()
        checkGlError()

        this.entities = arrayOf(
            Domain(),
        )

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        startMillis = SystemClock.uptimeMillis()
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        glClear(GL_COLOR_BUFFER_BIT)

        for (entity in this.entities) {
            entity.draw(this)
        }

        // Log.d(this::class.qualifiedName, "onDrawFrame: time:$time")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        this._width = width.toFloat()
        this._height = height.toFloat()

        glViewport(0, 0, width, height)
        Log.d(this::class.qualifiedName, "onSurfaceChanged: w:$width h:$height")
    }
}