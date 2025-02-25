package gbe4k.core.instructions.logic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CcfTest : CpuTestSupport() {
    @Test
    fun `should ccf`() {
        cpu.flags.n = true
        cpu.flags.h = true
        cpu.flags.c = false

        stepWith(0x3f)

        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
    }
}