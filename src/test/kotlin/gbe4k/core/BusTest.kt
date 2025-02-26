package gbe4k.core

import gbe4k.core.Bus.Companion.CART_DATA
import gbe4k.core.Bus.Companion.HRAM
import gbe4k.core.Bus.Companion.INTERRUPT_ENABLE
import gbe4k.core.Bus.Companion.INTERRUPT_FLAG
import gbe4k.core.Bus.Companion.VRAM
import gbe4k.core.Bus.Companion.WRAM
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BusTest {
    @MockK
    private lateinit var cart: Cart

    @MockK
    private lateinit var interrupts: Interrupts

    @InjectMockKs
    private lateinit var bus: Bus

    @Test
    fun `should read from cart`() {
        every { cart.read(any()) } returns 0x00

        for (address in CART_DATA) {
            bus.read(address)
        }

        verify(exactly = 0x8000) { cart.read(any()) }
    }

    @Test
    fun `should write & read vram`() {
        for (address in VRAM) {
            val value = 0xa4.toByte()

            bus.write(address, value)
            assertThat(bus.read(address)).isEqualTo(value)
        }
    }

    @Test
    fun `should  write & read wram`() {
        for (address in WRAM) {
            val value = 0xa4.toByte()

            bus.write(address, value)
            assertThat(bus.read(address)).isEqualTo(value)
        }
    }

    @Test
    fun `should  write & read hram`() {
        for (address in HRAM) {
            val value = 0xa4.toByte()

            bus.write(address, value)
            assertThat(bus.read(address)).isEqualTo(value)
        }
    }

    @Test
    fun `should write interrupt addresses`() {
        every { interrupts.ie = 0xf } just runs
        every { interrupts.`if` = 0xf } just runs

        bus.write(INTERRUPT_ENABLE, 0xf)
        bus.write(INTERRUPT_FLAG, 0xf)

        verify { interrupts.ie = 0xf }
        verify { interrupts.`if` = 0xf }
    }

    @Test
    fun `should read interrupt addresses`() {
        every { interrupts.ie } returns 0xf
        every { interrupts.`if` } returns 0xf

        assertThat(bus.read(INTERRUPT_ENABLE)).isEqualTo(0xf)
        assertThat(bus.read(INTERRUPT_FLAG)).isEqualTo(0xf)
    }
}