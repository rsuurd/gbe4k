package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.instructions.Instruction

object Daa : Instruction {
    override fun execute(cpu: Cpu) {
        var value = cpu.registers.a.toInt().and(0xffff)

        if (cpu.flags.h || (value.and(0x0f)) > 0x09) {
            value += if (cpu.flags.n) -0x06 else 0x06
        }

        if (cpu.flags.c || value.shr(4) > 0x09) {
            value += if (cpu.flags.n) -0x60 else 0x60
        }

        cpu.flags.z = value.and(0xff) == 0
        cpu.flags.h = false
        cpu.flags.c = cpu.flags.c || value > 0xff
        cpu.registers.a = value.toByte()
    }
}
