package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DiTest : CpuTestSupport() {
    @Test
    fun `should disable interrupts`() {
        Di.execute(cpu)

        assertThat(cpu.ime).isFalse()
    }
}