package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.bit.BitInstruction.Bit

object BitInstruction : Decoder {
    override fun decode(opcode: Byte) = when (opcode.asInt()) {
        0x40 -> Bit { cpu -> cpu.registers.b.isBitSet(0, cpu.flags) }
        0x41 -> Bit { cpu -> cpu.registers.c.isBitSet(0, cpu.flags) }
        0x42 -> Bit { cpu -> cpu.registers.d.isBitSet(0, cpu.flags) }
        0x43 -> Bit { cpu -> cpu.registers.e.isBitSet(0, cpu.flags) }
        0x44 -> Bit { cpu -> cpu.registers.h.isBitSet(0, cpu.flags) }
        0x45 -> Bit { cpu -> cpu.registers.l.isBitSet(0, cpu.flags) }
        0x46 -> Bit { cpu -> cpu.bus.read(cpu.registers.hl).isBitSet(0, cpu.flags) }
        0x47 -> Bit { cpu -> cpu.registers.a.isBitSet(0, cpu.flags) }
        0x48 -> Bit { cpu -> cpu.registers.b.isBitSet(1, cpu.flags) }
        0x49 -> Bit { cpu -> cpu.registers.c.isBitSet(1, cpu.flags) }
        0x4a -> Bit { cpu -> cpu.registers.d.isBitSet(1, cpu.flags) }
        0x4b -> Bit { cpu -> cpu.registers.e.isBitSet(1, cpu.flags) }
        0x4c -> Bit { cpu -> cpu.registers.h.isBitSet(1, cpu.flags) }
        0x4d -> Bit { cpu -> cpu.registers.l.isBitSet(1, cpu.flags) }
        0x4e -> Bit { cpu -> cpu.bus.read(cpu.registers.hl).isBitSet(1, cpu.flags) }
        0x4f -> Bit { cpu -> cpu.registers.a.isBitSet(1, cpu.flags) }
        0x50 -> Bit { cpu -> cpu.registers.b.isBitSet(2, cpu.flags) }
        0x51 -> Bit { cpu -> cpu.registers.c.isBitSet(2, cpu.flags) }
        0x52 -> Bit { cpu -> cpu.registers.d.isBitSet(2, cpu.flags) }
        0x53 -> Bit { cpu -> cpu.registers.e.isBitSet(2, cpu.flags) }
        0x54 -> Bit { cpu -> cpu.registers.h.isBitSet(2, cpu.flags) }
        0x55 -> Bit { cpu -> cpu.registers.l.isBitSet(2, cpu.flags) }
        0x56 -> Bit { cpu -> cpu.bus.read(cpu.registers.hl).isBitSet(2, cpu.flags) }
        0x57 -> Bit { cpu -> cpu.registers.a.isBitSet(2, cpu.flags) }
        0x58 -> Bit { cpu -> cpu.registers.b.isBitSet(3, cpu.flags) }
        0x59 -> Bit { cpu -> cpu.registers.c.isBitSet(3, cpu.flags) }
        0x5a -> Bit { cpu -> cpu.registers.d.isBitSet(3, cpu.flags) }
        0x5b -> Bit { cpu -> cpu.registers.e.isBitSet(3, cpu.flags) }
        0x5c -> Bit { cpu -> cpu.registers.h.isBitSet(3, cpu.flags) }
        0x5d -> Bit { cpu -> cpu.registers.l.isBitSet(3, cpu.flags) }
        0x5e -> Bit { cpu -> cpu.bus.read(cpu.registers.hl).isBitSet(3, cpu.flags) }
        0x5f -> Bit { cpu -> cpu.registers.a.isBitSet(3, cpu.flags) }
        0x60 -> Bit { cpu -> cpu.registers.b.isBitSet(4, cpu.flags) }
        0x61 -> Bit { cpu -> cpu.registers.c.isBitSet(4, cpu.flags) }
        0x62 -> Bit { cpu -> cpu.registers.d.isBitSet(4, cpu.flags) }
        0x63 -> Bit { cpu -> cpu.registers.e.isBitSet(4, cpu.flags) }
        0x64 -> Bit { cpu -> cpu.registers.h.isBitSet(4, cpu.flags) }
        0x65 -> Bit { cpu -> cpu.registers.l.isBitSet(4, cpu.flags) }
        0x66 -> Bit { cpu -> cpu.bus.read(cpu.registers.hl).isBitSet(4, cpu.flags) }
        0x67 -> Bit { cpu -> cpu.registers.a.isBitSet(4, cpu.flags) }
        0x68 -> Bit { cpu -> cpu.registers.b.isBitSet(5, cpu.flags) }
        0x69 -> Bit { cpu -> cpu.registers.c.isBitSet(5, cpu.flags) }
        0x6a -> Bit { cpu -> cpu.registers.d.isBitSet(5, cpu.flags) }
        0x6b -> Bit { cpu -> cpu.registers.e.isBitSet(5, cpu.flags) }
        0x6c -> Bit { cpu -> cpu.registers.h.isBitSet(5, cpu.flags) }
        0x6d -> Bit { cpu -> cpu.registers.l.isBitSet(5, cpu.flags) }
        0x6e -> Bit { cpu -> cpu.bus.read(cpu.registers.hl).isBitSet(5, cpu.flags) }
        0x6f -> Bit { cpu -> cpu.registers.a.isBitSet(5, cpu.flags) }
        0x70 -> Bit { cpu -> cpu.registers.b.isBitSet(6, cpu.flags) }
        0x71 -> Bit { cpu -> cpu.registers.c.isBitSet(6, cpu.flags) }
        0x72 -> Bit { cpu -> cpu.registers.d.isBitSet(6, cpu.flags) }
        0x73 -> Bit { cpu -> cpu.registers.e.isBitSet(6, cpu.flags) }
        0x74 -> Bit { cpu -> cpu.registers.h.isBitSet(6, cpu.flags) }
        0x75 -> Bit { cpu -> cpu.registers.l.isBitSet(6, cpu.flags) }
        0x76 -> Bit { cpu -> cpu.bus.read(cpu.registers.hl).isBitSet(6, cpu.flags) }
        0x77 -> Bit { cpu -> cpu.registers.a.isBitSet(6, cpu.flags) }
        0x78 -> Bit { cpu -> cpu.registers.b.isBitSet(7, cpu.flags) }
        0x79 -> Bit { cpu -> cpu.registers.c.isBitSet(7, cpu.flags) }
        0x7a -> Bit { cpu -> cpu.registers.d.isBitSet(7, cpu.flags) }
        0x7b -> Bit { cpu -> cpu.registers.e.isBitSet(7, cpu.flags) }
        0x7c -> Bit { cpu -> cpu.registers.h.isBitSet(7, cpu.flags) }
        0x7d -> Bit { cpu -> cpu.registers.l.isBitSet(7, cpu.flags) }
        0x7e -> Bit { cpu -> cpu.bus.read(cpu.registers.hl).isBitSet(7, cpu.flags) }
        0x7f -> Bit { cpu -> cpu.registers.a.isBitSet(7, cpu.flags) }

        else -> null
    }

    fun interface Bit : Instruction

    private fun Byte.isBitSet(position: Int, flags: Flags) {
        flags.z = !isBitSet(position)
        flags.n = false
        flags.h = true
    }
}
