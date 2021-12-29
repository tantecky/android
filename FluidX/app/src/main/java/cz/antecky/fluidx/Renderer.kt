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

    // in texture coordinates [0; 1]
    val sTouch: Float
    val tTouch: Float

    val projectionM: FloatArray
    val textureId: Int


    fun checkGlError()
}

class MyRenderer(private val context: Context) : GLSurfaceView.Renderer, IRenderer {
    companion object {
        const val GRID_SIZE = 32

        /*
        GL_EXT_color_buffer_half_float
        GL_OES_texture_half_float
        GL_OES_texture_half_float_linear
         */
        const val GL_HALF_FLOAT_OES = 0x8D61
    }

    private lateinit var entities: Array<Entity>
    private var startMillis: Long = 0
    private var _width: Int = -1
    private var _height: Int = -1
    private var _sTouch: Float = -1.0f
    private var _tTouch: Float = -1.0f
    private var _projectionM: FloatArray = FloatArray(16)
    private var _textureId: Int = -1
    private var frameBufferId: Int = -1

    private val gotTouch: Boolean get() = _sTouch >= 0 && _tTouch >= 0

    override val time: Float get() = (SystemClock.uptimeMillis() - startMillis) / 1000.0f

    override val width: Int get() = _width
    override val widthTexel: Float get() = 1.0f / GRID_SIZE

    override val height: Int get() = _height
    override val heightTexel: Float get() = 1.0f / GRID_SIZE

    override val sTouch: Float get() = _sTouch
    override val tTouch: Float get() = _tTouch

    override val projectionM: FloatArray get() = _projectionM
    override val textureId: Int get() = _textureId

    private fun printGlExtensions() {
        val extensions = glGetString(GL_EXTENSIONS)
        Log.d(this::class.qualifiedName, "GL_EXTENSIONS: $extensions")
    }

    private fun isHalfFloatSupported(): Boolean {
        val extensions = glGetString(GL_EXTENSIONS)

        for (extension in arrayOf(
            "GL_EXT_color_buffer_half_float",
            "GL_OES_texture_half_float",
            "GL_OES_texture_half_float_linear",
        )) {
            if (!extensions.contains(extension)) {
                return false
            }
        }

        return true

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
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        // use GL_UNSIGNED_BYTE if GL_HALF_FLOAT_OES is not supported

        val gotHalfFloat = isHalfFloatSupported()

        val formatType = if (gotHalfFloat) GL_HALF_FLOAT_OES else GL_UNSIGNED_BYTE

        Log.d(this::class.qualifiedName, "isHalfFloatSupported: $gotHalfFloat")

        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, GRID_SIZE, GRID_SIZE, 0, GL_RGBA,
            formatType, null
        )
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    private fun initFrameBuffer() {
        val frameBuffers: IntBuffer = IntBuffer.allocate(1)

        glGenFramebuffers(1, frameBuffers)

        frameBufferId = frameBuffers[0]

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
            GL_TEXTURE_2D, _textureId, 0
        )

        val status = glCheckFramebufferStatus(GL_FRAMEBUFFER);

        if (status != GL_FRAMEBUFFER_COMPLETE) {
            Log.e(this::class.qualifiedName, "glCheckFramebufferStatus: != GL_FRAMEBUFFER_COMPLETE")
            throw RuntimeException()
        }

        glClear(GL_COLOR_BUFFER_BIT);
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
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

        for (entity in this.entities) {
            glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId)
            glViewport(0, 0, GRID_SIZE, GRID_SIZE)

            entity.draw(Shader.TEMPERATURE, this)

            if (gotTouch) {
                entity.draw(Shader.TOUCH, this)
            }


            glBindFramebuffer(GL_FRAMEBUFFER, 0)
            glViewport(0, 0, this._width, this._height)
            glClear(GL_COLOR_BUFFER_BIT)
            entity.draw(Shader.SCREEN, this)
        }

        // Log.d(this::class.qualifiedName, "onDrawFrame: time:$time")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Log.d(this::class.qualifiedName, "onSurfaceChanged: w:$width h:$height")
        this._width = width
        this._height = height

        initTexture()
        checkGlError()

        initFrameBuffer()
        checkGlError()
    }

    fun onTouch(s: Float, t: Float) {
        this._sTouch = s
        this._tTouch = t

        //Log.d(this::class.qualifiedName, "onTouch: s:$s t:$t")

    }
}