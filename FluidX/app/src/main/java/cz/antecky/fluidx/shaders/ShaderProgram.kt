package cz.antecky.fluidx.shaders

import android.content.Context
import android.opengl.GLES20.*
import android.util.Log

enum class ShaderType(val value: Int) {
    VERTEX(GL_VERTEX_SHADER),
    FRAGMENT(GL_FRAGMENT_SHADER),
}

class ShaderProgram(private val vertexResId: Int, private val fragmentResId: Int) {
    private var _id: Int = 0

    val id: Int get() = this._id

    private fun readSource(context: Context, resId: Int): String {
        val inputStream = context.resources.openRawResource(resId)
        return inputStream.reader().use { it.readText() }
    }

    private fun compileShader(type: ShaderType, src: String): Int {
        val shaderId = glCreateShader(type.value)

        if (shaderId == 0 || shaderId == GL_INVALID_ENUM) {
            Log.e(this::class.qualifiedName, "compileShader: shaderId:$shaderId")
            throw RuntimeException()
        }

        glShaderSource(shaderId, src)
        glCompileShader(shaderId)

        val isCompiled = IntArray(1)
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, isCompiled, 0)

        if (isCompiled[0] == GL_FALSE) {
            Log.e(this::class.qualifiedName, "compileShader failed")
            Log.e(this::class.qualifiedName, glGetShaderInfoLog(shaderId))
            throw RuntimeException()
        }

        return shaderId

    }

    fun compile(context: Context) {
        val vertexShader = compileShader(ShaderType.VERTEX, readSource(context, vertexResId))
        val fragmentShader = compileShader(ShaderType.FRAGMENT, readSource(context, fragmentResId))

        this._id = glCreateProgram()

        glAttachShader(this._id, vertexShader)
        glAttachShader(this._id, fragmentShader)
        glLinkProgram(this._id);

        val isLinked = IntArray(1)
        glGetProgramiv(this._id, GL_LINK_STATUS, isLinked, 0)

        if (isLinked[0] == GL_FALSE) {
            Log.e(this::class.qualifiedName, "glLinkProgram failed")
            Log.e(this::class.qualifiedName, glGetProgramInfoLog(this._id))
            throw RuntimeException()
        }
    }
}