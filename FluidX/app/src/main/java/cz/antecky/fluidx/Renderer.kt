package cz.antecky.fluidx

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import cz.antecky.fluidx.Utils.Companion.checkFramebuffer
import cz.antecky.fluidx.Utils.Companion.checkGlError
import cz.antecky.fluidx.Utils.Companion.halfFloatFormat
import cz.antecky.fluidx.Utils.Companion.printGlExtensions
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

    val textureSrc: Int
    val fboSrc: Int
    val textureDst: Int
    val fboDst: Int
}

class MyRenderer(private val context: Context) : GLSurfaceView.Renderer, IRenderer {
    companion object {
        const val GRID_SIZE = 128
        const val JACOBI_ITERS = 10

        // keep Courant number below 0.5 for an explicit method
        const val TIMESTEP = 0.00006f
        const val CONDUCTIVITY = 0.1f
    }

    private val entities: Array<Entity> by lazy {
        arrayOf(
            Domain(),
        )
    }
    private val domain: Domain get() = entities[0] as Domain

    private var startMillis: Long = 0
    private var _width: Int = -1
    private var _height: Int = -1

    private var _projectionM: FloatArray = FloatArray(16)
    private var _textureSrc: Int = -1
    private var _textureDst: Int = -1
    private var _fboSrc: Int = -1
    private var _fboDst: Int = -1

    private val halfFloatFormat: Int by lazy {
        halfFloatFormat()
    }

    override val time: Float get() = (SystemClock.uptimeMillis() - startMillis) / 1000.0f

    override val width: Int get() = _width
    override val widthTexel: Float get() = 1.0f / GRID_SIZE

    override val height: Int get() = _height
    override val heightTexel: Float get() = 1.0f / GRID_SIZE

    override val projectionM: FloatArray get() = _projectionM
    override val textureSrc: Int get() = _textureSrc
    override val fboSrc: Int get() = _fboSrc
    override val textureDst: Int get() = _textureDst
    override val fboDst: Int get() = _fboDst

    private val courantNumber: Float
        get() = TIMESTEP * CONDUCTIVITY * (1.0f / (widthTexel * widthTexel) + 1.0f / (heightTexel * heightTexel))

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

        checkFramebuffer()
        checkGlError()

        glClear(GL_COLOR_BUFFER_BIT)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        return frameBufferId
    }

    private fun swap() {
        _textureSrc = _textureDst.also { _textureDst = _textureSrc }
        _fboSrc = _fboDst.also { _fboDst = _fboSrc }
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        Log.d(this::class.qualifiedName, "onSurfaceCreated")
        printGlExtensions()
        Log.d(this::class.qualifiedName, "Courant: $courantNumber")

        Matrix.orthoM(
            _projectionM, 0,
            0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f
        )

        ShaderManager.compileAll(this.context)
        checkGlError()

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        startMillis = SystemClock.uptimeMillis()
    }

    override fun onDrawFrame(unused: GL10) {
        for (i in 1..JACOBI_ITERS) {
            domain.solveTemperature(i == JACOBI_ITERS, this)
            swap()
        }

        domain.draw(Shader.SCREEN, this)

        // Log.d(this::class.qualifiedName, "onDrawFrame: time:$time")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Log.d(this::class.qualifiedName, "onSurfaceChanged: w:$width h:$height")
        this._width = width
        this._height = height

        _textureSrc = createTexture()
        _fboSrc = createFrameBuffer(_textureSrc)
        _textureDst = createTexture()
        _fboDst = createFrameBuffer(_textureDst)
    }

    fun onTouch(s: Float, t: Float) {
        domain.touch(s, t, this)
        swap()

        //Log.d(this::class.qualifiedName, "onTouch: s:$s t:$t")
    }
}