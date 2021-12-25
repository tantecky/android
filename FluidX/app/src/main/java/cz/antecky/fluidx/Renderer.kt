package cz.antecky.fluidx

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import cz.antecky.fluidx.entities.Entity
import cz.antecky.fluidx.entities.Triangle
import cz.antecky.fluidx.shaders.ShaderManager

class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var entities: Array<Entity>

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        ShaderManager.compileAll(this.context)
        this.entities = arrayOf(Triangle())

        // Set the background frame color
        GLES20.glClearColor(0.5f, 0.5f, 0.0f, 1.0f)
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        for (entity in this.entities) {
            entity.draw()
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        Log.d(this::class.qualifiedName, "onSurfaceChanged: w:$width h:$height")
    }
}