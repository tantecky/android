package cz.antecky.fluidx.entities

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle : Entity() {
    override val coords = floatArrayOf(
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
    )

    override val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(coordsBytes).run {
        // use the device hardware's native byte order
        order(ByteOrder.nativeOrder())

        // create a floating point buffer from the ByteBuffer
        asFloatBuffer().apply {
            // add the coordinates to the FloatBuffer
            put(coords)
            // set the buffer to read the first coordinate
            position(0)
        }
    }


    override fun draw() {
    }
}