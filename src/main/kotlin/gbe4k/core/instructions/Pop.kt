package gbe4k.core.instructions

import gbe4k.core.Cpu
import gbe4k.core.Register

class Pop(private val register: Register) : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.stack.pop(register)
    }
}