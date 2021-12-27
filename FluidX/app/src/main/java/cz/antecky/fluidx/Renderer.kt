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
import cz.antecky.fluidx.entities.Quad
import cz.antecky.fluidx.entities.Triangle
import cz.antecky.fluidx.shaders.ShaderManager

interface IRenderer {
    val time: Float
    val width: Float
    val height: Float
    val projectionM: FloatArray
}

class Renderer(private val context: Context) : GLSurfaceView.Renderer, IRenderer {
    private lateinit var entities: Array<Entity>
    private var startMillis: Long = 0
    private var _width: Float = 0.0f
    private var _height: Float = 0.0f
    private var _projectionM: FloatArray = FloatArray(16)

    override val time: Float get() = (SystemClock.uptimeMillis() - startMillis) / 1000.0f
    override val width: Float get() = _width
    override val height: Float get() = _height
    override val projectionM: FloatArray get() = _projectionM

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        Matrix.orthoM(
            _projectionM, 0,
            0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f
        )

        ShaderManager.compileAll(this.context)
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