package gbe4k.ui

import gbe4k.Gbe4k
import gbe4k.core.Bus.Companion.VRAM
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.dump
import java.awt.Canvas
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage

/**
 * The screen draws depending on the state of LCD
 */
class Screen(var emulator: Gbe4k?) : Canvas() {
    private val buffer: BufferedImage

    init {
        preferredSize = Dimension(WIDTH * SCALE, HEIGHT * SCALE)

        buffer = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)

        javax.swing.Timer(10) {
            flip()
        }.start()

        addKeyListener(object: KeyAdapter() {
            private fun handle(e: KeyEvent) {
                val pressed = e.id == KeyEvent.KEY_PRESSED

                emulator?.joypad?.apply {
                    when (e.keyCode) {
                        KeyEvent.VK_ENTER -> start = pressed
                        KeyEvent.VK_SHIFT -> select = pressed
                        KeyEvent.VK_CONTROL -> a = pressed
                        KeyEvent.VK_ALT -> b = pressed
                        KeyEvent.VK_W -> up = pressed
                        KeyEvent.VK_S -> down = pressed
                        KeyEvent.VK_A -> left = pressed
                        KeyEvent.VK_D -> right = pressed
                    }
                }
            }

            override fun keyPressed(e: KeyEvent) = handle(e)
            override fun keyReleased(e: KeyEvent) = handle(e)
        })
    }

    fun flip() {
        val strategy = bufferStrategy

        if (strategy == null) {
            createBufferStrategy(3)
        } else {
            val graphics = strategy.drawGraphics

            emulator?.ppu?.buffer?.let { buffer ->
                graphics.drawImage(buffer, 0, 0, width, height, null)
            }

            graphics.dispose()
            strategy.show()
        }
    }

    companion object {
        const val WIDTH: Int = 160
        const val HEIGHT: Int = 160 // 144
        const val SCALE = 3
    }
}
