package gbe4k.core.instructions

import gbe4k.core.Cpu

object Ei : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.interrupts.enableIme = true
    }
}
