package gbe4k.core

import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Cpu.Companion.hi
import gbe4k.core.Cpu.Companion.lo
import gbe4k.core.Cpu.Companion.n16
import gbe4k.core.Register.A
import gbe4k.core.Register.AF
import gbe4k.core.Register.B
import gbe4k.core.Register.BC
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.DE
import gbe4k.core.Register.E
import gbe4k.core.Register.F
import gbe4k.core.Register.H
import gbe4k.core.Register.HL
import gbe4k.core.Register.L
import gbe4k.core.Register.SP

enum class Register {
    A, B, C, D, E, F, H, L, AF, BC, DE, HL, SP;
}

data class Registers(
    var af: Int = 0,
    var bc: Int = 0,
    var de: Int = 0,
    var hl: Int = 0,
    var sp: Int = 0
) {
    operator fun get(register: Register): Int = when (register) {
        A -> a.toInt()
        B -> b.toInt()
        C -> c.toInt()
        D -> d.toInt()
        E -> e.toInt()
        F -> f.toInt()
        H -> h.toInt()
        L -> l.toInt()
        AF -> af
        BC -> bc
        DE -> de
        HL -> hl
        SP -> sp
    }

    operator fun set(register: Register, value: Int) {
        when (register) {
            AF -> af = value
            BC -> bc = value
            DE -> de = value
            HL -> hl = value
            SP -> sp = value
            A, B, C, D, E, F, H, L -> set(register, value.toByte())
        }
    }

    operator fun set(register: Register, value: Byte) {
        when (register) {
            A -> a = value
            B -> b = value
            C -> c = value
            D -> d = value
            E -> e = value
            F -> f = value
            H -> h = value
            L -> l = value
            else -> throw IllegalArgumentException("Can not store ${value.hex()} in $register")
        }
    }

    var a: Byte
        get() = af.hi()
        set(value) {
            af = n16(value, f)
        }

    var f: Byte
        get() = af.lo()
        set(value) {
            af = n16(a, value)
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
