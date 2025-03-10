package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddTest : CpuTestSupport() {
    @Test
    fun `should add b`() {
        cpu.registers.a = 0x0f
        cpu.registers.b = 0x01

        stepWith(0x80)

        verifyAdd()
    }

    @Test
    fun `should add c`() {
        cpu.registers.a = 0x0f
        cpu.registers.c = 0x01

        stepWith(0x81)

        verifyAdd()
    }

    @Test
    fun `should add d`() {
        cpu.registers.a = 0x0f
        cpu.registers.d = 0x01

        stepWith(0x82)

        verifyAdd()
    }

    @Test
    fun `should add e`() {
        cpu.registers.a = 0x0f
        cpu.registers.e = 0x01

        stepWith(0x83)

        verifyAdd()
    }

    @Test
    fun `should add h`() {
        cpu.registers.a = 0x0f
        cpu.registers.h = 0x01

        stepWith(0x84)

        verifyAdd()
    }

    @Test
    fun `should add l`() {
        cpu.registers.a = 0x0f
        cpu.registers.l = 0x01

        stepWith(0x85)

        verifyAdd()
    }

    @Test
    fun `should add (hl)`() {
        cpu.registers.a = 0x0f
        cpu.registers.hl = 0xff80
        setupMemory(0xff80, 0x01)

        stepWith(0x86)

        assertThat(cpu.registers.a).isEqualTo(0x10)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should add a`() {
        cpu.registers.a = 0x80.toByte()

        stepWith(0x87)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should add a, d8`() {
        cpu.registers.a = 0x80.toByte()

        stepWith(0xc6, 0x20)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0xa0)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    private fun verifyAdd() {
        assertThat(cpu.registers.a).isEqualTo(0x10)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should add sp, r8`() {
        cpu.registers.sp = 0x000f

        stepWith(0xe8, 0x1)

        assertThat(cpu.registers.sp).isEqualTo(0x0010)
        assertThat(cpu.flags.h).isTrue()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should add sp, r8 with carry & half carry`() {
        cpu.registers.sp = 0x0001

        stepWith(0xe8, 0xff)

        assertThat(cpu.registers.sp).isEqualTo(0x000)
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should add hl, bc`() {
        cpu.registers.hl = 0x9999
        cpu.registers.bc = 0x9999

        stepWith(0x09)

        assertThat(cpu.registers.hl).isEqualTo(0x3332)
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should add hl, de`() {
        cpu.registers.hl = 0x9999
        cpu.registers.de = 0x7

        stepWith(0x19)

        assertThat(cpu.registers.hl).isEqualTo(0x99a0)
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should add hl, hl`() {
        cpu.registers.hl = 0x8000

        stepWith(0x29)

        assertThat(cpu.registers.hl).isEqualTo(0x0000)
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should add hl, sp`() {
        cpu.registers.hl = 0x00af
        cpu.registers.sp = 0xffef

        stepWith(0x39)

        assertThat(cpu.registers.hl).isEqualTo(0x09e)
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }
}
