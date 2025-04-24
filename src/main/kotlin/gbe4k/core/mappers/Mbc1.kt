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

    override fun resolveRomBank() = romBank.and(0b11111).let { maskedBank ->
        if (advancedBanking) {
            (ramBank shl 5) or maskedBank
        } else {
            maskedBank
        }.let { bank ->
            // bank 0 not allowed
            if (bank.and(0x1f) == 0) {
                bank + 1
            } else {
                bank
            }
        }.coerceAtMost(banks - 1)
    }

    override fun Int.bank0Address() = if (advancedBanking) {
        ((ramBank shl 5) * ROM_BANK_SIZE) + this
    } else {
        this
    }

    override fun selectMode(value: Byte) {
        advancedBanking = value.asInt().and(0x1) == 1
    }
}
