package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DaaTest : CpuTestSupport() {
    @Test
    fun `should daa`() {
        cpu.registers.a = 0x53
        cpu.flags.n = true
        cpu.flags.h = true
        cpu.flags.c = true

        stepWith(0x27)

        assertThat(cpu.registers.a).isEqualTo(0xed.toByte())
    }
}