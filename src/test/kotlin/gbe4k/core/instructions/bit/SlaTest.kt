package gbe4k.core.instructions.bit

import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SlaTest : CpuTestSupport() {
    @Test
    fun `should sla b`() = test(0x20, { cpu.registers.b = it }, { cpu.registers.b })

    @Test
    fun `should sla c`() = test(0x21, { cpu.registers.c = it }, { cpu.registers.c })

    @Test
    fun `should sla d`() = test(0x22, { cpu.registers.d = it }, { cpu.registers.d })

    @Test
    fun `should sla e`() = test(0x23, { cpu.registers.e = it }, { cpu.registers.e })

    @Test
    fun `should sla h`() = test(0x24, { cpu.registers.h = it }, { cpu.registers.h })

    @Test
    fun `should sla l`() = test(0x25, { cpu.registers.l = it }, { cpu.registers.l })

    @Test
    fun `should sla (hl)`() {
        setupMemory(0xff80, 0x01)
        cpu.registers.hl = 0xff80

        stepWith(0xcb, 0x26)

        verify { bus.write(0xff80, 0x02) }
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should sla a`() = test(0x27, { cpu.registers.a = it }, { cpu.registers.a })

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