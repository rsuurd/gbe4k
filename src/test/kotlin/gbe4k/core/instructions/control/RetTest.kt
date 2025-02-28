package gbe4k.core.instructions.control

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RetTest : CpuTestSupport() {
    @BeforeEach
    fun `move sp`() {
        cpu.registers.sp = 0xff00
    }

    @Test
    fun `should ret`() {
        stepWith(0xc9, 0x40, 0x15)

        assertThat(cpu.pc).isEqualTo(0x1540)
        assertThat(cpu.registers.sp).isEqualTo(0xff02)
    }

    @Test
    fun `should ret nz when z not set`() {
        cpu.flags.z = false

        stepWith(0xc0, 0x40, 0x00)

        assertThat(cpu.pc).isEqualTo(0x0040)
        assertThat(cpu.registers.sp).isEqualTo(0xff02)
    }

    @Test
    fun `should not ret nz when z set`() {
        cpu.flags.z = true

        stepWith(0xc0)

        assertThat(cpu.pc).isEqualTo(0x101)
        assertThat(cpu.registers.sp).isEqualTo(0xff00)
    }

    @Test
    fun `should ret nc when c not set`() {
        cpu.flags.c = false

        stepWith(0xd0, 0x40, 0x40)

        assertThat(cpu.pc).isEqualTo(0x4040)
        assertThat(cpu.registers.sp).isEqualTo(0xff02)
    }

    @Test
    fun `should not ret nc when c set`() {
        cpu.flags.c = true

        stepWith(0xd0)

        assertThat(cpu.pc).isEqualTo(0x101)
        assertThat(cpu.registers.sp).isEqualTo(0xff00)
    }

    @Test
    fun `should ret z when z set`() {
        cpu.flags.z = true

        stepWith(0xc8, 0x00, 0x40)

        assertThat(cpu.pc).isEqualTo(0x4000)
        assertThat(cpu.registers.sp).isEqualTo(0xff02)
    }

    @Test
    fun `should not ret z when z not set`() {
        cpu.flags.z = false

        stepWith(0xc8)

        assertThat(cpu.pc).isEqualTo(0x101)
        assertThat(cpu.registers.sp).isEqualTo(0xff00)
    }

    @Test
    fun `should ret c when c set`() {
        cpu.flags.c = true

        stepWith(0xd8, 0x40, 0xaa)

        assertThat(cpu.pc).isEqualTo(0xaa40)
        assertThat(cpu.registers.sp).isEqualTo(0xff02)
    }

    @Test
    fun `should not ret c when c not set`() {
        cpu.flags.c = false

        stepWith(0xd8)

        assertThat(cpu.pc).isEqualTo(0x101)
        assertThat(cpu.registers.sp).isEqualTo(0xff00)
    }

    @Test
    fun `should reti`() {
        stepWith(0xd9, 0x40, 0x00)

        assertThat(cpu.pc).isEqualTo(0x0040)
        assertThat(cpu.registers.sp).isEqualTo(0xff02)
        assertThat(cpu.interrupts.ime).isTrue()
    }
}