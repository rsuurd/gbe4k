package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RlcTest : CpuTestSupport() {
    @Test
    fun `should rlca`() {
        cpu.registers.a = 0b10000000.toByte()

        stepWith(0x07)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0b00000001)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should rlc b`() {
        cpu.registers.b = 0b10000000.toByte()

        stepWith(0xcb, 0x00)

        assert(cpu.registers.b)
    }

    @Test
    fun `should rlc c`() {
        cpu.registers.c = 0b10000000.toByte()

        stepWith(0xcb, 0x01)

        assert(cpu.registers.c)
    }

    @Test
    fun `should rlc d`() {
        cpu.registers.d = 0b10000000.toByte()

        stepWith(0xcb, 0x02)

        assert(cpu.registers.d)
    }

    @Test
    fun `should rlc e`() {
        cpu.registers.e = 0b10000000.toByte()

        stepWith(0xcb, 0x03)

        assert(cpu.registers.e)
    }

    @Test
    fun `should rlc h`() {
        cpu.registers.h = 0b10000000.toByte()

        stepWith(0xcb, 0x04)

        assert(cpu.registers.h)
    }

    @Test
    fun `should rlc l`() {
        cpu.registers.l = 0b10000000.toByte()

        stepWith(0xcb, 0x05)

        assert(cpu.registers.l)
    }

    @Test
    fun `should rlc (hl)`() {
        setupMemory(0xff80, 0b10000000.toByte())
        cpu.registers.hl = 0xff80

        stepWith(0xcb, 0x06)

        verify { bus.write(0xff80, 0b0000001) }
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rlc a`() {
        cpu.registers.a = 0b10000000.toByte()

        stepWith(0xcb, 0x07)

        assert(cpu.registers.a)
    }

    private fun assert(value: Byte) {
        assertThat(value).isEqualTo(0b00000001)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }
}
