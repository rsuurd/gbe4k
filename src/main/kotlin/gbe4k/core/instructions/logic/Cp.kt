package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.Mode
import kotlin.experimental.and

class Cp private constructor(private val source: Any, private val mode: Mode = Mode.DIRECT): Instruction {
    constructor(register: Register) : this(register as Any)
    constructor(value: Byte) : this(value as Any)
    constructor(address: Int) : this(address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = source.get(cpu, mode) as Byte
        val result = cpu.registers.a - value

        cpu.flags.z = result == 0x00
        cpu.flags.n = true
        cpu.flags.h = cpu.registers.a.and(0x0f) - value.and(0x0f) < 0
        cpu.flags.c = result < 0
    }

    override fun toString(): String = "CP ${if (source is Byte) source.hex() else source}"
}
