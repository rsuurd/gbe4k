package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StopTest : CpuTestSupport() {
    @Test
    fun `should stop`() {
        stepWith(0x10, 0x00)

        assertThat(timer.div).isEqualTo(4)
    }
}
