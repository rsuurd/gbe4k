package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SwapTest : CpuTestSupport() {
    @Test
    fun `should swap b`() {
        cpu.registers.b = 0x01

        stepWith(0xcb, 0x30)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x10)
        assertThat(timer.div).isEqualTo(8)
    }
}