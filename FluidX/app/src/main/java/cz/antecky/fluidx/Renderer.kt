package cz.antecky.fluidx

import android.content.Context
import android.opengl.GLES20.glClearColor
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
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

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
    val dye: Field
    val divergence: Field
}

class MyRenderer(private val context: Context) : GLSurfaceView.Renderer, IRenderer {
    companion object {
        const val GRID_SIZE = 128
        const val JACOBI_ITERS = 5
        const val TIMESTEP = 0.002f
        const val CONDUCTIVITY = 0.1f
        const val VISCOSITY = 0.05f
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
        get() = TIMESTEP * VISCOSITY * (1.0f / (widthTexel * widthTexel) + 1.0f / (heightTexel * heightTexel))

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

    override val dye: Field by lazy {
        Field(halfFloatFormat, "u_dye")
    }

    override val divergence: Field by lazy {
        Field(halfFloatFormat, "u_divergence")
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
//        domain.advection(velocity, 1.001f, this)
//        velocity.update()
//
//        domain.advection(dye, 1.01f, this)
//        dye.update()
//
//        repeat(JACOBI_ITERS) {
//            domain.diffusion(this)
//            velocity.update()
//        }
//
//        domain.divergence(this)
//        divergence.update()
//
//        domain.clearField(pressure)
//
//        repeat(JACOBI_ITERS) {
//            domain.pressure(this)
//            pressure.update()
//        }
//
//        domain.subtractPressure(this)
//        velocity.update()


        repeat(JACOBI_ITERS) {
            domain.solveTemperature(this)
            temperature.update()
        }

        domain.temperatureDecay(this)
        temperature.update()

//        domain.display(Shader.SCREEN_VELOCITY, velocity, this)
//        domain.display(Shader.SCREEN_DIVERGENCE, divergence, this)
//        domain.display(Shader.SCREEN_DYE, dye, this)
        domain.display(Shader.SCREEN_TEMPERATURE, temperature, this)
//         domain.display(Shader.SCREEN_PRESSURE, pressure, this)

        // Log.d(this::class.qualifiedName, "onDrawFrame: time:$time")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Log.d(this::class.qualifiedName, "onSurfaceChanged: w:$width h:$height")
        this._width = width
        this._height = height
    }

    fun onTouch(s: Float, t: Float) {
//        domain.splash(
//            s, t, velocity,
//            Shader.SPLASH_VELOCITY,
//            this
//        )
//        domain.splash(
//            s, t, dye,
//            Shader.SPLASH_DYE,
//            this
//        )


        domain.touch(
            s, t, Shader.TOUCH_TEMPERATURE, temperature,
            this
        )

        //Log.d(this::class.qualifiedName, "onTouch: s:$s t:$t")
    }
}