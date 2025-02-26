package gbe4k.core

import gbe4k.core.Interrupts.Interrupt.VBLANK
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class InterruptsTest : CpuTestSupport() {
    @Test
    fun `should indicate no waiting interrupts`() {
        val interrupt = interrupts.handle(cpu)

        assertThat(interrupt).isFalse()
        assertThat(cpu.pc).isEqualTo(0x100)
    }

    @ParameterizedTest
    @EnumSource
    fun `should indicate pending interrupt and call`(interrupt: Interrupts.Interrupt) {
        every { bus.write(any(), any()) } just runs
        interrupts.ime = true
        interrupts.ie = 0x1f
        interrupts.dispatch(interrupt)

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
        interrupts.dispatch(interrupt)

        val pending = interrupts.handle(cpu)

        assertThat(interrupts.ime).isFalse()
        assertThat(pending).isTrue()
        assertThat(interrupts.`if`).isEqualTo((1 shl interrupt.ordinal).toByte())
        assertThat(cpu.pc).isEqualTo(0x100)
    }

    @ParameterizedTest
    @EnumSource
    fun `should not indicate pending interrupt if disabled`(interrupt: Interrupts.Interrupt) {
        interrupts.ime = true
        interrupts.ie = 0x0
        interrupts.dispatch(interrupt)

        val pending = interrupts.handle(cpu)

        assertThat(interrupts.ime).isTrue()
        assertThat(pending).isFalse()
        assertThat(interrupts.`if`).isEqualTo((1 shl interrupt.ordinal).toByte())
        assertThat(cpu.pc).isEqualTo(0x100)
    }

    @Test
    fun `should prioritize interrupts in order`() {
        every { bus.write(any(), any()) } just runs
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
        every { bus.write(any(), any()) } just runs
        cpu.halted = true
        interrupts.ime = true
        interrupts.ie = 0x1f
        interrupts.dispatch(VBLANK)

        cpu.step()

        assertThat(cpu.halted).isFalse()
    }

    @Test
    fun `should wake up cpu when halted and interrupt pending even if ime is disabled`() {
        every { bus.write(any(), any()) } just runs
        cpu.halted = true
        interrupts.ime = false
        interrupts.ie = 0x1f
        interrupts.dispatch(VBLANK)

        cpu.step()

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
        interrupts.dispatch(VBLANK)

        cpu.step()

        assertThat(cpu.halted).isTrue()
    }
}
