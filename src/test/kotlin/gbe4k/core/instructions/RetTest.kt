package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RetTest : CpuTestSupport() {
    @Test
    fun `should ret`() {
        cpu.registers.sp = 0xfffc

        stepWith(0xc9, 0x64, 0x3a)

        assertThat(cpu.pc).isEqualTo(0x3a64)
        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
    }

    @Test
    fun `should reti`() {
        cpu.interrupts.ime = false
        cpu.registers.sp = 0xfffc

        stepWith(0xd9, 0x64, 0x3a)

        assertThat(cpu.pc).isEqualTo(0x3a64)
        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
        assertThat(cpu.interrupts.ime).isTrue()
    }

    @Test
    fun `should rst 00h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xc7)

        assertThat(cpu.pc).isEqualTo(0x00)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
    }

    @Test
    fun `should rst 08h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xcf)

        assertThat(cpu.pc).isEqualTo(0x08)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
    }

    @Test
    fun `should rst 10h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xd7)

        assertThat(cpu.pc).isEqualTo(0x10)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
    }

    @Test
    fun `should rst 18h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xdf)

        assertThat(cpu.pc).isEqualTo(0x18)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
    }

    @Test
    fun `should rst 20h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xe7)

        assertThat(cpu.pc).isEqualTo(0x20)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
    }

    @Test
    fun `should rst 28h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xef)

        assertThat(cpu.pc).isEqualTo(0x28)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
    }

    @Test
    fun `should rst 30h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xf7)

        assertThat(cpu.pc).isEqualTo(0x30)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
    }

    @Test
    fun `should rst 38h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xff)

        assertThat(cpu.pc).isEqualTo(0x38)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
    }
}
