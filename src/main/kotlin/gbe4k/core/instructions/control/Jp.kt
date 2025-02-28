package gbe4k.core.instructions.control

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hex

open class Jp(protected val address: Int, z: Boolean? = null, c: Boolean? = null) : AbstractJump(z, c) {
    override fun jump(cpu: Cpu) {
        cpu.pc = address
    }

    override fun toString(): String = "JP ${condition()} ${address.hex()}"
}
