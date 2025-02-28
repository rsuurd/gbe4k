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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class RlTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r8")
    fun `should rr r8`(registers: Register, opcode: Int) {
        cpu.registers[registers] = 0b10000000

        stepWith(0xcb, opcode)

        assertThat(cpu.registers[registers].toByte()).isEqualTo(0x00.toByte())
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
    }

    @Test
    fun `should rr (hl)`() {
        cpu.registers.hl = 0xa535
        cpu.flags.c = true

        stepWith(0xcb, 0x16, 0b10000000)

        verify { bus.write(0xa535, 0b00000001.toByte()) }
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(B, 0x10),
            Arguments.of(C, 0x11),
            Arguments.of(D, 0x12),
            Arguments.of(E, 0x13),
            Arguments.of(H, 0x14),
            Arguments.of(L, 0x15),
            Arguments.of(A, 0x17)
        )
    }
}