package gbe4k.core

import java.nio.file.Files
import java.nio.file.Path

class Cart(private val data: ByteArray) {
    val title: String get() = String(data.sliceArray(IntRange(0x134, 0x142))).replace("\u0000", "")
    val size: Int get() = 32 shl data[0x148].toInt()

    fun read(address: Int) = data[address]

    override fun toString() = "Cart(title=$title, size=$size kb)"

    companion object {
        fun load(path: Path): Cart {
            val bytes = Files.readAllBytes(path)

            return Cart(bytes)
        }
    }
}
