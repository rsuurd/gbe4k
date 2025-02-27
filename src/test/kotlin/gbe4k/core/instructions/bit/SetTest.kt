package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.isBitSet
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

class SetTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r8")
    fun `should set bit, r8`(opcode: Int, position: Int, register: Register) {
        cpu.registers[register] = 0

        stepWith(0xcb, opcode)

        assertThat(cpu.registers[register].toByte().isBitSet(position)).isTrue()
    }

    @ParameterizedTest
    @MethodSource("hl")
    fun `should set position, (hl)`(opcode: Int, position: Int) {
        cpu.registers.hl = 0x6430

        stepWith(0xcb, opcode, 0x00)

        verify { bus.write(0x6430, 0x1.shl(position).toByte()) }
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(0xc0, 0, B),
            Arguments.of(0xc1, 0, C),
            Arguments.of(0xc2, 0, D),
            Arguments.of(0xc3, 0, E),
            Arguments.of(0xc4, 0, H),
            Arguments.of(0xc5, 0, L),
            Arguments.of(0xc7, 0, A),
            Arguments.of(0xc8, 1, B),
            Arguments.of(0xc9, 1, C),
            Arguments.of(0xca, 1, D),
            Arguments.of(0xcb, 1, E),
            Arguments.of(0xcc, 1, H),
            Arguments.of(0xcd, 1, L),
            Arguments.of(0xcf, 1, A),
            Arguments.of(0xd0, 2, B),
            Arguments.of(0xd1, 2, C),
            Arguments.of(0xd2, 2, D),
            Arguments.of(0xd3, 2, E),
            Arguments.of(0xd4, 2, H),
            Arguments.of(0xd5, 2, L),
            Arguments.of(0xd7, 2, A),
            Arguments.of(0xd8, 3, B),
            Arguments.of(0xd9, 3, C),
            Arguments.of(0xda, 3, D),
            Arguments.of(0xdb, 3, E),
            Arguments.of(0xdc, 3, H),
            Arguments.of(0xdd, 3, L),
            Arguments.of(0xdf, 3, A),
            Arguments.of(0xe0, 4, B),
            Arguments.of(0xe1, 4, C),
            Arguments.of(0xe2, 4, D),
            Arguments.of(0xe3, 4, E),
            Arguments.of(0xe4, 4, H),
            Arguments.of(0xe5, 4, L),
            Arguments.of(0xe7, 4, A),
            Arguments.of(0xe8, 5, B),
            Arguments.of(0xe9, 5, C),
            Arguments.of(0xea, 5, D),
            Arguments.of(0xeb, 5, E),
            Arguments.of(0xec, 5, H),
            Arguments.of(0xed, 5, L),
            Arguments.of(0xef, 5, A),
            Arguments.of(0xf0, 6, B),
            Arguments.of(0xf1, 6, C),
            Arguments.of(0xf2, 6, D),
            Arguments.of(0xf3, 6, E),
            Arguments.of(0xf4, 6, H),
            Arguments.of(0xf5, 6, L),
            Arguments.of(0xf7, 6, A),
            Arguments.of(0xf8, 7, B),
            Arguments.of(0xf9, 7, C),
            Arguments.of(0xfa, 7, D),
            Arguments.of(0xfb, 7, E),
            Arguments.of(0xfc, 7, H),
            Arguments.of(0xfd, 7, L),
            Arguments.of(0xff, 7, A)
        )

        @JvmStatic
        fun hl(): Stream<Arguments> = Stream.of(
            Arguments.of(0xc6, 0),
            Arguments.of(0xce, 1),
            Arguments.of(0xd6, 2),
            Arguments.of(0xde, 3),
            Arguments.of(0xe6, 4),
            Arguments.of(0xee, 5),
            Arguments.of(0xf6, 6),
            Arguments.of(0xfe, 7)
        )
    }
}