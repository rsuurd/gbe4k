package gbe4k.core.instructions.logic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CpTest : CpuTestSupport() {
    @Test
    fun `should cp b`() = test(0xb8) { cpu.registers.b = it }

    @Test
    fun `should cp c`() = test(0xb9) { cpu.registers.c = it }

    @Test
    fun `should cp d`() = test(0xba) { cpu.registers.d = it }

    @Test
    fun `should cp e`() = test(0xbb) { cpu.registers.e = it }

    @Test
    fun `should cp h`() = test(0xbc) { cpu.registers.h = it }

    @Test
    fun `should cp l`() = test(0xbd) { cpu.registers.l = it }

    @Test
    fun `should cp (hl)`() {
        setupMemory(0xff80, 0xfa.toByte())
        cpu.registers.a = 0x0f.toByte()
        cpu.registers.hl = 0xff80

        stepWith(0xbe)

        assertThat(cpu.registers.a).isEqualTo(0x0f.toByte())
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should cp a`() = test(0xbf) { cpu.registers.a = it }

    private fun test(opcode: Int, set: (Byte) -> Unit) {
        cpu.registers.a = 0x19

        set(0x19)

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x19)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }
}