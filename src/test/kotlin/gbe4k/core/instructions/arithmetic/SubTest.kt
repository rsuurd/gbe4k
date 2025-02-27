package gbe4k.core.instructions.arithmetic

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import gbe4k.core.Register.B
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.E
import gbe4k.core.Register.H
import gbe4k.core.Register.L
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SubTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("r8")
    fun `should sub a, r8`(register: Register, opcode: Int) {
        cpu.registers.a = 0x6b
        cpu.registers[register] = 0x23

        stepWith(opcode)

        assertThat(cpu.registers.a).isEqualTo(0x48)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
    }

    @Test
    fun `should sub a, d8`() {
        cpu.registers.a = 0x23

        stepWith(0xd6, 0x23)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
    }

    @Test
    fun `should set z flag`() {
        cpu.registers.a = 0x11

        stepWith(0xd6, 0x11)

        assertThat(cpu.registers.a).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
    }

    @Test
    fun `should set h flag`() {
        cpu.registers.a = 0x11

        stepWith(0xd6, 0x02)

        assertThat(cpu.registers.a).isEqualTo(0x0f)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isTrue()
    }

    @Test
    fun `should sub a, (hl)`() {
        cpu.registers.a = 0x99.toByte()
        cpu.registers.hl = 0x4563

        stepWith(0x96, 0x55)

        assertThat(cpu.registers.a).isEqualTo(0x44)
    }

    companion object {
        @JvmStatic
        fun r8(): Stream<Arguments> = Stream.of(
            Arguments.of(B, 0x90),
            Arguments.of(C, 0x91),
            Arguments.of(D, 0x92),
            Arguments.of(E, 0x93),
            Arguments.of(H, 0x94),
            Arguments.of(L, 0x95),
        )
    }
}