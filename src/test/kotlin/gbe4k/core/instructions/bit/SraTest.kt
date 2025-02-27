package gbe4k.core.instructions.bit

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SraTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("registersAndOpcodes")
    fun `should sra r8`(registers: Register, opcode: Int) {
        cpu.registers[registers] = 0b00100001

        stepWith(0xcb, opcode)

        assertThat(cpu.registers[registers].toByte()).isEqualTo(0b100010000.toByte())
    }

    @Test
    fun `should sra (hl)`() {
        cpu.registers.hl = 0xa535

        stepWith(0xcb, 0x2e, 0b00100001.toByte())

        verify { bus.write(0xa535, 0b100010000.toByte()) }
    }

    @Test
    fun `should set flags`() {
        cpu.registers.b = 0b10000000.toByte()

        stepWith(0xcb, 0x28)

        assertThat(cpu.registers.b).isEqualTo(0b01000000.toByte())
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
    }

    companion object {
        @JvmStatic
        fun registersAndOpcodes(): Stream<Arguments> = Stream.of(
            Arguments.of(Register.B, 0x28),
            Arguments.of(Register.C, 0x29),
            Arguments.of(Register.D, 0x2a),
            Arguments.of(Register.E, 0x2b),
            Arguments.of(Register.H, 0x2c),
            Arguments.of(Register.L, 0x2d),
            Arguments.of(Register.A, 0x2f)
        )
    }
}