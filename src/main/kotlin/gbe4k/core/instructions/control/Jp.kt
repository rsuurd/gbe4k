package gbe4k.core.instructions.control

import gbe4k.core.Cpu

open class Jp(private val address: Int, z: Boolean? = null, c: Boolean? = null) : AbstractJump(z, c) {
    override fun jump(cpu: Cpu) {
        cpu.pc = address
    }
}
