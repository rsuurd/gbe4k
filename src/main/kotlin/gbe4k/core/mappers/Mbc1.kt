package gbe4k.core.mappers

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Ram
import java.nio.file.Files
import java.nio.file.Path
import kotlin.experimental.and
import kotlin.io.path.exists

class Mbc1(
    private val data: ByteArray,
    private val ram: Boolean = false,
    private val battery: Boolean = false,
    path: Path? = null
) : Mapper, BatteryPowered {
    var romBank = 1
        private set(value) {
            val bank = value.and(0b00011111)

            field = if (bank == 0) {
                1
            } else {
                bank.and(banks)
            }
        }

    private val banks: Int
        get() = (data.size / ROM_BANK_SIZE) - 1

    var ramBank = 0
        private set

    private var ramBanks = (0..3).map { Ram(RAM_BANK) }

    var ramEnabled = false
        private set

    var advancedBanking = true
        private set

    private val savePath = path?.parent?.resolve("${path.toFile().nameWithoutExtension}.sav")

    override fun get(address: Int) = when (address) {
        in ROM -> data[address]
        in ROM_BANK -> data[(romBank * ROM_BANK_SIZE) + (address - ROM_BANK_SIZE)]
        in RAM_BANK -> {
            if (ram && ramEnabled) {
                ramBanks[ramBank][address]
            } else {
                0xff.toByte()
            }
        }

        else -> 0xff.toByte()
    }

    override fun set(address: Int, value: Byte) {
        when (address) {
            in RAM_ENABLE -> ramEnabled = ram && value.and(0xf).asInt() == 0xa
            in ROM_BANK_SELECT -> romBank = value.asInt()
            in RAM_BANK_SELECT -> {
                val bank = value.asInt().and(0b00000011)

                if (advancedBanking) {
                    romBank = romBank.or(bank.shl(4))
                } else {
                    ramBank = bank
                }
            }
            in MODE_SELECT -> advancedBanking = value.asInt() == 1
            in RAM_BANK -> {
                if (ram && ramEnabled) {
                    ramBanks[ramBank][address] = value
                }
            }
        }
    }

    override fun save() {
        if (ram && battery) {
            Files.write(savePath!!, ramBanks.flatten().toByteArray())
        }
    }

    override fun load() {
        if (ram && battery && savePath?.exists() == true) {
            val bytes = Files.readAllBytes(savePath)

            bytes.toList().chunked(RAM_BANK.last - RAM_BANK.first + 1).forEachIndexed { bank, data ->
                ramBanks[bank].copyFrom(data.toByteArray())
            }
        }
    }

    companion object {
        val ROM = 0x0000..0x3fff
        val ROM_BANK = 0x4000..0x7fff
        const val ROM_BANK_SIZE = 0x4000
        val RAM_BANK = 0xa000..0xbfff

        val RAM_ENABLE = 0x0000..0x1fff
        val ROM_BANK_SELECT = 0x2000..0x3fff
        val RAM_BANK_SELECT = 0x4000..0x5fff
        val MODE_SELECT = 0x6000..0x7fff
    }
}
