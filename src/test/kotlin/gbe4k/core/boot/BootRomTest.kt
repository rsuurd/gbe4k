package gbe4k.core.boot

import gbe4k.core.boot.BootRom.Companion.BOOTROM
import gbe4k.core.boot.BootRom.Companion.BOOTROM_BANK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BootRomTest {
    private val bootRom = BootRom(ByteArray(BOOTROM.last + 1) { 0x11 })

    @Test
    fun `should be booting`() {
        assertThat(bootRom.booting).isTrue()
    }

    @Test
    fun `should read`() {
        for (address in BOOTROM) {
            assertThat(bootRom[address]).isEqualTo(0x11)
        }
    }

    @Test
    fun `should write`() {
        for (address in BOOTROM) {
            assertThat(bootRom[address]).isEqualTo(0x11)
        }
    }

    @Test
    fun `should set booting flag to false`() {
        bootRom[BOOTROM_BANK] = 0x1

        assertThat(bootRom.booting).isFalse()
    }

    @Test
    fun `should not set booting flag to false when writing 0`() {
        bootRom[BOOTROM_BANK] = 0x0

        assertThat(bootRom.booting).isTrue()
    }
}