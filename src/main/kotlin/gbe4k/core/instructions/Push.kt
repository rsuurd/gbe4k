package gbe4k.core.instructions

import gbe4k.core.Cpu
import gbe4k.core.Register

class Push(private val register: Register) : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.stack.push(register)
    }

    override fun toString() = "PUSH $register"
}
