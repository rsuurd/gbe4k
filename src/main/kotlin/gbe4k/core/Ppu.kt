package gbe4k.core

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.io.Interrupts
import gbe4k.core.io.Lcd
import java.awt.Color
import java.awt.image.BufferedImage

class Ppu(val bus: Bus, val lcd: Lcd, val interrupts: Interrupts) {
    private var buffer = BufferedImage(160, 144, BufferedImage.TYPE_INT_RGB)
    private var dots = 0
    private var drawn = false

    private val listeners = mutableListOf<VBlankListener>()

    private val backgroundPalette: Array<Color>
        get() = arrayOf(
            GRAY[lcd.bgPalette.and(0x3)],
            GRAY[lcd.bgPalette.shr(2).and(0x3)],
            GRAY[lcd.bgPalette.shr(4).and(0x3)],
            GRAY[lcd.bgPalette.shr(6).and(0x3)]
        )

    private val objectPalettes: Array<Array<Color>>
        get() = arrayOf(
            arrayOf(
                GRAY[lcd.objPalette0.and(0x3)],
                GRAY[lcd.objPalette0.shr(2).and(0x3)],
                GRAY[lcd.objPalette0.shr(4).and(0x3)],
                GRAY[lcd.objPalette0.shr(6).and(0x3)]
            ),
            arrayOf(
                GRAY[lcd.objPalette1.and(0x3)],
                GRAY[lcd.objPalette1.shr(2).and(0x3)],
                GRAY[lcd.objPalette1.shr(4).and(0x3)],
                GRAY[lcd.objPalette1.shr(6).and(0x3)]
            ),
        )


    fun addVBlankListener(listener: VBlankListener) {
        listeners.add(listener)
    }

    fun update(cycles: Int) {
        dots += cycles

        if (lcd.stat.ppuMode != Mode.VBLANK) {
            setMode(
                when {
                    dots < OAM_SCAN_DOTS -> Mode.OAM_SCAN
                    dots < HBLANK_DOTS -> Mode.DRAWING
                    else -> Mode.HBLANK
                }
            )

            if (lcd.stat.ppuMode == Mode.DRAWING) {
                draw()
            }
        }

        if (dots >= DOTS_PER_LINE) {
            nextLine()
        }
    }

    private fun setMode(mode: Mode) {
        if (lcd.stat.ppuMode != mode) {
            lcd.stat.ppuMode = mode

            if (lcd.stat.isSelected(mode)) {
                interrupts.request(Interrupts.Interrupt.STAT)
            }
        }
    }

    private fun draw() {
        if (drawn) return

        drawTiles()
        drawOam()

        drawn = true
    }

    private fun drawTiles() {
        val g = buffer.graphics

        for (x in 0 until 160) {
            val backgroundPixel = fetchPixel(x, lcd.scx, lcd.scy, lcd.control.backgroundTileMap)

            g.color = backgroundPalette[backgroundPixel]
            g.fillRect(x, lcd.ly, 1, 1)
        }
    }

    private fun fetchPixel(screenX: Int, offsetX: Int, offsetY: Int, tileMap: IntRange): Int {
        val x = (offsetX + screenX) % 256
        val y = (offsetY + lcd.ly) % 256

        val tileX = x / 8
        val tileY = y / 8
        val lineInTile = y % 8
        val tileIndex = tileY * 32 + tileX
        val tileNumber = bus[tileMap.first + tileIndex]
        val address = lcd.control.tileData.first.takeIf { it == 0x8800 }?.let {
            0x9000 + (tileNumber * 16)
        } ?: (lcd.control.tileData.first + (tileNumber.asInt() * 16))
        val tileLow = bus[address + lineInTile * 2]
        val tileHigh = bus[address + lineInTile * 2 + 1]
        val bitIndex = 7 - (x % 8)
        val index = ((tileHigh.toInt() shr bitIndex) and 1) shl 1 or ((tileLow.toInt() shr bitIndex) and 1)

        return index
    }

    private fun drawOam() {
        val g = buffer.graphics

        if (!lcd.control.objEnable) return

        bus.oam.entries.filter { e -> e.y <= lcd.ly && (e.y + lcd.control.objSize) > lcd.ly }.forEach { e ->
            val address = 0x8000 + (e.tile.asInt() * 16)
            val line = lcd.ly - e.y
            val offset = if (e.yFlip) 7 - (line * 2) else line * 2
            val lo = bus[address + offset]
            val hi = bus[address + offset + 1]

            // check prio & palette

            for (x in 0..7) {
                val bitIndex = if (e.xFlip) x else 7 - x

                val index = ((hi.toInt() shr bitIndex) and 1) shl 1 or ((lo.toInt() shr bitIndex) and 1)

                if (index > 0) {
                    g.color = objectPalettes[e.palette.asInt()][index]
                    g.fillRect(e.x + x, lcd.ly, 1, 1)
                }
            }
        }
    }

    private fun nextLine() {
        dots -= DOTS_PER_LINE
        lcd.ly++
        drawn = false

        if (lcd.ly < VISIBLE_SCANLINES) {
            setMode(Mode.OAM_SCAN)
        } else if (lcd.ly == VISIBLE_SCANLINES) {
            interrupts.request(Interrupts.Interrupt.VBLANK)
            setMode(Mode.VBLANK)

            // inform the listeners of a complete frame
            listeners.forEach { it.onVBlank(buffer) }
        } else if (lcd.ly == TOTAL_SCANLINES) {
            lcd.ly = 0
            setMode(Mode.OAM_SCAN)
        }
    }

    companion object {
        private const val OAM_SCAN_DOTS = 80
        private const val HBLANK_DOTS = 252
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

    fun interface VBlankListener {
        fun onVBlank(frame: BufferedImage)
    }
}
