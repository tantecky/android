package cz.antecky.fluidx.entities

import java.nio.FloatBuffer

abstract class Entity {
    companion object {
        const val COORDS_PER_VERTEX = 3
    }

    abstract val coords: FloatArray
    val coordsBytes: Int = coords.size * Float.SIZE_BYTES

    abstract val vertexBuffer: FloatBuffer

    abstract fun draw()

}
