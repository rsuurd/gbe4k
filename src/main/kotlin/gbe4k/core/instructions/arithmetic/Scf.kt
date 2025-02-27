package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.instructions.Instruction

object Scf : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.flags.n = false
        cpu.flags.h = false
        cpu.flags.c = true
    }
}