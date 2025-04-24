package gbe4k.core.mappers

import gbe4k.core.Bus.Companion.CART_RAM
import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.mappers.Mbc.Companion.MODE_SELECT
import gbe4k.core.mappers.Mbc.Companion.RAM_BANK
import gbe4k.core.mappers.Mbc.Companion.RAM_BANK_SELECT
import gbe4k.core.mappers.Mbc.Companion.RAM_ENABLE
import gbe4k.core.mappers.Mbc.Companion.ROM
import gbe4k.core.mappers.Mbc.Companion.ROM_BANK
import gbe4k.core.mappers.Mbc.Companion.ROM_BANK_SELECT
import gbe4k.core.mappers.Mbc.Companion.ROM_BANK_SIZE
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.util.Files
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes

class Mbc1Test {
    private val mbc1 = Mbc1(ByteArray(512 * 1024) { 0x32 }, ramSize = 32768, battery = false)

    @Test
    fun `should directly read from first rom bank`() {
        for (address in ROM) {
            assertThat(mbc1[address]).isEqualTo(0x32)
        }
    }

    @Test
    fun `should default to rom bank 1`() {
        assertThat(mbc1.romBank).isEqualTo(1)

        for (address in ROM_BANK) {
            assertThat(mbc1[address]).isEqualTo(0x32)
        }
    }

    @Test
    fun `should read from all rom banks`() {
        for (bank in 1..31) {
            mbc1[ROM_BANK_SELECT.random()] = bank.toByte()
            assertThat(mbc1.romBank).isEqualTo(bank)

            for (address in ROM_BANK) {
                assertThat(mbc1[address]).isEqualTo(0x32)
            }
        }
    }

    @Test
    fun `should enable, read, write and finally disable ram across all banks`() {
        mbc1[RAM_ENABLE.random()] = 0xa
        assertThat(mbc1.ramEnabled).isTrue()

        for (bank in 0..3) {
            mbc1[RAM_BANK_SELECT.random()] = bank.toByte()

            for (address in RAM_BANK) {
                mbc1[address] = 0x50
                assertThat(mbc1[address].asInt()).isEqualTo(0x50)
            }
        }

        mbc1[RAM_ENABLE.random()] = 0x0
        assertThat(mbc1.ramEnabled).isFalse()
    }

    @Test
    fun `should not overwrite ram if not enabled`() {
        mbc1[RAM_ENABLE.random()] = 0xa //enable
        for (address in RAM_BANK) {
            mbc1[address] = 0x50 // set some values
        }

        mbc1[RAM_ENABLE.random()] = 0x0 // disable
        for (address in RAM_BANK) {
            mbc1[address] = 0x0f // try overwrite
        }

        mbc1[RAM_ENABLE.random()] = 0xa // enable
        for (address in RAM_BANK) {
            assertThat(mbc1[address]).isEqualTo(0x50) // original value
        }
    }

    @Test
    fun `should not read ram if cart has no ram`() {
        val noRam = Mbc1(ByteArray(512 * 1024) { 0x32 }, ramSize = 0)

        for (address in RAM_BANK) {
            assertThat(noRam[address].asInt()).isEqualTo(0xff)
        }
    }

    @Test
    fun `should not enable ram if cart has no ram`() {
        val noRam = Mbc1(ByteArray(512 * 1024) { 0x32 }, ramSize = 0)

        for (address in RAM_ENABLE) {
            noRam[address] = 0xa
            assertThat(noRam.ramEnabled).isFalse()
        }
    }

    @Test
    fun `should select banking mode`() {
        mbc1[MODE_SELECT.random()] = 1

        assertThat(mbc1.advancedBanking).isTrue()
    }

    @Test
    fun `should store ram if cart has battery`() {
        val temp = Path.of(Files.temporaryFolderPath())

        val mbc = Mbc1(ByteArray(512 * 1024) { 0x32 }, ramSize = 65536, battery = true, path = temp.resolve("game.gb"))

        mbc[RAM_ENABLE.random()] = 0xa
        mbc[CART_RAM.random()] = 0x23

        mbc.save()

        val save = temp.resolve("game.sav")
        assertThat(save.exists()).isTrue()
        assertThat(save.readBytes()).contains(0x23)
    }

    @Test
    fun `should load ram if cart has battery and save file exists`() {
        val temp = Path.of(Files.temporaryFolderPath())
        temp.resolve("game.sav").writeBytes(ByteArray(32 * 1024) { 0x11 })

        val mbc = Mbc1(ByteArray(512 * 1024), ramSize = 32768, battery = true, path = temp.resolve("game.gb"))
        mbc.load()
        mbc[RAM_ENABLE.random()] = 0xa

        for (bank in 0..3) {
            mbc[RAM_BANK_SELECT.random()] = bank.toByte()

            for (address in RAM_BANK) {
                assertThat(mbc[address]).isEqualTo(0x11)
            }
        }
    }

    @Test
    fun `should ignore rom bank 0 and fallback to bank 1`() {
        mbc1[ROM_BANK_SELECT.random()] = 0x00
        assertThat(mbc1.romBank).isEqualTo(1)
    }

    @Test
    fun `should select correct rom bank in advanced banking mode`() {
        mbc1[MODE_SELECT.random()] = 1 // advanced banking mode
        mbc1[RAM_BANK_SELECT.random()] = 1 // upper bits
        mbc1[ROM_BANK_SELECT.random()] = 2 // lower bits

        val combinedBank = (1 shl 5) or 2 // 0x22
        val expectedOffset = (combinedBank * ROM_BANK_SIZE) - ROM_BANK_SIZE
        val actualByte = mbc1[ROM_BANK.first]

        assertThat(mbc1.romBank).isEqualTo(2) // still shows raw lower bits
        assertThat(actualByte).isEqualTo(0x32)
    }
}
