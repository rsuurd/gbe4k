package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.Mode
import kotlin.experimental.and

class Sbc private constructor(
    private val source: Any,
    private val mode: Mode = Mode.DIRECT
) : Instruction {
    constructor(source: Register) : this(source as Any)
    constructor(value: Byte) : this(value as Any)
    constructor(address: Int) : this(address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = source.get(cpu, mode).toInt().and(0xff)
        val carry = if (cpu.flags.c) 1 else 0
        val result = cpu.registers.a.toInt().and(0xff) - value - carry

        cpu.flags.z = result == 0
        cpu.flags.n = true
        cpu.flags.h = cpu.registers.a.and(0x0f) - value.toByte().and(0x0f) - carry < 0
        cpu.flags.c = result < 0

        cpu.registers.a = result.toByte()
    }
}
