package gbe4k.core.instructions

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.address
import gbe4k.core.Cpu.Companion.hi
import gbe4k.core.Cpu.Companion.lo
import gbe4k.core.Register
import gbe4k.core.Register.A
import gbe4k.core.Register.B
import gbe4k.core.Register.BC
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.DE
import gbe4k.core.Register.E
import gbe4k.core.Register.H
import gbe4k.core.Register.HL
import gbe4k.core.Register.L
import gbe4k.core.Register.SP

object InstructionSupport {
    fun Any.get(cpu: Cpu, mode: Mode = Mode.DIRECT) = when (this) {
        BC, DE, HL, SP -> cpu.registers[this as Register]
        A, B, C, D, E, H, L -> cpu.registers[this as Register].toByte()
        is Int, is Byte -> this as Number
        else -> throw IllegalArgumentException("Can not resolve value from $this")
    }.let { value ->
        if (mode == Mode.DIRECT) {
            value
        } else {
            cpu.bus.read(value.address())
        }
    }

    fun Any.set(value: Number, cpu: Cpu) = when (this) {
        is Register -> cpu.registers[this] = value.toInt()
        is Number -> {
            val address = this.address()

            when (value) {
                is Int -> {
                    cpu.bus.write(address, value.lo())
                    cpu.bus.write(address + 1, value.hi())
                }
                is Byte -> cpu.bus.write(address, value)
                else -> throw IllegalArgumentException("Can not set $value to $this")
            }
        } else -> throw IllegalArgumentException("Can not set $value to $this")
    }
}
