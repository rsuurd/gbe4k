package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SbcTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r8")
    fun `should sbc r8`(register: Register, opcode: Int) {
        cpu.registers.a = 0xf4.toByte()
        cpu.registers[register] = 0xf3.toByte()
        cpu.flags.c = true

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x0)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    @Test
    fun `should sbc a`() {
        cpu.registers.a = 0xf4.toByte()
        cpu.flags.c = true

        stepWith(0x9f)

        assertThat(cpu.registers.a).isEqualTo(0xff.toByte())
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isTrue()
    }

    @Test
    fun `should sbc d8`() {
        cpu.registers.a = 0xf4.toByte()
        cpu.flags.c = true

        stepWith(0xde, 0x00)

        assertThat(cpu.registers.a).isEqualTo(0xf3.toByte())
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(Register.B, 0x98),
            Arguments.of(Register.C, 0x99),
            Arguments.of(Register.D, 0x9a),
            Arguments.of(Register.E, 0x9b),
            Arguments.of(Register.H, 0x9c),
            Arguments.of(Register.L, 0x9d)
        )
    }
}
