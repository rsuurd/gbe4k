package gbe4k.core.mappers

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Ram
import java.nio.file.Files
import java.nio.file.Path
import kotlin.experimental.and
import kotlin.io.path.exists

class Mbc1(
    private val data: ByteArray,
    private val ramSize: Int = 0,
    private val battery: Boolean = false,
    path: Path? = null
) : Mapper, BatteryPowered {
    var romBank = 1
        private set(value) {
            field = (value and 0b11111).coerceAtLeast(1)
        }
    private val banks: Int
        get() = data.size / ROM_BANK_SIZE

    private val ram: Boolean = ramSize > 0

    private var ramBank = 0
        private set(value) {
            field = value and 0b11
        }
    private var ramBanks = (0 until (ramSize / 8)).map { Ram(RAM_BANK) }

    var ramEnabled = false
        private set(value) {
            field = value

            if (!value) {
                save()
            }
        }

    var advancedBanking = true
        private set(value) {
            field = value

            if (!value) {
                romBank = 1
            }
        }

    private val savePath = path?.parent?.resolve("${path.toFile().nameWithoutExtension}.sav")

    override fun get(address: Int) = when (address) {
        in ROM -> data[address.bank0Address()]
        in ROM_BANK -> data[address.bankAddress()]
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
            in RAM_BANK_SELECT -> ramBank = value.asInt()
            in MODE_SELECT -> advancedBanking = value.asInt().and(0x1) == 1
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

    private fun Int.bank0Address() = if (advancedBanking) {
        ((ramBank shl 5) * ROM_BANK_SIZE) + this
    } else {
        this
    }

    private fun Int.bankAddress(): Int {
        val romBank = if (advancedBanking) {
            ((ramBank shl 5) or romBank).and(0x7F)
        } else {
             romBank
        }.coerceAtMost(banks - 1)

        return (romBank * ROM_BANK_SIZE) + (this - ROM_BANK_SIZE)
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
