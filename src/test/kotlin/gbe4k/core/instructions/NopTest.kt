package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NopTest : CpuTestSupport() {
    @Test
    fun `should do nothing`() {
        Nop.execute(cpu)

        assertThat(cpu.pc).isEqualTo(0x0100)
    }
}