package gbe4k.core.instructions.logic

import gbe4k.core.instructions.Decoder

object LogicInstructions : Decoder {
    override fun decode(opcode: Byte) =
        AndInstructions.decode(opcode) ?: OrInstructions.decode(opcode) ?: XorInstructions.decode(opcode)
        ?: CpInstructions.decode(opcode)
}
