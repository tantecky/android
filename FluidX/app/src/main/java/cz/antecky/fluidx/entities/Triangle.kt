package cz.antecky.fluidx.entities

import android.opengl.GLES20.*
import cz.antecky.fluidx.shaders.Shader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import cz.antecky.fluidx.shaders.ShaderManager

class Triangle : Entity() {
    override val coords = floatArrayOf(
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
    )

    override val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(coordsBytes).run {
        // use the device hardware's native byte order
        order(ByteOrder.nativeOrder())

        // create a floating point buffer from the ByteBuffer
        asFloatBuffer().apply {
            // add the coordinates to the FloatBuffer
            put(coords)
            // set the buffer to read the first coordinate
            position(0)
        }
    }


    override fun draw() {
        val programId = ShaderManager.use(Shader.FLAT)
        val positionAttrib = glGetAttribLocation(programId, "a_position")

        glVertexAttribPointer(
            positionAttrib,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            0,
            vertexBuffer
        )

        glEnableVertexAttribArray(positionAttrib)
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkError()
    }
}