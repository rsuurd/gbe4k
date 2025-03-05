package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DecTest : CpuTestSupport() {
    @Test
    fun `should dec bc`() {
        cpu.registers.bc = 0x0010

        stepWith(0x0b)

        assertThat(cpu.registers.bc).isEqualTo(0x000f)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should dec de`() {
        cpu.registers.de = 0x0010

        stepWith(0x1b)

        assertThat(cpu.registers.de).isEqualTo(0x000f)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should dec hl`() {
        cpu.registers.hl = 0x0010

        stepWith(0x2b)

        assertThat(cpu.registers.hl).isEqualTo(0x000f)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should dec sp`() {
        cpu.registers.sp = 0xfffe

        stepWith(0x3b)

        assertThat(cpu.registers.sp).isEqualTo(0xfffd)
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should dec b`() {
        cpu.registers.b = 0x10
        stepWith(0x05)
        assertDec(cpu.registers.b)
    }

    @Test
    fun `should dec c`() {
        cpu.registers.c = 0x10
        stepWith(0x0d)
        assertDec(cpu.registers.c)
    }

    @Test
    fun `should dec d`() {
        cpu.registers.d = 0x10
        stepWith(0x15)
        assertDec(cpu.registers.d)
    }

    @Test
    fun `should dec e`() {
        cpu.registers.e = 0x10
        stepWith(0x1d)
        assertDec(cpu.registers.e)
    }

    @Test
    fun `should dec h`() {
        cpu.registers.h = 0x10
        stepWith(0x25)
        assertDec(cpu.registers.h)
    }

    @Test
    fun `should dec l`() {
        cpu.registers.l = 0x10
        stepWith(0x2d)
        assertDec(cpu.registers.l)
    }

    @Test
    fun `should dec (hl)`() {
        setupMemory(0xff80, 0x01.toByte())
        cpu.registers.hl = 0xff80

        stepWith(0x35)

        verify { bus.write(0xff80, 0x00) }
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(timer.div).isEqualTo(12)
    }

    @Test
    fun `should dec a`() {
        cpu.registers.a = 0x10
        stepWith(0x3d)
        assertDec(cpu.registers.a)
    }

    private fun assertDec(value: Byte) {
        assertThat(value).isEqualTo(0x0f)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }
}
