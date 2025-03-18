package gbe4k.core.mappers

import gbe4k.core.mappers.Mbc2.Companion.ROM
import gbe4k.core.mappers.Mbc2.Companion.ROM_BANK
import gbe4k.core.mappers.Mbc2.Companion.RAM
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Mbc2Test {
    private val mbc = Mbc2(ByteArray(512 * 1024) { 0x32 }, battery = false)

    @Test
    fun `should directly read from banks 0 and 1`() {
        for (address in ROM) {
            assertThat(mbc[address]).isEqualTo(0x32)
        }

        for (address in ROM_BANK) {
            assertThat(mbc[address]).isEqualTo(0x32)
        }
    }

    @Test
    fun `should read from all rom banks`() {
        for (bank in 1..15) {
            mbc[0x0100 + bank] = bank.toByte()

            for (address in ROM_BANK) {
                assertThat(mbc[address]).isEqualTo(0x32)
            }
        }
    }


    @Test
    fun `should enable, read & write ram`() {
        mbc[0x0000] = 0xa

        for (address in RAM) {
            mbc[address] = 0x50
            assertThat(mbc[address]).isEqualTo(0x50)
        }
    }

    @Test
    fun `should not overwrite ram if not enabled`() {
        mbc[0x0000] = 0xa
        for (address in RAM) {
            mbc[address] = 0x50
        }

        mbc[0x0000] = 0x0
        for (address in RAM) {
            mbc[address] = 0x0f
        }

        mbc[0x0000] = 0xa
        for (address in RAM) {
            assertThat(mbc[address]).isEqualTo(0x50)
        }
    }
}