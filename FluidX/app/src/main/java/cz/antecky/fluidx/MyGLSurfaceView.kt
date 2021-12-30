package cz.antecky.fluidx

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyRenderer(context)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = it.x
            val y = it.y

            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Log.d(this::class.qualifiedName, "onTouchEvent: ACTION_DOWN x:$x y:$y")
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val s = x / this.width
                    val t = (this.height - y) / this.height
                    /* Log.d(
                         this::class.qualifiedName,
                         "onTouchEvent: ACTION_MOVE x:$x y:$y s:$s t:$t"
                     )*/
                    this.queueEvent { renderer.onTouch(s, t) }

                }
                MotionEvent.ACTION_UP -> {
                    //Log.d(this::class.qualifiedName, "onTouchEvent: ACTION_UP x:$x y:$y")
                }
                else -> {}
            }
        }


        return super.onTouchEvent(event)
    }
}