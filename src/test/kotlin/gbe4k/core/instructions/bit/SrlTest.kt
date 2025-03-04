package gbe4k.core.instructions.bit

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SrlTest : CpuTestSupport() {
    @Test
    fun `should srl b`() {
        cpu.registers.b = 0xff.toByte()

        stepWith(0xcb, 0x38)

        assertThat(cpu.registers.b).isEqualTo(0x7f)
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }

    // TODO add tests
}