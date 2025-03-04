package gbe4k.core.io

import gbe4k.core.Addressable

class Io(private val serial: Serial, val timer: Timer, private val lcd: Lcd, private val interrupts: Interrupts) : Addressable {
    override fun get(address: Int): Byte = when (address) {
        in SERIAL -> serial[address]
        in TIMER -> timer[address]
        in LCD -> lcd[address]
        INTERRUPT_FLAG -> interrupts.`if`
        INTERRUPT_ENABLE -> interrupts.ie
        else -> 0xff.toByte()
    }

    override fun set(address: Int, value: Byte) = when (address) {
        in SERIAL -> serial[address] = value
        in TIMER -> timer[address] = value
        in LCD -> lcd[address] = value
        INTERRUPT_FLAG -> interrupts.`if` = value
        INTERRUPT_ENABLE -> interrupts.ie = value
        else -> {}
    }

    companion object {
        val SERIAL = 0xff01..0xff02
        val TIMER = Timer.DIV..Timer.TAC
        val LCD = 0xff40..0xff4b
        const val INTERRUPT_FLAG = 0xff0f
        const val INTERRUPT_ENABLE = 0xffff
    }
}
