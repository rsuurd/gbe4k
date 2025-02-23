package gbe4k.core.instructions

import gbe4k.core.Cpu

interface Instruction {
    fun execute(cpu: Cpu)
}
