package gbe4k.core

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.mappers.BatteryPowered
import gbe4k.core.mappers.Mapper
import gbe4k.core.mappers.Mbc1
import gbe4k.core.mappers.Mbc2
import java.nio.file.Files
import java.nio.file.Path

class Cart(private val data: ByteArray, path: Path? = null) : Addressable {
    val title: String get() = String(data.sliceArray(IntRange(0x134, 0x142))).replace("\u0000", "")
    val type: Byte get() = data[0x147]
    val size: Int get() = 32 shl data[0x148].toInt()
    val ramSize: Int get() = when(data[0x149].toInt()) {
        0x2 -> 8
        0x3 -> 32
        0x4 -> 128
        0x5 -> 64
        else -> 0
    }

    private val mapper: Mapper? = when (type.asInt()) {
        0x1 -> Mbc1(data)
        0x2 -> Mbc1(data, ramSize = ramSize)
        0x3 -> Mbc1(data, ramSize = ramSize, battery = true, path = path)
        0x5 -> Mbc2(data)
        0x6 -> Mbc2(data, battery = true, path = path)

        else -> null
    }

    override fun get(address: Int): Byte = mapper?.get(address) ?: data[address]
    override fun set(address: Int, value: Byte) {
        mapper?.set(address, value)
    }

    fun load() {
        if (mapper is BatteryPowered) {
            mapper.load()
        }
    }

    override fun toString() = "Cart(title=$title, type=${type.hex()}, size=${size}kb, ram=${ramSize}kb)"

    companion object {
        fun load(path: Path): Cart {
            val bytes = Files.readAllBytes(path)

            return Cart(bytes, path)
        }
    }
}
