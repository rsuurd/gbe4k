package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.logic.XorInstructions.Xor
import kotlin.experimental.or
import kotlin.experimental.xor

object OrInstructions : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0xb0 -> Or { cpu -> or(cpu, cpu.registers.b) }
        0xb1 -> Or { cpu -> or(cpu, cpu.registers.c) }
        0xb2 -> Or { cpu -> or(cpu, cpu.registers.d) }
        0xb3 -> Or { cpu -> or(cpu, cpu.registers.e) }
        0xb4 -> Or { cpu -> or(cpu, cpu.registers.h) }
        0xb5 -> Or { cpu -> or(cpu, cpu.registers.l) }
        0xb6 -> Or { cpu -> or(cpu, cpu.bus.read(cpu.registers.hl)) }
        0xb7 -> Or { cpu -> or(cpu, cpu.registers.a) }
        0xf6 -> Or { cpu -> or(cpu, cpu.read()) }
        else -> null
    }

    fun interface Or : Instruction

    private fun or(cpu: Cpu, value: Byte) {
        cpu.apply {
            val result = registers.a.or(value)
            registers.a = result
            flags.z = result == 0x00.toByte()
            flags.n = false
            flags.h = false
            flags.c = false
        }
    }
}
