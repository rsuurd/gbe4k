package gbe4k.core.instructions.logic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CplTest : CpuTestSupport() {
    @Test
    fun `should cpl`() {
        cpu.registers.a = 0xa

        stepWith(0x2f)

        assertThat(cpu.registers.a).isEqualTo(0xf5.toByte())
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
    }
}
