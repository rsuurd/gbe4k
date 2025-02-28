package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import gbe4k.core.Register.AF
import gbe4k.core.Register.BC
import gbe4k.core.Register.DE
import gbe4k.core.Register.HL
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PushTest : CpuTestSupport() {
    @Test
    fun `should push BC`() {
        checkRegisterPushed(BC, 0xc5)
    }

    @Test
    fun `should push DE`() {
        checkRegisterPushed(DE, 0xd5)
    }

    @Test
    fun `should push HL`() {
        checkRegisterPushed(HL, 0xe5)
    }

    @Test
    fun `should push AF`() {
        checkRegisterPushed(AF, 0xf5)

    }

    private fun checkRegisterPushed(register: Register, opcode: Int) {
        cpu.registers.sp = 0x253c
        cpu.registers[register] = 0x35a0

        stepWith(opcode)

        assertThat(cpu.registers.sp).isEqualTo(0x253a)
        verify {
            bus.write(0x253a, 0xa0.toByte())
            bus.write(0x253b, 0x35)
        }
    }
}