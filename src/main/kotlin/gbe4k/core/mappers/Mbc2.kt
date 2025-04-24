package gbe4k.core.mappers

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Ram
import java.nio.file.Files
import java.nio.file.Path
import kotlin.experimental.and
import kotlin.io.path.exists

// TODO refactor -> extend Mbc class
class Mbc2(
    private val data: ByteArray,
    private val battery: Boolean = false,
    path: Path? = null
) : Mapper, BatteryPowered {
    private var romBank = 1
        set(value) {
            field = value.and(0xf).takeUnless { it == 0 } ?: 1
        }
    private var ramEnabled: Boolean = false
    private val ram = Ram(RAM)
    private val savePath = path?.parent?.resolve("${path.toFile().nameWithoutExtension}.sav")

    override fun get(address: Int) = when (address) {
        in ROM -> data[address]
        in ROM_BANK -> data[(romBank * ROM_BANK_SIZE) + (address - ROM_BANK_SIZE)]
        in RAM, in ECHO_RAM -> {
            if (ramEnabled) {
                ram[address.ramAddress()]
            } else {
                0xff.toByte()
            }
        }

        else -> 0xff.toByte()
    }

    override fun set(address: Int, value: Byte) {
        when (address) {
            in SELECT -> {
                if (address.and(0x0100) == 0) { // bit 8 clear
                    ramEnabled = value.and(0xf) == 0xa.toByte()
                } else { // bit 8 is set
                    romBank = value.asInt()
                }
            }

            in RAM, in ECHO_RAM -> {
                if (ramEnabled) {
                    ram[address.ramAddress()] = value
                }
            }
        }
    }

    private fun Int.ramAddress() = (this % RAM_SIZE) + RAM.first

    override fun save() {
        if (battery) {
            Files.write(savePath!!, Iterable { ram.iterator() }.toList().toByteArray())
        }
    }

    override fun load() {
        if (battery && savePath?.exists() == true) {
            val bytes = Files.readAllBytes(savePath)

            ram.copyFrom(bytes)
        }
    }

    companion object {
        val ROM = 0x0000..0x3fff
        val ROM_BANK = 0x4000..0x7fff
        const val ROM_BANK_SIZE = 0x4000
        val RAM = 0xa000..0xa1ff
        val RAM_SIZE = RAM.last - RAM.first + 1
        val ECHO_RAM = 0xa200..0xbfff

        val SELECT = 0x0000..0x3fff
    }
}
