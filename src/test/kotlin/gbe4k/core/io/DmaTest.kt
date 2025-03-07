package gbe4k.core.io

import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmaTest : CpuTestSupport() {
    @Test
    fun `should start`() {
        dma.start(0xc0.toByte())

        assertThat(dma.address).isEqualTo(0xc000)
        assertThat(dma.transferring).isTrue()
    }

    @Test
    fun `should not transfer when not started`() {
        dma.transfer(cpu)

        verify(exactly = 0) { cpu.bus[any()] = any() }
    }

    @Test
    fun `should wait one cycle before transferring`() {
        dma.start(0xc0.toByte())

        dma.transfer(cpu)

        verify(exactly = 0) { cpu.bus[any()] = any() }
    }

    @Test
    fun `should complete transfer in 160 bytes`() {
        dma.start(0xc0.toByte())

        // first transfer is the wait
        dma.transfer(cpu)

        for (i in 0..160) {
            dma.transfer(cpu)
        }

        verify(exactly = 160) { cpu.bus[any()] = any() }
        assertThat(dma.transferring).isFalse()
    }
}
