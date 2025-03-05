package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SubTest : CpuTestSupport() {
    @Test
    fun `should sub b`() {
        cpu.registers.a = 0x00
        cpu.registers.b = 0x01

        stepWith(0x90)

        verifySub()
    }

    @Test
    fun `should sub c`() {
        cpu.registers.a = 0x00
        cpu.registers.c = 0x01

        stepWith(0x91)

        verifySub()
    }

    @Test
    fun `should sub d`() {
        cpu.registers.a = 0x00
        cpu.registers.d = 0x01

        stepWith(0x92)

        verifySub()
    }

    @Test
    fun `should sub e`() {
        cpu.registers.a = 0x00
        cpu.registers.e = 0x01

        stepWith(0x93)

        verifySub()
    }

    @Test
    fun `should sub h`() {
        cpu.registers.a = 0x00
        cpu.registers.h = 0x01

        stepWith(0x94)

        verifySub()
    }

    @Test
    fun `should sub l`() {
        cpu.registers.a = 0x00
        cpu.registers.l = 0x01

        stepWith(0x95)

        verifySub()
    }

    @Test
    fun `should sub (hl)`() {
        cpu.registers.a = 0x01
        cpu.registers.hl = 0xff80
        setupMemory(0xff80, 0x01)

        stepWith(0x96)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should sub a`() {
        cpu.registers.a = 0x03

        stepWith(0x97)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should sub a, d8`() {
        cpu.registers.a = 0x80.toByte()

        stepWith(0xd6, 0x20)

        assertThat(cpu.registers.a).isEqualTo(0x60)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    private fun verifySub() {
        assertThat(cpu.registers.a).isEqualTo(0xff.toByte())
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }
}
