package gbe4k

import gbe4k.core.Ppu
import java.awt.Color
import java.awt.event.KeyEvent
import java.nio.file.Path
import java.util.*
import kotlin.io.path.outputStream
import kotlin.io.path.reader

data class Settings(
    val skipBootRom: Boolean = true,
    val palette: List<Color> = Ppu.GRAY_PALETTE,
    val keys: Keys = Keys()
) {
    data class Keys(
        val start: Int = KeyEvent.VK_ENTER,
        val select: Int = KeyEvent.VK_SHIFT,
        val a: Int = KeyEvent.VK_L,
        val b: Int = KeyEvent.VK_K,
        val up: Int = KeyEvent.VK_W,
        val down: Int = KeyEvent.VK_S,
        val left: Int = KeyEvent.VK_A,
        val right: Int = KeyEvent.VK_D
    )

    companion object {
        private const val SKIP_BOOT_ROM_PROPERTY_NAME = "skipBootRom"
        private const val PALETTE_PROPERTY_NAME = "palette"
        private const val KEYS_PROPERTY_NAME = "keys"

        fun Settings.store(path: Path) {
            val keyMappings = listOf(keys.start, keys.select, keys.a, keys.b, keys.up, keys.down, keys.left, keys.right)

            val properties = Properties().apply {
                setProperty(SKIP_BOOT_ROM_PROPERTY_NAME, "$skipBootRom")
                setProperty(PALETTE_PROPERTY_NAME, palette.joinToString { Integer.toHexString(it.rgb).substring(2) })
                setProperty(KEYS_PROPERTY_NAME, keyMappings.joinToString())
            }

            properties.store(path.outputStream(), null)
        }

        fun load(path: Path): Settings = try {
            val properties = Properties()
            properties.load(path.reader())

            val skipBootRom = properties.getProperty(SKIP_BOOT_ROM_PROPERTY_NAME)?.toBoolean() ?: false
            val palette = properties.getProperty(PALETTE_PROPERTY_NAME)?.let { property ->
                property.split(',').map { Color.decode(it.trim()) }
            } ?: Ppu.GRAY_PALETTE
            val keys = properties.getProperty(KEYS_PROPERTY_NAME)?.let { property ->
                property.split(',').map { it.trim().toInt() }
            }?.let {
                Keys(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7])
            } ?: Keys()

            Settings(skipBootRom, palette, keys)
        } catch (e: Exception) {
            Settings()
        }
    }
}
