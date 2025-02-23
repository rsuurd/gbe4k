package gbe4k.core.instructions

import gbe4k.core.Cpu

object Di : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.ime = false
    }
}
