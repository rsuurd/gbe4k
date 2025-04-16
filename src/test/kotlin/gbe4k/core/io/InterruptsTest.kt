package gbe4k.core.io

import gbe4k.core.CpuTestSupport
import gbe4k.core.io.Interrupts.Interrupt.VBLANK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class InterruptsTest : CpuTestSupport() {
    @Test
    fun `should indicate no waiting interrupts`() {
        val interrupt = interrupts.handle(cpu)

        assertThat(interrupt).isFalse()
        assertThat(cpu.pc).isEqualTo(0x0)
    }

    @ParameterizedTest
    @EnumSource
    fun `should indicate pending interrupt and call`(interrupt: Interrupts.Interrupt) {
        interrupts.ime = true
        interrupts.ie = 0x1f
        interrupts.request(interrupt)

        val pending = interrupts.handle(cpu)

        assertThat(pending).isTrue()
        assertThat(interrupts.ime).isFalse()
        assertThat(interrupts.`if`).isEqualTo(0)
        assertThat(cpu.pc).isEqualTo(interrupt.address)
    }

    @ParameterizedTest
    @EnumSource
    fun `should indicate pending interrupt but not call if ime is disabled`(interrupt: Interrupts.Interrupt) {
        interrupts.ime = false
        interrupts.ie = 0x1f
        interrupts.request(interrupt)

        val pending = interrupts.handle(cpu)

        assertThat(interrupts.ime).isFalse()
        assertThat(pending).isTrue()
        assertThat(interrupts.`if`).isEqualTo((1 shl interrupt.ordinal).toByte())
        assertThat(cpu.pc).isEqualTo(0x0)
    }

    @ParameterizedTest
    @EnumSource
    fun `should not indicate pending interrupt if disabled`(interrupt: Interrupts.Interrupt) {
        interrupts.ime = true
        interrupts.ie = 0x0
        interrupts.request(interrupt)

        val pending = interrupts.handle(cpu)

        assertThat(interrupts.ime).isTrue()
        assertThat(pending).isFalse()
        assertThat(interrupts.`if`).isEqualTo((1 shl interrupt.ordinal).toByte())
        assertThat(cpu.pc).isEqualTo(0x0)
    }

    @Test
    fun `should prioritize interrupts in order`() {
        interrupts.`if` = 0x1f
        interrupts.ie = 0x1f

        for (interrupt in Interrupts.Interrupt.entries) {
            interrupts.ime = true
            val pending = interrupts.handle(cpu)

            assertThat(pending).isTrue()
            assertThat(cpu.pc).isEqualTo(interrupt.address)
        }
    }

    @Test
    fun `should wake up cpu when halted and interrupt pending`() {
        cpu.halted = true
        interrupts.ime = true
        interrupts.ie = 0x1f
        interrupts.request(VBLANK)

        stepWith(0x00)

        assertThat(cpu.halted).isFalse()
    }

    @Test
    fun `should wake up cpu when halted and interrupt pending even if ime is disabled`() {
        cpu.halted = true
        interrupts.ime = false
        interrupts.ie = 0x1f
        interrupts.request(VBLANK)

        stepWith(0x00)

        assertThat(cpu.halted).isFalse()
    }

    @Test
    fun `should not wake up cpu when halted and no interrupt pending`() {
        cpu.halted = true
        interrupts.ime = true
        interrupts.ie = 0x1f

        cpu.step()

        assertThat(cpu.halted).isTrue()
    }

    @Test
    fun `should not wake up cpu when halted and interrupt is disabled`() {
        cpu.halted = true
        interrupts.ime = true
        interrupts.ie = 0x00
        interrupts.request(VBLANK)

        cpu.step()

        assertThat(cpu.halted).isTrue()
    }

    @Test
    fun `handling an interrupt should take 20 cycles`() {
        interrupts.`if` = 0x05
        interrupts.ie = 0x1f
        interrupts.ime = true

        val pending = interrupts.handle(cpu)

        assertThat(pending).isTrue()
        assertThat(timer.div).isEqualTo(20)
    }
}
