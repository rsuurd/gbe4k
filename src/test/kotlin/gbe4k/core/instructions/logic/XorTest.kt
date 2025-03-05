package gbe4k.core.instructions.logic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class XorTest : CpuTestSupport() {
    @Test
    fun `should xor b`() = test(0xa8) { cpu.registers.b = it }

    @Test
    fun `should xor c`() = test(0xa9) { cpu.registers.c = it }

    @Test
    fun `should xor d`() = test(0xaa) { cpu.registers.d = it }

    @Test
    fun `should xor e`() = test(0xab) { cpu.registers.e = it }

    @Test
    fun `should xor h`() = test(0xac) { cpu.registers.h = it }

    @Test
    fun `should xor l`() = test(0xad) { cpu.registers.l = it }

    @Test
    fun `should xor (hl)`() {
        setupMemory(0xff80, 0b00000001)
        cpu.registers.hl = 0xff80
        cpu.registers.a = 0b00001111

        stepWith(0xae)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0b00001110)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should xor a`() = test(0xaf) { cpu.registers.a = it }

    @Test
    fun `should xor d8`() {
        cpu.registers.a = 0x1

        stepWith(0xee, 0x1)

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

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }
}
