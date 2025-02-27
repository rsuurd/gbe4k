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

class ResTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r8")
    fun `should res bit, r8`(opcode: Int, position: Int, register: Register) {
        cpu.registers[register] = 0xff

        stepWith(0xcb, opcode)

        assertThat(cpu.registers[register].toByte().isBitSet(position)).isFalse()
    }

    @ParameterizedTest
    @MethodSource("hl")
    fun `should res position, (hl)`(opcode: Int, position: Int) {
        cpu.registers.hl = 0x6430

        stepWith(0xcb, opcode, 0x1.shl(position))

        verify { bus.write(0x6430, 0x00) }
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(0x80, 0, B),
            Arguments.of(0x81, 0, C),
            Arguments.of(0x82, 0, D),
            Arguments.of(0x83, 0, E),
            Arguments.of(0x84, 0, H),
            Arguments.of(0x85, 0, L),
            Arguments.of(0x87, 0, A),
            Arguments.of(0x88, 1, B),
            Arguments.of(0x89, 1, C),
            Arguments.of(0x8a, 1, D),
            Arguments.of(0x8b, 1, E),
            Arguments.of(0x8c, 1, H),
            Arguments.of(0x8d, 1, L),
            Arguments.of(0x8f, 1, A),
            Arguments.of(0x90, 2, B),
            Arguments.of(0x91, 2, C),
            Arguments.of(0x92, 2, D),
            Arguments.of(0x93, 2, E),
            Arguments.of(0x94, 2, H),
            Arguments.of(0x95, 2, L),
            Arguments.of(0x97, 2, A),
            Arguments.of(0x98, 3, B),
            Arguments.of(0x99, 3, C),
            Arguments.of(0x9a, 3, D),
            Arguments.of(0x9b, 3, E),
            Arguments.of(0x9c, 3, H),
            Arguments.of(0x9d, 3, L),
            Arguments.of(0x9f, 3, A),
            Arguments.of(0xa0, 4, B),
            Arguments.of(0xa1, 4, C),
            Arguments.of(0xa2, 4, D),
            Arguments.of(0xa3, 4, E),
            Arguments.of(0xa4, 4, H),
            Arguments.of(0xa5, 4, L),
            Arguments.of(0xa7, 4, A),
            Arguments.of(0xa8, 5, B),
            Arguments.of(0xa9, 5, C),
            Arguments.of(0xaa, 5, D),
            Arguments.of(0xab, 5, E),
            Arguments.of(0xac, 5, H),
            Arguments.of(0xad, 5, L),
            Arguments.of(0xaf, 5, A),
            Arguments.of(0xb0, 6, B),
            Arguments.of(0xb1, 6, C),
            Arguments.of(0xb2, 6, D),
            Arguments.of(0xb3, 6, E),
            Arguments.of(0xb4, 6, H),
            Arguments.of(0xb5, 6, L),
            Arguments.of(0xb7, 6, A),
            Arguments.of(0xb8, 7, B),
            Arguments.of(0xb9, 7, C),
            Arguments.of(0xba, 7, D),
            Arguments.of(0xbb, 7, E),
            Arguments.of(0xbc, 7, H),
            Arguments.of(0xbd, 7, L),
            Arguments.of(0xbf, 7, A)
        )

        @JvmStatic
        fun hl(): Stream<Arguments> = Stream.of(
            Arguments.of(0x86, 0),
            Arguments.of(0x8e, 1),
            Arguments.of(0x96, 2),
            Arguments.of(0x9e, 3),
            Arguments.of(0xa6, 4),
            Arguments.of(0xae, 5),
            Arguments.of(0xb6, 6),
            Arguments.of(0xbe, 7)
        )
    }
}