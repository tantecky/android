package cz.antecky.fluidx.entities

import android.opengl.GLES20.*
import cz.antecky.fluidx.shaders.Shader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import cz.antecky.fluidx.shaders.ShaderManager

class Quad : Entity() {
    override val coords = floatArrayOf(
        0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 1.0f, 0.0f,
    )

    override val vertexBuffer: FloatBuffer = createFloatBuffer(coords)

    override fun draw(time: Float) {
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

        val colorUniform = glGetUniformLocation(programId, "u_color")
        glUniform4f(colorUniform, 1.0f, 0.0f, 0.0f, 1.0f) // RGBA

        glEnableVertexAttribArray(positionAttrib)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkError()
    }
}