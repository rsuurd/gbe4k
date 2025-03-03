package gbe4k.core.instructions

import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.instructions.arithmetic.ArithmeticInstructions
import gbe4k.core.instructions.logic.LogicInstructions

interface Decoder {
    fun decode(opcode: Byte): Instruction?
}

object InstructionDecoder : Decoder {
    override fun decode(opcode: Byte): Instruction {
        return MiscInstructions.decode(opcode) ?: LdInstructions.decode(opcode) ?: LogicInstructions.decode(opcode)
        ?: JumpInstructions.decode(opcode) ?: ArithmeticInstructions.decode(opcode)
        ?: TODO("${opcode.hex()} not supported")
    }
}
