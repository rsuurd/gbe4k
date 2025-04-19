package gbe4k.ui

import gbe4k.Gbe4k
import gbe4k.Settings
import gbe4k.core.Cart
import gbe4k.dump
import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetAdapter
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.SwingConstants
import javax.swing.WindowConstants


class Ui : JFrame("GBE 4k") {
    private val settings = Settings.load(Paths.get("gbe4k.properties"))

    private var status = JLabel("No ROM Selected", SwingConstants.CENTER)
    private var emulator: Gbe4k? = null
    private val screen = Screen(settings.keys)

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

        DropTarget(this, object : DropTargetAdapter() {
            override fun drop(e: DropTargetDropEvent) {
                e.acceptDrop(DnDConstants.ACTION_COPY)

                e.transferable.transferDataFlavors
                    .filter { flavor -> flavor.isFlavorJavaFileListType }
                    .flatMap { flavor ->
                        @Suppress("UNCHECKED_CAST")
                        e.transferable.getTransferData(flavor) as List<File>
                    }.first().let { file ->
                        emulate(file.toPath())
                    }

                e.dropComplete(true)
            }
        })

        javax.swing.Timer(10) {
            status.text = emulator?.cpu?.dump() ?: "No ROM selected"
        }.start()
    }

    private fun emulate(path: Path) {
        emulator?.stop()
        val cart = Cart.load(path)
        println("Loaded cart: $cart")
        title = "GBE 4k - ${cart.title}"
        emulator = Gbe4k(settings, cart)
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
