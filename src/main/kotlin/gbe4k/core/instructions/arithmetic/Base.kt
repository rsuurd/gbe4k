package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.Register.A
import gbe4k.core.Register.B
import gbe4k.core.Register.BC
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.DE
import gbe4k.core.Register.E
import gbe4k.core.Register.H
import gbe4k.core.Register.HL
import gbe4k.core.Register.L
import gbe4k.core.Register.SP
import gbe4k.core.instructions.Instruction

abstract class Base(private val destination: Any) : Instruction {
    constructor(register: Register) : this(register as Any)
    constructor(address: Int) : this(address as Any)

    override fun execute(cpu: Cpu) {
        val previous = getValue(cpu)

        if (previous is Byte) {
            val newValue = calculate(previous)
            setValue(newValue, cpu)
            setFlags(newValue, previous.toByte(), cpu)
        } else {
            val newValue = calculate(previous as Int)
            setValue(newValue, cpu)
        }
    }

    private fun getValue(cpu: Cpu): Number = when (destination) {
        is Int -> cpu.bus.read(destination)
        BC, DE, HL, SP -> cpu.registers[destination as Register]
        A, B, C, D, E, H, L -> cpu.registers[destination as Register].toByte()
        else -> throw IllegalArgumentException("Can't read value from $destination")
    }

    private fun setValue(newValue: Number, cpu: Cpu) = when (destination) {
        is Int -> cpu.bus.write(destination, newValue.toByte())
        BC, DE, HL, SP -> cpu.registers[destination as Register] = newValue as Int
        A, B, C, D, E, H, L -> cpu.registers[destination as Register] = newValue as Byte
        else -> throw IllegalArgumentException("Can't read value from $destination")
    }

    abstract fun calculate(value: Byte): Byte
    abstract fun calculate(value: Int): Int
    abstract fun setFlags(newValue: Byte, previousValue: Byte, cpu: Cpu)

    companion object {
        fun Byte.isZero() = this == 0x00.toByte()
    }
}