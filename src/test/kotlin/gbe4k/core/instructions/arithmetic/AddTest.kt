package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddTest : CpuTestSupport() {
    @Test
    fun `should add sp, r8`() {
        cpu.registers.sp = 0x000f
        // A:12 F:00 B:56 C:91 D:9A E:BC H:00 L:00 SP:000F PC:DEF8 PCMEM:E8,01,00,C3
        stepWith(0xe8, 0x1)

        assertThat(cpu.registers.sp).isEqualTo(0x0010)
        assertThat(cpu.flags.h).isTrue()
    }
    @Test
    fun `should add sp, r8 with carry & half carry`() {
        cpu.registers.sp = 0x0001
        // A:12 F:00 B:56 C:91 D:9A E:BC H:00 L:00 SP:0001 PC:DEF8 PCMEM:E8,FF,00,C3
        stepWith(0xe8, 0xff)

        assertThat(cpu.registers.sp).isEqualTo(0x000)
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
    }
}