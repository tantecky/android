package cz.antecky.fluidx.entities

import java.nio.FloatBuffer

import android.opengl.GLES20.*
import android.util.Log
import cz.antecky.fluidx.IRenderer
import cz.antecky.fluidx.shaders.Shader
import java.nio.ByteBuffer
import java.nio.ByteOrder

abstract class Entity {
    companion object {
        const val COORDS_PER_VERTEX = 3

        fun createFloatBuffer(floatArray: FloatArray): FloatBuffer {
            val buffer = ByteBuffer.allocateDirect(floatArray.size * Float.SIZE_BYTES)
            buffer.order(ByteOrder.nativeOrder())
            val floatBuffer = buffer.asFloatBuffer()
            floatBuffer.put(floatArray)
            floatBuffer.position(0)
            return floatBuffer
        }
    }

    abstract val coords: FloatArray
    val vertexCount: Int get() = coords.size / COORDS_PER_VERTEX

    abstract val vertexBuffer: FloatBuffer

    abstract fun draw(shader: Shader, renderer: IRenderer)


}
