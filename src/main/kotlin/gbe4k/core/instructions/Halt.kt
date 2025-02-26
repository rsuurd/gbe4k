package gbe4k.core.instructions

import gbe4k.core.Cpu

object Halt : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.halted = true
    }
}
