package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DiTest : CpuTestSupport() {
    @Test
    fun `should disable interrupts`() {
        stepWith(0xf3)

        assertThat(cpu.pc).isEqualTo(0x0101)
        assertThat( interrupts.ime).isFalse()
    }
}