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

    fun touch(
        s: Float, t: Float, shader: Shader, field: Field,
        renderer: IRenderer
    ) {
        glBindFramebuffer(GL_FRAMEBUFFER, field.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(shader)
        prepareVertexShader(renderer)

        val texture = field.texture
        glActiveTexture(GL_TEXTURE0 + texture)
        glBindTexture(GL_TEXTURE_2D, texture)

        glUniform2f(glGetUniformLocation(programId, "u_touch"), s, t)

        glUniform1i(glGetUniformLocation(programId, field.uniformName), texture)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()
        field.update()
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


    fun advection(quantity: Field, decay: Float, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, quantity.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.ADVECTION)
        prepareVertexShader(renderer)

        val velocity = renderer.velocity.texture
        glActiveTexture(GL_TEXTURE0 + velocity)
        glBindTexture(GL_TEXTURE_2D, velocity)
        glUniform1i(glGetUniformLocation(programId, "u_velocity"), velocity)

        val quantityId = quantity.texture
        glActiveTexture(GL_TEXTURE0 + quantityId)
        glBindTexture(GL_TEXTURE_2D, quantityId)
        glUniform1i(glGetUniformLocation(programId, "u_quantity"), quantityId)

        glUniform1f(glGetUniformLocation(programId, "u_dt"), MyRenderer.TIMESTEP)
        glUniform1f(glGetUniformLocation(programId, "u_dx"), renderer.widthTexel)
        glUniform1f(glGetUniformLocation(programId, "u_dy"), renderer.heightTexel)
        glUniform1f(glGetUniformLocation(programId, "u_decay"), decay)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()
    }

    fun diffusion(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.velocity.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.DIFFUSION)
        prepareVertexShader(renderer)

        val velocity = renderer.velocity.texture
        glActiveTexture(GL_TEXTURE0 + velocity)
        glBindTexture(GL_TEXTURE_2D, velocity)
        glUniform1i(glGetUniformLocation(programId, "u_velocity"), velocity)

        val alpha =
            renderer.widthTexel * renderer.heightTexel / (MyRenderer.VISCOSITY * MyRenderer.TIMESTEP)

        glUniform1f(glGetUniformLocation(programId, "alpha"), alpha)
        glUniform1f(glGetUniformLocation(programId, "beta"), 4.0f + alpha)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()
    }

    fun splash(
        s: Float, t: Float, field: Field,
        shader: Shader,
        renderer: IRenderer
    ) {
        glBindFramebuffer(GL_FRAMEBUFFER, field.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(shader)
        prepareVertexShader(renderer)

        val texture = field.texture
        glActiveTexture(GL_TEXTURE0 + texture)
        glBindTexture(GL_TEXTURE_2D, texture)

        glUniform2f(glGetUniformLocation(programId, "u_touch"), s, t)

        glUniform1i(glGetUniformLocation(programId, "u_quantity"), texture)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()
        field.update()
    }

    fun divergence(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.divergence.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.DIVERGENCE)
        prepareVertexShader(renderer)

        val velocity = renderer.velocity.texture
        glActiveTexture(GL_TEXTURE0 + velocity)
        glBindTexture(GL_TEXTURE_2D, velocity)
        glUniform1i(glGetUniformLocation(programId, "u_velocity"), velocity)

        glUniform1f(glGetUniformLocation(programId, "u_dx"), renderer.widthTexel)
        glUniform1f(glGetUniformLocation(programId, "u_dy"), renderer.heightTexel)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()
    }

    fun clearField(field: Field) {
        glBindFramebuffer(GL_FRAMEBUFFER, field.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)
        glClear(GL_COLOR_BUFFER_BIT)
        checkGlError()
    }

    fun pressure(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.pressure.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.PRESSURE)
        prepareVertexShader(renderer)

        val pressure = renderer.pressure.texture
        glActiveTexture(GL_TEXTURE0 + pressure)
        glBindTexture(GL_TEXTURE_2D, pressure)
        glUniform1i(glGetUniformLocation(programId, "u_pressure"), pressure)

        val divergence = renderer.divergence.texture
        glActiveTexture(GL_TEXTURE0 + divergence)
        glBindTexture(GL_TEXTURE_2D, divergence)
        glUniform1i(glGetUniformLocation(programId, "u_divergence"), divergence)

        val alpha = -renderer.widthTexel * renderer.heightTexel

        glUniform1f(glGetUniformLocation(programId, "alpha"), alpha)
        glUniform1f(glGetUniformLocation(programId, "beta"), 4.0f)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()
    }

    fun subtractPressure(renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, renderer.velocity.framebuffer)
        glViewport(0, 0, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE)

        programId = ShaderManager.use(Shader.SUBTRACT_PRESSURE)
        prepareVertexShader(renderer)

        val pressure = renderer.pressure.texture
        glActiveTexture(GL_TEXTURE0 + pressure)
        glBindTexture(GL_TEXTURE_2D, pressure)
        glUniform1i(glGetUniformLocation(programId, "u_pressure"), pressure)

        val velocity = renderer.velocity.texture
        glActiveTexture(GL_TEXTURE0 + velocity)
        glBindTexture(GL_TEXTURE_2D, velocity)
        glUniform1i(glGetUniformLocation(programId, "u_velocity"), velocity)

        glUniform1f(glGetUniformLocation(programId, "u_dx"), renderer.widthTexel)
        glUniform1f(glGetUniformLocation(programId, "u_dy"), renderer.heightTexel)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()
    }

    override fun draw(shader: Shader, renderer: IRenderer) {
        throw NotImplementedError()
    }

    fun display(shader: Shader, field: Field, renderer: IRenderer) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, renderer.width, renderer.height)
        glClear(GL_COLOR_BUFFER_BIT)

        programId = ShaderManager.use(shader)
        prepareVertexShader(renderer)

        val texture = field.texture

        glActiveTexture(GL_TEXTURE0 + texture)
        glBindTexture(GL_TEXTURE_2D, texture)
        glUniform1i(glGetUniformLocation(programId, field.uniformName), texture)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount)

        glDisableVertexAttribArray(positionAttrib)
        checkGlError()
    }
}