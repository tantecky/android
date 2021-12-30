package cz.antecky.fluidx.entities

import android.opengl.GLES20.*
import cz.antecky.fluidx.IRenderer
import cz.antecky.fluidx.MyRenderer
import cz.antecky.fluidx.shaders.Shader
import cz.antecky.fluidx.shaders.ShaderManager

class Domain : Quad() {
    fun touch(s: Float, t: Float, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.frameBufferId)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        val programId = ShaderManager.use(Shader.TOUCH)
        val positionAttrib = glGetAttribLocation(programId, "a_position")

        glVertexAttribPointer(
            positionAttrib,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            0,
            vertexBuffer
        )

        val mvpUniform = glGetUniformLocation(programId, "u_mvp")
        glUniformMatrix4fv(mvpUniform, 1, false, renderer.projectionM, 0)

        glActiveTexture(GL_TEXTURE0 + renderer.textureId)
        glBindTexture(GL_TEXTURE_2D, renderer.textureId)

        val touchUniform = glGetUniformLocation(programId, "u_touch")
        glUniform2f(touchUniform, s, t)

        val textureUniform = glGetUniformLocation(programId, "u_temperature")
        glUniform1i(textureUniform, renderer.textureId)

        glEnableVertexAttribArray(positionAttrib)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        renderer.checkGlError()

    }

    fun solveTemperature(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.frameBufferId)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        val programId = ShaderManager.use(Shader.TEMPERATURE)
        val positionAttrib = glGetAttribLocation(programId, "a_position")

        glVertexAttribPointer(
            positionAttrib,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            0,
            vertexBuffer
        )

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

        glEnableVertexAttribArray(positionAttrib)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        renderer.checkGlError()

    }

    override fun draw(shader: Shader, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, renderer.width, renderer.height)
        glClear(GL_COLOR_BUFFER_BIT)

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

        glEnableVertexAttribArray(positionAttrib)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        renderer.checkGlError()
    }
}