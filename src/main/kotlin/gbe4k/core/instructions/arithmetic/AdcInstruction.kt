package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.arithmetic.AdcInstruction.Adc
import kotlin.experimental.and

object AdcInstruction : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0x88 -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.registers.b, cpu.flags) }
        0x89 -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.registers.c, cpu.flags) }
        0x8a -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.registers.d, cpu.flags) }
        0x8b -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.registers.e, cpu.flags) }
        0x8c -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.registers.h, cpu.flags) }
        0x8d -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.registers.l, cpu.flags) }
        0x8e -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.bus.read(cpu.registers.hl), cpu.flags) }
        0x8f -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.registers.a, cpu.flags) }
        0xce -> Adc { cpu -> cpu.registers.a = adc(cpu.registers.a, cpu.read(), cpu.flags) }

        else -> null
    }

    fun interface Adc : Instruction

    private fun adc(a: Byte, b: Byte, flags: Flags): Byte {
        val carry = if (flags.c) 1 else 0
        val result = a.asInt() + b.asInt() + carry

        flags.apply {
            z = result.and(0xff) == 0
            n = false
            h = a.and(0x0f) + b.and(0x0f) + carry > 0x0f
            c = result > 0xff
        }

        return result.and(0xff).toByte()
    }
}
