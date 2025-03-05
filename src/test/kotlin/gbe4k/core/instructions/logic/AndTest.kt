package gbe4k.core.instructions.logic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AndTest : CpuTestSupport() {
    @Test
    fun `should and b`() = test(0xa0) { cpu.registers.b = it }

    @Test
    fun `should and c`() = test(0xa1) { cpu.registers.c = it }

    @Test
    fun `should and d`() = test(0xa2) { cpu.registers.d = it }

    @Test
    fun `should and e`() = test(0xa3) { cpu.registers.e = it }

    @Test
    fun `should and h`() = test(0xa4) { cpu.registers.h = it }

    @Test
    fun `should and l`() = test(0xa5) { cpu.registers.l = it }

    @Test
    fun `should and (hl)`() {
        setupMemory(0xff80, 0x00)
        cpu.registers.hl = 0xff80
        cpu.registers.a = 0x23

        stepWith(0xa6)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    @Test
    fun `should and a`() = test(0xa7) { cpu.registers.a = it }

    @Test
    fun `should and d8`() {
        cpu.registers.a = 0x1

        stepWith(0xe6, 0x1)

        assertThat(cpu.registers.a).isEqualTo(0x01)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }

    private fun test(opcode: Int, set: (Byte) -> Unit) {
        cpu.registers.a = 0x1
        set(0x1)

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x1)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }
}
