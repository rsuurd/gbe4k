package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import gbe4k.core.Register.F
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class LdTest : CpuTestSupport() {
    @ParameterizedTest
    @EnumSource(names = ["AF", "BC", "DE", "HL", "SP"])
    fun `should ld r16, d16`(register: Register) {
        Ld(register, 0xffff).execute(cpu)

        assertThat(cpu.registers[register]).isEqualTo(0xffff)
    }

    @ParameterizedTest
    @EnumSource(names = ["AF", "BC", "DE", "HL", "SP"])
    fun `should reject byte for r16`(register: Register) {
        assertThatThrownBy {
            Ld(register, 0x12.toByte()).execute(cpu)
        }

        assertThat(cpu.registers[register]).isEqualTo(0x0000)
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E"])
    fun `should ld r8, d8`(register: Register) {
        Ld(register, 0xab.toByte()).execute(cpu)

        assertThat(cpu.registers[register].toByte()).isEqualTo(0xab.toByte())
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E"])
    fun `should ld a16, r8`(register: Register) {
        cpu.registers[register] = 0x03
        every { bus.write(any(), any()) } just runs

        Ld(0xab01, register).execute(cpu)

        verify { bus.write(0xab01, 0x03) }
    }

    @Test
    fun `should not ld F, d8`() {
        assertThatThrownBy {
            Ld(F, 0xab.toByte()).execute(cpu)
        }.hasMessage("Can not LD F, 0xab")

        assertThat(cpu.registers.f).isEqualTo(0x00)
    }

    @ParameterizedTest
    @EnumSource(names = ["A", "B", "C", "D", "E", "F"])
    fun `should reject int for r8`(register: Register) {
        assertThatThrownBy {
            Ld(register, 0xffff).execute(cpu)
        }

        assertThat(cpu.registers[register]).isEqualTo(0x0000)
    }
}
