package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.Register.A
import gbe4k.core.Register.B
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.E
import gbe4k.core.Register.H
import gbe4k.core.Register.L
import gbe4k.core.instructions.Instruction

abstract class BaseLogic(private val source: Any) : Instruction {
    override fun execute(cpu: Cpu) {
        val acc = cpu.registers.a
        val value = logic(acc, getValue(cpu))
        storeResult(value, cpu)
        setFlags(value, acc, cpu)
    }

    protected open fun storeResult(result: Byte, cpu: Cpu) {
        cpu.registers.a = result
    }

    private fun getValue(cpu: Cpu): Byte = when (source) {
        is Byte -> source
        is Int -> cpu.bus.read(source)
        A, B, C, D, E, H, L -> cpu.registers[source as Register].toByte()
        else -> throw IllegalArgumentException("Can't read value from $source")
    }

    abstract fun logic(acc: Byte, value: Byte): Byte

    abstract fun setFlags(value: Byte, acc: Byte, cpu: Cpu)
}