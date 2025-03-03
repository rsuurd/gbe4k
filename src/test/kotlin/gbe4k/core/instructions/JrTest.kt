package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JrTest : CpuTestSupport() {
    @Test
    fun `should jr r8`() {
        cpu.pc = 0x150

        stepWith(0x18, 0xfd.toByte())

        assertThat(cpu.pc).isEqualTo(0x14f)
    }

    @Test
    fun `should jr nz, r8 when z is not set`() {
        cpu.pc = 0x150
        cpu.flags.z = false

        stepWith(0x20, 0x02)

        assertThat(cpu.pc).isEqualTo(0x154)
    }

    @Test
    fun `should not jr nz, r8 when z is set`() {
        cpu.pc = 0x150
        cpu.flags.z = true

        stepWith(0x20, 0x02)

        assertThat(cpu.pc).isEqualTo(0x152)
    }

    @Test
    fun `should jr z, r8 when z is set`() {
        cpu.pc = 0x150
        cpu.flags.z = true

        stepWith(0x28, 0x02)

        assertThat(cpu.pc).isEqualTo(0x154)
    }

    @Test
    fun `should not jr z, r8 when z is not set`() {
        cpu.pc = 0x150
        cpu.flags.z = false

        stepWith(0x28, 0x02)

        assertThat(cpu.pc).isEqualTo(0x152)
    }

    @Test
    fun `should jr nc, r8 when c is not set`() {
        cpu.pc = 0x150
        cpu.flags.c = false

        stepWith(0x30, 0x02)

        assertThat(cpu.pc).isEqualTo(0x154)
    }

    @Test
    fun `should not jr nc, r8 when c is set`() {
        cpu.pc = 0x150
        cpu.flags.c = true

        stepWith(0x30, 0x02)

        assertThat(cpu.pc).isEqualTo(0x152)
    }

    @Test
    fun `should jr c, r8 when c is set`() {
        cpu.pc = 0x150
        cpu.flags.c = true

        stepWith(0x38, 0x02)

        assertThat(cpu.pc).isEqualTo(0x154)
    }

    @Test
    fun `should not jr c, r8 when c is not set`() {
        cpu.pc = 0x150
        cpu.flags.c = false

        stepWith(0x38, 0x02)

        assertThat(cpu.pc).isEqualTo(0x152)
    }
}
