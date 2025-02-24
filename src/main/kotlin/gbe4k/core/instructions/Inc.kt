package gbe4k.core.instructions

import gbe4k.core.Cpu
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

class Inc : Instruction {
    private val destination: Any

    constructor(register: Register) {
        destination = register
    }

    constructor(address: Int) {
        destination = address
    }

    override fun execute(cpu: Cpu) {
        when (destination) {
            is Int -> incrementValueAtAddress(cpu)
            else -> incrementRegister(cpu)
        }
    }

    private fun incrementValueAtAddress(cpu: Cpu) {
        val current = cpu.bus.read(destination as Int)
        val incremented = current.inc()

        cpu.bus.write(destination, incremented)
        setFlags(incremented, current, cpu)
    }

    private fun incrementRegister(cpu: Cpu) {
        when (val register = destination as Register) {
            BC, DE, HL, SP -> cpu.registers[register]++
            A, B, C, D, E, H, L -> {
                val current = cpu.registers[register].toByte()
                val incremented = current.inc()

                cpu.registers[register] = incremented
                setFlags(incremented, current, cpu)
            }

            else -> throw IllegalArgumentException("Can't inc $destination")
        }
    }

    private fun setFlags(current: Byte, prev: Byte, cpu: Cpu) {
        cpu.flags.z = current == 0x00.toByte()
        cpu.flags.n = false
        // cpu.flags.h = check half carry bit
    }
}
