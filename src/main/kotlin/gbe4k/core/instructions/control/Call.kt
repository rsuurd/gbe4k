package gbe4k.core.instructions.control

import gbe4k.core.Cpu

open class Call(address: Int, z: Boolean? = null, c: Boolean? = null) : Jp(address, z, c) {
    override fun jump(cpu: Cpu) {
        cpu.stack.push(cpu.pc)

        super.jump(cpu)
    }
}
