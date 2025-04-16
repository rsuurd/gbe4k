package gbe4k.core.boot

import gbe4k.core.Addressable

class BootRom(
    private val data: ByteArray = Companion::class.java.getResourceAsStream("/boot/dmg_boot.bin")!!.readBytes()
) : Addressable {
    var booting = true
        private set

    override fun get(address: Int) = data[address]
    override fun set(address: Int, value: Byte) {
        booting = !(address == BOOTROM_BANK && value > 0)
    }

    companion object {
        val BOOTROM = 0x00..0xff
        const val BOOTROM_BANK = 0xff50
    }
}
