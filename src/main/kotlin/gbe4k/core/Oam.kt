package gbe4k.core

import gbe4k.core.Cpu.Companion.isBitSet
import kotlin.experimental.and

class Oam : Addressable {
    private val ram = Ram(OAM)

    override fun get(address: Int) = ram[address]

    override fun set(address: Int, value: Byte) {
        ram[address] = value
    }

    val entries: List<OamEntry>
        get() = ram.chunked(4).map { chunk ->
            OamEntry(chunk[1], chunk[0], chunk[2], chunk[3])
        }

    companion object {
        val OAM = 0xfe00..0xfe9f
    }
}

data class OamEntry(val x: Byte, val y: Byte, val tile: Byte, val attributes: Byte) {
    val cgbPalette: Byte
        get() = attributes.and(0b111)

    val bank: Byte
        get() = if (attributes.isBitSet(3)) 1 else 0

    val palette: Byte
        get() = if (attributes.isBitSet(4)) 1 else 0

    val xFlip: Boolean
        get() = attributes.isBitSet(5)

    val yFlip: Boolean
        get() = attributes.isBitSet(6)

    val priority: Boolean
        get() = attributes.isBitSet(7)

    companion object {
        val BLANK = OamEntry(0, 0, 0, 0)
    }
}
