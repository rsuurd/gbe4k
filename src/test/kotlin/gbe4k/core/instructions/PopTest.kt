package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PopTest : CpuTestSupport() {
    @Test
    fun `should pop BC`() {
        pop(0xc1)

        assertThat(cpu.registers.bc).isEqualTo(0x350a)
        assertThat(timer.div).isEqualTo(12)
    }

    @Test
    fun `should pop DE`() {
        pop(0xd1)

        assertThat(cpu.registers.de).isEqualTo(0x350a)
        assertThat(timer.div).isEqualTo(12)
    }

    @Test
    fun `should pop HL`() {
        pop(0xe1)

        assertThat(cpu.registers.hl).isEqualTo(0x350a)
        assertThat(timer.div).isEqualTo(12)
    }

    @Test
    fun `should pop AF`() {
        pop(0xf1)

        assertThat(cpu.registers.af).isEqualTo(0x3500)
        assertThat(timer.div).isEqualTo(12)
    }

    private fun pop(opcode: Int) {
        cpu.registers.sp = 0x253a

        stepWith(opcode, 0x0a, 0x35)

        assertThat(cpu.registers.sp).isEqualTo(0x253c)
    }
}
