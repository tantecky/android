package cz.antecky.fluidx.shaders

import android.content.Context
import android.opengl.GLES20.glUseProgram
import android.util.Log
import cz.antecky.fluidx.R
import kotlin.system.measureTimeMillis

enum class Shader {
    FLAT,
    COMPLEX,
    TEMPERATURE,
    PRESSURE,
    SUBTRACT_PRESSURE,
    SCREEN_TEMPERATURE,
    SCREEN_VELOCITY,
    SCREEN_PRESSURE,
    SCREEN_DYE,
    SCREEN_DIVERGENCE,
    TOUCH_TEMPERATURE,
    ADVECTION,
    DIFFUSION,
    SPLASH_VELOCITY,
    SPLASH_DYE,
    DIVERGENCE,
}

object ShaderManager {
    private val shaders = mapOf(
        Shader.FLAT to ShaderProgram(R.raw.flat_vs, R.raw.flat_fs),
        Shader.COMPLEX to ShaderProgram(R.raw.flat_vs, R.raw.complex_fs),
        Shader.TEMPERATURE to ShaderProgram(R.raw.domain_vs, R.raw.temperature_fs),
        Shader.PRESSURE to ShaderProgram(R.raw.domain_vs, R.raw.pressure_fs),
        Shader.SUBTRACT_PRESSURE to ShaderProgram(R.raw.domain_vs, R.raw.subtract_pressure_fs),
        Shader.SCREEN_TEMPERATURE to ShaderProgram(R.raw.domain_vs, R.raw.screen_temperature_fs),
        Shader.SCREEN_VELOCITY to ShaderProgram(R.raw.domain_vs, R.raw.screen_velocity_fs),
        Shader.SCREEN_PRESSURE to ShaderProgram(R.raw.domain_vs, R.raw.screen_pressure_fs),
        Shader.SCREEN_DYE to ShaderProgram(R.raw.domain_vs, R.raw.screen_dye_fs),
        Shader.SCREEN_DIVERGENCE to ShaderProgram(R.raw.domain_vs, R.raw.screen_divergence_fs),
        Shader.TOUCH_TEMPERATURE to ShaderProgram(R.raw.domain_vs, R.raw.touch_temperature_fs),
        Shader.ADVECTION to ShaderProgram(R.raw.domain_vs, R.raw.advection_fs),
        Shader.DIFFUSION to ShaderProgram(R.raw.domain_vs, R.raw.diffusion_fs),
        Shader.SPLASH_VELOCITY to ShaderProgram(R.raw.domain_vs, R.raw.splash_velocity_fs),
        Shader.SPLASH_DYE to ShaderProgram(R.raw.domain_vs, R.raw.splash_dye_fs),
        Shader.DIVERGENCE to ShaderProgram(R.raw.domain_vs, R.raw.divergence_fs),

        )

    fun compileAll(context: Context) {

        val timeInMillis = measureTimeMillis {
            for (shader in shaders.values) {
                shader.compile(context)
            }
        }

        Log.d(this::class.qualifiedName, "compileAll: took $timeInMillis ms")
    }

    fun use(shader: Shader): Int {
        val id = shaders[shader]!!.id
        glUseProgram(id)
        return id
    }
}