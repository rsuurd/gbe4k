package gbe4k.core.instructions.bit

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RrTest : CpuTestSupport() {
    @Test
    fun `should rra`() {
        cpu.registers.a = 0b10000001.toByte()

        stepWith(0x1f)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0b01000000)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should rr b`() = test(0x18, { cpu.registers.b = it }, { cpu.registers.b })

    @Test
    fun `should rr c`() = test(0x19, { cpu.registers.c = it }, { cpu.registers.c })

    @Test
    fun `should rr d`() = test(0x1a, { cpu.registers.d = it }, { cpu.registers.d })

    @Test
    fun `should rr e`() = test(0x1b, { cpu.registers.e = it }, { cpu.registers.e })

    @Test
    fun `should rr h`() = test(0x1c, { cpu.registers.h = it }, { cpu.registers.h })

    @Test
    fun `should rr l`() = test(0x1d, { cpu.registers.l = it }, { cpu.registers.l })

    @Test
    fun `should rr (hl)`() {
        setupMemory(0xff80, 0b10000001.toByte())
        cpu.registers.hl = 0xff80

        stepWith(0xcb, 0x1e)

        verify { bus.write(0xff80, 0b01000000) }
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(16)
    }

    @Test
    fun `should rr a`() = test(0x1f, { cpu.registers.a = it }, { cpu.registers.a })

    private fun test(opcode: Byte, set: (Byte) -> Unit, get: () -> Byte) {
        set(0b10000001.toByte())

        stepWith(0xcb, opcode)

        assertThat(get().asInt()).isEqualTo(0b01000000)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(8)
    }
}
