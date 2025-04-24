package gbe4k.core.mappers

import gbe4k.core.Cpu.Companion.asInt
import java.nio.file.Path

class Mbc1(data: ByteArray, ramSize: Int = 0, battery: Boolean = false, path: Path? = null) : Mbc(data, ramSize, battery, path) {
    var advancedBanking = true
        private set(value) {
            field = value

            if (!value) {
                romBank = 1
            }
        }

    override fun Int.bank0Address() = if (advancedBanking) {
        ((ramBank shl 5) * ROM_BANK_SIZE) + this
    } else {
        this
    }

    override fun Int.bankAddress(): Int {
        val romBank = if (advancedBanking) {
            ((ramBank shl 5) or romBank).and(0x7F)
        } else {
            romBank
        }.coerceAtMost(banks - 1)

        return (romBank * ROM_BANK_SIZE) + (this - ROM_BANK_SIZE)
    }

    override fun selectMode(value: Byte) {
        advancedBanking = value.asInt().and(0x1) == 1
    }
}
