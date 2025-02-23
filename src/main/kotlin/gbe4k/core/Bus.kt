package gbe4k.core

import gbe4k.core.Cpu.Companion.hex

class Bus(private val cart: Cart) {
    fun read(address: Int): Byte = when (address) {
        in CART_DATA -> cart.read(address)
        else -> throw IllegalArgumentException("Can not read from: ${address.hex()}")
    }

    fun write(address: Int, value: Byte) {}

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