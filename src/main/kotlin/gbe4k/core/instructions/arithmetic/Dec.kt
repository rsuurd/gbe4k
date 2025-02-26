package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set
import gbe4k.core.instructions.Mode

class Dec : Instruction {
    private val destination: Any
    private val mode: Mode

    constructor(register: Register) {
        this.destination = register
        mode = Mode.DIRECT
    }

    constructor(address: Int) {
        this.destination = address
        mode = Mode.INDIRECT
    }

    override fun execute(cpu: Cpu) {
        val result = when (val prev = destination.get(cpu, mode)) {
            is Byte -> prev.dec()
            is Int -> prev.dec()
            else -> throw IllegalArgumentException("Can't dec $destination")
        }

        destination.set(result, cpu)

        cpu.flags.z = result.toInt() == 0
        cpu.flags.n = true
        // cpu.flags.h = true
    }
}
