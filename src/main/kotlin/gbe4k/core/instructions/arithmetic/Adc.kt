package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.Mode
import kotlin.experimental.and
import kotlin.math.absoluteValue

class Adc private constructor(
    private val source: Any,
    private val mode: Mode = Mode.DIRECT
) : Instruction {
    constructor(source: Register) : this(source as Any)
    constructor(value: Byte) : this(value as Any)
    constructor(address: Int) : this(address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = source.get(cpu, mode).toInt().and(0xff)
        val carry = if (cpu.flags.c) 1 else 0
        val result = cpu.registers.a.toInt().and(0xff) + value + carry

        cpu.flags.z = result.and(0xff) == 0
        cpu.flags.n = false
        cpu.flags.h = cpu.registers.a.and(0x0f) + value.and(0x0f) + carry > 0x0f
        cpu.flags.c = result.absoluteValue > 0xff
        cpu.registers.a = result.toByte()
    }
}
