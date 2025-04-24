package gbe4k.core.mappers

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Ram
import java.nio.file.Files
import java.nio.file.Path
import kotlin.experimental.and
import kotlin.io.path.exists

abstract class Mbc(
    private val data: ByteArray,
    private val ramSize: Int,
    private val battery: Boolean,
    path: Path?
) : Mapper, BatteryPowered {
    protected val banks = data.size / ROM_BANK_SIZE
    private val hasRam: Boolean = ramSize > 0
    private val ramBanks = if (hasRam) {
        // create N 8k ram banks
        (0 until (ramSize / 0x2000).coerceAtLeast(1)).map { Ram(RAM_BANK) }
    } else {
        listOf()
    }
    private val savePath = path?.parent?.resolve("${path.toFile().nameWithoutExtension}.sav")

    var romBank = 1
        protected set

    var ramBank = 0
        protected set(value) {
            field = value and 0b11
        }

    var ramEnabled = false
        private set(value) {
            field = value

            if (!value) {
                save()
            }
        }

    override fun get(address: Int) = when (address) {
        in ROM -> data[address.bank0Address()]
        in ROM_BANK -> data[address.bankAddress()]
        in RAM_BANK -> readRam(address)

        else -> `0xff`
    }

    protected open fun Int.bank0Address() = this
    protected open fun resolveRomBank() = romBank.coerceAtMost(banks - 1)

    private fun Int.bankAddress(): Int {
        val romBank = resolveRomBank()

        return (romBank * ROM_BANK_SIZE) + (this - ROM_BANK_SIZE)
    }

    protected open fun readRam(address: Int) = if (hasRam && ramEnabled) {
        ramBanks[ramBank][address]
    } else {
        `0xff`
    }

    override fun set(address: Int, value: Byte) {
        when (address) {
            in RAM_ENABLE -> ramEnabled = hasRam && value.and(0xf).asInt() == 0xa
            in ROM_BANK_SELECT -> romBank = value.asInt().coerceAtLeast(1).and(0x7f)
            in RAM_BANK_SELECT -> ramBank = value.asInt()
            in MODE_SELECT -> selectMode(value)
            in RAM_BANK -> writeRam(address, value)
        }
    }

    protected abstract fun selectMode(value: Byte)

    protected open fun writeRam(address: Int, value: Byte) {
        if (hasRam && ramEnabled) {
            ramBanks[ramBank][address] = value
        }
    }

    override fun save() {
        if (hasRam && battery) {
            Files.write(savePath!!, ramBanks.flatten().toByteArray())
        }
    }

    override fun load() {
        if (hasRam && battery && savePath?.exists() == true) {
            val bytes = Files.readAllBytes(savePath)

            bytes.toList().chunked(RAM_BANK.last - RAM_BANK.first + 1).forEachIndexed { bank, data ->
                ramBanks[bank].copyFrom(data.toByteArray())
            }
        }
    }

    companion object {
        private const val `0xff` = 0xff.toByte()

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