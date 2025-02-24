package gbe4k.core.instructions

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Cpu.Companion.hi
import gbe4k.core.Cpu.Companion.lo
import gbe4k.core.Cpu.Companion.n16
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

open class Ld private constructor(
    private val destination: Any,
    private val source: Any,
    private val mode: Mode,
) : Instruction {
    // LD to register
    constructor(destination: Register, source: Register) : this(destination as Any, source, Mode.DIRECT)
    constructor(destination: Register, source: Int, mode: Mode = Mode.DIRECT) : this(destination as Any, source, mode)
    constructor(destination: Register, source: Byte, mode: Mode = Mode.DIRECT) : this(destination as Any, source, mode)

    // LD to memory address
    constructor(destination: Int, source: Register, mode: Mode = Mode.DIRECT) : this(destination as Any, source, mode)
    constructor(destination: Byte, source: Register, mode: Mode = Mode.DIRECT) : this(destination as Any, source, mode)
    constructor(destination: Int, source: Byte, mode: Mode = Mode.DIRECT) : this(destination as Any, source, mode)

    override fun execute(cpu: Cpu) {
        when (destination) {
            is Register -> writeToRegister(cpu)
            is Int, is Byte -> writeToMemory(cpu)
            else -> throw IllegalArgumentException("Can not $this")
        }
    }

    private fun writeToRegister(cpu: Cpu) {
        val value = resolveValue(cpu)

        when (val register = destination as Register) {
            AF, BC, DE, HL, SP -> cpu.registers[register] = value as Int
            A, B, C, D, E, H, L -> cpu.registers[register] = value as Byte
            else -> throw IllegalArgumentException("Can not write to $register")
        }
    }

    private fun writeToMemory(cpu: Cpu) {
        val address = resolveAddress(destination)

        when (val value = resolveValue(cpu)) {
            is Int -> {
                cpu.bus.write(address, value.hi())
                cpu.bus.write(address + 1, value.lo())
            }

            is Byte -> cpu.bus.write(address, value)
        }
    }

    private fun resolveAddress(address: Any) = when (address) {
        is Int -> address
        is Byte -> n16(0xff.toByte(), address)
        else -> throw IllegalArgumentException("$address is not an address")
    }

    private fun resolveValue(cpu: Cpu): Any {
        val value = when (source) {
            is Register -> readFromRegister(cpu, source)
            is Int, is Byte -> source
            else -> throw IllegalArgumentException("Can not resolve value from $source")
        }

        return if (mode == Mode.INDIRECT) {
            readFromMemory(cpu, value)
        } else {
            value
        }
    }

    private fun readFromMemory(cpu: Cpu, address: Any) =
        cpu.bus.read(resolveAddress(address))

    private fun readFromRegister(cpu: Cpu, register: Register) = when (register) {
        AF, BC, DE, HL, SP -> cpu.registers[register]
        A, B, C, D, E, H, L -> cpu.registers[register].toByte()
        else -> throw IllegalArgumentException("Can not read from $register")
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

    enum class Mode {
        DIRECT, INDIRECT
    }
}
