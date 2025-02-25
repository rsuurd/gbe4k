package gbe4k.core

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
import gbe4k.core.instructions.Call
import gbe4k.core.instructions.Di
import gbe4k.core.instructions.arithmetic.Inc
import gbe4k.core.instructions.Jp
import gbe4k.core.instructions.Jr
import gbe4k.core.instructions.Ld
import gbe4k.core.instructions.Mode.INDIRECT
import gbe4k.core.instructions.Nop
import gbe4k.core.instructions.Pop
import gbe4k.core.instructions.Push
import gbe4k.core.instructions.Ret
import gbe4k.core.instructions.Reti
import gbe4k.core.instructions.Rst
import gbe4k.core.instructions.arithmetic.Dec
import gbe4k.core.instructions.logic.And
import gbe4k.core.instructions.logic.Ccf
import gbe4k.core.instructions.logic.Cp
import gbe4k.core.instructions.logic.Cpl
import gbe4k.core.instructions.logic.Or
import gbe4k.core.instructions.logic.Xor
import kotlin.experimental.and

class Cpu(val bus: Bus) {
    var pc = 0x0100

    val registers = Registers()
    val flags = Flags(registers)
    val stack = Stack(bus, registers)

    var ime = true

    fun step() {
        val instruction = nextInstruction()

        instruction.execute(this)
    }

