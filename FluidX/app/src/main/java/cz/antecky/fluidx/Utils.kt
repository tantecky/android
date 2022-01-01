package cz.antecky.fluidx

import android.opengl.GLES20.*
import android.util.Log

class Utils {
    companion object {
        /*
        GL_EXT_color_buffer_half_float
        GL_OES_texture_half_float
        GL_OES_texture_half_float_linear
         */
        const val GL_HALF_FLOAT_OES = 0x8D61

        /*
        GL_NV_half_float
        Used by Android EMU
         */
        const val GL_HALF_FLOAT_NV = 0x140B

        fun checkGlError() {
            val erno = glGetError()
            if (erno != GL_NO_ERROR) {
                Log.e(this::class.qualifiedName, "checkError: glGetError:$erno")
                throw RuntimeException()
            }
        }

        fun checkFramebuffer() {
            val status = glCheckFramebufferStatus(GL_FRAMEBUFFER)

            if (status != GL_FRAMEBUFFER_COMPLETE) {
                Log.e(
                    this::class.qualifiedName,
                    "glCheckFramebufferStatus: != GL_FRAMEBUFFER_COMPLETE"
                )
                throw RuntimeException()
            }
        }

        fun printGlExtensions() {
            val extensions = glGetString(GL_EXTENSIONS)
            Log.d(this::class.qualifiedName, "GL_EXTENSIONS: $extensions")
        }

        fun halfFloatFormat(): Int {
            val extensions = glGetString(GL_EXTENSIONS)

            if (arrayOf(
                    "ANDROID_EMU",
                ).all { extensions.contains(it) }
            ) {
                return GL_HALF_FLOAT_NV
            }

            if (arrayOf(
                    "GL_EXT_color_buffer_half_float",
                    "GL_OES_texture_half_float",
                    "GL_OES_texture_half_float_linear",
                ).all { extensions.contains(it) }
            ) {
                return GL_HALF_FLOAT_OES
            }

            return GL_UNSIGNED_BYTE
        }
    }
}