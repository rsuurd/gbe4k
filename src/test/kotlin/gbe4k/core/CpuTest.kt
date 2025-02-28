package gbe4k.core

import gbe4k.core.Cpu.Companion.hi
import gbe4k.core.Cpu.Companion.hiNibble
import gbe4k.core.Cpu.Companion.lo
import gbe4k.core.Cpu.Companion.loNibble
import gbe4k.core.Cpu.Companion.n16
import gbe4k.core.Cpu.Companion.n8
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CpuTest : CpuTestSupport() {
    @Test
    fun `should split int to bytes`() {
        val value = 0xfc44

        assertThat(value.hi()).isEqualTo(0xfc.toByte())
        assertThat(value.lo()).isEqualTo(0x44.toByte())
    }

    @Test
    fun `should combine bytes to int`() {
        assertThat(n16(0xfc.toByte(), 0x44.toByte())).isEqualTo(0xfc44)
    }

    @Test
    fun `should split byte to nibbles`() {
        val value = 0xaf.toByte()

        assertThat(value.hiNibble()).isEqualTo(0xa)
        assertThat(value.loNibble()).isEqualTo(0xf)
    }

    @Test
    fun `should combine nibbles to byte`() {
        assertThat(n8(0xa, 0xf)).isEqualTo(0xaf)
    }
}
