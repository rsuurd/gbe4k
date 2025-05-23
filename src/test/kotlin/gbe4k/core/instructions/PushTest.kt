package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PushTest : CpuTestSupport() {
    @Test
    fun `should push BC`() {
        cpu.registers.bc = 0x532c

        push(0xc5)

        verify {
            bus.write(0xdffd, 0x2c)
            bus.write(0xdffe, 0x53)
        }
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should push DE`() {
        cpu.registers.de = 0x532c

        push(0xd5)

        verify {
            bus.write(0xdffd, 0x2c)
            bus.write(0xdffe, 0x53)
        }
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should push HL`() {
        cpu.registers.hl = 0x532c

        push(0xe5)

        verify {
            bus.write(0xdffd, 0x2c)
            bus.write(0xdffe, 0x53)
        }
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should push AF`() {
        cpu.registers.af = 0x5320

        push(0xf5)

        verify {
            bus.write(0xdffd, 0x20)
            bus.write(0xdffe, 0x53)
        }
        assertThat(timer.div).isEqualTo(16)
    }

    private fun push(opcode: Int) {
        cpu.registers.sp = 0xdfff

        stepWith(opcode)

        assertThat(cpu.registers.sp).isEqualTo(0xdffd)
    }
}