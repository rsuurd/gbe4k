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
    private val is8bit: Boolean

    constructor(register: Register) {
        this.destination = register
        mode = Mode.DIRECT
        is8bit = register.is8bit
    }

    constructor(address: Int) {
        this.destination = address
        mode = Mode.INDIRECT
        is8bit = true
    }

    override fun execute(cpu: Cpu) {
        val previous = destination.get(cpu, mode).toInt()
        val incremented = previous.inc()

        val result = if (is8bit) {
            cpu.flags.z = incremented == 0
            cpu.flags.n = false
            cpu.flags.h = previous.and(0x0f) + 1 > 0x0f

            incremented.toByte()
        } else {
            incremented
        }

        destination.set(result, cpu)
    }
}
