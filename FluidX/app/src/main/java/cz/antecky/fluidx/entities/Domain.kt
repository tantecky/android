package cz.antecky.fluidx.entities

import android.opengl.GLES20.*
import cz.antecky.fluidx.IRenderer
import cz.antecky.fluidx.MyRenderer
import cz.antecky.fluidx.shaders.Shader
import cz.antecky.fluidx.shaders.ShaderManager

class Domain : Quad() {
    private var programId: Int = -1
    private var positionAttrib: Int = -1

    private fun prepareVertexShader(renderer: IRenderer) {
        positionAttrib = glGetAttribLocation(programId, "a_position")

        glVertexAttribPointer(
            positionAttrib,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            0,
            vertexBuffer
        )

        glUniformMatrix4fv(
            glGetUniformLocation(programId, "u_mvp"), 1, false,
            renderer.projectionM, 0
        )

        glEnableVertexAttribArray(positionAttrib)
    }

    fun touch(s: Float, t: Float, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.fboDst)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.TOUCH)
        prepareVertexShader(renderer)

        glActiveTexture(GL_TEXTURE0 + renderer.textureSrc)
        glBindTexture(GL_TEXTURE_2D, renderer.textureSrc)

        glUniform2f(glGetUniformLocation(programId, "u_touch"), s, t)

        glUniform1i(glGetUniformLocation(programId, "u_temperature"), renderer.textureSrc)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        renderer.checkGlError()

    }

    fun solveTemperature(isLastIter: Boolean, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.fboDst)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.TEMPERATURE)
        prepareVertexShader(renderer)

        glUniform1f(glGetUniformLocation(programId, "u_widthTexel"), renderer.widthTexel)
        glUniform1f(glGetUniformLocation(programId, "u_heightTexel"), renderer.heightTexel)

        val factor = MyRenderer.TIMESTEP * MyRenderer.CONDUCTIVITY

        glUniform1f(
            glGetUniformLocation(programId, "u_dx"),
            factor / (renderer.widthTexel * renderer.widthTexel)
        )
        glUniform1f(
            glGetUniformLocation(programId, "u_dy"),
            factor / (renderer.heightTexel * renderer.heightTexel)
        )

        val sink = if (isLastIter) 0.001f else 0.0f
        glUniform1f(glGetUniformLocation(programId, "u_sink"), sink)


        glActiveTexture(GL_TEXTURE0 + renderer.textureSrc)
        glBindTexture(GL_TEXTURE_2D, renderer.textureSrc)

        glUniform1i(glGetUniformLocation(programId, "u_temperature"), renderer.textureSrc)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        renderer.checkGlError()

    }

    override fun draw(shader: Shader, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, renderer.width, renderer.height)
        glClear(GL_COLOR_BUFFER_BIT)

        programId = ShaderManager.use(shader)
        prepareVertexShader(renderer)

        glUniform1f(glGetUniformLocation(programId, "u_time"), renderer.time)

        glUniform2f(
            glGetUniformLocation(programId, "u_resolution"), renderer.width.toFloat(),
            renderer.height.toFloat()
        )

        glUniform1f(glGetUniformLocation(programId, "u_widthTexel"), renderer.widthTexel)

        glUniform1f(glGetUniformLocation(programId, "u_heightTexel"), renderer.heightTexel)

        glActiveTexture(GL_TEXTURE0 + renderer.textureSrc)
        glBindTexture(GL_TEXTURE_2D, renderer.textureSrc)

        glUniform1i(glGetUniformLocation(programId, "u_temperature"), renderer.textureSrc)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        renderer.checkGlError()
    }
}