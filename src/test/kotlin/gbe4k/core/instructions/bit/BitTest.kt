package gbe4k.core.instructions.bit

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import gbe4k.core.Register.A
import gbe4k.core.Register.B
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.E
import gbe4k.core.Register.H
import gbe4k.core.Register.L
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class BitTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r8")
    fun `should bit, r8`(opcode: Int, position: Int, register: Register) {
        cpu.registers[register] = 0x1.shl(position).toByte()

        stepWith(0xcb, opcode)

        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    @ParameterizedTest
    @MethodSource("hl")
    fun `should bit position, (hl)`(opcode: Int, position: Int) {
        cpu.registers.hl = 0x6430

        stepWith(0xcb, opcode, 0x1.shl(position))

        verify(exactly = 0) { bus.write(any(), any())  }
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.c).isFalse()
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(0x40, 0, B),
            Arguments.of(0x41, 0, C),
            Arguments.of(0x42, 0, D),
            Arguments.of(0x43, 0, E),
            Arguments.of(0x44, 0, H),
            Arguments.of(0x45, 0, L),
            Arguments.of(0x47, 0, A),
            Arguments.of(0x48, 1, B),
            Arguments.of(0x49, 1, C),
            Arguments.of(0x4a, 1, D),
            Arguments.of(0x4b, 1, E),
            Arguments.of(0x4c, 1, H),
            Arguments.of(0x4d, 1, L),
            Arguments.of(0x4f, 1, A),
            Arguments.of(0x50, 2, B),
            Arguments.of(0x51, 2, C),
            Arguments.of(0x52, 2, D),
            Arguments.of(0x53, 2, E),
            Arguments.of(0x54, 2, H),
            Arguments.of(0x55, 2, L),
            Arguments.of(0x57, 2, A),
            Arguments.of(0x58, 3, B),
            Arguments.of(0x59, 3, C),
            Arguments.of(0x5a, 3, D),
            Arguments.of(0x5b, 3, E),
            Arguments.of(0x5c, 3, H),
            Arguments.of(0x5d, 3, L),
            Arguments.of(0x5f, 3, A),
            Arguments.of(0x60, 4, B),
            Arguments.of(0x61, 4, C),
            Arguments.of(0x62, 4, D),
            Arguments.of(0x63, 4, E),
            Arguments.of(0x64, 4, H),
            Arguments.of(0x65, 4, L),
            Arguments.of(0x67, 4, A),
            Arguments.of(0x68, 5, B),
            Arguments.of(0x69, 5, C),
            Arguments.of(0x6a, 5, D),
            Arguments.of(0x6b, 5, E),
            Arguments.of(0x6c, 5, H),
            Arguments.of(0x6d, 5, L),
            Arguments.of(0x6f, 5, A),
            Arguments.of(0x70, 6, B),
            Arguments.of(0x71, 6, C),
            Arguments.of(0x72, 6, D),
            Arguments.of(0x73, 6, E),
            Arguments.of(0x74, 6, H),
            Arguments.of(0x75, 6, L),
            Arguments.of(0x77, 6, A),
            Arguments.of(0x78, 7, B),
            Arguments.of(0x79, 7, C),
            Arguments.of(0x7a, 7, D),
            Arguments.of(0x7b, 7, E),
            Arguments.of(0x7c, 7, H),
            Arguments.of(0x7d, 7, L),
            Arguments.of(0x7f, 7, A)
        )

        @JvmStatic
        fun hl(): Stream<Arguments> = Stream.of(
            Arguments.of(0x46, 0),
            Arguments.of(0x4e, 1),
            Arguments.of(0x56, 2),
            Arguments.of(0x5e, 3),
            Arguments.of(0x66, 4),
            Arguments.of(0x6e, 5),
            Arguments.of(0x76, 6),
            Arguments.of(0x7e, 7)
        )
    }
}