package gbe4k.core.instructions

import gbe4k.core.Registers
import kotlin.experimental.and
import kotlin.experimental.or

class Flags(private val registers: Registers) {
    var z: Boolean
        get() = getFlag(0)
        set(value) {

            registers.f = setFlag(value, 0)
        }

    var n: Boolean
        get() = getFlag(1)
        set(value) {
            registers.f = setFlag(value, 1)
        }

    var h: Boolean
        get() = getFlag(2)
        set(value) {
            registers.f = setFlag(value, 2)
        }

    var c: Boolean
        get() = getFlag(3)
        set(value) {
            registers.f = setFlag(value, 3)
        }

    fun set(z: Boolean? = null, n: Boolean? = null, h: Boolean? = null, c: Boolean? = null) {
        z?.let { this.z = it }
        n?.let { this.n = it }
        h?.let { this.h = it }
        c?.let { this.c = it }
    }

    private fun getFlag(position: Int) =
        (registers.f.toInt() shr position).and(1) == 1

    private fun setFlag(enabled: Boolean, position: Int): Byte {
        return if (enabled) {
            registers.f or (1 shl position).toByte()
        } else {
            val size = Byte.SIZE_BITS - registers.f.countLeadingZeroBits()

            registers.f and (((1 shl size) - 1) - (1 shl position)).toByte()
        }
    }
}