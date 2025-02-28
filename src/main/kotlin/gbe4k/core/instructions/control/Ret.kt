package gbe4k.core.instructions.control

import gbe4k.core.Cpu

open class Ret(z: Boolean? = null, c: Boolean? = null) : AbstractJump(z, c) {
    override fun jump(cpu: Cpu) {
        cpu.pc = cpu.stack.pop()
    }

    override fun toString(): String = "RET ${condition()}"
}

object Reti : Ret() {
    override fun execute(cpu: Cpu) {
        super.execute(cpu)

        cpu.interrupts.ime = true
    }

    override fun toString(): String = "RETI"
}
