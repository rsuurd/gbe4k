package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import kotlin.experimental.and

object CpInstructions : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0xb8 -> Cp { cpu -> cp(cpu, cpu.registers.b) }
        0xb9 -> Cp { cpu -> cp(cpu, cpu.registers.c) }
        0xba -> Cp { cpu -> cp(cpu, cpu.registers.d) }
        0xbb -> Cp { cpu -> cp(cpu, cpu.registers.e) }
        0xbc -> Cp { cpu -> cp(cpu, cpu.registers.h) }
        0xbd -> Cp { cpu -> cp(cpu, cpu.registers.l) }
        0xbe -> Cp { cpu -> cp(cpu, cpu.bus.read(cpu.registers.hl)) }
        0xbf -> Cp { cpu -> cp(cpu, cpu.registers.a) }
        0xfe -> Cp { cpu -> cp(cpu, cpu.read()) }
        else -> null
    }

    fun interface Cp : Instruction

    private fun cp(cpu: Cpu, value: Byte) {
        cpu.apply {
            val result = cpu.registers.a.asInt() - value.asInt()
            cpu.flags.z = result.and(0xff) == 0x00
            cpu.flags.n = true
            cpu.flags.h = cpu.registers.a.and(0x0f) - value.and(0x0f) < 0
            cpu.flags.c = result < 0
        }
    }
}
