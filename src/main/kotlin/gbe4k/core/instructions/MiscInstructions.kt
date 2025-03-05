package gbe4k.core.instructions

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.instructions.bit.ExtendedInstructions
import gbe4k.core.instructions.bit.RotateInstructions.Rl
import gbe4k.core.instructions.bit.RotateInstructions.Rlc
import gbe4k.core.instructions.bit.RotateInstructions.Rr
import gbe4k.core.instructions.bit.RotateInstructions.Rrc
import gbe4k.core.instructions.bit.RotateInstructions.rl
import gbe4k.core.instructions.bit.RotateInstructions.rlc
import gbe4k.core.instructions.bit.RotateInstructions.rr
import gbe4k.core.instructions.bit.RotateInstructions.rrc

object MiscInstructions : Decoder {
    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0x00 -> Nop
        0x10 -> Stop
        0x76 -> Halt
        0x07 -> Rlc { cpu ->
            cpu.registers.a = cpu.registers.a.rlc(cpu.flags)
            cpu.flags.z = false
        }

        0x17 -> Rl { cpu ->
            cpu.registers.a = cpu.registers.a.rl(cpu.flags)
            cpu.flags.z = false
        }

        0x0f -> Rrc { cpu ->
            cpu.registers.a = cpu.registers.a.rrc(cpu.flags)
            cpu.flags.z = false
        }

        0x1f -> Rr { cpu ->
            cpu.registers.a = cpu.registers.a.rr(cpu.flags)
            cpu.flags.z = false
        }

        0xcb -> Instruction { cpu ->
            // this looks weird, decoder should probably just be fetchNext(cpu: Cpu) instead
            ExtendedInstructions.decode(cpu.read())?.execute(cpu)
        }
        0xf3 -> Di
        0xfb -> Ei
        else -> null
    }
}

object Nop : Instruction {
    override fun execute(cpu: Cpu) {}
}

object Halt : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.halted = true
    }
}

object Stop : Instruction {
    override fun execute(cpu: Cpu) {}
}

object Di : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.interrupts.ime = false
    }
}

object Ei : Instruction {
    override fun execute(cpu: Cpu) {
        cpu.interrupts.enableIme = true
    }
}
