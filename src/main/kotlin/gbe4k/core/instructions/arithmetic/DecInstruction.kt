package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import kotlin.experimental.and

object DecInstruction : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        // 16 bit
        0x0b -> Dec { cpu -> cpu.registers.bc-- }
        0x1b -> Dec { cpu -> cpu.registers.de-- }
        0x2b -> Dec { cpu -> cpu.registers.hl-- }
        0x3b -> Dec { cpu -> cpu.registers.sp-- }
        // 8 bit
        0x05 -> Dec { cpu -> cpu.registers.b = dec(cpu.registers.b, cpu.flags) }
        0x15 -> Dec { cpu -> cpu.registers.d = dec(cpu.registers.d, cpu.flags) }
        0x25 -> Dec { cpu -> cpu.registers.h = dec(cpu.registers.h, cpu.flags) }
        0x35 -> Dec { cpu -> cpu.bus.write(cpu.registers.hl, dec(cpu.bus.read(cpu.registers.hl), cpu.flags)) }
        0x0d -> Dec { cpu -> cpu.registers.c = dec(cpu.registers.c, cpu.flags) }
        0x1d -> Dec { cpu -> cpu.registers.e = dec(cpu.registers.e, cpu.flags) }
        0x2d -> Dec { cpu -> cpu.registers.l = dec(cpu.registers.l, cpu.flags) }
        0x3d -> Dec { cpu -> cpu.registers.a = dec(cpu.registers.a, cpu.flags) }

        else -> null
    }

    fun interface Dec : Instruction

    private fun dec(current: Byte, flags: Flags): Byte {
        val incremented = current.dec().asInt()

        flags.apply {
            z = incremented == 0x00
            n = true
            h = current.and(0x0f) - 1 < 0x00
        }

        return incremented.toByte()
    }
}
