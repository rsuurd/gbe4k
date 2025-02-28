package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set
import gbe4k.core.instructions.Mode
import kotlin.experimental.and
import kotlin.math.absoluteValue

class Add private constructor(
    private val destination: Register,
    private val source: Any,
    private val mode: Mode = Mode.DIRECT
) : Instruction {
    constructor(destination: Register, source: Register) : this(destination, source as Any)
    constructor(destination: Register, value: Byte) : this(destination, value as Any)
    constructor(destination: Register, address: Int) : this(destination, address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = source.get(cpu, mode).toInt().and(0xffff)
        val original = destination.get(cpu).toInt().and(0xffff)
        val result = (original + value)

        if (destination.is8bit) {
            cpu.flags.z = result.and(0xff) == 0
            cpu.flags.h = original.toByte().and(0x0f) + value.toByte().and(0x0f) > 0x0f
            cpu.flags.c = result.absoluteValue > 0xff
        } else if (destination.is16bit) {
            cpu.flags.h = original.and(0x0fff) + value.and(0x0fff) > 0x0fff
            cpu.flags.c = result.absoluteValue > 0xffff
        }

        cpu.flags.n = false

        destination.set(result.and(0xffff), cpu)
    }
}
