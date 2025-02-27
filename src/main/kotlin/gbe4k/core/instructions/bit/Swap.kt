package gbe4k.core.instructions.bit

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hiNibble
import gbe4k.core.Cpu.Companion.lo
import gbe4k.core.Cpu.Companion.loNibble
import gbe4k.core.Cpu.Companion.n8
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set
import gbe4k.core.instructions.Mode

class Swap private constructor(private val source: Any, private val mode: Mode = Mode.DIRECT) : Instruction {
    constructor(register: Register) : this(register as Any)
    constructor(address: Int) : this(address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = source.get(cpu, mode).toInt()
        val result = n8(value.lo().loNibble(), value.lo().hiNibble())

        source.set(result.toByte(), cpu)

        cpu.flags.z = result.and(0xff) == 0
        cpu.flags.n = false
        cpu.flags.h = false
        cpu.flags.c = false
    }
}
