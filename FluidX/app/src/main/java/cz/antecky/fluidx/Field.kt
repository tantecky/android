package cz.antecky.fluidx

import android.opengl.GLES20.*
import android.util.Log
import java.nio.IntBuffer

class Field(private val halfFloatFormat: Int, val uniformName: String) {
    private var textureSrc: Int = -1
    private var fboSrc: Int = -1
    private var textureDst: Int = -1
    private var fboDst: Int = -1

    val texture: Int get() = textureSrc
    val framebuffer: Int get() = fboDst

    private fun createTexture(): Int {
        glActiveTexture(GL_TEXTURE0)
        val textures: IntBuffer = IntBuffer.allocate(1)
        glGenTextures(1, textures)
        val textureId = textures[0]

        if (textureId < 0) {
            Log.e(this::class.qualifiedName, "glGenTextures: GL_INVALID_VALUE")
            throw RuntimeException()
        }

        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, MyRenderer.GRID_SIZE, MyRenderer.GRID_SIZE, 0, GL_RGBA,
            halfFloatFormat, null
        )

        Utils.checkGlError()
        glBindTexture(GL_TEXTURE_2D, 0)

        return textureId
    }

    private fun createFrameBuffer(textureToAttach: Int): Int {
        val frameBuffers: IntBuffer = IntBuffer.allocate(1)

        glGenFramebuffers(1, frameBuffers)

        val frameBufferId = frameBuffers[0]

        if (frameBufferId < 0) {
            Log.e(this::class.qualifiedName, "glGenFramebuffers: GL_INVALID_VALUE")
            throw RuntimeException()
        } else {
            Log.d(this::class.qualifiedName, "glGenFramebuffers: $frameBufferId")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId)
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        glFramebufferTexture2D(
            GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D, textureToAttach, 0
        )

        Utils.checkFramebuffer()
        Utils.checkGlError()

        glClear(GL_COLOR_BUFFER_BIT)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)

        return frameBufferId
    }

    init {
        textureSrc = createTexture()
        fboSrc = createFrameBuffer(textureSrc)
        textureDst = createTexture()
        fboDst = createFrameBuffer(textureDst)
    }


    fun update() {
        textureSrc = textureDst.also { textureDst = textureSrc }
        fboSrc = fboDst.also { fboDst = fboSrc }
    }
}