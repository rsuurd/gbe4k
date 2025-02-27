package gbe4k.core.instructions.control

import gbe4k.core.Cpu
import gbe4k.core.instructions.Instruction

abstract class AbstractJump(val z: Boolean?, val c: Boolean?) : Instruction {
    override fun execute(cpu: Cpu) {
        if (isConditionTrue(cpu)) {
            jump(cpu)
        }
    }

    protected abstract fun jump(cpu: Cpu)

    private fun isConditionTrue(cpu: Cpu) = z?.let {
        cpu.flags.z == it
    } ?: c?.let {
        cpu.flags.c == it
    } ?: true
}
