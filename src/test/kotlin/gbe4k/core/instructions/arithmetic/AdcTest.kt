package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class AdcTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r8")
    fun `should adc r8`(register: Register, opcode: Int) {
        cpu.registers.a = 0xf4.toByte()
        cpu.registers[register] = 0x0b
        cpu.flags.c = true

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x0)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
    }

    @Test
    fun `should adc a`() {
        cpu.registers.a = 0xf4.toByte()
        cpu.flags.c = true

        stepWith(0x8f)

        assertThat(cpu.registers.a).isEqualTo(0xe9.toByte())
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
    }

    @Test
    fun `should adc d8`() {
        cpu.registers.a = 0xf4.toByte()
        cpu.flags.c = true

        stepWith(0xce, 0x00)

        assertThat(cpu.registers.a).isEqualTo(0xf5.toByte())
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(Register.B, 0x88),
            Arguments.of(Register.C, 0x89),
            Arguments.of(Register.D, 0x8a),
            Arguments.of(Register.E, 0x8b),
            Arguments.of(Register.H, 0x8c),
            Arguments.of(Register.L, 0x8d)
        )
    }
}
