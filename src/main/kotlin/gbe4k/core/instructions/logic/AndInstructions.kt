package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import kotlin.experimental.and

object AndInstructions : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0xa0 -> And { cpu -> and(cpu, cpu.registers.b) }
        0xa1 -> And { cpu -> and(cpu, cpu.registers.c) }
        0xa2 -> And { cpu -> and(cpu, cpu.registers.d) }
        0xa3 -> And { cpu -> and(cpu, cpu.registers.e) }
        0xa4 -> And { cpu -> and(cpu, cpu.registers.h) }
        0xa5 -> And { cpu -> and(cpu, cpu.registers.l) }
        0xa6 -> And { cpu -> and(cpu, cpu.bus.read(cpu.registers.hl)) }
        0xa7 -> And { cpu -> and(cpu, cpu.registers.a) }
        0xe6 -> And { cpu -> and(cpu, cpu.read()) }
        else -> null
    }

    fun interface And : Instruction

    private fun and(cpu: Cpu, value: Byte) {
        cpu.apply {
            val result = registers.a.and(value)
            registers.a = result
            flags.z = result == 0x00.toByte()
            flags.n = false
            flags.h = true
            flags.c = false
        }
    }
}
