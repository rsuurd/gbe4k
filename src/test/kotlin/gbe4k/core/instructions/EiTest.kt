package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EiTest : CpuTestSupport() {
    @Test
    fun `should enable interrupts on next cycle`() {
        stepWith(0xfb)

        assertThat(cpu.pc).isEqualTo(0x0101)
        assertThat(interrupts.ime).isFalse()
        assertThat(interrupts.enableIme).isTrue()
        assertThat(timer.div).isEqualTo(4)

        stepWith(0x00)

        assertThat(cpu.pc).isEqualTo(0x0102)
        assertThat(interrupts.ime).isTrue()
        assertThat(interrupts.enableIme).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }
}
