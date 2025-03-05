package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RlTest : CpuTestSupport() {
    @Test
    fun `should rla`() {
        cpu.registers.a = 0b10000000.toByte()

        stepWith(0x17)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0b00000000)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should rl b`() = test(0x10, { cpu.registers.b = it }, { cpu.registers.b })

    @Test
    fun `should rl c`() = test(0x11, { cpu.registers.c = it }, { cpu.registers.c })

    @Test
    fun `should rl d`() = test(0x12, { cpu.registers.d = it }, { cpu.registers.d })

    @Test
    fun `should rl e`() = test(0x13, { cpu.registers.e = it }, { cpu.registers.e })

    @Test
    fun `should rl h`() = test(0x14, { cpu.registers.h = it }, { cpu.registers.h })

    @Test
    fun `should rl l`() = test(0x15, { cpu.registers.l = it }, { cpu.registers.l })

    @Test
    fun `should rl (hl)`() {
        setupMemory(0xff80, 0b10000000.toByte())
        cpu.registers.hl = 0xff80

        stepWith(0xcb, 0x16)

        verify { bus.write(0xff80, 0b0000000) }
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rl a`() {
        test(0x17, { cpu.registers.a = it }, { cpu.registers.a })
    }

    private fun test(opcode: Byte, setupRegister: (Byte) -> Unit, readRegister: () -> Byte) {
        setupRegister(0b10000000.toByte())

        stepWith(0xcb, opcode)

        assertThat(readRegister()).isEqualTo(0b00000000)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }
}
