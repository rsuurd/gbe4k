package gbe4k.core

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.io.Interrupts
import gbe4k.core.io.Lcd
import java.awt.Color
import java.awt.image.BufferedImage

class Ppu(val bus: Bus, val lcd: Lcd, val interrupts: Interrupts) {
    // PPU draws to this buffer, or maybe pass in the Graphics object in the update?
    val buffer = BufferedImage(160, 144, BufferedImage.TYPE_INT_RGB)

    private var dots = 0

    fun update(cycles: Int) = repeat(cycles) {
        if (dots == DOTS_PER_LINE) { // ready to draw a line.
            for (x in 0..160) {
                val bgX = (lcd.scx.asInt() + x) % 256
                val bgY = (lcd.scy.asInt() + lcd.ly.asInt()) % 256
                val tileX = bgX / 8
                val tileY = bgY / 8
                val tileIndex = tileY * 32 + tileX
                val lineInTile = bgY % 8

                val tilemap = lcd.control.backgroundTileMap
                val tileNumber = bus[tilemap.first + tileIndex]
                val tiledata = lcd.control.tileData

                val address = tiledata.first.takeIf { it == 0x8800 }?.let {
                    0x9000 + (tileNumber * 16)
                } ?: (tiledata.first + (tileNumber.asInt() * 16))
                val tileLow = bus[address + lineInTile * 2]
                val tileHigh = bus[address + lineInTile * 2 + 1]
                val bitIndex = 7 - (bgX % 8)
                val colorBit = ((tileHigh.toInt() shr bitIndex) and 1) shl 1 or ((tileLow.toInt() shr bitIndex) and 1)

                val g = buffer.createGraphics()
                g.color = GRAY[colorBit]
                g.fillRect(x, lcd.ly.asInt(), 1, 1)

                drawObjects(x)
            }

            nextLine()
        }

        dots++
    }

    private fun drawObjects(x: Int) {
        bus.oam.entries.filter {
            (x in it.x until it.x + 8) && (lcd.ly.asInt() in (it.y until it.y + lcd.control.objSize.asInt()))
        }.take(10).forEach {
            val lineInTile = lcd.ly.asInt() - it.y
            val address = 0x9000 + (it.tile * 16)
            val tileLow = bus[address + lineInTile * 2]
            val tileHigh = bus[address + lineInTile * 2 + 1]

            // TODO xflip / yflip etc
            val bitIndex = 7 - (x % 8)
            val colorBit = ((tileHigh.toInt() shr bitIndex) and 1) shl 1 or ((tileLow.toInt() shr bitIndex) and 1)

            val g = buffer.createGraphics()
            g.color = GRAY[colorBit]
            g.fillRect(x, lcd.ly.asInt(), 1, 1)
        }
    }

    private fun nextLine() {
        dots = 0

        if (lcd.stat.mode2Selected) {
            interrupts.request(Interrupts.Interrupt.STAT)
        }

        lcd.ly = ((lcd.ly.asInt() + 1) % TOTAL_SCANLINES).toByte()
        lcd.stat.lyEqLyc = lcd.ly == lcd.lyc

        if (lcd.stat.lycSelected && lcd.stat.lyEqLyc) {
            interrupts.request(Interrupts.Interrupt.STAT)
        }

        if (lcd.ly.asInt() == VISIBLE_SCANLINES) {
            interrupts.request(Interrupts.Interrupt.VBLANK)
        }
    }

    companion object {
        private const val DOTS_PER_LINE = 456
        private const val VISIBLE_SCANLINES = 144
        private const val TOTAL_SCANLINES = 154

        private val GREEN = arrayOf(
            Color(0x9bbc0f),
            Color(0x8bac0f),
            Color(0x306230),
            Color(0x0f380f)
        )

        private val GRAY = arrayOf(
            Color(0xffffff),
            Color(0xa9a9a9),
            Color(0x545454),
            Color(0x000000)
        )
    }

    enum class Mode {
        HBLANK,
        VBLANK,
        OAM_SCAN,
        DRAWING
    }
}