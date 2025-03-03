package gbe4k.core.instructions.bit

import gbe4k.core.instructions.Decoder

object ExtendedInstructions : Decoder {
    override fun decode(opcode: Byte) =
        RotateInstructions.decode(opcode) ?: ShiftInstructions.decode(opcode) ?: SwapInstruction.decode(opcode)
        ?: BitInstruction.decode(opcode) ?: ResInstruction.decode(opcode) ?: SetInstruction.decode(opcode)
}
