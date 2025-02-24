package gbe4k.core.instructions

import gbe4k.core.Cpu

class Jr(
    private val offset: Byte,
    private val z: Boolean? = null,
    private val c: Boolean? = null
) : Instruction {
    override fun execute(cpu: Cpu) {
        if (isConditionTrue(cpu)) {
            cpu.pc += offset
        }
    }

    private fun isConditionTrue(cpu: Cpu) = z?.let {
        cpu.flags.z == it
    } ?: c?.let {
        cpu.flags.c == it
    } ?: true
}
