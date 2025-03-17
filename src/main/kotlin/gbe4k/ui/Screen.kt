package gbe4k.ui

import gbe4k.Gbe4k
import java.awt.Canvas
import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage

/**
 * The screen draws depending on the state of LCD
 */
class Screen : Canvas() {
    var emulator: Gbe4k? = null
        set(value) {
            field = value
            field?.ppu?.addVBlankListener { frame ->
                flip(frame)
            }
        }

    init {
        preferredSize = Dimension(WIDTH * SCALE, HEIGHT * SCALE)

        addKeyListener(object : KeyAdapter() {
            private fun handle(e: KeyEvent) {
                val pressed = e.id == KeyEvent.KEY_PRESSED

                emulator?.joypad?.apply {
                    when (e.keyCode) {
                        KeyEvent.VK_ENTER -> start = pressed
                        KeyEvent.VK_SHIFT -> select = pressed
                        KeyEvent.VK_L -> a = pressed
                        KeyEvent.VK_K -> b = pressed
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

    fun flip(frame: BufferedImage) {
        val strategy = bufferStrategy

        if (strategy == null) {
            createBufferStrategy(3)
        } else {
            val graphics = strategy.drawGraphics

            graphics.drawImage(frame, 0, 0, width, height, null)

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
