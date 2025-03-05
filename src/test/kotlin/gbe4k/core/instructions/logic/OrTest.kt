package gbe4k.core.instructions.logic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrTest : CpuTestSupport() {
    @Test
    fun `should or b`() = test(0xb0) { cpu.registers.b = it }

    @Test
    fun `should or c`() = test(0xb1) { cpu.registers.c = it }

    @Test
    fun `should or d`() = test(0xb2) { cpu.registers.d = it }

    @Test
    fun `should or e`() = test(0xb3) { cpu.registers.e = it }

    @Test
    fun `should or h`() = test(0xb4) { cpu.registers.h = it }

    @Test
    fun `should or l`() = test(0xb5) { cpu.registers.l = it }

    @Test
    fun `should or (hl)`() {
        setupMemory(0xff80, 0b00000001)
        cpu.registers.hl = 0xff80
        cpu.registers.a = 0b00001111

        stepWith(0xb6)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0b00001111)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should or a`() = test(0xb7) { cpu.registers.a = it }

    @Test
    fun `should or d8`() {
        cpu.registers.a = 0x00

        stepWith(0xf6, 0x00)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    private fun test(opcode: Int, set: (Byte) -> Unit) {
        cpu.registers.a = 0x1
        set(0x1)

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x01)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }
}
