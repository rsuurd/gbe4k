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

class AndTest : CpuTestSupport() {
    @Test
    fun `should and a`() {
        cpu.registers.a = 0xa

        stepWith(0xa7)

        assertThat(cpu.registers.a).isEqualTo(0xa)
    }

    @Test
    fun `should and b`() {
        checkAnd(B, 0xa0)
    }

    @Test
    fun `should and c`() {
        checkAnd(C, 0xa1)
    }

    @Test
    fun `should and d`() {
        checkAnd(D, 0xa2)
    }

    @Test
    fun `should and e`() {
        checkAnd(E, 0xa3)
    }

    @Test
    fun `should and h`() {
        checkAnd(H, 0xa4)
    }

    @Test
    fun `should and l`() {
        checkAnd(L, 0xa5)
    }

    @Test
    fun `should and (hl)`() {
        cpu.registers.a = 0x3
        cpu.registers.hl = 0x7f32

        stepWith(0xa6, 0x5)

        assertThat(cpu.registers.a).isEqualTo(0x1)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
    }

    @Test
    fun `should and d8`() {
        cpu.registers.a = 0x3
        cpu.registers.hl = 0x7f32

        stepWith(0xe6, 0x7)

        assertThat(cpu.registers.a).isEqualTo(0x3)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
    }

    private fun checkAnd(register: Register, opcode: Int) {
        cpu.registers.a = 0xa
        cpu.registers[register] = 0xc

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x8)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "H", "L"])
    fun `should set zero flag`(register: Register) {
        cpu.registers.a = 0x0
        cpu.registers[register] = 0x0

         And(register).execute(cpu)

        assertThat(cpu.flags.z).isTrue()
    }
}