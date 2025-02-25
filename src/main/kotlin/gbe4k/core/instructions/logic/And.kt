package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.Mode
import kotlin.experimental.and

class And private constructor(private val source: Any, private val mode: Mode = Mode.DIRECT): Instruction {
    constructor(register: Register) : this(register as Any)
    constructor(value: Byte) : this(value as Any)
    constructor(address: Int) : this(address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val current = source.get(cpu, mode) as Byte
        val value = cpu.registers.a.and(current)
        cpu.registers.a = value

        cpu.flags.z = value == 0x00.toByte()
        cpu.flags.n = false
        cpu.flags.h = true
        cpu.flags.c = false
    }
}
