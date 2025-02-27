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

class SlaTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("registersAndOpcodes")
    fun `should sla r8`(registers: Register, opcode: Int) {
        cpu.registers[registers] = 0x4

        stepWith(0xcb, opcode)

        assertThat(cpu.registers[registers]).isEqualTo(0x8)
    }

    @Test
    fun `should sla (hl)`() {
        cpu.registers.hl = 0xa535

        stepWith(0xcb, 0x26, 0x11)

        verify { bus.write(0xa535, 0x22) }
    }

    @Test
    fun `should set flags`() {
        cpu.registers.b = 0x80.toByte()

        stepWith(0xcb, 0x20)

        assertThat(cpu.registers.b).isEqualTo(0x0)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
    }

    companion object {
        @JvmStatic
        fun registersAndOpcodes(): Stream<Arguments> = Stream.of(
            Arguments.of(Register.B, 0x20),
            Arguments.of(Register.C, 0x21),
            Arguments.of(Register.D, 0x22),
            Arguments.of(Register.E, 0x23),
            Arguments.of(Register.H, 0x24),
            Arguments.of(Register.L, 0x25),
            Arguments.of(Register.A, 0x27)
        )
    }
}