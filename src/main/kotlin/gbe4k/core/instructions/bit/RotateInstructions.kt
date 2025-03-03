package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Flags
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.bit.RotateInstructions.Rl
import gbe4k.core.instructions.bit.RotateInstructions.Rlc
import gbe4k.core.instructions.bit.RotateInstructions.Rr
import gbe4k.core.instructions.bit.RotateInstructions.Rrc

object RotateInstructions : Decoder {
    override fun decode(opcode: Byte) = when (opcode.asInt()) {
        0x00 -> Rlc { cpu -> cpu.registers.b = cpu.registers.b.rlc(cpu.flags) }
        0x01 -> Rlc { cpu -> cpu.registers.c = cpu.registers.c.rlc(cpu.flags) }
        0x02 -> Rlc { cpu -> cpu.registers.d = cpu.registers.d.rlc(cpu.flags) }
        0x03 -> Rlc { cpu -> cpu.registers.e = cpu.registers.e.rlc(cpu.flags) }
        0x04 -> Rlc { cpu -> cpu.registers.h = cpu.registers.h.rlc(cpu.flags) }
        0x05 -> Rlc { cpu -> cpu.registers.l = cpu.registers.l.rlc(cpu.flags) }
        0x06 -> Rlc { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).rlc(cpu.flags)) }
        0x07 -> Rlc { cpu -> cpu.registers.a = cpu.registers.a.rlc(cpu.flags) }
        0x10 -> Rl { cpu -> cpu.registers.b = cpu.registers.b.rl(cpu.flags) }
        0x11 -> Rl { cpu -> cpu.registers.c = cpu.registers.c.rl(cpu.flags) }
        0x12 -> Rl { cpu -> cpu.registers.d = cpu.registers.d.rl(cpu.flags) }
        0x13 -> Rl { cpu -> cpu.registers.e = cpu.registers.e.rl(cpu.flags) }
        0x14 -> Rl { cpu -> cpu.registers.h = cpu.registers.h.rl(cpu.flags) }
        0x15 -> Rl { cpu -> cpu.registers.l = cpu.registers.l.rl(cpu.flags) }
        0x16 -> Rl { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).rl(cpu.flags)) }
        0x17 -> Rl { cpu -> cpu.registers.a = cpu.registers.a.rl(cpu.flags) }

        0x08 -> Rrc { cpu -> cpu.registers.b = cpu.registers.b.rrc(cpu.flags) }
        0x09 -> Rrc { cpu -> cpu.registers.c = cpu.registers.c.rrc(cpu.flags) }
        0x0a -> Rrc { cpu -> cpu.registers.d = cpu.registers.d.rrc(cpu.flags) }
        0x0b -> Rrc { cpu -> cpu.registers.e = cpu.registers.e.rrc(cpu.flags) }
        0x0c -> Rrc { cpu -> cpu.registers.h = cpu.registers.h.rrc(cpu.flags) }
        0x0d -> Rrc { cpu -> cpu.registers.l = cpu.registers.l.rrc(cpu.flags) }
        0x0e -> Rrc { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).rrc(cpu.flags)) }
        0x0f -> Rrc { cpu -> cpu.registers.a = cpu.registers.a.rrc(cpu.flags) }
        0x18 -> Rr { cpu -> cpu.registers.b = cpu.registers.b.rr(cpu.flags) }
        0x19 -> Rr { cpu -> cpu.registers.c = cpu.registers.c.rr(cpu.flags) }
        0x1a -> Rr { cpu -> cpu.registers.d = cpu.registers.d.rr(cpu.flags) }
        0x1b -> Rr { cpu -> cpu.registers.e = cpu.registers.e.rr(cpu.flags) }
        0x1c -> Rr { cpu -> cpu.registers.h = cpu.registers.h.rr(cpu.flags) }
        0x1d -> Rr { cpu -> cpu.registers.l = cpu.registers.l.rr(cpu.flags) }
        0x1e -> Rr { cpu -> cpu.bus.write(cpu.registers.hl, cpu.bus.read(cpu.registers.hl).rr(cpu.flags)) }
        0x1f -> Rr { cpu -> cpu.registers.a = cpu.registers.a.rr(cpu.flags) }

        else -> null
    }

    fun interface Rlc : Instruction
    fun interface Rl : Instruction
    fun interface Rrc : Instruction
    fun interface Rr : Instruction

    fun Byte.rlc(flags: Flags) = rotate(flags) {
        asInt().shl(1) + asInt().shr(7)
    }

    fun Byte.rl(flags: Flags) = rotate(flags) {
        asInt().shl(1) + if (flags.c) 1 else 0
    }

    fun Byte.rrc(flags: Flags): Byte = rotate(flags) {
        with(asInt()) {
            shr(1) + and(1).shl(7) + and(1).shl(8)
        }
    }

    fun Byte.rr(flags: Flags) = rotate(flags) {
        with(asInt()) {
            val carry = if (flags.c) 1 else 0
            shr(1) + carry.shl(7) + and(1).shl(8)
        }
    }

    private fun Byte.rotate(flags: Flags, rotation: (Byte) -> Int): Byte = rotation(this).let { result ->
        flags.apply {
            z = result.and(0xff) == 0
            n = false
            h = false
            c = result > 0xff
        }

        result.and(0xff).toByte()
    }
}
