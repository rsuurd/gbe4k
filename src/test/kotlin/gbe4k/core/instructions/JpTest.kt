package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JpTest : CpuTestSupport() {
    @Test
    fun `should jp a16`() {
        stepWith(0xc3, 0x50, 0x01)

        assertThat(cpu.pc).isEqualTo(0x0150)
    }

    @Test
    fun `should jp z, a16 when z is set`() {
        cpu.flags.z = true

        stepWith(0xca, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should not jp z, a16 when z is not set`() {
        cpu.flags.z = false

        stepWith(0xca, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x103)
    }

    @Test
    fun `should jp c, a16 when z is set`() {
        cpu.flags.c = true

        stepWith(0xda, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should not jp c, a16 when c is not set`() {
        cpu.flags.c = false

        stepWith(0xda, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x103)
    }

    @Test
    fun `should jp nz, a16 when z is not set`() {
        cpu.flags.z = false

        stepWith(0xc2, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should not jp nz, a16 when z is set`() {
        cpu.flags.z = true

        stepWith(0xc2, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x103)
    }

    @Test
    fun `should jp nc, a16 when c is not set`() {
        cpu.flags.c = false

        stepWith(0xd2, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x34ff)
    }

    @Test
    fun `should not jp nc, a16 when c is set`() {
        cpu.flags.c = true

        stepWith(0xd2, 0xff, 0x34)

        assertThat(cpu.pc).isEqualTo(0x103)
    }

    @Test
    fun `should jp (hl)`() {
        cpu.registers.hl = 0xff24
        stepWith(0xe9)

        assertThat(cpu.pc).isEqualTo(0xff24)
    }
}
