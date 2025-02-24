package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CallTest : CpuTestSupport() {
    @Test
    fun `should call a16`() {
        stepWith(0xcd, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should call z, a16 when z is set`() {
        cpu.flags.z = true

        stepWith(0xcc, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should not call z, a16 when z is not set`() {
        cpu.flags.z = false

        stepWith(0xcc, 0xff, 0x34)

        assertThat(cpu.pc).isNotEqualTo(0x34ff)
    }

    @Test
    fun `should call c, a16 when z is set`() {
        cpu.flags.c = true

        stepWith(0xdc, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should not call c, a16 when c is not set`() {
        cpu.flags.c = false

        stepWith(0xdc, 0xff, 0x34)

        assertThat(cpu.pc).isNotEqualTo(0x34ff)
    }

    @Test
    fun `should call nz, a16 when z is not set`() {
        cpu.flags.z = false

        stepWith(0xc4, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should not call nz, a16 when z is set`() {
        cpu.flags.z = true

        stepWith(0xc4, 0xff, 0x34)

        assertThat(cpu.pc).isNotEqualTo(0x34ff)
    }

    @Test
    fun `should call nc, a16 when c is not set`() {
        cpu.flags.c = false

        stepWith(0xd4, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should not call nc, a16 when c is set`() {
        cpu.flags.c = true

        stepWith(0xd4, 0xff, 0x34)

        assertThat(cpu.pc).isNotEqualTo(0x34ff)
    }
}