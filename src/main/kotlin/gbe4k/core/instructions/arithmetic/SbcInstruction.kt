package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.arithmetic.SbcInstruction.Sbc
import kotlin.experimental.and

object SbcInstruction : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0x98 -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.registers.b, cpu.flags) }
        0x99 -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.registers.c, cpu.flags) }
        0x9a -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.registers.d, cpu.flags) }
        0x9b -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.registers.e, cpu.flags) }
        0x9c -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.registers.h, cpu.flags) }
        0x9d -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.registers.l, cpu.flags) }
        0x9e -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.bus.read(cpu.registers.hl), cpu.flags) }
        0x9f -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.registers.a, cpu.flags) }
        0xde -> Sbc { cpu -> cpu.registers.a = sbc(cpu.registers.a, cpu.read(), cpu.flags) }

        else -> null
    }

    fun interface Sbc : Instruction

    private fun sbc(a: Byte, b: Byte, flags: Flags): Byte {
        val carry = if (flags.c) 1 else 0
        val result = a.asInt() - b.asInt() - carry

        flags.apply {
            z = result.and(0xff) == 0
            n = true
            h = a.and(0x0f) - b.and(0x0f) - carry < 0
            c = result < 0x00
        }

        return result.and(0xff).toByte()
    }
}
