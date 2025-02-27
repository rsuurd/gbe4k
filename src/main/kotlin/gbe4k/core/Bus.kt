package gbe4k.core

import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.io.Io

class Bus(
    private val cart: Cart,
    private val io: Io
) {
    private val vram = Ram(VRAM)
    private val wram = Ram(WRAM)
    private val hram = Ram(HRAM)

    fun read(address: Int): Byte = when (address) {
        in CART_DATA -> cart[address]
        in VRAM -> vram[address]
        in WRAM -> wram[address]
        in HRAM -> hram[address]
        in IO -> io[address]
        else -> throw IllegalArgumentException("Can not read from: ${address.hex()}")
    }

    fun write(address: Int, value: Byte) {
        when (address) {
            in VRAM -> vram[address] = value
            in WRAM -> wram[address] = value
            in HRAM -> hram[address] = value
            in IO -> io[address] = value
            else -> { /* nop */ }
        }
    }

    companion object {
        val CART_DATA = 0..0x7fff
        val VRAM = 0x8000..0x9fff
        val CART_RAM = 0xa000..0xbfff
        val WRAM = 0xc000..0xdfff
        val OAM = 0xfe00..0xfe9f
        val HRAM = 0xff80..0xfffe
        val IO = 0xff00..0xff7f
        val INTERRUPTS = 0xffff
    }
}