package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.setBit
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.bit.SetInstruction.Set

object SetInstruction : Decoder {
    override fun decode(opcode: Byte) = when (opcode.asInt()) {
        0xc0 -> Set { cpu -> cpu.registers.b = cpu.registers.b.setBit(true, 0) }
        0xc1 -> Set { cpu -> cpu.registers.c = cpu.registers.c.setBit(true, 0) }
        0xc2 -> Set { cpu -> cpu.registers.d = cpu.registers.d.setBit(true, 0) }
        0xc3 -> Set { cpu -> cpu.registers.e = cpu.registers.e.setBit(true, 0) }
        0xc4 -> Set { cpu -> cpu.registers.h = cpu.registers.h.setBit(true, 0) }
        0xc5 -> Set { cpu -> cpu.registers.l = cpu.registers.l.setBit(true, 0) }
        0xc6 -> Set { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(true, 0)) }
        0xc7 -> Set { cpu -> cpu.registers.a = cpu.registers.a.setBit(true, 0) }
        0xc8 -> Set { cpu -> cpu.registers.b = cpu.registers.b.setBit(true, 1) }
        0xc9 -> Set { cpu -> cpu.registers.c = cpu.registers.c.setBit(true, 1) }
        0xca -> Set { cpu -> cpu.registers.d = cpu.registers.d.setBit(true, 1) }
        0xcb -> Set { cpu -> cpu.registers.e = cpu.registers.e.setBit(true, 1) }
        0xcc -> Set { cpu -> cpu.registers.h = cpu.registers.h.setBit(true, 1) }
        0xcd -> Set { cpu -> cpu.registers.l = cpu.registers.l.setBit(true, 1) }
        0xce -> Set { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(true, 1)) }
        0xcf -> Set { cpu -> cpu.registers.a = cpu.registers.a.setBit(true, 1) }
        0xd0 -> Set { cpu -> cpu.registers.b = cpu.registers.b.setBit(true, 2) }
        0xd1 -> Set { cpu -> cpu.registers.c = cpu.registers.c.setBit(true, 2) }
        0xd2 -> Set { cpu -> cpu.registers.d = cpu.registers.d.setBit(true, 2) }
        0xd3 -> Set { cpu -> cpu.registers.e = cpu.registers.e.setBit(true, 2) }
        0xd4 -> Set { cpu -> cpu.registers.h = cpu.registers.h.setBit(true, 2) }
        0xd5 -> Set { cpu -> cpu.registers.l = cpu.registers.l.setBit(true, 2) }
        0xd6 -> Set { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(true, 2)) }
        0xd7 -> Set { cpu -> cpu.registers.a = cpu.registers.a.setBit(true, 2) }
        0xd8 -> Set { cpu -> cpu.registers.b = cpu.registers.b.setBit(true, 3) }
        0xd9 -> Set { cpu -> cpu.registers.c = cpu.registers.c.setBit(true, 3) }
        0xda -> Set { cpu -> cpu.registers.d = cpu.registers.d.setBit(true, 3) }
        0xdb -> Set { cpu -> cpu.registers.e = cpu.registers.e.setBit(true, 3) }
        0xdc -> Set { cpu -> cpu.registers.h = cpu.registers.h.setBit(true, 3) }
        0xdd -> Set { cpu -> cpu.registers.l = cpu.registers.l.setBit(true, 3) }
        0xde -> Set { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(true, 3)) }
        0xdf -> Set { cpu -> cpu.registers.a = cpu.registers.a.setBit(true, 3) }
        0xe0 -> Set { cpu -> cpu.registers.b = cpu.registers.b.setBit(true, 4) }
        0xe1 -> Set { cpu -> cpu.registers.c = cpu.registers.c.setBit(true, 4) }
        0xe2 -> Set { cpu -> cpu.registers.d = cpu.registers.d.setBit(true, 4) }
        0xe3 -> Set { cpu -> cpu.registers.e = cpu.registers.e.setBit(true, 4) }
        0xe4 -> Set { cpu -> cpu.registers.h = cpu.registers.h.setBit(true, 4) }
        0xe5 -> Set { cpu -> cpu.registers.l = cpu.registers.l.setBit(true, 4) }
        0xe6 -> Set { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(true, 4)) }
        0xe7 -> Set { cpu -> cpu.registers.a = cpu.registers.a.setBit(true, 4) }
        0xe8 -> Set { cpu -> cpu.registers.b = cpu.registers.b.setBit(true, 5) }
        0xe9 -> Set { cpu -> cpu.registers.c = cpu.registers.c.setBit(true, 5) }
        0xea -> Set { cpu -> cpu.registers.d = cpu.registers.d.setBit(true, 5) }
        0xeb -> Set { cpu -> cpu.registers.e = cpu.registers.e.setBit(true, 5) }
        0xec -> Set { cpu -> cpu.registers.h = cpu.registers.h.setBit(true, 5) }
        0xed -> Set { cpu -> cpu.registers.l = cpu.registers.l.setBit(true, 5) }
        0xee -> Set { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(true, 5)) }
        0xef -> Set { cpu -> cpu.registers.a = cpu.registers.a.setBit(true, 5) }
        0xf0 -> Set { cpu -> cpu.registers.b = cpu.registers.b.setBit(true, 6) }
        0xf1 -> Set { cpu -> cpu.registers.c = cpu.registers.c.setBit(true, 6) }
        0xf2 -> Set { cpu -> cpu.registers.d = cpu.registers.d.setBit(true, 6) }
        0xf3 -> Set { cpu -> cpu.registers.e = cpu.registers.e.setBit(true, 6) }
        0xf4 -> Set { cpu -> cpu.registers.h = cpu.registers.h.setBit(true, 6) }
        0xf5 -> Set { cpu -> cpu.registers.l = cpu.registers.l.setBit(true, 6) }
        0xf6 -> Set { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(true, 6)) }
        0xf7 -> Set { cpu -> cpu.registers.a = cpu.registers.a.setBit(true, 6) }
        0xf8 -> Set { cpu -> cpu.registers.b = cpu.registers.b.setBit(true, 7) }
        0xf9 -> Set { cpu -> cpu.registers.c = cpu.registers.c.setBit(true, 7) }
        0xfa -> Set { cpu -> cpu.registers.d = cpu.registers.d.setBit(true, 7) }
        0xfb -> Set { cpu -> cpu.registers.e = cpu.registers.e.setBit(true, 7) }
        0xfc -> Set { cpu -> cpu.registers.h = cpu.registers.h.setBit(true, 7) }
        0xfd -> Set { cpu -> cpu.registers.l = cpu.registers.l.setBit(true, 7) }
        0xfe -> Set { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).setBit(true, 7)) }
        0xff -> Set { cpu -> cpu.registers.a = cpu.registers.a.setBit(true, 7) }

        else -> null
    }

    fun interface Set : Instruction
}
