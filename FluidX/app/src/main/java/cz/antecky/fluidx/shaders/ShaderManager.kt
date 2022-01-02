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
    VELOCITY_NONFREE,
    VELOCITY,
    VELOCITY_NEW,
    PRESSURE,
    PRESSURE_GRAD,
    SCREEN_TEMPERATURE,
    SCREEN_VELOCITY,
    TOUCH,
}

object ShaderManager {
    private val shaders = mapOf(
        Shader.FLAT to ShaderProgram(R.raw.flat_vs, R.raw.flat_fs),
        Shader.COMPLEX to ShaderProgram(R.raw.flat_vs, R.raw.complex_fs),
        Shader.TEMPERATURE to ShaderProgram(R.raw.domain_vs, R.raw.temperature_fs),
        Shader.VELOCITY_NONFREE to ShaderProgram(R.raw.domain_vs, R.raw.velocity_nonfree_fs),
        Shader.VELOCITY to ShaderProgram(R.raw.domain_vs, R.raw.velocity_fs),
        Shader.VELOCITY_NEW to ShaderProgram(R.raw.domain_vs, R.raw.velocity_new_fs),
        Shader.PRESSURE to ShaderProgram(R.raw.domain_vs, R.raw.pressure_fs),
        Shader.PRESSURE_GRAD to ShaderProgram(R.raw.domain_vs, R.raw.pressure_grad_fs),
        Shader.SCREEN_TEMPERATURE to ShaderProgram(R.raw.domain_vs, R.raw.screen_temperature_fs),
        Shader.SCREEN_VELOCITY to ShaderProgram(R.raw.domain_vs, R.raw.screen_velocity_fs),
        Shader.TOUCH to ShaderProgram(R.raw.domain_vs, R.raw.touch_fs),
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