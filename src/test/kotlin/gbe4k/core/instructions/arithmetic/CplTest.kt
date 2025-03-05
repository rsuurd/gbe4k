package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CplTest : CpuTestSupport() {
    @Test
    fun `should cpl`() {
        cpu.registers.a = 0x00
        cpu.flags.n = false
        cpu.flags.h = false

        stepWith(0x2f)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0xff)
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }
}