package gbe4k.core.io

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.n16
import gbe4k.core.Oam.Companion.OAM

class Dma {
    var address = 0x0000
        private set

    var transferring: Boolean = false
        private set

    private var index = -1

    fun start(value: Byte) {
        address = n16(value, 0x00)
        transferring = true
        index = -1 // one cpu cycle of setup
    }

    fun transfer(cpu: Cpu) {
        if (transferring) {
            if (index >= 0) {
                cpu.bus[OAM.first + index] = cpu.bus[address + index]
            }

            index++

            if (index == 0xa0) {
                transferring = false
                index = -1
            }
        }
    }

    companion object {
        const val DMA_TRANSFER = 0xff46
    }
}
