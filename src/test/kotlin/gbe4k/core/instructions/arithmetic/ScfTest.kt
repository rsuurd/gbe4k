package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScfTest : CpuTestSupport() {
    @Test
    fun `should scf`() {
        cpu.flags.n = true
        cpu.flags.h = true
        cpu.flags.c = false

        stepWith(0x37)

        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }
}