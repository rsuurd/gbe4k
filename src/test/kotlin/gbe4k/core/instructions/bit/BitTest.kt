package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.setBit
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class BitTest : CpuTestSupport() {
    @ParameterizedTest
    @MethodSource("opcodes")
    fun `should check bit set`(opcode: Byte) = test(opcode, enabled = true)

    @ParameterizedTest
    @MethodSource("opcodes")
    fun `should check bit not set`(opcode: Byte) = test(opcode, enabled = false)

    private fun test(opcode: Byte, enabled: Boolean) {
        val position = (opcode / 0x8) - 0x8
        val value = (if (enabled) 0x00 else 0xff).toByte().setBit(enabled, position)

        when (opcode % 0x8) {
            0 -> cpu.registers.b = value
            1 -> cpu.registers.c = value
            2 -> cpu.registers.d = value
            3 -> cpu.registers.e = value
            4 -> cpu.registers.h = value
            5 -> cpu.registers.l = value
            6 -> {
                setupMemory(0xff80, value)
                cpu.registers.hl = 0xff80
            }

            7 -> cpu.registers.a = value
        }

        stepWith(0xcb, opcode)

        assertThat(cpu.flags.z).isNotEqualTo(enabled)
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()

        if (opcode % 0x8 == 6) {
            assertThat(timer.div).isEqualTo(16)
        } else {
            assertThat(timer.div).isEqualTo(8)
        }
    }

    companion object {
        @JvmStatic
        fun opcodes(): Stream<Arguments> = (0x40..0x7f).map { Arguments.of(it.toByte()) }.stream()
    }
}