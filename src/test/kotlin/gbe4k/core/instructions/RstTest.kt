package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RstTest : CpuTestSupport() {
    @Test
    fun `should rst 00h`() {
        cpu.registers.sp = 0x3fff
        stepWith(0xc7)

        verifyCpu(0x00)
    }

    @Test
    fun `should rst 10h`() {
        cpu.registers.sp = 0x3fff
        stepWith(0xd7)

        verifyCpu(0x10)
    }

    @Test
    fun `should rst 20h`() {
        cpu.registers.sp = 0x3fff
        stepWith(0xe7)

        verifyCpu(0x20)
    }

    @Test
    fun `should rst 30h`() {
        cpu.registers.sp = 0x3fff
        stepWith(0xf7)

        verifyCpu(0x30)
    }

    @Test
    fun `should rst 08h`() {
        cpu.registers.sp = 0x3fff

        stepWith(0xcf)

        verifyCpu(0x08)
    }

    @Test
    fun `should rst 18h`() {
        cpu.registers.sp = 0x3fff

        stepWith(0xdf)

        verifyCpu(0x18)
    }

    @Test
    fun `should rst 28h`() {
        cpu.registers.sp = 0x3fff

        stepWith(0xef)

        verifyCpu(0x28)
    }

    @Test
    fun `should rst 38h`() {
        cpu.registers.sp = 0x3fff

        stepWith(0xff)

        verifyCpu(0x38)
    }

    private fun verifyCpu(expectedPc: Int) {
        assertThat(cpu.pc).isEqualTo(expectedPc)
        assertThat(cpu.registers.sp).isEqualTo(0x3ffd)

        verify {
            bus.write(0x3ffe, 0x01)
            bus.write(0x3ffd, 0x01)
        }
    }
}

/*
object Rst00 : Call(0x00)
object Rst08 : Call(0x08)
object Rst10 : Call(0x10)
object Rst18 : Call(0x18)
object Rst20 : Call(0x20)
object Rst28 : Call(0x28)
object Rst30 : Call(0x30)
object Rst38 : Call(0x38)

 */