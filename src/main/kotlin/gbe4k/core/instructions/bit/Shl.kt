package gbe4k.core.instructions.bit

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set
import gbe4k.core.instructions.Mode
import kotlin.math.absoluteValue

class Sla private constructor(private val source: Any, private val mode: Mode = Mode.DIRECT) : Instruction {
    constructor(register: Register) : this(register as Any)
    constructor(address: Int) : this(address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = source.get(cpu, mode).toInt().absoluteValue
        val result = value.shl(1)

        source.set(result.toByte(), cpu)

        cpu.flags.z = result.and(0xff) == 0
        cpu.flags.n = false
        cpu.flags.h = false
        cpu.flags.c = result > 0xff
    }
}
