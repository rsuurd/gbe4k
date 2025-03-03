package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.instructions.Decoder
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.logic.XorInstructions.Xor
import kotlin.experimental.xor

object XorInstructions : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0xa8 -> Xor { cpu -> xor(cpu, cpu.registers.b) }
        0xa9 -> Xor { cpu -> xor(cpu, cpu.registers.c) }
        0xaa -> Xor { cpu -> xor(cpu, cpu.registers.d) }
        0xab -> Xor { cpu -> xor(cpu, cpu.registers.e) }
        0xac -> Xor { cpu -> xor(cpu, cpu.registers.h) }
        0xad -> Xor { cpu -> xor(cpu, cpu.registers.l) }
        0xae -> Xor { cpu -> xor(cpu, cpu.bus.read(cpu.registers.hl)) }
        0xaf -> Xor { cpu -> xor(cpu, cpu.registers.a) }
        0xee -> Xor { cpu -> xor(cpu, cpu.read()) }
        else -> null
    }

    fun interface Xor : Instruction

    private fun xor(cpu: Cpu, value: Byte) {
        cpu.apply {
            val result = registers.a.xor(value)
            registers.a = result
            flags.z = result == 0x00.toByte()
            flags.n = false
            flags.h = false
            flags.c = false
        }
    }
}
