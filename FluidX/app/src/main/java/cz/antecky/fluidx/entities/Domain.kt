package cz.antecky.fluidx.entities

import android.opengl.GLES20
import cz.antecky.fluidx.IRenderer
import cz.antecky.fluidx.shaders.Shader
import cz.antecky.fluidx.shaders.ShaderManager

class Domain : Quad() {
    override fun draw(renderer: IRenderer) {
        val programId = ShaderManager.use(Shader.TEMPERATURE)
        val positionAttrib = GLES20.glGetAttribLocation(programId, "a_position")

        GLES20.glVertexAttribPointer(
            positionAttrib,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            0,
            vertexBuffer
        )

        val timeUniform = GLES20.glGetUniformLocation(programId, "u_time")
        GLES20.glUniform1f(timeUniform, renderer.time)

        val resolutionUniform = GLES20.glGetUniformLocation(programId, "u_resolution")
        GLES20.glUniform2f(resolutionUniform, renderer.width, renderer.height)

        val mvpUniform = GLES20.glGetUniformLocation(programId, "u_mvp")
        GLES20.glUniformMatrix4fv(mvpUniform, 1, false, renderer.projectionM, 0)

        GLES20.glEnableVertexAttribArray(positionAttrib)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount)

        GLES20.glDisableVertexAttribArray(positionAttrib)
        checkError()
    }
}