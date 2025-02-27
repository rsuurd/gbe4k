package gbe4k.core.io

import gbe4k.core.Addressable

class Io(private val lcd: Lcd, private val interrupts: Interrupts) : Addressable {
    override fun get(address: Int): Byte = when (address) {
        in LCD -> lcd[address]
        INTERRUPT_FLAG -> interrupts.`if`
        INTERRUPT_ENABLE -> interrupts.ie
        else -> 0x00
    }

    override fun set(address: Int, value: Byte) = when (address) {
        in LCD -> lcd[address] = value
        INTERRUPT_FLAG -> interrupts.`if` = value
        INTERRUPT_ENABLE -> interrupts.ie = value
        else -> {}
    }

    companion object {
        val LCD = 0xff40..0xff4b
        const val INTERRUPT_FLAG = 0xff0f
        const val INTERRUPT_ENABLE = 0xffff
    }
}
