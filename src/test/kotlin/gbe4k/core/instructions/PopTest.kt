package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import gbe4k.core.Register
import gbe4k.core.Register.AF
import gbe4k.core.Register.BC
import gbe4k.core.Register.DE
import gbe4k.core.Register.HL
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PopTest : CpuTestSupport() {
    @Test
    fun `should pop BC`() {
        checkRegisterPopped(BC, 0xc1)
    }

    @Test
    fun `should pop DE`() {
        checkRegisterPopped(DE, 0xd1)
    }

    @Test
    fun `should pop HL`() {
        checkRegisterPopped(HL, 0xe1)
    }

    @Test
    fun `should pop AF`() {
        checkRegisterPopped(AF, 0xf1)
    }

    private fun checkRegisterPopped(register: Register, opcode: Int) {
        cpu.registers.sp = 0x253a
        cpu.registers[register] = 0x0000

        stepWith(opcode, 0x0a, 0x35)

        assertThat(cpu.registers.sp).isEqualTo(0x253c)
        assertThat(cpu.registers[register]).isEqualTo(0x350a)
    }
}