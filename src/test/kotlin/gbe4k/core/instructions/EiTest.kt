package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EiTest : CpuTestSupport() {
    @Test
    fun `should enable interrupts on next cycle`() {
        stepWith(0xfb)

        assertThat(cpu.pc).isEqualTo(0x0101)
        assertThat(cpu.interrupts.ime).isFalse()
        assertThat(cpu.interrupts.enableIme).isTrue()

        stepWith(0x00)

        assertThat(cpu.pc).isEqualTo(0x0102)
        assertThat(cpu.interrupts.ime).isTrue()
        assertThat(cpu.interrupts.enableIme).isFalse()
    }
}
