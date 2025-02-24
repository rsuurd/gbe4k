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

class DecTest : CpuTestSupport() {
    @Test
    fun `should dec bc`() {
        verifyDec(BC, 0x0b)
    }

    @Test
    fun `should dec de`() {
        verifyDec(DE, 0x1b)
    }

    @Test
    fun `should dec hl`() {
        verifyDec(HL, 0x2b)
    }

    @Test
    fun `should dec sp`() {
        verifyDec(SP, 0x3b)
    }

    @Test
    fun `should dec (hl)`() {
        cpu.registers.hl = 0x1223

        stepWith(0x35, 0x12)

        verify { bus.write(0x1223, 0x11) }
    }

    @Test
    fun `should dec a`() {
        verifyDec(A, 0x3d)
    }

    @Test
    fun `should dec b`() {
        verifyDec(B, 0x05)
    }

    @Test
    fun `should dec c`() {
        verifyDec(C, 0x0d)
    }

    @Test
    fun `should dec d`() {
        verifyDec(D, 0x15)
    }

    @Test
    fun `should dec e`() {
        verifyDec(E, 0x1d)
    }

    @Test
    fun `should dec h`() {
        verifyDec(H, 0x25)
    }

    @Test
    fun `should dec l`() {
        verifyDec(L, 0x2d)
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "H", "L"])
    fun `should set z flag to 1`(register: Register) {
        cpu.registers[register] = 0x01

        Dec(register).execute(cpu)

        assertThat(cpu.flags.z).isTrue()
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "H", "L"])
    fun `should set n flag to 1`(register: Register) {
        cpu.registers[register] = 0x13

        Dec(register).execute(cpu)

        assertThat(cpu.flags.n).isTrue()
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "H", "L"])
    fun `should set z flag to 0`(register: Register) {
        cpu.registers[register] = 0xfa

        Dec(register).execute(cpu)

        assertThat(cpu.flags.z).isFalse()
    }

    private fun verifyDec(register: Register, opcode: Byte) {
        cpu.registers[register] = 0x12

        stepWith(opcode)

        assertThat(cpu.registers[register]).isEqualTo(0x11)
    }
}