package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SwapTest : CpuTestSupport() {
    @Test
    fun `should swap b`() = test(0x30, { cpu.registers.b = it }, { cpu.registers.b })

    @Test
    fun `should swap c`() = test(0x31, { cpu.registers.c = it }, { cpu.registers.c })

    @Test
    fun `should swap d`() = test(0x32, { cpu.registers.d = it }, { cpu.registers.d })

    @Test
    fun `should swap e`() = test(0x33, { cpu.registers.e = it }, { cpu.registers.e })

    @Test
    fun `should swap h`() = test(0x34, { cpu.registers.h = it }, { cpu.registers.h })

    @Test
    fun `should swap l`() = test(0x35, { cpu.registers.l = it }, { cpu.registers.l })

    @Test
    fun `should swap (hl)`() {
        setupMemory(0xff80, 0x00)
        cpu.registers.hl = 0xff80

        stepWith(0xcb, 0x36)

        verify { bus.write(0xff80, 0x00) }
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should swap a`() = test(0x37, { cpu.registers.a = it }, { cpu.registers.a })

    private fun test(opcode: Byte, set: (Byte) -> Unit, get: () -> Byte) {
        set(0x01)

        stepWith(0xcb, opcode)

        assertThat(get().asInt()).isEqualTo(0x10)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(8)
    }
}
