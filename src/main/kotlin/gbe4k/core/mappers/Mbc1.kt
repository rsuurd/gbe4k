package gbe4k.core.mappers

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Ram
import kotlin.experimental.and

class Mbc1(
    private val data: ByteArray,
    private val ram: Boolean = false,
    private val battery: Boolean = false
) : Mapper {
    var romBank = 1
        private set(value) {
            val bank = value.and(0b00011111)

            field = if (bank == 0) {
                1
            } else {
                 // TODO mask by the amount of bits required to access the amount of banks
                bank
            }
        }

    var ramBank = 0
        private set(value) {
            val bank = value.and(0b00000011)

            // TODO might also select rom bank, depending on advanced banking mode
            field = bank
        }

    private var ramBanks = (0..3).map { Ram(RAM_BANK) }

    var ramEnabled = false
        private set

    var advancedBanking = false
        private set

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
            in RAM_BANK_SELECT -> ramBank = value.asInt()
            in MODE_SELECT -> advancedBanking = value.asInt() == 1
            in RAM_BANK -> {
                if (ram && ramEnabled) {
                    ramBanks[ramBank][address] = value

                    // if battery; store the ram banks
                }
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
