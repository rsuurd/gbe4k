package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NopTest : CpuTestSupport() {
    @Test
    fun `should do nothing`() {
        stepWith(0x00)

        assertThat(cpu.pc).isEqualTo(0x0101)
    }
}
