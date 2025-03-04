package gbe4k.core.io

import gbe4k.core.Addressable
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Cpu.Companion.setBit
import kotlin.experimental.and

class Timer(private val interrupts: Interrupts) : Addressable {
    var div: Byte = 0x00
    var tima: Byte = 0x00
    var tma: Byte = 0x00
    var tac: Byte = 0x00

    var enabled: Boolean
        get() = tac.isBitSet(2)
        set(value) {
            tac = tac.setBit(value, 2)
        }

    val frequency: Int
        get() = when (tac.and(0b11).asInt()) {
            0b01 -> 4
            0b10 -> 16
            0b11 -> 64
            else -> 256
        }

    override fun get(address: Int) = when (address) {
        DIV -> div
        TIMA -> tima
        TMA -> tma
        TAC -> tac
        else -> throw IllegalArgumentException("${address.hex()} is not a timer register")
    }

    override fun set(address: Int, value: Byte) = when (address) {
        DIV -> div = 0x00
        TIMA -> tima = value
        TMA -> tma = value
        TAC -> tac = value
        else -> throw IllegalArgumentException("${address.hex()} is not a timer register")
    }

    fun cycle(cycles: Int) = repeat(cycles) {
        tick()
    }

    fun tick() {
        div = div.inc()

        if (enabled && (div.asInt() % frequency == 0)) {
            tima = tima.inc()

            if (tima.asInt() == 0x00) {
                tima = tma

                interrupts.request(Interrupts.Interrupt.TIMER)
            }
        }
    }

    companion object {
        const val DIV = 0xff04
        const val TIMA = 0xff05
        const val TMA = 0xff06
        const val TAC = 0xff07
    }
}
