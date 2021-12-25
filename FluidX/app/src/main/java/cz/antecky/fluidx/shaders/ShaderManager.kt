package cz.antecky.fluidx.shaders

import android.content.Context
import android.opengl.*
import android.opengl.GLES20.glUseProgram
import android.util.Log
import cz.antecky.fluidx.R
import kotlin.system.measureTimeMillis

enum class Shader {
    FLAT,
}

object ShaderManager {
    private val shaders = mapOf(Shader.FLAT to ShaderProgram(R.raw.flat_vs, R.raw.flat_fs))

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