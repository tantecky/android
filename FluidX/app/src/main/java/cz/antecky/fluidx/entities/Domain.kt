package cz.antecky.fluidx.entities

import android.opengl.GLES20.*
import cz.antecky.fluidx.IRenderer
import cz.antecky.fluidx.shaders.Shader
import cz.antecky.fluidx.shaders.ShaderManager

class Domain : Quad() {
    override fun draw(frameBufferId: Int, shader: Shader, renderer: IRenderer) {
        val programId = ShaderManager.use(shader)
        val positionAttrib = glGetAttribLocation(programId, "a_position")

        glVertexAttribPointer(
            positionAttrib,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            0,
            vertexBuffer
        )

        val timeUniform = glGetUniformLocation(programId, "u_time")
        glUniform1f(timeUniform, renderer.time)

        val resolutionUniform = glGetUniformLocation(programId, "u_resolution")
        glUniform2f(resolutionUniform, renderer.width.toFloat(), renderer.height.toFloat())

        val widthTexelUniform = glGetUniformLocation(programId, "u_widthTexel")
        glUniform1f(widthTexelUniform, renderer.widthTexel)

        val heightTexelUniform = glGetUniformLocation(programId, "u_heightTexel")
        glUniform1f(heightTexelUniform, renderer.heightTexel)

        val mvpUniform = glGetUniformLocation(programId, "u_mvp")
        glUniformMatrix4fv(mvpUniform, 1, false, renderer.projectionM, 0)

        glActiveTexture(GL_TEXTURE0 + renderer.textureId)
        glBindTexture(GL_TEXTURE_2D, renderer.textureId)

        val textureUniform = glGetUniformLocation(programId, "u_temperature")
        glUniform1i(textureUniform, renderer.textureId)

        renderer.checkGlError()


        glEnableVertexAttribArray(positionAttrib)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        renderer.checkGlError()
    }
}