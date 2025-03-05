package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RrcTest : CpuTestSupport() {
    @Test
    fun `should rrca`() {
        cpu.registers.a = 0b10000001.toByte()

        stepWith(0x0f)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0b11000000)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should rrc b`() {
        cpu.registers.b = 0b10000001.toByte()

        stepWith(0xcb, 0x08)

        assert(cpu.registers.b)
    }

    @Test
    fun `should rrc c`() {
        cpu.registers.c = 0b10000001.toByte()

        stepWith(0xcb, 0x09)

        assert(cpu.registers.c)
    }

    @Test
    fun `should rrc d`() {
        cpu.registers.d = 0b10000001.toByte()

        stepWith(0xcb, 0x0a)

        assert(cpu.registers.d)
    }

    @Test
    fun `should rrc e`() {
        cpu.registers.e = 0b10000001.toByte()

        stepWith(0xcb, 0x0b)

        assert(cpu.registers.e)
    }

    @Test
    fun `should rrc h`() {
        cpu.registers.h = 0b10000001.toByte()

        stepWith(0xcb, 0x0c)

        assert(cpu.registers.h)
    }

    @Test
    fun `should rrc l`() {
        cpu.registers.l = 0b10000001.toByte()

        stepWith(0xcb, 0x0d)

        assert(cpu.registers.l)
    }

    @Test
    fun `should rrc (hl)`() {
        setupMemory(0xff80, 0b10000001.toByte())
        cpu.registers.hl = 0xff80

        stepWith(0xcb, 0x0e)

        verify { bus.write(0xff80, 0b11000000.toByte()) }
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rrc a`() {
        cpu.registers.a = 0b10000001.toByte()

        stepWith(0xcb, 0x0f)

        assert(cpu.registers.a)
    }

    private fun assert(value: Byte) {
        assertThat(value.asInt()).isEqualTo(0b11000000)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }
}
