package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.instructions.Instruction

object Ccf : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.flags.n = false
        cpu.flags.h = false
        cpu.flags.c = !cpu.flags.c
    }
}