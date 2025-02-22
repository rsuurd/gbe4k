package gbe4k.core

import gbe4k.core.Register.A
import gbe4k.core.Register.SP
import gbe4k.core.instructions.Di
import gbe4k.core.instructions.Jp
import gbe4k.core.instructions.Ld
import gbe4k.core.instructions.Nop
import kotlin.experimental.and

class Cpu(val bus: Bus) {
    var pc = 0x0100

    var registers = Registers()
    var ime = true

    fun step() {
        val instruction = nextInstruction()

        instruction.execute(this)
    }

    private fun nextInstruction() = when (val opcode = read()) {
        0x00.toByte() -> Nop
        0x31.toByte() -> Ld(SP, readInt())
        0x3e.toByte() -> Ld(A, read())
        0xc3.toByte() -> Jp(readInt())
        0xea.toByte() -> Ld(readInt(), A)
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
    }
}
