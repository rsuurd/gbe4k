package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.instructions.Instruction
import kotlin.experimental.inv

object Cpl : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.registers.a = cpu.registers.a.inv()
        cpu.flags.n = true
        cpu.flags.h = true
    }
}