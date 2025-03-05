package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.bit.ShiftInstructions.Sla
import gbe4k.core.instructions.bit.ShiftInstructions.Sra
import gbe4k.core.instructions.bit.ShiftInstructions.Srl

object ShiftInstructions : Decoder {
    override fun decode(opcode: Byte) = when (opcode.asInt()) {
        0x20 -> Sla { cpu -> cpu.registers.b = cpu.registers.b.sla(cpu.flags) }
        0x21 -> Sla { cpu -> cpu.registers.c = cpu.registers.c.sla(cpu.flags) }
        0x22 -> Sla { cpu -> cpu.registers.d = cpu.registers.d.sla(cpu.flags) }
        0x23 -> Sla { cpu -> cpu.registers.e = cpu.registers.e.sla(cpu.flags) }
        0x24 -> Sla { cpu -> cpu.registers.h = cpu.registers.h.sla(cpu.flags) }
        0x25 -> Sla { cpu -> cpu.registers.l = cpu.registers.l.sla(cpu.flags) }
        0x26 -> Sla { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).sla(cpu.flags))}
        0x27 -> Sla { cpu -> cpu.registers.a = cpu.registers.a.sla(cpu.flags) }

        0x28 -> Sra { cpu -> cpu.registers.b = cpu.registers.b.sra(cpu.flags) }
        0x29 -> Sra { cpu -> cpu.registers.c = cpu.registers.c.sra(cpu.flags) }
        0x2a -> Sra { cpu -> cpu.registers.d = cpu.registers.d.sra(cpu.flags) }
        0x2b -> Sra { cpu -> cpu.registers.e = cpu.registers.e.sra(cpu.flags) }
        0x2c -> Sra { cpu -> cpu.registers.h = cpu.registers.h.sra(cpu.flags) }
        0x2d -> Sra { cpu -> cpu.registers.l = cpu.registers.l.sra(cpu.flags) }
        0x2e -> Sra { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).sra(cpu.flags)) }
        0x2f -> Sra { cpu -> cpu.registers.a = cpu.registers.a.sra(cpu.flags) }

        0x38 -> Srl { cpu -> cpu.registers.b = cpu.registers.b.srl(cpu.flags) }
        0x39 -> Srl { cpu -> cpu.registers.c = cpu.registers.c.srl(cpu.flags) }
        0x3a -> Srl { cpu -> cpu.registers.d = cpu.registers.d.srl(cpu.flags) }
        0x3b -> Srl { cpu -> cpu.registers.e = cpu.registers.e.srl(cpu.flags) }
        0x3c -> Srl { cpu -> cpu.registers.h = cpu.registers.h.srl(cpu.flags) }
        0x3d -> Srl { cpu -> cpu.registers.l = cpu.registers.l.srl(cpu.flags) }
        0x3e -> Srl { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).srl(cpu.flags)) }
        0x3f -> Srl { cpu -> cpu.registers.a = cpu.registers.a.srl(cpu.flags) }

        else -> null
    }

    fun interface Sla : Instruction
    fun interface Sra : Instruction
    fun interface Srl : Instruction

    private fun Byte.sla(flags: Flags) = shift(flags) { value ->
        value.shl(1)
    }

    private fun Byte.sra(flags: Flags) = shift(flags) { value ->
        value.shr(1) + value.and(0x80) + value.and(1).shl(8)
    }

    private fun Byte.srl(flags: Flags) = shift(flags) { value ->
        value.shr(1) + value.and(1).shl(8)
    }

    private fun Byte.shift(flags: Flags, shift: (Int) -> Int): Byte = shift(asInt()).let { result ->
        flags.apply {
            z = result.and(0xff) == 0x00
            n = false
            h = false
            c = result > 0xff
        }

        result.and(0xff).toByte()
    }
}
