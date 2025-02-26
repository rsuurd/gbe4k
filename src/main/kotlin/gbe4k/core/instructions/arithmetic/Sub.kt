package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set
import gbe4k.core.instructions.Mode

class Sub private constructor(
    private val destination: Register,
    private val source: Any,
    private val mode: Mode = Mode.DIRECT
) : Instruction {
    constructor(destination: Register, source: Register) : this(destination, source as Any)
    constructor(destination: Register, value: Byte) : this(destination, value as Any)
    constructor(destination: Register, address: Int) : this(destination, address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = source.get(cpu, mode).toInt()
        val original = destination.get(cpu).toInt()
        val result = (original - value) % 0x10000

        destination.set(result, cpu)

        cpu.flags.z = result == 0
        cpu.flags.n = true
        cpu.flags.h = false // TODO
        cpu.flags.c = false // TODO
    }
}
