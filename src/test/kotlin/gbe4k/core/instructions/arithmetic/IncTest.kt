package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IncTest : CpuTestSupport() {
    @Test
    fun `should inc bc`() {
        cpu.registers.bc = 0x000f

        stepWith(0x03)

        assertThat(cpu.registers.bc).isEqualTo(0x0010)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should inc de`() {
        cpu.registers.de = 0x000f

        stepWith(0x13)

        assertThat(cpu.registers.de).isEqualTo(0x0010)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should inc hl`() {
        cpu.registers.hl = 0x000f

        stepWith(0x23)

        assertThat(cpu.registers.hl).isEqualTo(0x0010)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should inc sp`() {
        cpu.registers.sp = 0xfffd

        stepWith(0x33)

        assertThat(cpu.registers.sp).isEqualTo(0xfffe)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should inc b`() {
        cpu.registers.b = 0x0f
        stepWith(0x04)
        assertInc(cpu.registers.b)
    }

    @Test
    fun `should inc c`() {
        cpu.registers.c = 0x0f
        stepWith(0x0c)
        assertInc(cpu.registers.c)
    }

    @Test
    fun `should inc d`() {
        cpu.registers.d = 0x0f
        stepWith(0x14)
        assertInc(cpu.registers.d)
    }

    @Test
    fun `should inc e`() {
        cpu.registers.e = 0x0f
        stepWith(0x1c)
        assertInc(cpu.registers.e)
    }

    @Test
    fun `should inc h`() {
        cpu.registers.h = 0x0f
        stepWith(0x24)
        assertInc(cpu.registers.h)
    }

    @Test
    fun `should inc l`() {
        cpu.registers.l = 0x0f
        stepWith(0x2c)
        assertInc(cpu.registers.l)
    }

    @Test
    fun `should inc (hl)`() {
        setupMemory(0xff80, 0xff.toByte())
        cpu.registers.hl = 0xff80

        stepWith(0x34)

        verify { bus.write(0xff80, 0x00) }
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(timer.div).isEqualTo(12)
    }

    @Test
    fun `should inc a`() {
        cpu.registers.a = 0x0f
        stepWith(0x3c)
        assertInc(cpu.registers.a)
    }

    private fun assertInc(value: Byte) {
        assertThat(value).isEqualTo(0x10)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }
}
