package cz.antecky.fluidx

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.util.Log
import cz.antecky.fluidx.entities.Entity
import cz.antecky.fluidx.entities.Triangle
import cz.antecky.fluidx.shaders.ShaderManager

class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var entities: Array<Entity>
    private var startMillis: Long = 0
    private val time: Float get() = (System.currentTimeMillis() - startMillis) / 1000.0f

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        ShaderManager.compileAll(this.context)
        this.entities = arrayOf(Triangle())

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        startMillis = System.currentTimeMillis()
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        glClear(GL_COLOR_BUFFER_BIT)

        for (entity in this.entities) {
            entity.draw(time)
        }

        // Log.d(this::class.qualifiedName, "onDrawFrame: time:$time")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        Log.d(this::class.qualifiedName, "onSurfaceChanged: w:$width h:$height")
    }
}