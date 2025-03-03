package gbe4k.core

import gbe4k.core.Cpu.Companion.hi
import gbe4k.core.Cpu.Companion.lo
import gbe4k.core.Cpu.Companion.n16
import kotlin.experimental.and

class Registers {
    var af: Int = 0x0000
        set(value) {
            // clear lower 4 bits of the f register
            field = value.and(0xfff0)
        }

    var bc: Int = 0x0000
    var de: Int = 0x0000
    var hl: Int = 0x0000
    var sp: Int = 0x0000
        set(value) {
            field = value.and(0xffff)
        }

    var a: Byte
        get() = af.hi()
        set(value) {
            af = n16(value, f)
        }

    var f: Byte
        get() = af.lo()
        set(value) {
            af = n16(a, value.and(0xf0.toByte()))
        }

    var b: Byte
        get() = bc.hi()
        set(value) {
            bc = n16(value, c)
        }

    var c: Byte
        get() = bc.lo()
        set(value) {
            bc = n16(b, value)
        }

    var d: Byte
        get() = de.hi()
        set(value) {
            de = n16(value, e)
        }

    var e: Byte
        get() = de.lo()
        set(value) {
            de = n16(d, value)
        }

    var h: Byte
        get() = hl.hi()
        set(value) {
            hl = n16(value, l)
        }

    var l: Byte
        get() = hl.lo()
        set(value) {
            hl = n16(h, value)
        }
}
