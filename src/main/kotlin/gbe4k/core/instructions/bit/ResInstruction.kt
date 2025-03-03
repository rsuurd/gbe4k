package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.setBit
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.bit.ResInstruction.Res

object ResInstruction : Decoder {
    override fun decode(opcode: Byte) = when (opcode.asInt()) {
        0x80 -> Res { cpu -> cpu.registers.b = cpu.registers.b.setBit(false, 0) }
        0x81 -> Res { cpu -> cpu.registers.c = cpu.registers.c.setBit(false, 0) }
        0x82 -> Res { cpu -> cpu.registers.d = cpu.registers.d.setBit(false, 0) }
        0x83 -> Res { cpu -> cpu.registers.e = cpu.registers.e.setBit(false, 0) }
        0x84 -> Res { cpu -> cpu.registers.h = cpu.registers.h.setBit(false, 0) }
        0x85 -> Res { cpu -> cpu.registers.l = cpu.registers.l.setBit(false, 0) }
        0x86 -> Res { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(false, 0)) }
        0x87 -> Res { cpu -> cpu.registers.a = cpu.registers.a.setBit(false, 0) }
        0x88 -> Res { cpu -> cpu.registers.b = cpu.registers.b.setBit(false, 1) }
        0x89 -> Res { cpu -> cpu.registers.c = cpu.registers.c.setBit(false, 1) }
        0x8a -> Res { cpu -> cpu.registers.d = cpu.registers.d.setBit(false, 1) }
        0x8b -> Res { cpu -> cpu.registers.e = cpu.registers.e.setBit(false, 1) }
        0x8c -> Res { cpu -> cpu.registers.h = cpu.registers.h.setBit(false, 1) }
        0x8d -> Res { cpu -> cpu.registers.l = cpu.registers.l.setBit(false, 1) }
        0x8e -> Res { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(false, 1)) }
        0x8f -> Res { cpu -> cpu.registers.a = cpu.registers.a.setBit(false, 1) }
        0x90 -> Res { cpu -> cpu.registers.b = cpu.registers.b.setBit(false, 2) }
        0x91 -> Res { cpu -> cpu.registers.c = cpu.registers.c.setBit(false, 2) }
        0x92 -> Res { cpu -> cpu.registers.d = cpu.registers.d.setBit(false, 2) }
        0x93 -> Res { cpu -> cpu.registers.e = cpu.registers.e.setBit(false, 2) }
        0x94 -> Res { cpu -> cpu.registers.h = cpu.registers.h.setBit(false, 2) }
        0x95 -> Res { cpu -> cpu.registers.l = cpu.registers.l.setBit(false, 2) }
        0x96 -> Res { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(false, 2)) }
        0x97 -> Res { cpu -> cpu.registers.a = cpu.registers.a.setBit(false, 2) }
        0x98 -> Res { cpu -> cpu.registers.b = cpu.registers.b.setBit(false, 3) }
        0x99 -> Res { cpu -> cpu.registers.c = cpu.registers.c.setBit(false, 3) }
        0x9a -> Res { cpu -> cpu.registers.d = cpu.registers.d.setBit(false, 3) }
        0x9b -> Res { cpu -> cpu.registers.e = cpu.registers.e.setBit(false, 3) }
        0x9c -> Res { cpu -> cpu.registers.h = cpu.registers.h.setBit(false, 3) }
        0x9d -> Res { cpu -> cpu.registers.l = cpu.registers.l.setBit(false, 3) }
        0x9e -> Res { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(false, 3)) }
        0x9f -> Res { cpu -> cpu.registers.a = cpu.registers.a.setBit(false, 3) }
        0xa0 -> Res { cpu -> cpu.registers.b = cpu.registers.b.setBit(false, 4) }
        0xa1 -> Res { cpu -> cpu.registers.c = cpu.registers.c.setBit(false, 4) }
        0xa2 -> Res { cpu -> cpu.registers.d = cpu.registers.d.setBit(false, 4) }
        0xa3 -> Res { cpu -> cpu.registers.e = cpu.registers.e.setBit(false, 4) }
        0xa4 -> Res { cpu -> cpu.registers.h = cpu.registers.h.setBit(false, 4) }
        0xa5 -> Res { cpu -> cpu.registers.l = cpu.registers.l.setBit(false, 4) }
        0xa6 -> Res { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(false, 4)) }
        0xa7 -> Res { cpu -> cpu.registers.a = cpu.registers.a.setBit(false, 4) }
        0xa8 -> Res { cpu -> cpu.registers.b = cpu.registers.b.setBit(false, 5) }
        0xa9 -> Res { cpu -> cpu.registers.c = cpu.registers.c.setBit(false, 5) }
        0xaa -> Res { cpu -> cpu.registers.d = cpu.registers.d.setBit(false, 5) }
        0xab -> Res { cpu -> cpu.registers.e = cpu.registers.e.setBit(false, 5) }
        0xac -> Res { cpu -> cpu.registers.h = cpu.registers.h.setBit(false, 5) }
        0xad -> Res { cpu -> cpu.registers.l = cpu.registers.l.setBit(false, 5) }
        0xae -> Res { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(false, 5)) }
        0xaf -> Res { cpu -> cpu.registers.a = cpu.registers.a.setBit(false, 5) }
        0xb0 -> Res { cpu -> cpu.registers.b = cpu.registers.b.setBit(false, 6) }
        0xb1 -> Res { cpu -> cpu.registers.c = cpu.registers.c.setBit(false, 6) }
        0xb2 -> Res { cpu -> cpu.registers.d = cpu.registers.d.setBit(false, 6) }
        0xb3 -> Res { cpu -> cpu.registers.e = cpu.registers.e.setBit(false, 6) }
        0xb4 -> Res { cpu -> cpu.registers.h = cpu.registers.h.setBit(false, 6) }
        0xb5 -> Res { cpu -> cpu.registers.l = cpu.registers.l.setBit(false, 6) }
        0xb6 -> Res { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(false, 6)) }
        0xb7 -> Res { cpu -> cpu.registers.a = cpu.registers.a.setBit(false, 6) }
        0xb8 -> Res { cpu -> cpu.registers.b = cpu.registers.b.setBit(false, 7) }
        0xb9 -> Res { cpu -> cpu.registers.c = cpu.registers.c.setBit(false, 7) }
        0xba -> Res { cpu -> cpu.registers.d = cpu.registers.d.setBit(false, 7) }
        0xbb -> Res { cpu -> cpu.registers.e = cpu.registers.e.setBit(false, 7) }
        0xbc -> Res { cpu -> cpu.registers.h = cpu.registers.h.setBit(false, 7) }
        0xbd -> Res { cpu -> cpu.registers.l = cpu.registers.l.setBit(false, 7) }
        0xbe -> Res { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(false, 7)) }
        0xbf -> Res { cpu -> cpu.registers.a = cpu.registers.a.setBit(false, 7) }

        else -> null
    }

    fun interface Res : Instruction
}