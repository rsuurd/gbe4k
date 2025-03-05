package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AdcTest : CpuTestSupport() {
    @Test
    fun `should adc a, b`() = test(0x88) { cpu.registers.b = it }

    @Test
    fun `should adc a, c`() = test(0x89) { cpu.registers.c = it }

    @Test
    fun `should adc a, d`() = test(0x8a) { cpu.registers.d = it }

    @Test
    fun `should adc a, e`() = test(0x8b) { cpu.registers.e = it }

    @Test
    fun `should adc a, h`() = test(0x8c) { cpu.registers.h = it }

    @Test
    fun `should adc a, l`() = test(0x8d) { cpu.registers.l = it }

    @Test
    fun `should adc a, (hl)`() {
        setupMemory(0xff80, 0xf3.toByte())
        cpu.registers.a = 0xd
        cpu.registers.hl = 0xff80
        cpu.flags.c = false

        stepWith(0x8e)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should adc a, d8`() {
        cpu.registers.a = 0xd
        cpu.flags.c = false

        stepWith(0xce, 0x1e)

        assertThat(cpu.registers.a).isEqualTo(0x2b)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should adc a, a`() = test(0x8f) { cpu.registers.a = it }

    private fun test(opcode: Int, set: (Byte) -> Unit) {
        set(0x10)
        cpu.registers.a = 0x10
        cpu.flags.c = true

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x21)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }
}