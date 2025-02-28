package gbe4k.core.instructions.bit

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.Instruction
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set
import gbe4k.core.instructions.Mode

class Rr(private val destination: Any, private val mode: Mode) : Instruction {
    constructor(register: Register) : this(register as Any, Mode.DIRECT)
    constructor(address: Int) : this(address as Any, Mode.INDIRECT)

    override fun execute(cpu: Cpu) {
        val value = destination.get(cpu, mode).toInt().and(0xff)
        val carry = if (cpu.flags.c) 1 else 0
        val result = value.shr(1) + carry.shl(7) + value.and(0x1).shl(8)

        cpu.flags.z = result.and(0xff) == 0
        cpu.flags.n = false
        cpu.flags.h = false
        cpu.flags.c = result > 0xff

        destination.set(result.toByte(), cpu)
    }
}
