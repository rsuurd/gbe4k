package gbe4k.core.instructions.bit

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.setBit
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set
import gbe4k.core.instructions.Mode

class Res(private val position: Int, private val destination: Any, private val mode: Mode) : Instruction {
    constructor(position: Int, register: Register) : this(position, register as Any, Mode.DIRECT)
    constructor(position: Int, address: Int) : this(position, address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = destination.get(cpu, mode).toByte()
        val result = value.setBit(false, position)

        destination.set(result, cpu)
    }
}
