package gbe4k.core.instructions.bit

import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SraTest : CpuTestSupport() {
    @Test
    fun `should sra b`() = test(0x28, { cpu.registers.b = it }, { cpu.registers.b })

    @Test
    fun `should sra c`() = test(0x29, { cpu.registers.c = it }, { cpu.registers.c })

    @Test
    fun `should sra d`() = test(0x2a, { cpu.registers.d = it }, { cpu.registers.d })

    @Test
    fun `should sra e`() = test(0x2b, { cpu.registers.e = it }, { cpu.registers.e })

    @Test
    fun `should sra h`() = test(0x2c, { cpu.registers.h = it }, { cpu.registers.h })

    @Test
    fun `should sra l`() = test(0x2d, { cpu.registers.l = it }, { cpu.registers.l })

    @Test
    fun `should sra (hl)`() {
        setupMemory(0xff80, 0x04)
        cpu.registers.hl = 0xff80

        stepWith(0xcb, 0x2e)

        verify { bus.write(0xff80, 0x02) }
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should sra a`() = test(0x2f, { cpu.registers.a = it }, { cpu.registers.a })

    private fun test(opcode: Byte, setupRegister: (Byte) -> Unit, readRegister: () -> Byte) {
        setupRegister(0b00000001.toByte())

        stepWith(0xcb, opcode)

        assertThat(readRegister()).isEqualTo(0b00000000)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }
}