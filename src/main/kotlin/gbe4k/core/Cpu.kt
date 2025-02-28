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
import gbe4k.core.instructions.control.Call
import gbe4k.core.instructions.Di
import gbe4k.core.instructions.Ei
import gbe4k.core.instructions.Halt
import gbe4k.core.instructions.control.Jp
import gbe4k.core.instructions.control.Jr
import gbe4k.core.instructions.Ld
import gbe4k.core.instructions.`LdHlSp+r8`
import gbe4k.core.instructions.Mode.INDIRECT
import gbe4k.core.instructions.Nop
import gbe4k.core.instructions.Pop
import gbe4k.core.instructions.Push
import gbe4k.core.instructions.arithmetic.Adc
import gbe4k.core.instructions.control.Ret
import gbe4k.core.instructions.control.Reti
import gbe4k.core.instructions.control.Rst
import gbe4k.core.instructions.arithmetic.Add
import gbe4k.core.instructions.arithmetic.Daa
import gbe4k.core.instructions.arithmetic.Dec
import gbe4k.core.instructions.arithmetic.Inc
import gbe4k.core.instructions.arithmetic.Sbc
import gbe4k.core.instructions.arithmetic.Scf
import gbe4k.core.instructions.arithmetic.Sub
import gbe4k.core.instructions.bit.Bit
import gbe4k.core.instructions.bit.Res
import gbe4k.core.instructions.bit.Rl
import gbe4k.core.instructions.bit.Rr
import gbe4k.core.instructions.bit.Set
import gbe4k.core.instructions.bit.Sla
import gbe4k.core.instructions.bit.Sra
import gbe4k.core.instructions.bit.Srl
import gbe4k.core.instructions.bit.Swap
import gbe4k.core.instructions.logic.And
import gbe4k.core.instructions.logic.Ccf
import gbe4k.core.instructions.logic.Cp
import gbe4k.core.instructions.logic.Cpl
import gbe4k.core.instructions.logic.Or
import gbe4k.core.instructions.logic.Xor
import gbe4k.core.io.Interrupts
import kotlin.experimental.and
import kotlin.experimental.or

class Cpu(val bus: Bus, val interrupts: Interrupts) {
    var pc = 0x0100

    val registers = Registers()
    val flags = Flags(registers)
    val stack = Stack(bus, registers)

    var halted = false

    fun step() {
        if (interrupts.handle(this)) {
            halted = false
        } else if (!halted) {
            val instruction = nextInstruction()

            instruction.execute(this)
        }
    }

