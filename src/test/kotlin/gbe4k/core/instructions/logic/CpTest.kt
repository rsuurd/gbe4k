package gbe4k.core.instructions.logic

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import gbe4k.core.Register.B
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.E
import gbe4k.core.Register.H
import gbe4k.core.Register.L
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CpTest : CpuTestSupport() {
    @Test
    fun `should cp a`() {
        cpu.registers.a = 0x3

        stepWith(0xbf)

        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    @Test
    fun `should cp b`() = checkCp(B, 0xb8)

    @Test
    fun `should cp c`() = checkCp(C, 0xb9)

    @Test
    fun `should cp d`() = checkCp(D, 0xba)

    @Test
    fun `should cp e`() = checkCp(E, 0xbb)

    @Test
    fun `should cp h`() = checkCp(H, 0xbc)

    @Test
    fun `should cp l`() = checkCp(L, 0xbd)

    @Test
    fun `should cp (hl)`() {
        cpu.registers.a = 0x3
        cpu.registers.hl = 0x7f32

        stepWith(0xbe, 0x4)

        assertThat(cpu.registers.a).isEqualTo(0x3)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
    }

    @Test
    fun `should cp d8`() {
        cpu.registers.a = 0x3

        stepWith(0xfe, 0x4)

        assertThat(cpu.registers.a).isEqualTo(0x3)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
    }

    private fun checkCp(registers: Register, opcode: Int) {
        cpu.registers.a = 0x03
        cpu.registers[registers] = 0x01

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x03)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }
}