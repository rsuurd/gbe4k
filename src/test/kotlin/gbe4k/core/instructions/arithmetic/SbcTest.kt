package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SbcTest : CpuTestSupport() {
    @Test
    fun `should sbc a, b`() = test(0x98) { cpu.registers.b = it }

    @Test
    fun `should sbc a, c`() = test(0x99) { cpu.registers.c = it }

    @Test
    fun `should sbc a, d`() = test(0x9a) { cpu.registers.d = it }

    @Test
    fun `should sbc a, e`() = test(0x9b) { cpu.registers.e = it }

    @Test
    fun `should sbc a, h`() = test(0x9c) { cpu.registers.h = it }

    @Test
    fun `should sbc a, l`() = test(0x9d) { cpu.registers.l = it }

    @Test
    fun `should sbc a, (hl)`() {
        setupMemory(0xff80, 0xf3.toByte())
        cpu.registers.a = 0xd
        cpu.registers.hl = 0xff80
        cpu.flags.c = false

        stepWith(0x9e)

        assertThat(cpu.registers.a).isEqualTo(0x1a)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should sbc a, d8`() {
        cpu.registers.a = 0xd
        cpu.flags.c = true

        stepWith(0xde, 0x1d)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0xef)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should sbc a, a`() = test(0x9f) { cpu.registers.a = it }

    private fun test(opcode: Int, set: (Byte) -> Unit) {
        set(0x10)
        cpu.registers.a = 0x10
        cpu.flags.c = false

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }
}