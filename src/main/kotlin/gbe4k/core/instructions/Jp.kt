package gbe4k.core.instructions

import gbe4k.core.Cpu

class Jp(
    private val address: Int,
    private val z: Boolean? = null,
    private val c: Boolean? = null
) : Instruction {
    override fun execute(cpu: Cpu) {
        if (isConditionTrue(cpu)) {
            cpu.pc = address
        }
    }

    private fun isConditionTrue(cpu: Cpu) = z?.let {
        cpu.flags.z == it
    } ?: c?.let {
        cpu.flags.c == it
    } ?: true
}
