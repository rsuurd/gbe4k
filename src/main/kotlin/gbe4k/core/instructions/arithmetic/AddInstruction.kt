package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.arithmetic.AddInstruction.Add
import kotlin.experimental.and

object AddInstruction : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        // 16 bit
        0x09 -> Add { cpu -> cpu.registers.hl = cpu.add16(cpu.registers.hl, cpu.registers.bc) }
        0x19 -> Add { cpu -> cpu.registers.hl = cpu.add16(cpu.registers.hl, cpu.registers.de) }
        0x29 -> Add { cpu -> cpu.registers.hl = cpu.add16(cpu.registers.hl, cpu.registers.hl) }
        0x39 -> Add { cpu -> cpu.registers.hl = cpu.add16(cpu.registers.hl, cpu.registers.sp) }
        0xe8 -> Add { cpu ->
            val offset = cpu.read()

            cpu.flags.z = false
            cpu.flags.n = false
            cpu.flags.h = cpu.registers.sp.and(0x0f) + offset.and(0x0f) > 0x0f
            cpu.flags.c = cpu.registers.sp.and(0xff) + offset.asInt() > 0xff

            cpu.registers.sp += offset

            cpu.cycle(8)
        }

        // 8 bit
        0x80 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.registers.b) }
        0x81 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.registers.c) }
        0x82 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.registers.d) }
        0x83 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.registers.e) }
        0x84 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.registers.h) }
        0x85 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.registers.l) }
        0x86 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.bus.read(cpu.registers.hl)) }
        0x87 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.registers.a) }
        0xc6 -> Add { cpu -> cpu.registers.a = cpu.add8(cpu.registers.a, cpu.read()) }

        else -> null
    }

    fun interface Add : Instruction

    private fun Cpu.add16(a: Int, b: Int): Int {
        val result = a + b

        flags.apply {
            n = false
            h = result > 0x0fff
            c = result > 0xffff
        }

        return result.and(0xffff)
    }

    private fun Cpu.add8(a: Byte, b: Byte): Byte {
        val result = a.asInt() + b.asInt()

        flags.apply {
            z = result.and(0xff) == 0x00
            n = false
            h = a.and(0x0f) + b.and(0x0f) > 0x0f
            c = result > 0xff
        }

        return result.and(0xff).toByte()
    }
}
