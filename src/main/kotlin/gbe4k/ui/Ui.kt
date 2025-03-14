package gbe4k.ui

import gbe4k.Gbe4k
import gbe4k.core.Cart
import gbe4k.dump
import java.awt.BorderLayout
import java.awt.EventQueue
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.SwingConstants
import javax.swing.WindowConstants

class Ui : JFrame("GBE 4k") {
    private var status = JLabel("No ROM Selected", SwingConstants.CENTER)
    private var emulator: Gbe4k? = null
    private val screen = Screen()

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        contentPane.layout = BorderLayout()
        contentPane.add(screen, BorderLayout.CENTER)
        contentPane.add(status, BorderLayout.SOUTH)

        jMenuBar = JMenuBar().apply {
            add(JMenu("File").apply {
                add(JMenuItem("Load rom...").apply {
                    addActionListener {
                        val chooser = JFileChooser(".roms")

                        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                            emulate(chooser.selectedFile.toPath())
                        }
                    }
                })
            })
        }

        pack()
        setLocationRelativeTo(null)

        isVisible = true

        javax.swing.Timer(10) {
            status.text = emulator?.cpu?.dump() ?: "No ROM selected"
        }.start()
    }

    private fun emulate(path: Path) {
        emulator?.stop()
        val cart = Cart.load(path)
        title = "GBE 4k - ${cart.title}"
        emulator = Gbe4k(cart, System.out)
        screen.emulator = emulator

        Thread {
            emulator!!.emulate()
        }.start()
    }
}

fun main(vararg arg: String) {
    EventQueue.invokeLater {
        Ui()
    }
}
