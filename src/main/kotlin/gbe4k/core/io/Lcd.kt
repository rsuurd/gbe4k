package gbe4k.core.io

import gbe4k.core.Addressable
import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Cpu.Companion.setBit
import gbe4k.core.Ppu
import gbe4k.core.io.Dma.Companion.DMA_TRANSFER
import kotlin.experimental.and

class Lcd(private val dma: Dma) : Addressable {
    val control = Control(0x00)
    val stat = Stat(0x00)

    var scx: Byte = 0x00
    var scy: Byte = 0x00
    var ly: Byte = 0x00
    var lyc: Byte = 0x00
    var wx: Byte = 0x00
    var wy: Byte = 0x00

    override operator fun get(address: Int) = when (address) {
        LCDC -> control.value
        STAT -> stat.value
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
            LCDC -> control.value = value
            STAT -> stat.value = value
            SCY -> scy = value
            SCX -> scx = value
            LY -> { /* read only */ }
            LYC -> lyc = value
            WY -> wy = value
            WX -> wx = value
            DMA_TRANSFER -> dma.start(value)
        }
    }

    data class Control(var value: Byte) {
        val priority: Boolean
            get() = value.isBitSet(0)

        val objEnable: Boolean
            get() = value.isBitSet(1)

        val objSize: Byte
            get() = if (value.isBitSet(2)) 16 else 8

        val backgroundTileMap: IntRange
            get() = if (value.isBitSet(3)) 0x9c00..0x9fff else 0x9800..0x9bff

        val tileData: IntRange
            get() = if (value.isBitSet(4)) 0x8000..0x8fff else 0x8800..0x97ff

        val windowEnabled: Boolean
            get() = value.isBitSet(5)

        val windowTileMap: IntRange
            get() = if (value.isBitSet(6)) 0x9c00..0x9fff else 0x9800..0x9bff

        val lcdPpuEnabled: Boolean
            get() = value.isBitSet(7)
    }

    data class Stat(var value: Byte) {
        val lycSelected: Boolean
            get() = value.isBitSet(6)

        val mode2Selected: Boolean
            get() = value.isBitSet(5)

        val mode1Selected: Boolean
            get() = value.isBitSet(4)

        val mode0Selected: Boolean
            get() = value.isBitSet(3)

        var lyEqLyc: Boolean
            get() = value.isBitSet(2)
            set(eq) {
                value = value.setBit(eq, 2)
            }

        val ppuMode: Byte
            get() = value.and(0b00000011)
    }

    companion object {
        const val LCDC = 0xff40
        const val STAT = 0xff41
        const val SCY = 0xff42
        const val SCX = 0xff43
        const val LY = 0xff44
        const val LYC = 0xff45

        const val WY = 0xff4a
        const val WX = 0xff4b
    }
}
