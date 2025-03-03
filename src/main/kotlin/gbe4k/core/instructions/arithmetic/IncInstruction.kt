package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.arithmetic.IncInstruction.Inc
import kotlin.experimental.and

object IncInstruction : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        // 16 bit
        0x03 -> Inc { cpu -> cpu.registers.bc++ }
        0x13 -> Inc { cpu -> cpu.registers.de++ }
        0x23 -> Inc { cpu -> cpu.registers.hl++ }
        0x33 -> Inc { cpu -> cpu.registers.sp++ }
        // 8 bit
        0x04 -> Inc { cpu -> cpu.registers.b = inc(cpu.registers.b, cpu.flags) }
        0x14 -> Inc { cpu -> cpu.registers.d = inc(cpu.registers.d, cpu.flags) }
        0x24 -> Inc { cpu -> cpu.registers.h = inc(cpu.registers.h, cpu.flags) }
        0x34 -> Inc { cpu -> cpu.bus.write(cpu.registers.hl, inc(cpu.bus.read(cpu.registers.hl), cpu.flags)) }
        0x0c -> Inc { cpu -> cpu.registers.c = inc(cpu.registers.c, cpu.flags) }
        0x1c -> Inc { cpu -> cpu.registers.e = inc(cpu.registers.e, cpu.flags) }
        0x2c -> Inc { cpu -> cpu.registers.l = inc(cpu.registers.l, cpu.flags) }
        0x3c -> Inc { cpu -> cpu.registers.a = inc(cpu.registers.a, cpu.flags) }

        else -> null
    }

    fun interface Inc : Instruction

    private fun inc(current: Byte, flags: Flags): Byte {
        val incremented = current.inc().asInt()

        flags.apply {
            z = incremented == 0x00
            n = false
            h = current.and(0x0f) + 1 > 0x0f
        }

        return incremented.toByte()
    }
}
