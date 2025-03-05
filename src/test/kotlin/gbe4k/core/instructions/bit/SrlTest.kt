package gbe4k.core.instructions.bit

import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SrlTest : CpuTestSupport() {
    @Test
    fun `should srl b`() = test(0x38, { cpu.registers.b = it }, { cpu.registers.b })

    @Test
    fun `should srl c`() = test(0x39, { cpu.registers.c = it }, { cpu.registers.c })

    @Test
    fun `should srl d`() = test(0x3a, { cpu.registers.d = it }, { cpu.registers.d })

    @Test
    fun `should srl e`() = test(0x3b, { cpu.registers.e = it }, { cpu.registers.e })

    @Test
    fun `should srl h`() = test(0x3c, { cpu.registers.h = it }, { cpu.registers.h })

    @Test
    fun `should srl l`() = test(0x3d, { cpu.registers.l = it }, { cpu.registers.l })

    @Test
    fun `should srl (hl)`() {
        setupMemory(0xff80, 0x00)
        cpu.registers.hl = 0xff80

        stepWith(0xcb, 0x3e)

        verify { bus.write(0xff80, 0x00) }
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should srl a`() = test(0x3f, { cpu.registers.a = it }, { cpu.registers.a })

    private fun test(opcode: Byte, set: (Byte) -> Unit, get: () -> Byte) {
        set(0xff.toByte())

        stepWith(0xcb, opcode)

        assertThat(get()).isEqualTo(0x7f)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }
}
