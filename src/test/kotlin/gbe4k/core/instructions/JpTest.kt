package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JpTest : CpuTestSupport() {
    @Test
    fun `should jump to address`() {
        stepWith(0xc3, 0x50, 0x01)

        assertThat(cpu.pc).isEqualTo(0x0150)
    }
}
