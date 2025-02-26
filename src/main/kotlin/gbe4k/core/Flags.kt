package gbe4k.core

import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Cpu.Companion.setBit

class Flags(private val registers: Registers) {
    var z: Boolean
        get() = registers.f.isBitSet(0)
        set(value) {
            registers.f = registers.f.setBit(value, 0)
        }

    var n: Boolean
        get() = registers.f.isBitSet(1)
        set(value) {
            registers.f = registers.f.setBit(value, 1)
        }

    var h: Boolean
        get() = registers.f.isBitSet(2)
        set(value) {
            registers.f = registers.f.setBit(value, 2)
        }

    var c: Boolean
        get() = registers.f.isBitSet(3)
        set(value) {
            registers.f = registers.f.setBit(value, 3)
        }
}