    private fun nextInstruction() = when (val opcode = read().toInt().and(0x00ff)) {
        0x00 -> Nop

        // add
        0x09 -> Add(HL, BC)
        0x19 -> Add(HL, DE)
        0x29 -> Add(HL, HL)
        0x39 -> Add(HL, SP)
        0x80 -> Add(A, B)
        0x81 -> Add(A, C)
        0x82 -> Add(A, D)
        0x83 -> Add(A, E)
        0x84 -> Add(A, H)
        0x85 -> Add(A, L)
        0x86 -> Add(A, registers.hl)
        0x87 -> Add(A, A)
        0xc6 -> Add(A, read())
        // adc
        0x88 -> Adc(B)
        0x89 -> Adc(C)
        0x8a -> Adc(D)
        0x8b -> Adc(E)
        0x8c -> Adc(H)
        0x8d -> Adc(L)
        0x8e -> Adc(registers.hl)
        0x8f -> Adc(A)
        0xce -> Adc(read())
        // sub
        0x90 -> Sub(A, B)
        0x91 -> Sub(A, C)
        0x92 -> Sub(A, D)
        0x93 -> Sub(A, E)
        0x94 -> Sub(A, H)
        0x95 -> Sub(A, L)
        0x96 -> Sub(A, registers.hl)
        0x97 -> Sub(A, A)
        0xd6 -> Sub(A, read())
        // sbc
        0x98 -> Sbc(B)
        0x99 -> Sbc(C)
        0x9a -> Sbc(D)
        0x9b -> Sbc(E)
        0x9c -> Sbc(H)
        0x9d -> Sbc(L)
        0x9e -> Sbc(registers.hl)
        0x9f -> Sbc(A)
        0xde -> Sbc(read())
        // inc
        0x03 -> Inc(BC)
        0x04 -> Inc(B)
        0x0c -> Inc(C)
        0x13 -> Inc(DE)
        0x14 -> Inc(D)
        0x1c -> Inc(E)
        0x23 -> Inc(HL)
        0x24 -> Inc(H)
        0x2c -> Inc(L)
        0x33 -> Inc(SP)
        0x34 -> Inc(registers.hl)
        0x3c -> Inc(A)
        //dec
        0x0b -> Dec(BC)
        0x05 -> Dec(B)
        0x0d -> Dec(C)
        0x1b -> Dec(DE)
        0x15 -> Dec(D)
        0x1d -> Dec(E)
        0x2b -> Dec(HL)
        0x25 -> Dec(H)
        0x2d -> Dec(L)
        0x3b -> Dec(SP)
        0x35 -> Dec(registers.hl)
        0x3d -> Dec(A)
        0x27 -> Daa
        0x37 -> Scf

        // all supported ld instructions
        0x01 -> Ld(BC, readInt())
        0x02 -> Ld(registers.bc, A)
        0x06 -> Ld(B, read())
        0x08 -> Ld(readInt(), SP)
        0x0a -> Ld(A, registers.bc, mode = INDIRECT)
        0x0e -> Ld(C, read())
        0x11 -> Ld(DE, readInt())
        0x12 -> Ld(registers.de, A)
        0x16 -> Ld(D, read())
        0x1a -> Ld(A, registers.de, mode = INDIRECT)
        0x1e -> Ld(E, read())
        0x21 -> Ld(HL, readInt())
        0x22 -> Ld(registers.hl++, A)
        0x26 -> Ld(H, read())
        0x2a -> Ld(A, registers.hl++, mode = INDIRECT)
        0x2e -> Ld(L, read())
        0x31 -> Ld(SP, readInt())
        0x32 -> Ld(registers.hl--, A)
        0x36 -> Ld(registers.hl, read())
        0x3a -> Ld(A, registers.hl--, mode = INDIRECT)
        0x3e -> Ld(A, read())
        0x40 -> Ld(B, B)
        0x41 -> Ld(B, C)
        0x42 -> Ld(B, D)
        0x43 -> Ld(B, E)
        0x44 -> Ld(B, H)
        0x45 -> Ld(B, L)
        0x46 -> Ld(B, registers.hl, mode = INDIRECT)
        0x47 -> Ld(B, A)
        0x48 -> Ld(C, B)
        0x49 -> Ld(C, C)
        0x4a -> Ld(C, D)
        0x4b -> Ld(C, E)
        0x4c -> Ld(C, H)
        0x4d -> Ld(C, L)
        0x4e -> Ld(C, registers.hl, mode = INDIRECT)
        0x4f -> Ld(C, A)
        0x50 -> Ld(D, B)
        0x51 -> Ld(D, C)
        0x52 -> Ld(D, D)
        0x53 -> Ld(D, E)
        0x54 -> Ld(D, H)
        0x55 -> Ld(D, L)
        0x56 -> Ld(D, registers.hl, mode = INDIRECT)
        0x57 -> Ld(D, A)
        0x58 -> Ld(E, B)
        0x59 -> Ld(E, C)
        0x5a -> Ld(E, D)
        0x5b -> Ld(E, E)
        0x5c -> Ld(E, H)
        0x5d -> Ld(E, L)
        0x5e -> Ld(E, registers.hl, mode = INDIRECT)
        0x5f -> Ld(E, A)
        0x60 -> Ld(H, B)
        0x61 -> Ld(H, C)
        0x62 -> Ld(H, D)
        0x63 -> Ld(H, E)
        0x64 -> Ld(H, H)
        0x65 -> Ld(H, L)
        0x66 -> Ld(H, registers.hl, mode = INDIRECT)
        0x67 -> Ld(H, A)
        0x68 -> Ld(L, B)
        0x69 -> Ld(L, C)
        0x6a -> Ld(L, D)
        0x6b -> Ld(L, E)
        0x6c -> Ld(L, H)
        0x6d -> Ld(L, L)
        0x6e -> Ld(L, registers.hl, mode = INDIRECT)
        0x6f -> Ld(L, A)
        0x70 -> Ld(registers.hl, B)
        0x71 -> Ld(registers.hl, C)
        0x72 -> Ld(registers.hl, D)
        0x73 -> Ld(registers.hl, E)
        0x74 -> Ld(registers.hl, H)
        0x75 -> Ld(registers.hl, L)
        0x77 -> Ld(registers.hl, A)
        0x78 -> Ld(A, B)
        0x79 -> Ld(A, C)
        0x7a -> Ld(A, D)
        0x7b -> Ld(A, E)
        0x7c -> Ld(A, H)
        0x7d -> Ld(A, L)
        0x7e -> Ld(A, registers.hl, mode = INDIRECT)
        0x7f -> Ld(A, A)
        0xe0 -> Ld(read(), A)
        0xe2 -> Ld(registers.c, A)
        0xea -> Ld(readInt(), A)
        0xf0 -> Ld(A, read(), INDIRECT)
        0xf2 -> Ld(A, registers.c, INDIRECT)
        0xf8 -> `LdHlSp+r8`(read())
        0xf9 -> Ld(SP, HL)
        0xfa -> Ld(A, readInt(), INDIRECT)

        0xc1 -> Pop(BC)
        0xd1 -> Pop(DE)
        0xe1 -> Pop(HL)
        0xf1 -> Pop(AF)
        0xc5 -> Push(BC)
        0xd5 -> Push(DE)
        0xe5 -> Push(HL)
        0xf5 -> Push(AF)

        // jumps/call
        0x18 -> Jr(read())
        0x20 -> Jr(read(), z = false)
        0x28 -> Jr(read(), z = true)
        0x30 -> Jr(read(), c = false)
        0x38 -> Jr(read(), c = true)
        0xc2 -> Jp(readInt(), z = false)
        0xc3 -> Jp(readInt())
        0xca -> Jp(readInt(), z = true)
        0xd2 -> Jp(readInt(), c = false)
        0xda -> Jp(readInt(), c = true)
        0xe9 -> Jp(registers.hl)
        0xc4 -> Call(readInt(), z = false)
        0xcc -> Call(readInt(), z = true)
        0xcd -> Call(readInt())
        0xd4 -> Call(readInt(), c = false)
        0xdc -> Call(readInt(), c = true)
        0xc0 -> Ret(z = false)
        0xc8 -> Ret(z = true)
        0xc9 -> Ret()
        0xd0 -> Ret(c = false)
        0xd8 -> Ret(c = true)
        0xd9 -> Reti
        0xc7 -> Rst(0x00)
        0xcf -> Rst(0x08)
        0xd7 -> Rst(0x10)
        0xdf -> Rst(0x18)
        0xe7 -> Rst(0x20)
        0xef -> Rst(0x28)
        0xf7 -> Rst(0x30)
        0xff -> Rst(0x38)

        // logic
        0x2f -> Cpl
        0x3f -> Ccf
        0xa0 -> And(B)
        0xa1 -> And(C)
        0xa2 -> And(D)
        0xa3 -> And(E)
        0xa4 -> And(H)
        0xa5 -> And(L)
        0xa6 -> And(registers.hl)
        0xa7 -> And(A)
        0xe6 -> And(read())
        0xa8 -> Xor(B)
        0xa9 -> Xor(C)
        0xaa -> Xor(D)
        0xab -> Xor(E)
        0xac -> Xor(H)
        0xad -> Xor(L)
        0xae -> Xor(registers.hl)
        0xaf -> Xor(A)
        0xee -> Xor(read())
        0xb0 -> Or(B)
        0xb1 -> Or(C)
        0xb2 -> Or(D)
        0xb3 -> Or(E)
        0xb4 -> Or(H)
        0xb5 -> Or(L)
        0xb6 -> Or(registers.hl)
        0xb7 -> Or(A)
        0xf6 -> Or(read())
        0xb8 -> Cp(B)
        0xb9 -> Cp(C)
        0xba -> Cp(D)
        0xbb -> Cp(E)
        0xbc -> Cp(H)
        0xbd -> Cp(L)
        0xbe -> Cp(registers.hl)
        0xbf -> Cp(A)
        0xfe -> Cp(read())

        // extended stuff
        0xcb -> extendedInstruction()

        // other
        0x76 -> Halt
        0xf3 -> Di
        0xfb -> Ei

        else -> TODO("Unsupported opcode: ${opcode.hex()}")
    }

