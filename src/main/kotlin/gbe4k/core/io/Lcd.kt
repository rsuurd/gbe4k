package gbe4k.core.io

import gbe4k.core.Addressable
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Ppu
import gbe4k.core.io.Dma.Companion.DMA_TRANSFER

class Lcd(private val dma: Dma, private val interrupts: Interrupts) : Addressable {
    val control = Control(0x00)
    val stat = Stat()

    var scx: Int = 0x00
    var scy: Int = 0x00
    var ly: Int = 0x00
        set(value) {
            field = value

            stat.lyEqLyc = value == lyc

            if (stat.lycSelected && stat.lyEqLyc) {
                interrupts.request(Interrupts.Interrupt.STAT)
            }
        }
    var lyc: Int = 0x00
    var bgPalette: Int = 0x00
    var objPalette0: Int = 0x00
    var objPalette1: Int = 0x00
    var wx: Int = 0x00
    var wy: Int = 0x00

    override operator fun get(address: Int) = when (address) {
        LCDC -> control.value
        STAT -> stat.value
        SCY -> scy.toByte()
        SCX -> scx.toByte()
        LY -> ly.toByte()
        LYC -> lyc.toByte()
        BG_PALETTE -> bgPalette.toByte()
        OBJ_PALETTE_0 -> objPalette0.toByte()
        OBJ_PALETTE_1 -> objPalette1.toByte()
        WY -> wy.toByte()
        WX -> wx.toByte()
        else -> 0xff.toByte()
    }

    override operator fun set(address: Int, value: Byte) {
        when (address) {
            LCDC -> control.value = value
            STAT -> stat.value = value
            SCY -> scy = value.asInt()
            SCX -> scx = value.asInt()
            LYC -> lyc = value.asInt()
            BG_PALETTE -> bgPalette = value.asInt()
            OBJ_PALETTE_0 -> objPalette0 = value.asInt()
            OBJ_PALETTE_1 -> objPalette1 = value.asInt()
            WY -> wy = value.asInt()
            WX -> wx = value.asInt() - 7
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

        val enabled: Boolean
            get() = value.isBitSet(7)
    }

    class Stat {
        var value: Byte = 0x00
            // value is composed of top 5 bits + lycSelected (bit 2) and ppuMode (bits 1 and 0)
            get() = (field + (if (lyEqLyc) 1 else 0).shl(2) + ppuMode.ordinal).toByte()
            set(value) {
                field = value.asInt().and(0b11111000).toByte()
            }

        var ppuMode: Ppu.Mode = Ppu.Mode.HBLANK
        var lyEqLyc: Boolean = false

        fun isSelected(mode: Ppu.Mode) = when (mode) {
            Ppu.Mode.HBLANK, Ppu.Mode.VBLANK, Ppu.Mode.OAM_SCAN -> value.isBitSet(3 + mode.ordinal)
            Ppu.Mode.DRAWING -> false
        }

        val lycSelected: Boolean
            get() = value.isBitSet(6)
    }

    companion object {
        const val LCDC = 0xff40
        const val STAT = 0xff41
        const val SCY = 0xff42
        const val SCX = 0xff43
        const val LY = 0xff44
        const val LYC = 0xff45
        const val BG_PALETTE = 0xff47
        const val OBJ_PALETTE_0 = 0xff48
        const val OBJ_PALETTE_1 = 0xff49
        const val WY = 0xff4a
        const val WX = 0xff4b
    }
}
