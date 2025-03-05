package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.setBit
import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SetTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("opcodes")
    fun `should set bit`(opcode: Int) = test(opcode)

    private fun test(opcode: Int) {
        val base = opcode - 0xc0
        if (base % 0x8 == 6) {
            cpu.registers.hl = 0xff80
        }

        stepWith(0xcb, opcode)

        val position = (base / 0x8)

        val value = 0x00.toByte().setBit(true, position)
        if (base % 0x8 == 6) {
            verify { bus.write(0xff80, value) }
            assertThat(timer.div).isEqualTo(16)
        } else {
            val result = when (base % 0x8) {
                0 -> cpu.registers.b
                1 -> cpu.registers.c
                2 -> cpu.registers.d
                3 -> cpu.registers.e
                4 -> cpu.registers.h
                5 -> cpu.registers.l
                7 -> cpu.registers.a
                else -> 0x00
            }

            assertThat(result).isEqualTo(value)
            assertThat(timer.div).isEqualTo(8)
        }
    }

    companion object {
        @JvmStatic
        fun opcodes(): Stream<Arguments> = (0xc0..0xff).map { Arguments.of(it) }.stream()
    }
}