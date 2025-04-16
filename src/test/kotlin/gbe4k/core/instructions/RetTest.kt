package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RetTest : CpuTestSupport() {
    @BeforeEach
    fun `setup stack`() {
        cpu.stack.push(0x3a64)
        timer.div = 0
    }

    @Test
    fun `should ret`() {
        stepWith(0xc9)

        assertThat(cpu.pc).isEqualTo(0x3a64)
        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should ret nz when z not set`() {
        cpu.flags.z = false

        stepWith(0xc0)

        assertThat(cpu.pc).isEqualTo(0x3a64)
        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
        assertThat(timer.div).isEqualTo(20)
    }

    @Test
    fun `should not ret nz when z is set`() {
        cpu.flags.z = true

        stepWith(0xc0)

        assertThat(cpu.pc).isEqualTo(0x0001)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should ret z when z is set`() {
        cpu.flags.z = true

        stepWith(0xc8)

        assertThat(cpu.pc).isEqualTo(0x3a64)
        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
        assertThat(timer.div).isEqualTo(20)
    }

    @Test
    fun `should not ret z when z not set`() {
        cpu.flags.z = false

        stepWith(0xc8)

        assertThat(cpu.pc).isEqualTo(0x0001)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(8)
    }


    @Test
    fun `should ret nc when c not set`() {
        cpu.flags.c = false

        stepWith(0xd0)

        assertThat(cpu.pc).isEqualTo(0x3a64)
        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
        assertThat(timer.div).isEqualTo(20)
    }

    @Test
    fun `should not ret nc when c is set`() {
        cpu.flags.c = true

        stepWith(0xd0)

        assertThat(cpu.pc).isEqualTo(0x0001)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should ret c when c is set`() {
        cpu.flags.c = true

        stepWith(0xd8)

        assertThat(cpu.pc).isEqualTo(0x3a64)
        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
        assertThat(timer.div).isEqualTo(20)
    }

    @Test
    fun `should not ret c when c not set`() {
        cpu.flags.z = false

        stepWith(0xd8)

        assertThat(cpu.pc).isEqualTo(0x0001)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should reti`() {
        cpu.interrupts.ime = false

        stepWith(0xd9)

        assertThat(cpu.pc).isEqualTo(0x3a64)
        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
        assertThat(cpu.interrupts.ime).isTrue()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rst 00h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xc7)

        assertThat(cpu.pc).isEqualTo(0x00)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rst 08h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xcf)

        assertThat(cpu.pc).isEqualTo(0x08)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rst 10h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xd7)

        assertThat(cpu.pc).isEqualTo(0x10)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rst 18h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xdf)

        assertThat(cpu.pc).isEqualTo(0x18)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rst 20h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xe7)

        assertThat(cpu.pc).isEqualTo(0x20)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rst 28h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xef)

        assertThat(cpu.pc).isEqualTo(0x28)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rst 30h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xf7)

        assertThat(cpu.pc).isEqualTo(0x30)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rst 38h`() {
        cpu.registers.sp = 0xfffe

        stepWith(0xff)

        assertThat(cpu.pc).isEqualTo(0x38)
        assertThat(cpu.registers.sp).isEqualTo(0xfffc)
        assertThat(timer.div).isEqualTo(16)
    }
}
