package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScfTest : CpuTestSupport() {
    @Test
    fun `should scf`() {
        stepWith(0x37)

        assertThat(cpu.flags.c).isTrue()
    }
}