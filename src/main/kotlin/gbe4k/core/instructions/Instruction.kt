package gbe4k.core.instructions

import gbe4k.core.Cpu

fun interface Instruction {
    fun execute(cpu: Cpu)
}
