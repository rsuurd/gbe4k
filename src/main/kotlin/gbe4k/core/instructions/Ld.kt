package gbe4k.core.instructions

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Register
import gbe4k.core.instructions.InstructionSupport.get
import gbe4k.core.instructions.InstructionSupport.set

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
        destination.set(source.get(cpu, mode), cpu)
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
