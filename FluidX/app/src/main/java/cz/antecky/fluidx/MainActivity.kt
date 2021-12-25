package cz.antecky.fluidx

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log

class MainActivity : Activity() {
    private lateinit var glView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        glView = MyGLSurfaceView(this)
        setContentView(glView)
        Log.d(this::class.qualifiedName, "onCreate")
    }
}