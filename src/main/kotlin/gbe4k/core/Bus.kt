package gbe4k.core

import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Oam.Companion.OAM
import gbe4k.core.boot.BootRom
import gbe4k.core.boot.BootRom.Companion.BOOTROM
import gbe4k.core.boot.BootRom.Companion.BOOTROM_BANK
import gbe4k.core.io.Io
import gbe4k.core.io.Io.Companion.INTERRUPT_ENABLE

class Bus(
    private val cart: Cart,
    private val io: Io,
    private val bootRom: BootRom
) : Addressable {
    private val vram = Ram(VRAM)
    private val wram = Ram(WRAM)
    private val hram = Ram(HRAM)
    val oam = Oam()

    fun read(address: Int): Byte = this[address].also { io.timer.cycle(4) }

    override fun get(address: Int) = when (address) {
        in BOOTROM -> {
            if (bootRom.booting) {
                bootRom[address]
            } else {
                cart[address]
            }
        }
        in CART_DATA -> cart[address]
        in VRAM -> vram[address]
        in CART_RAM -> cart[address]
        in WRAM -> wram[address]
        in OAM -> oam[address]
        in IO, INTERRUPT_ENABLE -> io[address]
        in HRAM -> hram[address]
        else -> throw IllegalArgumentException("Can not read from: ${address.hex()}")
    }

    fun write(address: Int, value: Byte) {
        this[address] = value
        io.timer.cycle(4)
    }

    override fun set(address: Int, value: Byte) {
        when (address) {
            in CART_DATA -> cart[address] = value
            in VRAM -> vram[address] = value
            in CART_RAM -> cart[address] = value
            in WRAM -> wram[address] = value
            in HRAM -> hram[address] = value
            in OAM -> oam[address] = value
            BOOTROM_BANK -> bootRom[address] = value
            in IO, INTERRUPT_ENABLE -> io[address] = value
            else -> { /* nop */ }
        }
    }

    companion object {
        val CART_DATA = 0..0x7fff
        val VRAM = 0x8000..0x9fff
        val CART_RAM = 0xa000..0xbfff
        val WRAM = 0xc000..0xdfff
        val IO = 0xff00..0xff7f
        val HRAM = 0xff80..0xfffe
    }
}
