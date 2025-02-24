package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import gbe4k.core.Register.A
import gbe4k.core.Register.B
import gbe4k.core.Register.BC
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.DE
import gbe4k.core.Register.E
import gbe4k.core.Register.H
import gbe4k.core.Register.HL
import gbe4k.core.Register.L
import gbe4k.core.Register.SP
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class IncTest : CpuTestSupport() {
    @Test
    fun `should inc bc`() {
        verifyInc(BC, 0x03)
    }

    @Test
    fun `should inc de`() {
        verifyInc(DE, 0x13)
    }

    @Test
    fun `should inc hl`() {
        verifyInc(HL, 0x23)
    }

    @Test
    fun `should inc sp`() {
        verifyInc(SP, 0x33)
    }

    @Test
    fun `should inc (hl)`() {
        cpu.registers.hl = 0x1223

        stepWith(0x34, 0x12)

        verify { bus.write(0x1223, 0x13) }
    }

    @Test
    fun `should inc a`() {
        verifyInc(A, 0x3c)
    }

    @Test
    fun `should inc b`() {
        verifyInc(B, 0x04)
    }

    @Test
    fun `should inc c`() {
        verifyInc(C, 0x0c)
    }

    @Test
    fun `should inc d`() {
        verifyInc(D, 0x14)
    }

    @Test
    fun `should inc e`() {
        verifyInc(E, 0x1c)
    }

    @Test
    fun `should inc h`() {
        verifyInc(H, 0x24)
    }

    @Test
    fun `should inc l`() {
        verifyInc(L, 0x2c)
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "H", "L"])
    fun `should set z flag to 1`(register: Register) {
        cpu.registers[register] = 0xff

        Inc(register).execute(cpu)

        assertThat(cpu.flags.z).isTrue()
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "H", "L"])
    fun `should set n flag to 0`(register: Register) {
        cpu.registers[register] = 0x13

        Inc(register).execute(cpu)

        assertThat(cpu.flags.n).isFalse()
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "H", "L"])
    fun `should set z flag to 0`(register: Register) {
        cpu.registers[register] = 0xfa

        Inc(register).execute(cpu)

        assertThat(cpu.flags.z).isFalse()
    }

    private fun verifyInc(register: Register, opcode: Byte) {
        cpu.registers[register] = 0x12

        stepWith(opcode)

        assertThat(cpu.registers[register]).isEqualTo(0x13)
    }
}