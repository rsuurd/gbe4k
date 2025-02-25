package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set
import gbe4k.core.instructions.Mode

class Inc : Instruction {
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
        val new = when (val prev = destination.get(cpu, mode)) {
            is Byte -> prev.inc()
            is Int -> prev.inc()
            else -> throw IllegalArgumentException("Can't inc $destination")
        }

        destination.set(new, cpu)

        cpu.flags.z = new.toInt() == 0
        cpu.flags.n = false
        // cpu.flags.h = true
    }
}
