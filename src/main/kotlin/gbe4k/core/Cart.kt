package gbe4k.core

import gbe4k.core.Cpu.Companion.hex
import java.nio.file.Files
import java.nio.file.Path

class Cart(private val data: ByteArray) : Addressable {
    val title: String get() = String(data.sliceArray(IntRange(0x134, 0x142))).replace("\u0000", "")
    val size: Int get() = 32 shl data[0x148].toInt()
    val type: Byte get() = data[0x147]

    override fun get(address: Int): Byte = data[address]
    override fun set(address: Int, value: Byte) {}

    override fun toString() = "Cart(title=$title, size=$size kb, type=${type.hex()}"

    companion object {
        fun load(path: Path): Cart {
            val bytes = Files.readAllBytes(path)

            return Cart(bytes)
        }
    }
}
