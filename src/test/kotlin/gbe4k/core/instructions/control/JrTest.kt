package gbe4k.core.instructions.control

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JrTest : CpuTestSupport() {
    @Test
    fun `should jr r8`() {
        stepWith(0x18, 0x43)

        assertThat(cpu.pc).isEqualTo(0x145)
    }

    @Test
    fun `should jr nz, r8 when z is not set`() {
        cpu.flags.z = false

        stepWith(0x20, 0x34)

        assertThat(cpu.pc).isEqualTo(0x136)
    }

    @Test
    fun `should not jr nz, r8 when z is set`() {
        cpu.flags.z = true

        stepWith(0x20, 0x34)

        assertThat(cpu.pc).isEqualTo(0x102)
    }

    @Test
    fun `should jr nc, r8 when c is not set`() {
        cpu.flags.c = false

        stepWith(0x30, 0x34)

        assertThat(cpu.pc).isEqualTo(0x136)
    }

    @Test
    fun `should not jr nc, r8 when c is set`() {
        cpu.flags.c = true

        stepWith(0x30, 0x34)

        assertThat(cpu.pc).isEqualTo(0x102)
    }

    @Test
    fun `should jr z, r8 when z is set`() {
        cpu.flags.z = true

        stepWith(0x28, 0x34)

        assertThat(cpu.pc).isEqualTo(0x136)
    }

    @Test
    fun `should not jr z, r8 when z is not set`() {
        cpu.flags.z = false

        stepWith(0x28, 0x34)

        assertThat(cpu.pc).isEqualTo(0x102)
    }

    @Test
    fun `should jr c, r8 when c is set`() {
        cpu.flags.c = true

        stepWith(0x38, 0x34)

        assertThat(cpu.pc).isEqualTo(0x136)
    }

    @Test
    fun `should not jr c, r8 when c is not set`() {
        cpu.flags.c = false

        stepWith(0x38, 0x34)

        assertThat(cpu.pc).isEqualTo(0x102)
    }
}