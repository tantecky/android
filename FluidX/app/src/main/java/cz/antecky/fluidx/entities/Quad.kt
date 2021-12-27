package cz.antecky.fluidx.entities

import android.opengl.GLES20.*
import cz.antecky.fluidx.IRenderer
import cz.antecky.fluidx.shaders.Shader
import java.nio.FloatBuffer

import cz.antecky.fluidx.shaders.ShaderManager

open class Quad : Entity() {
    override val coords = floatArrayOf(
        0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 1.0f, 0.0f,
    )

    override val vertexBuffer: FloatBuffer = createFloatBuffer(coords)

    override fun draw(renderer: IRenderer) {
        val programId = ShaderManager.use(Shader.COMPLEX)
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

        val timeUniform = glGetUniformLocation(programId, "u_time")
        glUniform1f(timeUniform, renderer.time)

        val resolutionUniform = glGetUniformLocation(programId, "u_resolution")
        glUniform2f(resolutionUniform, renderer.width, renderer.height)

        val mvpUniform = glGetUniformLocation(programId, "u_mvp")
        glUniformMatrix4fv(mvpUniform, 1, false, renderer.projectionM, 0)

        glEnableVertexAttribArray(positionAttrib)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        renderer.checkGlError()
    }
}