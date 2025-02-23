package gbe4k.core.instructions

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hex

class Jp(private val address: Int) : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.pc = address
    }

    override fun toString() = "JP ${address.hex()}"
}
