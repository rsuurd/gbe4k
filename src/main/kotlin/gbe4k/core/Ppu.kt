package gbe4k.core

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.setBit
import gbe4k.core.io.Interrupts
import gbe4k.core.io.Lcd
import java.awt.Color
import java.awt.image.BufferedImage

class Ppu(val bus: Bus, val lcd: Lcd, val interrupts: Interrupts) {
    private var buffer = BufferedImage(160, 144, BufferedImage.TYPE_INT_RGB)
    private val graphics = buffer.graphics

    private var enabled = true
    private var dots = 0
    private var drawn = false
    private var drawWindow = false

    private var objects = listOf<OamEntry>()
    private val listeners = mutableListOf<VBlankListener>()

    private val scanline = Array(160) { 0 }
    private var windowY = 0

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

    private fun disable() {
        enabled = false
        drawn = false
        windowY = 0
        drawWindow = false
        dots = 0
        lcd.ly = 0
        lcd.stat.ppuMode = Mode.HBLANK
    }

    fun update(cycles: Int) {
        if (enabled && !lcd.control.enabled) {
            disable()
        } else if (!enabled && lcd.control.enabled) {
            enabled = true
        }

        if (!enabled) return

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

        objects = bus.oam.entries.filter { e -> e.isVisible() }.take(10).distinctBy { it.x }

        var windowOnScanline = false

        for (x in 0 until 160) {
            windowOnScanline = lcd.control.windowEnabled && drawWindow && x >= lcd.wx

            val index = if (windowOnScanline) {
                fetchPixel((x - lcd.wx), windowY, lcd.control.windowTileMap)
            } else if (lcd.control.priority) {
                fetchPixel((x + lcd.scx) % 256, (lcd.ly + lcd.scy) % 256, lcd.control.backgroundTileMap)
            } else {
                0
            }

            scanline[x] = index
            graphics.color = backgroundPalette[index]
            graphics.fillRect(x, lcd.ly, 1, 1)

            drawOam(x)
        }

        if (windowOnScanline) {
            windowY++
        }

        drawn = true
    }

    private fun fetchPixel(x: Int, y: Int, tileMap: IntRange): Int {
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

    private fun drawOam(x: Int) {
        if (!lcd.control.objEnable) return

        objects.filter { x in it.x until (it.x + 8) }.forEach { obj ->
            val tile = if (lcd.control.objSize > 8) {
                obj.tile.setBit(false, 0)
            } else {
                obj.tile
            }

            val address = 0x8000 + (tile.asInt() * 16)
            val offset = if (obj.yFlip) {
                (obj.y + lcd.control.objSize - 1) - lcd.ly
            } else {
                lcd.ly - obj.y
            } * 2

            val lo = bus[address + offset]
            val hi = bus[address + offset + 1]

            val bitIndex = if (obj.xFlip) {
                x - obj.x
            } else {
                7 - (x - obj.x)
            }

            val index = ((hi.toInt() shr bitIndex) and 1) shl 1 or ((lo.toInt() shr bitIndex) and 1)

            if ((index > 0) && (!obj.priority || scanline[x] == 0)) {
                graphics.color = objectPalettes[obj.palette.asInt()][index]
                graphics.fillRect(x, lcd.ly, 1, 1)
            }
        }
    }

    private fun OamEntry.isVisible() = y <= lcd.ly && (y + lcd.control.objSize) > lcd.ly

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
            windowY = 0
            drawWindow = false
            setMode(Mode.OAM_SCAN)
        }

        drawWindow = drawWindow || (lcd.wy == lcd.ly)
    }

    companion object {
        private const val OAM_SCAN_DOTS = 80
        private const val HBLANK_DOTS = 289
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
