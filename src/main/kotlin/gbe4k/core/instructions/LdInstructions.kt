package gbe4k.core.instructions

import gbe4k.core.Bus
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.hi
import gbe4k.core.Cpu.Companion.lo
import gbe4k.core.Cpu.Companion.n16
import gbe4k.core.instructions.LdInstructions.Ld
import gbe4k.core.instructions.LdInstructions.Pop
import gbe4k.core.instructions.LdInstructions.Push
import kotlin.experimental.and

object LdInstructions : Decoder {
    private fun Bus.writeInt(address: Int, value: Int) {
        write(address, value.lo())
        write(address + 1, value.hi())
    }

    override fun decode(opcode: Byte): Instruction? = when (opcode.asInt()) {
        0x01 -> Ld { cpu -> cpu.registers.bc = cpu.readInt() }
        0x11 -> Ld { cpu -> cpu.registers.de = cpu.readInt() }
        0x21 -> Ld { cpu -> cpu.registers.hl = cpu.readInt() }
        0x31 -> Ld { cpu -> cpu.registers.sp = cpu.readInt() }
        0x02 -> Ld { cpu -> cpu.bus.write(cpu.registers.bc, cpu.registers.a) }
        0x12 -> Ld { cpu -> cpu.bus.write(cpu.registers.de, cpu.registers.a) }
        0x22 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl++, cpu.registers.a) }
        0x32 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl--, cpu.registers.a) }
        0x06 -> Ld { cpu -> cpu.registers.b = cpu.read() }
        0x16 -> Ld { cpu -> cpu.registers.d = cpu.read() }
        0x26 -> Ld { cpu -> cpu.registers.h = cpu.read() }
        0x36 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl, cpu.read()) }
        0x08 -> Ld { cpu -> cpu.bus.writeInt(cpu.readInt(), cpu.registers.sp) }
        0x0a -> Ld { cpu -> cpu.registers.a = cpu.bus.read(cpu.registers.bc) }
        0x1a -> Ld { cpu -> cpu.registers.a = cpu.bus.read(cpu.registers.de) }
        0x2a -> Ld { cpu -> cpu.registers.a = cpu.bus.read(cpu.registers.hl++) }
        0x3a -> Ld { cpu -> cpu.registers.a = cpu.bus.read(cpu.registers.hl--) }
        0x0e -> Ld { cpu -> cpu.registers.c = cpu.read() }
        0x1e -> Ld { cpu -> cpu.registers.e = cpu.read() }
        0x2e -> Ld { cpu -> cpu.registers.l = cpu.read() }
        0x3e -> Ld { cpu -> cpu.registers.a = cpu.read() }
        0x40 -> Ld { cpu -> cpu.registers.b = cpu.registers.b }
        0x41 -> Ld { cpu -> cpu.registers.b = cpu.registers.c }
        0x42 -> Ld { cpu -> cpu.registers.b = cpu.registers.d }
        0x43 -> Ld { cpu -> cpu.registers.b = cpu.registers.e }
        0x44 -> Ld { cpu -> cpu.registers.b = cpu.registers.h }
        0x45 -> Ld { cpu -> cpu.registers.b = cpu.registers.l }
        0x46 -> Ld { cpu -> cpu.registers.b = cpu.bus.read(cpu.registers.hl) }
        0x47 -> Ld { cpu -> cpu.registers.b = cpu.registers.a }
        0x48 -> Ld { cpu -> cpu.registers.c = cpu.registers.b }
        0x49 -> Ld { cpu -> cpu.registers.c = cpu.registers.c }
        0x4a -> Ld { cpu -> cpu.registers.c = cpu.registers.d }
        0x4b -> Ld { cpu -> cpu.registers.c = cpu.registers.e }
        0x4c -> Ld { cpu -> cpu.registers.c = cpu.registers.h }
        0x4d -> Ld { cpu -> cpu.registers.c = cpu.registers.l }
        0x4e -> Ld { cpu -> cpu.registers.c = cpu.bus.read(cpu.registers.hl) }
        0x4f -> Ld { cpu -> cpu.registers.c = cpu.registers.a }
        0x50 -> Ld { cpu -> cpu.registers.d = cpu.registers.b }
        0x51 -> Ld { cpu -> cpu.registers.d = cpu.registers.c }
        0x52 -> Ld { cpu -> cpu.registers.d = cpu.registers.d }
        0x53 -> Ld { cpu -> cpu.registers.d = cpu.registers.e }
        0x54 -> Ld { cpu -> cpu.registers.d = cpu.registers.h }
        0x55 -> Ld { cpu -> cpu.registers.d = cpu.registers.l }
        0x56 -> Ld { cpu -> cpu.registers.d = cpu.bus.read(cpu.registers.hl) }
        0x57 -> Ld { cpu -> cpu.registers.d = cpu.registers.a }
        0x58 -> Ld { cpu -> cpu.registers.e = cpu.registers.b }
        0x59 -> Ld { cpu -> cpu.registers.e = cpu.registers.c }
        0x5a -> Ld { cpu -> cpu.registers.e = cpu.registers.d }
        0x5b -> Ld { cpu -> cpu.registers.e = cpu.registers.e }
        0x5c -> Ld { cpu -> cpu.registers.e = cpu.registers.h }
        0x5d -> Ld { cpu -> cpu.registers.e = cpu.registers.l }
        0x5e -> Ld { cpu -> cpu.registers.e = cpu.bus.read(cpu.registers.hl) }
        0x5f -> Ld { cpu -> cpu.registers.e = cpu.registers.a }
        0x60 -> Ld { cpu -> cpu.registers.h = cpu.registers.b }
        0x61 -> Ld { cpu -> cpu.registers.h = cpu.registers.c }
        0x62 -> Ld { cpu -> cpu.registers.h = cpu.registers.d }
        0x63 -> Ld { cpu -> cpu.registers.h = cpu.registers.e }
        0x64 -> Ld { cpu -> cpu.registers.h = cpu.registers.h }
        0x65 -> Ld { cpu -> cpu.registers.h = cpu.registers.l }
        0x66 -> Ld { cpu -> cpu.registers.h = cpu.bus.read(cpu.registers.hl) }
        0x67 -> Ld { cpu -> cpu.registers.h = cpu.registers.a }
        0x68 -> Ld { cpu -> cpu.registers.l = cpu.registers.b }
        0x69 -> Ld { cpu -> cpu.registers.l = cpu.registers.c }
        0x6a -> Ld { cpu -> cpu.registers.l = cpu.registers.d }
        0x6b -> Ld { cpu -> cpu.registers.l = cpu.registers.e }
        0x6c -> Ld { cpu -> cpu.registers.l = cpu.registers.h }
        0x6d -> Ld { cpu -> cpu.registers.l = cpu.registers.l }
        0x6e -> Ld { cpu -> cpu.registers.l = cpu.bus.read(cpu.registers.hl) }
        0x6f -> Ld { cpu -> cpu.registers.l = cpu.registers.a }
        0x70 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl, cpu.registers.b) }
        0x71 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl, cpu.registers.c) }
        0x72 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl, cpu.registers.d) }
        0x73 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl, cpu.registers.e) }
        0x74 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl, cpu.registers.h) }
        0x75 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl, cpu.registers.l) }
        0x77 -> Ld { cpu -> cpu.bus.write(cpu.registers.hl, cpu.registers.a) }
        0x78 -> Ld { cpu -> cpu.registers.a = cpu.registers.b }
        0x79 -> Ld { cpu -> cpu.registers.a = cpu.registers.c }
        0x7a -> Ld { cpu -> cpu.registers.a = cpu.registers.d }
        0x7b -> Ld { cpu -> cpu.registers.a = cpu.registers.e }
        0x7c -> Ld { cpu -> cpu.registers.a = cpu.registers.h }
        0x7d -> Ld { cpu -> cpu.registers.a = cpu.registers.l }
        0x7e -> Ld { cpu -> cpu.registers.a = cpu.bus.read(cpu.registers.hl) }
        0x7f -> Ld { cpu -> cpu.registers.a = cpu.registers.a }
        0xe0 -> Ld { cpu -> cpu.bus.write(n16(0xff.toByte(), cpu.read()), cpu.registers.a) }
        0xea -> Ld { cpu -> cpu.bus.write(cpu.readInt(), cpu.registers.a) }
        0xf0 -> Ld { cpu -> cpu.registers.a = cpu.bus.read(n16(0xff.toByte(), cpu.read())) }
        0xfa -> Ld { cpu -> cpu.registers.a = cpu.bus.read(cpu.readInt()) }
        0xe2 -> Ld { cpu -> cpu.bus.write(n16(0xff.toByte(), cpu.registers.c), cpu.registers.a) }
        0xf2 -> Ld { cpu -> cpu.registers.a = cpu.bus.read(n16(0xff.toByte(), cpu.registers.c)) }
        0xf8 -> Ld { cpu ->
            val value = cpu.read()
            cpu.registers.hl = cpu.registers.sp + value

            cpu.flags.z = false
            cpu.flags.n = false
            cpu.flags.h = cpu.registers.sp.and(0x0f) + value.and(0x0f) > 0xf
            cpu.flags.c = cpu.registers.sp.and(0xff) + value.asInt() > 0xff

            cpu.cycle()
        }

        0xf9 -> Ld { cpu ->
            cpu.registers.sp = cpu.registers.hl
            cpu.cycle()
        }

        0xc1 -> Pop { cpu -> cpu.registers.bc = cpu.stack.pop() }
        0xd1 -> Pop { cpu -> cpu.registers.de = cpu.stack.pop() }
        0xe1 -> Pop { cpu -> cpu.registers.hl = cpu.stack.pop() }
        0xf1 -> Pop { cpu -> cpu.registers.af = cpu.stack.pop() }

        0xc5 -> Push { cpu ->
            cpu.stack.push(cpu.registers.bc)
            cpu.cycle()
        }

        0xd5 -> Push { cpu ->
            cpu.stack.push(cpu.registers.de)
            cpu.cycle()
        }

        0xe5 -> Push { cpu ->
            cpu.stack.push(cpu.registers.hl)
            cpu.cycle()
        }

        0xf5 -> Push { cpu ->
            cpu.stack.push(cpu.registers.af)
            cpu.cycle()
        }

        else -> null
    }

    fun interface Ld : Instruction
    fun interface Push : Instruction
    fun interface Pop : Instruction
}
