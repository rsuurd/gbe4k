package gbe4k.core.io

import gbe4k.core.Cpu.Companion.hiNibble
import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Cpu.Companion.n8
import kotlin.experimental.and

class Joypad {
    var value: Byte = 0x3f
        get() {
            val lo = if (!field.isBitSet(5)) { // buttons
                (start shl 3) + (select shl 2) + (b shl 1) + (a shl 0)
            } else if (!field.isBitSet(4)) { // dpad
                (down shl 3) + (up shl 2) + (left shl 1) + (right shl 0)
            } else {
                0xf
            }

            return n8(field.hiNibble(), lo.toByte()).toByte()
        }
        set(value) {
            field = value.and(0xf0.toByte())
        }

    var a = false
    var b = false
    var select = false
    var start = false

    var up = false
    var down = false
    var left = false
    var right = false

    // if the bit is NOT set it's considered pressed
    private infix fun Boolean.shl(bits: Int) = (if (this) 0 else 1).shl(bits)
}
