package gbe4k.core

import gbe4k.core.instructions.InstructionDecoder
import gbe4k.core.io.Dma
import gbe4k.core.io.Interrupts
import gbe4k.core.io.Timer
import kotlin.experimental.and
import kotlin.experimental.or

class Cpu(val bus: Bus, val timer: Timer, val interrupts: Interrupts) {
    var pc = 0x0100

    val registers = Registers()
    val flags = Flags(registers)
    val stack = Stack(bus, registers)

    var halted = false

    init {
        init()
    }

    private fun init() {
        // simulates a boot rom that has run
        pc = 0x0100

        registers.apply {
            af = 0x01b0
            bc = 0x0013
            de = 0x00d8
            hl = 0x014d
            sp = 0xfffe
        }
    }

    fun step() {
        if (interrupts.handle(this)) {
            halted = false
        }

        if (halted) {
            cycle()
        } else {
            val instruction = InstructionDecoder.decode(read())

            instruction.execute(this)
        }
    }

    fun read() = bus.read(pc++)

    fun readInt(): Int {
        val lo = read()
        val hi = read()

        return n16(hi, lo)
    }

    fun cycle(cycles: Int = 4) = timer.cycle(cycles)

    companion object {
        fun n8(hiNibble: Byte, loNibble: Byte) = hiNibble.toInt().shl(4) + loNibble
        fun n16(hi: Byte, lo: Byte) = hi.asInt().shl(8) + lo.asInt()

        fun Byte.asInt() = toInt().and(0xff)
        fun Int.hi() = and(0xff00).shr(8).toByte()
        fun Int.lo() = and(0xff).toByte()

        fun Byte.hiNibble() = asInt().shr(4).and(0xf).toByte()
        fun Byte.loNibble() = and(0x0f)

        fun Byte.hex() = "0x%02x".format(this)
        fun Int.hex() = "0x%04x".format(this)

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
