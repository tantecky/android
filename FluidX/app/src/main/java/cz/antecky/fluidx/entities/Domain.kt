package cz.antecky.fluidx.entities

import android.opengl.GLES20.*
import cz.antecky.fluidx.Field
import cz.antecky.fluidx.IRenderer
import cz.antecky.fluidx.MyRenderer
import cz.antecky.fluidx.Utils.Companion.checkGlError
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

        glUniform1f(glGetUniformLocation(programId, "u_widthTexel"), renderer.widthTexel)
        glUniform1f(glGetUniformLocation(programId, "u_heightTexel"), renderer.heightTexel)

        glEnableVertexAttribArray(positionAttrib)
    }

    fun touch(s: Float, t: Float, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.temperature.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.TOUCH)
        prepareVertexShader(renderer)

        val temperature = renderer.temperature.texture
        glActiveTexture(GL_TEXTURE0 + temperature)
        glBindTexture(GL_TEXTURE_2D, temperature)

        glUniform2f(glGetUniformLocation(programId, "u_touch"), s, t)

        glUniform1i(glGetUniformLocation(programId, "u_temperature"), temperature)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()

    }

    fun solveTemperature(isLastIter: Boolean, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.temperature.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.TEMPERATURE)
        prepareVertexShader(renderer)


        val factor = MyRenderer.TIMESTEP * MyRenderer.CONDUCTIVITY

        glUniform1f(
            glGetUniformLocation(programId, "u_fx"),
            factor / (renderer.widthTexel * renderer.widthTexel)
        )
        glUniform1f(
            glGetUniformLocation(programId, "u_fy"),
            factor / (renderer.heightTexel * renderer.heightTexel)
        )

        val sink = if (isLastIter) 0.001f else 0.0f
        glUniform1f(glGetUniformLocation(programId, "u_sink"), sink)

        val temperature = renderer.temperature.texture
        glActiveTexture(GL_TEXTURE0 + temperature)
        glBindTexture(GL_TEXTURE_2D, temperature)

        glUniform1i(glGetUniformLocation(programId, "u_temperature"), temperature)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()

    }

    fun solveVelocityNonFree(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.velocity.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.VELOCITY_NONFREE)
        prepareVertexShader(renderer)

        glUniform1f(glGetUniformLocation(programId, "u_dx"), renderer.widthTexel)
        glUniform1f(glGetUniformLocation(programId, "u_dy"), renderer.heightTexel)
        glUniform1f(
            glGetUniformLocation(programId, "u_dx2"),
            renderer.widthTexel * renderer.widthTexel
        )
        glUniform1f(
            glGetUniformLocation(programId, "u_dy2"),
            renderer.heightTexel * renderer.heightTexel
        )
        glUniform1f(glGetUniformLocation(programId, "u_viscosity"), MyRenderer.VISCOSITY)

        val velocity = renderer.velocity.texture
        glActiveTexture(GL_TEXTURE0 + velocity)
        glBindTexture(GL_TEXTURE_2D, velocity)

        glUniform1i(glGetUniformLocation(programId, "u_velocity"), velocity)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()

    }

    fun solvePressure(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.pressure.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.PRESSURE)
        prepareVertexShader(renderer)

        glUniform1f(glGetUniformLocation(programId, "u_dx"), renderer.widthTexel)
        glUniform1f(glGetUniformLocation(programId, "u_dy"), renderer.heightTexel)
        glUniform1f(
            glGetUniformLocation(programId, "u_dx2"),
            renderer.widthTexel * renderer.widthTexel
        )
        glUniform1f(
            glGetUniformLocation(programId, "u_dy2"),
            renderer.heightTexel * renderer.heightTexel
        )

        val pressure = renderer.pressure.texture
        glActiveTexture(GL_TEXTURE0 + pressure)
        glBindTexture(GL_TEXTURE_2D, pressure)
        glUniform1i(glGetUniformLocation(programId, "u_pressure"), pressure)

        val velocity = renderer.velocity.texture
        glActiveTexture(GL_TEXTURE0 + velocity)
        glBindTexture(GL_TEXTURE_2D, velocity)
        glUniform1i(glGetUniformLocation(programId, "u_velocity"), velocity)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()

    }

    fun solvePressureGrad(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.pressure.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.PRESSURE)
        prepareVertexShader(renderer)

        glUniform1f(glGetUniformLocation(programId, "u_dx"), renderer.widthTexel)
        glUniform1f(glGetUniformLocation(programId, "u_dy"), renderer.heightTexel)

        val pressure = renderer.pressure.texture

        glActiveTexture(GL_TEXTURE0 + pressure)
        glBindTexture(GL_TEXTURE_2D, pressure)
        glUniform1i(glGetUniformLocation(programId, "u_pressure"), pressure)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()

    }

    fun solveVelocity(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.velocity.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.VELOCITY)
        prepareVertexShader(renderer)

        val velocity = renderer.velocity.texture
        glActiveTexture(GL_TEXTURE0 + velocity)
        glBindTexture(GL_TEXTURE_2D, velocity)
        glUniform1i(glGetUniformLocation(programId, "u_velocity"), velocity)

        val pressure = renderer.pressure.texture
        glActiveTexture(GL_TEXTURE0 + pressure)
        glBindTexture(GL_TEXTURE_2D, pressure)
        glUniform1i(glGetUniformLocation(programId, "u_pressure"), pressure)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()

    }

    fun solveVelocityNew(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.velocity.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.VELOCITY_NEW)
        prepareVertexShader(renderer)

        val velocity = renderer.velocity.texture
        glActiveTexture(GL_TEXTURE0 + velocity)
        glBindTexture(GL_TEXTURE_2D, velocity)
        glUniform1i(glGetUniformLocation(programId, "u_velocity"), velocity)

        glUniform1f(glGetUniformLocation(programId, "u_dt"), MyRenderer.TIMESTEP)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()

    }

    override fun draw(shader: Shader, renderer: IRenderer) {

    }

    fun display(shader: Shader, field:Field, textureUniform:String, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, renderer.width, renderer.height)
        glClear(GL_COLOR_BUFFER_BIT)

        programId = ShaderManager.use(shader)
        prepareVertexShader(renderer)

        val texture = field.texture

        glActiveTexture(GL_TEXTURE0 + texture)
        glBindTexture(GL_TEXTURE_2D, texture)

        glUniform1i(glGetUniformLocation(programId, textureUniform), texture)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()

    }
}