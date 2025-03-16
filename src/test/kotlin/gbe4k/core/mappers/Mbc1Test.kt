package gbe4k.core.mappers

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.mappers.Mbc1.Companion.MODE_SELECT
import gbe4k.core.mappers.Mbc1.Companion.RAM_BANK
import gbe4k.core.mappers.Mbc1.Companion.RAM_BANK_SELECT
import gbe4k.core.mappers.Mbc1.Companion.RAM_ENABLE
import gbe4k.core.mappers.Mbc1.Companion.ROM
import gbe4k.core.mappers.Mbc1.Companion.ROM_BANK
import gbe4k.core.mappers.Mbc1.Companion.ROM_BANK_SELECT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Mbc1Test {
    private val mbc1 = Mbc1(ByteArray(512 * 1024) { 0x32 }, ram = true, battery = false)

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
        val noRam = Mbc1(ByteArray(512 * 1024) { 0x32 }, ram = false)

        for (address in RAM_BANK) {
            assertThat(noRam[address].asInt()).isEqualTo(0xff)
        }
    }

    @Test
    fun `should not enable ram if cart has no ram`() {
        val noRam = Mbc1(ByteArray(512 * 1024) { 0x32 }, ram = false)

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
}
