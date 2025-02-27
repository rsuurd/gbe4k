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

class SwapTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("registersAndOpcodes")
    fun `should swap r8`(registers: Register, opcode: Int) {
        cpu.registers[registers] = 0x3b

        stepWith(0xcb, opcode)

        assertThat(cpu.registers[registers].toByte()).isEqualTo(0xb3.toByte())
    }

    @Test
    fun `should swap (hl)`() {
        cpu.registers.hl = 0xa535

        stepWith(0xcb, 0x36, 0x63)

        verify { bus.write(0xa535, 0x36) }
    }

    @Test
    fun `should set flags`() {
        cpu.registers.b = 0x00

        stepWith(0xcb, 0x30)

        assertThat(cpu.registers.b).isEqualTo(0x0)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    companion object {
        @JvmStatic
        fun registersAndOpcodes(): Stream<Arguments> = Stream.of(
            Arguments.of(Register.B, 0x30),
            Arguments.of(Register.C, 0x31),
            Arguments.of(Register.D, 0x32),
            Arguments.of(Register.E, 0x33),
            Arguments.of(Register.H, 0x34),
            Arguments.of(Register.L, 0x35),
            Arguments.of(Register.A, 0x37)
        )
    }
}