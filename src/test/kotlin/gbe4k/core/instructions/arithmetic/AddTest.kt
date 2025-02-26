package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import gbe4k.core.Register.B
import gbe4k.core.Register.BC
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.DE
import gbe4k.core.Register.E
import gbe4k.core.Register.H
import gbe4k.core.Register.HL
import gbe4k.core.Register.L
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class AddTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r16")
    fun `should add hl, r16`(register: Register, opcode: Int) {
        cpu.registers.hl = 0x0023
        cpu.registers[register] = 0x0023

        stepWith(opcode)

        assertThat(cpu.registers.hl).isEqualTo(0x0046)
    }

    @ParameterizedTest
    @MethodSource("r8")
    fun `should add a, r8`(register: Register, opcode: Int) {
        cpu.registers.a = 0x23
        cpu.registers[register] = 0x23

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x46)
    }

    @Test
    fun `should add a, (hl)`() {
        cpu.registers.a = 0x23
        cpu.registers.hl = 0x4563

        stepWith(0x86, 0x55)

        assertThat(cpu.registers.a).isEqualTo(0x78)
    }

    @Test
    fun `should add a, d8`() {
        cpu.registers.a = 0x23

        stepWith(0xc6, 0x23)

        assertThat(cpu.registers.a).isEqualTo(0x46)
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(B, 0x80),
            Arguments.of(C, 0x81),
            Arguments.of(D, 0x82),
            Arguments.of(E, 0x83),
            Arguments.of(H, 0x84),
            Arguments.of(L, 0x85),
        )

        @JvmStatic
        fun r16(): Stream<Arguments> = Stream.of(
            Arguments.of(BC, 0x09),
            Arguments.of(DE, 0x19),
            Arguments.of(HL, 0x29),
            Arguments.of(Register.SP, 0x39),
        )
    }
}
