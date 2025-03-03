package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SwapTest : CpuTestSupport() {
    @Test
    fun `should swap b`() {
        // A:00 F:00 B:01 C:0F D:7F E:80 H:10 L:1F SP:DFF1 PC:DEF8 PCMEM:CB,30,00,C3
        cpu.registers.b = 0x01

        stepWith(0xcb, 0x30)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x10)
    }
}