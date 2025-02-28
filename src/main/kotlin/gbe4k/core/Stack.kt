package gbe4k.core

import gbe4k.core.Cpu.Companion.hi
import gbe4k.core.Cpu.Companion.lo
import gbe4k.core.Cpu.Companion.n16

class Stack(private val bus: Bus, private val registers: Registers) {
    fun push(register: Register) = push(registers[register])

    fun push(value: Int) {
        bus.write(--registers.sp, value.hi())
        bus.write(--registers.sp, value.lo())
    }

    fun pop(): Int {
        val lo = bus.read(registers.sp++)
        val hi = bus.read(registers.sp++)

        return n16(hi, lo)
    }

    fun pop(register: Register) {
        registers[register] = pop()
    }
}