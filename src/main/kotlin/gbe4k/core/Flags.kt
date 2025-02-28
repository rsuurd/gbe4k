package gbe4k.core

import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Cpu.Companion.setBit

class Flags(private val registers: Registers) {
    var z: Boolean
        get() = registers.f.isBitSet(7)
        set(value) {
            registers.f = registers.f.setBit(value, 7)
        }

    var n: Boolean
        get() = registers.f.isBitSet(6)
        set(value) {
            registers.f = registers.f.setBit(value, 6)
        }

    var h: Boolean
        get() = registers.f.isBitSet(5)
        set(value) {
            registers.f = registers.f.setBit(value, 5)
        }

    var c: Boolean
        get() = registers.f.isBitSet(4)
        set(value) {
            registers.f = registers.f.setBit(value, 4)
        }
}
