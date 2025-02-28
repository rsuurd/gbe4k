package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.hex
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class RrTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r8")
    fun `should rr r8`(register: Register, opcode: Int) {
        cpu.flags.c = false
        cpu.registers[register] = 0b00000001

        stepWith(0xcb, opcode)

        println(cpu.registers[register].hex())
        assertThat(cpu.registers[register].toByte()).isEqualTo(0x00.toByte())
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
    }

    @Test
    fun `should rr (hl)`() {
        cpu.registers.hl = 0xa535
        cpu.flags.c = true

        stepWith(0xcb, 0x1e, 0b00000001)

        verify { bus.write(0xa535, 0b10000000.toByte()) }
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(B, 0x18),
            Arguments.of(C, 0x19),
            Arguments.of(D, 0x1a),
            Arguments.of(E, 0x1b),
            Arguments.of(H, 0x1c),
            Arguments.of(L, 0x1d),
            Arguments.of(A, 0x1f)
        )
    }
}