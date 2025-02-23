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
    fun `should init`() {
        assertThat(cpu.pc).isEqualTo(0x100)
        assertThat(cpu.ime).isTrue()
        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.registers.b).isEqualTo(0x00)
        assertThat(cpu.registers.c).isEqualTo(0x00)
        assertThat(cpu.registers.d).isEqualTo(0x00)
        assertThat(cpu.registers.e).isEqualTo(0x00)
        assertThat(cpu.registers.f).isEqualTo(0x00)
        assertThat(cpu.registers.h).isEqualTo(0x00)
        assertThat(cpu.registers.l).isEqualTo(0x00)
        assertThat(cpu.registers.af).isEqualTo(0x00)
        assertThat(cpu.registers.bc).isEqualTo(0x00)
        assertThat(cpu.registers.de).isEqualTo(0x00)
        assertThat(cpu.registers.hl).isEqualTo(0x00)
        assertThat(cpu.registers.sp).isEqualTo(0x00)
    }

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

    @Test
    fun `should execute nop`() {
        stepWith(0x00)

        assertThat(cpu.pc).isEqualTo(0x0101)
    }
    @Test
    fun `should execute LD SP, d16`() {
        stepWith(0x31, 0xff, 0xff)

        assertThat(cpu.pc).isEqualTo(0x0103)
        assertThat(cpu.registers.sp).isEqualTo(0xffff)
    }

    @Test
    fun `should execute jp`() {
        stepWith(0xc3, 0x50, 0x01)

        assertThat(cpu.pc).isEqualTo(0x0150)
    }

    @Test
    fun `should execute di`() {
        stepWith(0xf3)

        assertThat(cpu.pc).isEqualTo(0x0101)
        assertThat(cpu.ime).isFalse()
    }

    private fun stepWith(vararg bytes: Number) {
        withBytes(*bytes) {
            cpu.step()
        }
    }
}
