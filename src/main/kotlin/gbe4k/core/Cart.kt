package gbe4k.core

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.mappers.Mapper
import gbe4k.core.mappers.Mbc1
import java.nio.file.Files
import java.nio.file.Path

class Cart(private val data: ByteArray) : Addressable {
    val title: String get() = String(data.sliceArray(IntRange(0x134, 0x142))).replace("\u0000", "")
    val size: Int get() = 32 shl data[0x148].toInt()
    val type: Byte get() = data[0x147]

    private val mapper: Mapper? = when(type.asInt()) {
        0x1 -> Mbc1(data, hasRam = false)
        0x2 -> Mbc1(data, hasRam = true)
        0x3 -> Mbc1(data, hasRam = true) // battery
        else -> null
    }

    override fun get(address: Int): Byte = mapper?.get(address) ?: data[address]
    override fun set(address: Int, value: Byte) {
        mapper?.set(address, value)

        // TODO
    }

    override fun toString() = "Cart(title=$title, size=$size kb, rom size=${data.size.hex()}, type=${type.hex()}"

    companion object {
        fun load(path: Path): Cart {
            val bytes = Files.readAllBytes(path)

            return Cart(bytes)
        }
    }
}
