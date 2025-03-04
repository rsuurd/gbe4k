package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddTest : CpuTestSupport() {
    @Test
    fun `should add sp, r8`() {
        cpu.registers.sp = 0x000f

        stepWith(0xe8, 0x1)

        assertThat(cpu.registers.sp).isEqualTo(0x0010)
        assertThat(cpu.flags.h).isTrue()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should add sp, r8 with carry & half carry`() {
        cpu.registers.sp = 0x0001

        stepWith(0xe8, 0xff)

        assertThat(cpu.registers.sp).isEqualTo(0x000)
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(16)
    }

    //  TODO add more tests
}