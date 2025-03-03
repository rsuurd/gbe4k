package gbe4k.core.instructions

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.instructions.JumpInstructions.Call
import gbe4k.core.instructions.JumpInstructions.Jp
import gbe4k.core.instructions.JumpInstructions.Jr
import gbe4k.core.instructions.JumpInstructions.Ret
import gbe4k.core.instructions.JumpInstructions.Reti
import gbe4k.core.instructions.JumpInstructions.Rst

object JumpInstructions : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0x18 -> Jr { cpu ->
            val offset = cpu.read()
            cpu.pc += offset
        }
        0x20 -> Jr { cpu ->
            val offset = cpu.read()
            if (!cpu.flags.z) {
                cpu.pc += offset
            }
        }

        0x28 -> Jr { cpu ->
            val offset = cpu.read()

            if (cpu.flags.z) {
                cpu.pc += offset
            }
        }

        0x30 -> Jr { cpu ->
            val offset = cpu.read()

            if (!cpu.flags.c) {
                cpu.pc += offset
            }
        }

        0x38 -> Jr { cpu ->
            val offset = cpu.read()

            if (cpu.flags.c) {
                cpu.pc += offset
            }
        }

        0xc2 -> Jp { cpu ->
            val address = cpu.readInt()

            if (!cpu.flags.z) {
                cpu.pc = address
            }
        }

        0xc3 -> Jp { cpu ->
            val address = cpu.readInt()
            cpu.pc = address
        }

        0xca -> Jp { cpu ->
            val address = cpu.readInt()

            if (cpu.flags.z) {
                cpu.pc = address
            }
        }

        0xd2 -> Jp { cpu ->
            val address = cpu.readInt()

            if (!cpu.flags.c) {
                cpu.pc = address
            }
        }

        0xda -> Jp { cpu ->
            val address = cpu.readInt()

            if (cpu.flags.c) {
                cpu.pc = address
            }
        }

        0xe9 -> Jp { cpu -> cpu.pc = cpu.registers.hl }
        0xc4 -> Call { cpu ->
            val address = cpu.readInt()

            if (!cpu.flags.z) {
                cpu.stack.push(cpu.pc)
                cpu.pc = address
            }
        }

        0xcc -> Call { cpu ->
            val address = cpu.readInt()

            if (cpu.flags.z) {
                cpu.stack.push(cpu.pc)
                cpu.pc = address
            }
        }

        0xcd -> Call { cpu ->
            val address = cpu.readInt()
            cpu.stack.push(cpu.pc)
            cpu.pc = address
        }

        0xd4 -> Call { cpu ->
            val address = cpu.readInt()

            if (!cpu.flags.c) {
                cpu.stack.push(cpu.pc)
                cpu.pc = address
            }
        }

        0xdc -> Call { cpu ->
            val address = cpu.readInt()

            if (cpu.flags.c) {
                cpu.stack.push(cpu.pc)
                cpu.pc = address
            }
        }

        0xc0 -> Ret { cpu ->
            if (!cpu.flags.z) {
                cpu.pc = cpu.stack.pop()
            }
        }

        0xc8 -> Ret { cpu ->
            if (cpu.flags.z) {
                cpu.pc = cpu.stack.pop()
            }
        }

        0xc9 -> Ret { cpu ->
            cpu.pc = cpu.stack.pop()
        }

        0xd0 -> Ret { cpu ->
            if (!cpu.flags.c) {
                cpu.pc = cpu.stack.pop()
            }
        }

        0xd8 -> Ret { cpu ->
            if (cpu.flags.c) {
                cpu.pc = cpu.stack.pop()
            }
        }

        0xd9 -> Reti { cpu ->
            cpu.pc = cpu.stack.pop()
            cpu.interrupts.ime = true
        }

        0xc7 -> Rst { cpu ->
            cpu.stack.push(cpu.pc)
            cpu.pc = 0x00
        }

        0xcf -> Rst { cpu ->
            cpu.stack.push(cpu.pc)
            cpu.pc = 0x08
        }

        0xd7 -> Rst { cpu ->
            cpu.stack.push(cpu.pc)
            cpu.pc = 0x10
        }

        0xdf -> Rst { cpu ->
            cpu.stack.push(cpu.pc)
            cpu.pc = 0x18
        }

        0xe7 -> Rst { cpu ->
            cpu.stack.push(cpu.pc)
            cpu.pc = 0x20
        }

        0xef -> Rst { cpu ->
            cpu.stack.push(cpu.pc)
            cpu.pc = 0x28
        }

        0xf7 -> Rst { cpu ->
            cpu.stack.push(cpu.pc)
            cpu.pc = 0x30
        }

        0xff -> Rst { cpu ->
            cpu.stack.push(cpu.pc)
            cpu.pc = 0x38
        }

        else -> null
    }

    fun interface Jp : Instruction
    fun interface Jr : Instruction
    fun interface Call : Instruction
    fun interface Ret : Instruction
    fun interface Reti : Instruction
    fun interface Rst : Instruction
}
