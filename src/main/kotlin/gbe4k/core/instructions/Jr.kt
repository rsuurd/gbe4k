package gbe4k.core.instructions

import gbe4k.core.Cpu

class Jr(private val offset: Byte, z: Boolean? = null, c: Boolean? = null) : AbstractJump(z, c) {
    override fun jump(cpu: Cpu) {
        cpu.pc += offset
    }
}
