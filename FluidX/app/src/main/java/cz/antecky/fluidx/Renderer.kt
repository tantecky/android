package cz.antecky.fluidx

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import cz.antecky.fluidx.Utils.Companion.checkGlError
import cz.antecky.fluidx.Utils.Companion.halfFloatFormat
import cz.antecky.fluidx.Utils.Companion.printGlExtensions
import cz.antecky.fluidx.entities.Domain
import cz.antecky.fluidx.entities.Entity
import cz.antecky.fluidx.shaders.Shader
import cz.antecky.fluidx.shaders.ShaderManager

interface IRenderer {
    val time: Float

    // screen width in pixels
    val width: Int
    val widthTexel: Float

    // screen height in pixels
    val height: Int
    val heightTexel: Float

    val projectionM: FloatArray

    val temperature: Field
    val velocity: Field
    val pressure: Field
    val force: Field
}

class MyRenderer(private val context: Context) : GLSurfaceView.Renderer, IRenderer {
    companion object {
        const val GRID_SIZE = 128
        const val JACOBI_ITERS = 10

        // keep Courant number below 0.5 for an explicit method
        const val TIMESTEP = 0.00006f
        const val CONDUCTIVITY = 0.1f
        const val VISCOSITY = 0.1f
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

    private val halfFloatFormat: Int by lazy {
        halfFloatFormat()
    }

    private val courantNumber: Float
        get() = TIMESTEP * CONDUCTIVITY * (1.0f / (widthTexel * widthTexel) + 1.0f / (heightTexel * heightTexel))

    override val time: Float get() = (SystemClock.uptimeMillis() - startMillis) / 1000.0f

    override val width: Int get() = _width
    override val widthTexel: Float get() = 1.0f / GRID_SIZE

    override val height: Int get() = _height
    override val heightTexel: Float get() = 1.0f / GRID_SIZE

    override val projectionM: FloatArray get() = _projectionM

    override val temperature: Field by lazy {
        Field(halfFloatFormat, "u_temperature")
    }

    override val velocity: Field by lazy {
        Field(halfFloatFormat, "u_velocity")
    }

    override val pressure: Field by lazy {
        Field(halfFloatFormat, "u_pressure")
    }

    override val force: Field by lazy {
        Field(halfFloatFormat, "u_force")
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        Log.d(this::class.qualifiedName, "onSurfaceCreated")
        printGlExtensions()
        Log.d(this::class.qualifiedName, "halfFloatFormat: $halfFloatFormat")
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
        domain.solveVelocityNonFree(this)
        velocity.update()

        for (i in 1..JACOBI_ITERS) {
            domain.solvePressure(this)
            pressure.update()
        }
//
//        domain.solvePressureGrad(this)
//        pressure.update()
//
//        domain.solveVelocity(this)
//        velocity.update()

//        for (i in 1..JACOBI_ITERS) {
//            domain.solveVelocityNew(this)
//            velocity.update()
//        }

        for (i in 1..JACOBI_ITERS) {
            domain.solveTemperature(i == JACOBI_ITERS, this)
            temperature.update()
        }

        domain.display(Shader.SCREEN_TEMPERATURE, temperature, this)
        //domain.display(Shader.SCREEN_VELOCITY, velocity, this)

        // Log.d(this::class.qualifiedName, "onDrawFrame: time:$time")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Log.d(this::class.qualifiedName, "onSurfaceChanged: w:$width h:$height")
        this._width = width
        this._height = height
    }

    fun onTouch(s: Float, t: Float) {
        domain.touch(
            s, t, Shader.TOUCH_TEMPERATURE, temperature,
            this
        )
        domain.touch(
            s, t, Shader.TOUCH_FORCE, force,
            this
        )
        //Log.d(this::class.qualifiedName, "onTouch: s:$s t:$t")
    }
}