    private fun extendedInstruction() = when (val opcode = read().toInt().and(0x00ff)) {
        0x10 -> Rl(B)
        0x11 -> Rl(C)
        0x12 -> Rl(D)
        0x13 -> Rl(E)
        0x14 -> Rl(H)
        0x15 -> Rl(L)
        0x16 -> Rl(registers.hl)
        0x17 -> Rl(A)
        0x18 -> Rr(B)
        0x19 -> Rr(C)
        0x1a -> Rr(D)
        0x1b -> Rr(E)
        0x1c -> Rr(H)
        0x1d -> Rr(L)
        0x1e -> Rr(registers.hl)
        0x1f -> Rr(A)
        0x20 -> Sla(B)
        0x21 -> Sla(C)
        0x22 -> Sla(D)
        0x23 -> Sla(E)
        0x24 -> Sla(H)
        0x25 -> Sla(L)
        0x26 -> Sla(registers.hl)
        0x27 -> Sla(A)
        0x28 -> Sra(B)
        0x29 -> Sra(C)
        0x2a -> Sra(D)
        0x2b -> Sra(E)
        0x2c -> Sra(H)
        0x2d -> Sra(L)
        0x2e -> Sra(registers.hl)
        0x2f -> Sra(A)
        0x30 -> Swap(B)
        0x31 -> Swap(C)
        0x32 -> Swap(D)
        0x33 -> Swap(E)
        0x34 -> Swap(H)
        0x35 -> Swap(L)
        0x36 -> Swap(registers.hl)
        0x37 -> Swap(A)
        0x38 -> Srl(B)
        0x39 -> Srl(C)
        0x3a -> Srl(D)
        0x3b -> Srl(E)
        0x3c -> Srl(H)
        0x3d -> Srl(L)
        0x3e -> Srl(registers.hl)
        0x3f -> Srl(A)
        0x40 -> Bit(0, B)
        0x41 -> Bit(0, C)
        0x42 -> Bit(0, D)
        0x43 -> Bit(0, E)
        0x44 -> Bit(0, H)
        0x45 -> Bit(0, L)
        0x46 -> Bit(0, registers.hl)
        0x47 -> Bit(0, A)
        0x48 -> Bit(1, B)
        0x49 -> Bit(1, C)
        0x4a -> Bit(1, D)
        0x4b -> Bit(1, E)
        0x4c -> Bit(1, H)
        0x4d -> Bit(1, L)
        0x4e -> Bit(1, registers.hl)
        0x4f -> Bit(1, A)
        0x50 -> Bit(2, B)
        0x51 -> Bit(2, C)
        0x52 -> Bit(2, D)
        0x53 -> Bit(2, E)
        0x54 -> Bit(2, H)
        0x55 -> Bit(2, L)
        0x56 -> Bit(2, registers.hl)
        0x57 -> Bit(2, A)
        0x58 -> Bit(3, B)
        0x59 -> Bit(3, C)
        0x5a -> Bit(3, D)
        0x5b -> Bit(3, E)
        0x5c -> Bit(3, H)
        0x5d -> Bit(3, L)
        0x5e -> Bit(3, registers.hl)
        0x5f -> Bit(3, A)
        0x60 -> Bit(4, B)
        0x61 -> Bit(4, C)
        0x62 -> Bit(4, D)
        0x63 -> Bit(4, E)
        0x64 -> Bit(4, H)
        0x65 -> Bit(4, L)
        0x66 -> Bit(4, registers.hl)
        0x67 -> Bit(4, A)
        0x68 -> Bit(5, B)
        0x69 -> Bit(5, C)
        0x6a -> Bit(5, D)
        0x6b -> Bit(5, E)
        0x6c -> Bit(5, H)
        0x6d -> Bit(5, L)
        0x6e -> Bit(5, registers.hl)
        0x6f -> Bit(5, A)
        0x70 -> Bit(6, B)
        0x71 -> Bit(6, C)
        0x72 -> Bit(6, D)
        0x73 -> Bit(6, E)
        0x74 -> Bit(6, H)
        0x75 -> Bit(6, L)
        0x76 -> Bit(6, registers.hl)
        0x77 -> Bit(6, A)
        0x78 -> Bit(7, B)
        0x79 -> Bit(7, C)
        0x7a -> Bit(7, D)
        0x7b -> Bit(7, E)
        0x7c -> Bit(7, H)
        0x7d -> Bit(7, L)
        0x7e -> Bit(7, registers.hl)
        0x7f -> Bit(7, A)
        0x80 -> Res(0, B)
        0x81 -> Res(0, C)
        0x82 -> Res(0, D)
        0x83 -> Res(0, E)
        0x84 -> Res(0, H)
        0x85 -> Res(0, L)
        0x86 -> Res(0, registers.hl)
        0x87 -> Res(0, A)
        0x88 -> Res(1, B)
        0x89 -> Res(1, C)
        0x8a -> Res(1, D)
        0x8b -> Res(1, E)
        0x8c -> Res(1, H)
        0x8d -> Res(1, L)
        0x8e -> Res(1, registers.hl)
        0x8f -> Res(1, A)
        0x90 -> Res(2, B)
        0x91 -> Res(2, C)
        0x92 -> Res(2, D)
        0x93 -> Res(2, E)
        0x94 -> Res(2, H)
        0x95 -> Res(2, L)
        0x96 -> Res(2, registers.hl)
        0x97 -> Res(2, A)
        0x98 -> Res(3, B)
        0x99 -> Res(3, C)
        0x9a -> Res(3, D)
        0x9b -> Res(3, E)
        0x9c -> Res(3, H)
        0x9d -> Res(3, L)
        0x9e -> Res(3, registers.hl)
        0x9f -> Res(3, A)
        0xa0 -> Res(4, B)
        0xa1 -> Res(4, C)
        0xa2 -> Res(4, D)
        0xa3 -> Res(4, E)
        0xa4 -> Res(4, H)
        0xa5 -> Res(4, L)
        0xa6 -> Res(4, registers.hl)
        0xa7 -> Res(4, A)
        0xa8 -> Res(5, B)
        0xa9 -> Res(5, C)
        0xaa -> Res(5, D)
        0xab -> Res(5, E)
        0xac -> Res(5, H)
        0xad -> Res(5, L)
        0xae -> Res(5, registers.hl)
        0xaf -> Res(5, A)
        0xb0 -> Res(6, B)
        0xb1 -> Res(6, C)
        0xb2 -> Res(6, D)
        0xb3 -> Res(6, E)
        0xb4 -> Res(6, H)
        0xb5 -> Res(6, L)
        0xb6 -> Res(6, registers.hl)
        0xb7 -> Res(6, A)
        0xb8 -> Res(7, B)
        0xb9 -> Res(7, C)
        0xba -> Res(7, D)
        0xbb -> Res(7, E)
        0xbc -> Res(7, H)
        0xbd -> Res(7, L)
        0xbe -> Res(7, registers.hl)
        0xbf -> Res(7, A)
        0xc0 -> Set(0, B)
        0xc1 -> Set(0, C)
        0xc2 -> Set(0, D)
        0xc3 -> Set(0, E)
        0xc4 -> Set(0, H)
        0xc5 -> Set(0, L)
        0xc6 -> Set(0, registers.hl)
        0xc7 -> Set(0, A)
        0xc8 -> Set(1, B)
        0xc9 -> Set(1, C)
        0xca -> Set(1, D)
        0xcb -> Set(1, E)
        0xcc -> Set(1, H)
        0xcd -> Set(1, L)
        0xce -> Set(1, registers.hl)
        0xcf -> Set(1, A)
        0xd0 -> Set(2, B)
        0xd1 -> Set(2, C)
        0xd2 -> Set(2, D)
        0xd3 -> Set(2, E)
        0xd4 -> Set(2, H)
        0xd5 -> Set(2, L)
        0xd6 -> Set(2, registers.hl)
        0xd7 -> Set(2, A)
        0xd8 -> Set(3, B)
        0xd9 -> Set(3, C)
        0xda -> Set(3, D)
        0xdb -> Set(3, E)
        0xdc -> Set(3, H)
        0xdd -> Set(3, L)
        0xde -> Set(3, registers.hl)
        0xdf -> Set(3, A)
        0xe0 -> Set(4, B)
        0xe1 -> Set(4, C)
        0xe2 -> Set(4, D)
        0xe3 -> Set(4, E)
        0xe4 -> Set(4, H)
        0xe5 -> Set(4, L)
        0xe6 -> Set(4, registers.hl)
        0xe7 -> Set(4, A)
        0xe8 -> Set(5, B)
        0xe9 -> Set(5, C)
        0xea -> Set(5, D)
        0xeb -> Set(5, E)
        0xec -> Set(5, H)
        0xed -> Set(5, L)
        0xee -> Set(5, registers.hl)
        0xef -> Set(5, A)
        0xf0 -> Set(6, B)
        0xf1 -> Set(6, C)
        0xf2 -> Set(6, D)
        0xf3 -> Set(6, E)
        0xf4 -> Set(6, H)
        0xf5 -> Set(6, L)
        0xf6 -> Set(6, registers.hl)
        0xf7 -> Set(6, A)
        0xf8 -> Set(7, B)
        0xf9 -> Set(7, C)
        0xfa -> Set(7, D)
        0xfb -> Set(7, E)
        0xfc -> Set(7, H)
        0xfd -> Set(7, L)
        0xfe -> Set(7, registers.hl)
        0xff -> Set(7, A)

        else -> TODO("Unsupported extended opcode: ${opcode.hex()}")
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

        fun Byte.isBitSet(position: Int) = (toInt() shr position).and(1) == 1
        fun Byte.setBit(enabled: Boolean, position: Int): Byte {
            return if (enabled) {
                or((1 shl position).toByte())
            } else {
                val size = Byte.SIZE_BITS - countLeadingZeroBits()

                and((((1 shl size) - 1) - (1 shl position)).toByte())
            }
        }
    }
}
