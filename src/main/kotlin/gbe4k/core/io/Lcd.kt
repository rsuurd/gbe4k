package gbe4k.core.io

import gbe4k.core.Addressable
import gbe4k.core.io.Dma.Companion.DMA_TRANSFER

class Lcd(private val dma: Dma) : Addressable {
    var scx: Byte = 0x00
    var scy: Byte = 0x00
    var ly: Byte = 0x90.toByte()
    var lyc: Byte = 0x00
    var wx: Byte = 0x00
    var wy: Byte = 0x00

    override operator fun get(address: Int) = when (address) {
        SCY -> scy
        SCX -> scx
        LY -> ly
        LYC -> lyc
        WY -> wy
        WX -> wx
        else -> 0xff.toByte()
    }

    override operator fun set(address: Int, value: Byte) {
        when (address) {
            SCY -> scy = value
            SCX -> scx = value
            LYC -> lyc = value
            WY -> wy = value
            WX -> wx = value
            DMA_TRANSFER -> dma.start(value)
        }
    }

    companion object {
        const val SCY = 0xff42
        const val SCX = 0xff43
        const val LY = 0xff44
        const val LYC = 0xff45

        const val WY = 0xff4a
        const val WX = 0xff4b
    }
}
