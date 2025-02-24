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

class XorTest : CpuTestSupport() {
    @Test
    fun `should xor a`() {
        cpu.registers.a = 0xa

        stepWith(0xaf)

        assertThat(cpu.registers.a).isEqualTo(0x0)
    }

    @Test
    fun `should xor b`() {
        checkXor(B, 0xa8)
    }

    @Test
    fun `should xor c`() {
        checkXor(C, 0xa9)
    }

    @Test
    fun `should xor d`() {
        checkXor(D, 0xaa)
    }

    @Test
    fun `should xor e`() {
        checkXor(E, 0xab)
    }

    @Test
    fun `should xor h`() {
        checkXor(H, 0xac)
    }

    @Test
    fun `should xor l`() {
        checkXor(L, 0xad)
    }

    @Test
    fun `should xor (hl)`() {
        cpu.registers.a = 0x3
        cpu.registers.hl = 0x7f32

        stepWith(0xae, 0x4)

        assertThat(cpu.registers.a).isEqualTo(0x7)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    @Test
    fun `should xor d8`() {
        cpu.registers.a = 0x3

        stepWith(0xee, 0x4)

        assertThat(cpu.registers.a).isEqualTo(0x7)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    private fun checkXor(register: Register, opcode: Int) {
        cpu.registers.a = 0xa
        cpu.registers[register] = 0xc

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x6)
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

         Xor(register).execute(cpu)

        assertThat(cpu.flags.z).isTrue()
    }
}