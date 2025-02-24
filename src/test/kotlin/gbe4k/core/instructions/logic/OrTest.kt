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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class OrTest : CpuTestSupport() {
    @Test
    fun `should or a`() {
        cpu.registers.a = 0xa

        stepWith(0x87)

        assertThat(cpu.registers.a).isEqualTo(0xa)
    }

    @Test
    fun `should or b`() {
        checkOr(B, 0x80)
    }

    @Test
    fun `should or c`() {
        checkOr(C, 0x81)
    }

    @Test
    fun `should or d`() {
        checkOr(D, 0x82)
    }

    @Test
    fun `should or e`() {
        checkOr(E, 0x83)
    }

    @Test
    fun `should or h`() {
        checkOr(H, 0x84)
    }

    @Test
    fun `should or l`() {
        checkOr(L, 0x85)
    }

    @Test
    fun `should or (hl)`() {
        cpu.registers.a = 0x3
        cpu.registers.hl = 0x7f32

        stepWith(0x86, 0x4)

        assertThat(cpu.registers.a).isEqualTo(0x7)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    private fun checkOr(register: Register, opcode: Int) {
        cpu.registers.a = 0xa
        cpu.registers[register] = 0xc

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0xe)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "H", "L"])
    fun `should set zero flag`(register: Register) {
        cpu.registers.a = 0x0
        cpu.registers[register] = 0x0

         Or(register).execute(cpu)

        assertThat(cpu.flags.z).isTrue()
    }
}