    private fun nextInstruction() = when (val opcode = read()) {
        0x00.toByte() -> Nop

        // inc
        0x03.toByte() -> Inc(BC)
        0x04.toByte() -> Inc(B)
        0x0c.toByte() -> Inc(C)
        0x13.toByte() -> Inc(DE)
        0x14.toByte() -> Inc(D)
        0x1c.toByte() -> Inc(E)
        0x23.toByte() -> Inc(HL)
        0x24.toByte() -> Inc(H)
        0x2c.toByte() -> Inc(L)
        0x33.toByte() -> Inc(SP)
        0x34.toByte() -> Inc(registers.hl)
        0x3c.toByte() -> Inc(A)
        //dec
        0x0b.toByte() -> Dec(BC)
        0x05.toByte() -> Dec(B)
        0x0d.toByte() -> Dec(C)
        0x1b.toByte() -> Dec(DE)
        0x15.toByte() -> Dec(D)
        0x1d.toByte() -> Dec(E)
        0x2b.toByte() -> Dec(HL)
        0x25.toByte() -> Dec(H)
        0x2d.toByte() -> Dec(L)
        0x3b.toByte() -> Dec(SP)
        0x35.toByte() -> Dec(registers.hl)
        0x3d.toByte() -> Dec(A)
        // all supported ld instructions
        0x01.toByte() -> Ld(BC, readInt())
        0x02.toByte() -> Ld(registers.bc, A)
        0x06.toByte() -> Ld(B, read())
        0x08.toByte() -> Ld(readInt(), SP)
        0x0a.toByte() -> Ld(A, registers.bc, mode = INDIRECT)
        0x0e.toByte() -> Ld(C, read())
        0x11.toByte() -> Ld(DE, readInt())
        0x12.toByte() -> Ld(registers.de, A)
        0x16.toByte() -> Ld(D, read())
        0x1a.toByte() -> Ld(A, registers.de, mode = INDIRECT)
        0x1e.toByte() -> Ld(E, read())
        0x21.toByte() -> Ld(HL, readInt())
        0x22.toByte() -> Ld(registers.hl++, A)
        0x26.toByte() -> Ld(H, read())
        0x2a.toByte() -> Ld(A, registers.hl++, mode = INDIRECT)
        0x2e.toByte() -> Ld(L, read())
        0x31.toByte() -> Ld(SP, readInt())
        0x32.toByte() -> Ld(registers.hl--, A)
        0x36.toByte() -> Ld(registers.hl, read())
        0x3a.toByte() -> Ld(A, registers.hl--, mode = INDIRECT)
        0x3e.toByte() -> Ld(A, read())
        0x40.toByte() -> Ld(B, B)
        0x41.toByte() -> Ld(B, C)
        0x42.toByte() -> Ld(B, D)
        0x43.toByte() -> Ld(B, E)
        0x44.toByte() -> Ld(B, H)
        0x45.toByte() -> Ld(B, L)
        0x46.toByte() -> Ld(B, registers.hl, mode = INDIRECT)
        0x47.toByte() -> Ld(B, A)
        0x48.toByte() -> Ld(C, B)
        0x49.toByte() -> Ld(C, C)
        0x4a.toByte() -> Ld(C, D)
        0x4b.toByte() -> Ld(C, E)
        0x4c.toByte() -> Ld(C, H)
        0x4d.toByte() -> Ld(C, L)
        0x4e.toByte() -> Ld(C, registers.hl, mode = INDIRECT)
        0x4f.toByte() -> Ld(C, A)
        0x50.toByte() -> Ld(D, B)
        0x51.toByte() -> Ld(D, C)
        0x52.toByte() -> Ld(D, D)
        0x53.toByte() -> Ld(D, E)
        0x54.toByte() -> Ld(D, H)
        0x55.toByte() -> Ld(D, L)
        0x56.toByte() -> Ld(D, registers.hl, mode = INDIRECT)
        0x57.toByte() -> Ld(D, A)
        0x58.toByte() -> Ld(E, B)
        0x59.toByte() -> Ld(E, C)
        0x5a.toByte() -> Ld(E, D)
        0x5b.toByte() -> Ld(E, E)
        0x5c.toByte() -> Ld(E, H)
        0x5d.toByte() -> Ld(E, L)
        0x5e.toByte() -> Ld(E, registers.hl, mode = INDIRECT)
        0x5f.toByte() -> Ld(E, A)
        0x60.toByte() -> Ld(H, B)
        0x61.toByte() -> Ld(H, C)
        0x62.toByte() -> Ld(H, D)
        0x63.toByte() -> Ld(H, E)
        0x64.toByte() -> Ld(H, H)
        0x65.toByte() -> Ld(H, L)
        0x66.toByte() -> Ld(H, registers.hl, mode = INDIRECT)
        0x67.toByte() -> Ld(H, A)
        0x68.toByte() -> Ld(L, B)
        0x69.toByte() -> Ld(L, C)
        0x6a.toByte() -> Ld(L, D)
        0x6b.toByte() -> Ld(L, E)
        0x6c.toByte() -> Ld(L, H)
        0x6d.toByte() -> Ld(L, L)
        0x6e.toByte() -> Ld(L, registers.hl, mode = INDIRECT)
        0x6f.toByte() -> Ld(L, A)
        0x70.toByte() -> Ld(registers.hl, B)
        0x71.toByte() -> Ld(registers.hl, C)
        0x72.toByte() -> Ld(registers.hl, D)
        0x73.toByte() -> Ld(registers.hl, E)
        0x74.toByte() -> Ld(registers.hl, H)
        0x75.toByte() -> Ld(registers.hl, L)
        0x77.toByte() -> Ld(registers.hl, A)
        0x78.toByte() -> Ld(A, B)
        0x79.toByte() -> Ld(A, C)
        0x7a.toByte() -> Ld(A, D)
        0x7b.toByte() -> Ld(A, E)
        0x7c.toByte() -> Ld(A, H)
        0x7d.toByte() -> Ld(A, L)
        0x7e.toByte() -> Ld(A, registers.hl, mode = INDIRECT)
        0x7f.toByte() -> Ld(A, A)
        0xe0.toByte() -> Ld(read(), A)
        0xe2.toByte() -> Ld(registers.c, A)
        0xea.toByte() -> Ld(readInt(), A)
        0xf0.toByte() -> Ld(A, read(), INDIRECT)
        0xf2.toByte() -> Ld(A, registers.c, INDIRECT)
        0xf8.toByte() -> Ld(HL, registers.sp + read()) // this is probably not correct
        0xf9.toByte() -> Ld(SP, HL)
        0xfa.toByte() -> Ld(A, readInt(), INDIRECT)

        0xc1.toByte() -> Pop(BC)
        0xd1.toByte() -> Pop(DE)
        0xe1.toByte() -> Pop(HL)
        0xf1.toByte() -> Pop(AF)
        0xc5.toByte() -> Push(BC)
        0xd5.toByte() -> Push(DE)
        0xe5.toByte() -> Push(HL)
        0xf5.toByte() -> Push(AF)

        // jumps/call
        0x18.toByte() -> Jr(read())
        0x20.toByte() -> Jr(read(), z = false)
        0x28.toByte() -> Jr(read(), z = true)
        0x30.toByte() -> Jr(read(), c = false)
        0x38.toByte() -> Jr(read(), c = true)
        0xc2.toByte() -> Jp(readInt(), z = false)
        0xc3.toByte() -> Jp(readInt())
        0xca.toByte() -> Jp(readInt(), z = true)
        0xd2.toByte() -> Jp(readInt(), c = false)
        0xda.toByte() -> Jp(readInt(), c = true)
        0xe9.toByte() -> Jp(registers.hl)
        0xc4.toByte() -> Call(readInt(), z = false)
        0xcc.toByte() -> Call(readInt(), z = true)
        0xcd.toByte() -> Call(readInt())
        0xd4.toByte() -> Call(readInt(), c = false)
        0xdc.toByte() -> Call(readInt(), c = true)
        0xc0.toByte() -> Ret(z = false)
        0xc8.toByte() -> Ret(z = true)
        0xc9.toByte() -> Ret()
        0xd0.toByte() -> Ret(c = false)
        0xd8.toByte() -> Ret(c = true)
        0xd9.toByte() -> Reti
        0xc7.toByte() -> Rst(0x00)
        0xcf.toByte() -> Rst(0x08)
        0xd7.toByte() -> Rst(0x10)
        0xdf.toByte() -> Rst(0x18)
        0xe7.toByte() -> Rst(0x20)
        0xef.toByte() -> Rst(0x28)
        0xf7.toByte() -> Rst(0x30)
        0xff.toByte() -> Rst(0x38)

        // logic
        0x2f.toByte() -> Cpl
        0x3f.toByte() -> Ccf
        0xa0.toByte() -> And(B)
        0xa1.toByte() -> And(C)
        0xa2.toByte() -> And(D)
        0xa3.toByte() -> And(E)
        0xa4.toByte() -> And(H)
        0xa5.toByte() -> And(L)
        0xa6.toByte() -> And(registers.hl)
        0xa7.toByte() -> And(A)
        0xe6.toByte() -> And(read())
        0xa8.toByte() -> Xor(B)
        0xa9.toByte() -> Xor(C)
        0xaa.toByte() -> Xor(D)
        0xab.toByte() -> Xor(E)
        0xac.toByte() -> Xor(H)
        0xad.toByte() -> Xor(L)
        0xae.toByte() -> Xor(registers.hl)
        0xaf.toByte() -> Xor(A)
        0xee.toByte() -> Xor(read())
        0xb0.toByte() -> Or(B)
        0xb1.toByte() -> Or(C)
        0xb2.toByte() -> Or(D)
        0xb3.toByte() -> Or(E)
        0xb4.toByte() -> Or(H)
        0xb5.toByte() -> Or(L)
        0xb6.toByte() -> Or(registers.hl)
        0xb7.toByte() -> Or(A)
        0xf6.toByte() -> Or(read())
        0xb8.toByte() -> Cp(B)
        0xb9.toByte() -> Cp(C)
        0xba.toByte() -> Cp(D)
        0xbb.toByte() -> Cp(E)
        0xbc.toByte() -> Cp(H)
        0xbd.toByte() -> Cp(L)
        0xbe.toByte() -> Cp(registers.hl)
        0xbf.toByte() -> Cp(A)
        0xfe.toByte() -> Cp(read())

        // other
        0xf3.toByte() -> Di

        else -> TODO("Unsupported opcode: ${opcode.hex()}")
    }

    private fun read() = bus.read(pc++)

    private fun readInt(): Int {
        val lo = read()
        val hi = read()

        return n16(hi, lo)
    }

    companion object {
        fun n8(hiNibble: Byte, loNibble: Byte) = hiNibble.toInt().shl(4) + loNibble
        fun n16(hi: Byte, lo: Byte) = hi.toInt().and(0xff).shl(8) + lo.toInt().and(0xff)

        fun Int.hi() = and(0xff00).shr(8).toByte()
        fun Int.lo() = and(0xff).toByte()

        fun Byte.hiNibble() = toInt().shr(4).and(0xf).toByte()
        fun Byte.loNibble() = and(0x0f)

        fun Byte.hex() = "0x%02x".format(this)
        fun Int.hex() = "0x%04x".format(this)

        fun Number.address() = when (this) {
            is Int -> this
            is Byte -> n16(0xff.toByte(), this)
            else -> throw IllegalArgumentException("$this is not an address")
        }
    }
}
