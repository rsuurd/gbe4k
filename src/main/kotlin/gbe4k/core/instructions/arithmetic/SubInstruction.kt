package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.arithmetic.SubInstruction.Sub
import kotlin.experimental.and

object SubInstruction : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0x90 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.registers.b, cpu.flags) }
        0x91 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.registers.c, cpu.flags) }
        0x92 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.registers.d, cpu.flags) }
        0x93 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.registers.e, cpu.flags) }
        0x94 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.registers.h, cpu.flags) }
        0x95 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.registers.l, cpu.flags) }
        0x96 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.bus.read(cpu.registers.hl), cpu.flags) }
        0x97 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.registers.a, cpu.flags) }
        0xd6 -> Sub { cpu -> cpu.registers.a = sub(cpu.registers.a, cpu.read(), cpu.flags) }

        else -> null
    }

    fun interface Sub : Instruction

    private fun sub(a: Byte, b: Byte, flags: Flags): Byte {
        val result = a.asInt() - b.asInt()

        flags.apply {
            z = result.and(0xff) == 0x00
            n = true
            h = a.and(0x0f) - b.and(0x0f) < 0x00
            c = result < 0x00
        }

        return result.and(0xff).toByte()
    }
}
