package cz.antecky.fluidx.shaders

import android.content.Context
import android.opengl.*
import android.opengl.GLES20.glUseProgram
import android.util.Log
import cz.antecky.fluidx.R
import kotlin.system.measureTimeMillis

enum class Shader {
    FLAT,
    COMPLEX,
    TEMPERATURE,
    VELOCITY_NONFREE,
    PRESSURE,
    SCREEN,
    TOUCH,
}

object ShaderManager {
    private val shaders = mapOf(
        Shader.FLAT to ShaderProgram(R.raw.flat_vs, R.raw.flat_fs),
        Shader.COMPLEX to ShaderProgram(R.raw.flat_vs, R.raw.complex_fs),
        Shader.TEMPERATURE to ShaderProgram(R.raw.domain_vs, R.raw.temperature_fs),
        Shader.VELOCITY_NONFREE to ShaderProgram(R.raw.domain_vs, R.raw.velocity_nonfree_fs),
        Shader.PRESSURE to ShaderProgram(R.raw.domain_vs, R.raw.pressure_fs),
        Shader.SCREEN to ShaderProgram(R.raw.domain_vs, R.raw.screen_fs),
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