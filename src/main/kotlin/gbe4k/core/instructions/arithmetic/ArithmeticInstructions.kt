package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.arithmetic.ArithmeticInstructions.Ccf
import gbe4k.core.instructions.arithmetic.ArithmeticInstructions.Cpl
import gbe4k.core.instructions.arithmetic.ArithmeticInstructions.Daa
import gbe4k.core.instructions.arithmetic.ArithmeticInstructions.Scf
import kotlin.experimental.inv

object ArithmeticInstructions : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0x37 -> Scf { cpu ->
            cpu.flags.n = false
            cpu.flags.h = false
            cpu.flags.c = true
        }

        0x2f -> Cpl { cpu ->
            cpu.registers.a = cpu.registers.a.inv()
            cpu.flags.n = true
            cpu.flags.h = true
        }

        0x27 -> Daa { cpu ->
            var offset = 0
            val a = cpu.registers.a.asInt()

            if (cpu.flags.h || (!cpu.flags.n && a.and(0xf) > 0x09)) {
                offset = 0x06
            }

            if (cpu.flags.c || (!cpu.flags.n && a > 0x99)) {
                offset += 0x60
            }

            val value = if (cpu.flags.n) {
                a - offset
            } else {
                a + offset
            }

            cpu.flags.apply {
                z = value.and(0xff) == 0x00
                h = false
                c = cpu.flags.c || (value > 0xff)
            }

            cpu.registers.a = value.and(0xff).toByte()
        }

        0x3f -> Ccf { cpu ->
            cpu.flags.n = false
            cpu.flags.h = false
            cpu.flags.c = !cpu.flags.c
        }

        else ->
            AddInstruction.decode(opcode) ?: AdcInstruction.decode(opcode) ?: SubInstruction.decode(opcode)
            ?: SbcInstruction.decode(opcode) ?: IncInstruction.decode(opcode) ?: DecInstruction.decode(opcode)
    }

    fun interface Scf : Instruction
    fun interface Cpl : Instruction
    fun interface Ccf : Instruction
    fun interface Daa : Instruction
}
