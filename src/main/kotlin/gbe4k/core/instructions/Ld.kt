package gbe4k.core.instructions

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Register
import gbe4k.core.Register.A
import gbe4k.core.Register.AF
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

class Ld : Instruction {
    private val destination: Any
    private val source: Any

    constructor(destination: Register, value: Int) {
        this.destination = destination
        this.source = value
    }

    constructor(destination: Register, value: Byte) {
        this.destination = destination
        this.source = value
    }

    override fun execute(cpu: Cpu) {
        when (destination) {
            is Register -> storeInRegister(cpu)
            else -> throw IllegalArgumentException("Can not $this")
        }
    }

    private fun storeInRegister(cpu: Cpu) {
        val value = resolveValue(cpu)

        when (destination as Register) {
            AF, BC, DE, HL, SP -> cpu.registers[destination] = value as Int
            A, B, C, D, E, H, L -> cpu.registers[destination] = value as Byte
            else -> throw IllegalArgumentException("Can not $this")
        }
    }

    private fun resolveValue(cpu: Cpu) = when (source) {
        AF, BC, DE, HL, SP -> cpu.registers[source as Register]
        A, B, C, D, E, H, L -> cpu.registers[source as Register].toByte()
        is Int -> source
        is Byte -> source
        else -> throw IllegalArgumentException("Can not resolve value from $source")
    }

    override fun toString(): String {
        val builder = StringBuilder("LD ")

        when (destination) {
            is Int -> builder.append(destination.hex())
            else -> builder.append(destination)
        }

        builder.append(", ")

        when (source) {
            is Byte -> builder.append(source.hex())
            is Int -> builder.append(source.hex())
            else -> builder.append(source)
        }

        return builder.toString()
    }
}