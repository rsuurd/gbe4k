package gbe4k.core.instructions

import gbe4k.core.Cpu

open class Ret(
    private val z: Boolean? = null,
    private val c: Boolean? = null
) : Instruction {
    override fun execute(cpu: Cpu) {
        if (isConditionTrue(cpu)) {
            cpu.pc = cpu.stack.pop()
        }
    }

    private fun isConditionTrue(cpu: Cpu) = z?.let {
        cpu.flags.z == it
    } ?: c?.let {
        cpu.flags.c == it
    } ?: true
}

object Reti : Ret() {
    override fun execute(cpu: Cpu) {
        super.execute(cpu)

        cpu.ime = true
    }
}