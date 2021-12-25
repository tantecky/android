package cz.antecky.fluidx.entities

import java.nio.FloatBuffer

import android.opengl.GLES20.*
import android.util.Log

abstract class Entity {
    companion object {
        const val COORDS_PER_VERTEX = 3
    }

    abstract val coords: FloatArray
    val vertexCount: Int get() = coords.size / COORDS_PER_VERTEX
    val coordsBytes: Int get() = coords.size * Float.SIZE_BYTES

    abstract val vertexBuffer: FloatBuffer

    abstract fun draw()

    fun checkError() {
        val erno = glGetError()
        if (erno != GL_NO_ERROR) {
            Log.e(this::class.qualifiedName, "checkError: glGetError:$erno")
            throw RuntimeException()
        }
    }
}
