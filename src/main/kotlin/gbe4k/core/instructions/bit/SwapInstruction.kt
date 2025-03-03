package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.hiNibble
import gbe4k.core.Cpu.Companion.loNibble
import gbe4k.core.Cpu.Companion.n8
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.bit.SwapInstruction.Swap

object SwapInstruction : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0x30 -> Swap { cpu -> cpu.registers.b = cpu.registers.b.swap(cpu.flags) }
        0x31 -> Swap { cpu -> cpu.registers.c = cpu.registers.c.swap(cpu.flags) }
        0x32 -> Swap { cpu -> cpu.registers.d = cpu.registers.d.swap(cpu.flags) }
        0x33 -> Swap { cpu -> cpu.registers.e = cpu.registers.e.swap(cpu.flags) }
        0x34 -> Swap { cpu -> cpu.registers.h = cpu.registers.h.swap(cpu.flags) }
        0x35 -> Swap { cpu -> cpu.registers.l = cpu.registers.l.swap(cpu.flags) }
        0x36 -> Swap { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).swap(cpu.flags)) }
        0x37 -> Swap { cpu -> cpu.registers.a = cpu.registers.a.swap(cpu.flags) }
        else -> null
    }

    fun interface Swap : Instruction

    private fun Byte.swap(flags: Flags): Byte {
        val result = n8(loNibble(), hiNibble())

        flags.apply {
            z = result.and(0xff) == 0x00
            n = false
            h = false
            c = false
        }

        return result.toByte()
    }
}
