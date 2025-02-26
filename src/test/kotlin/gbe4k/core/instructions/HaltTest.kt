package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HaltTest : CpuTestSupport() {
    @Test
    fun `should halt`() {
        stepWith(0x76)

        assertThat(cpu.halted).isTrue()
    }
}