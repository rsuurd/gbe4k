package gbe4k.core

import gbe4k.core.Bus.Companion.CART_DATA
import gbe4k.core.Bus.Companion.HRAM
import gbe4k.core.Bus.Companion.IO
import gbe4k.core.Bus.Companion.VRAM
import gbe4k.core.Bus.Companion.WRAM
import gbe4k.core.Oam.Companion.OAM
import gbe4k.core.io.Io
import gbe4k.core.io.Timer
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BusTest {
    @MockK
    private lateinit var cart: Cart

    @MockK
    private lateinit var io: Io

    @MockK
    private lateinit var timer: Timer

    @InjectMockKs
    private lateinit var bus: Bus

    @BeforeEach
    fun `mock io`() {
        every { io.timer } returns timer
        every { timer.cycle(any()) } just runs
    }

    @Test
    fun `should read from cart`() {
        every { cart[any()] } returns 0x00

        for (address in CART_DATA) {
            bus.read(address)
        }

        verify(exactly = 0x8000) { cart[any()] }
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
    fun `should write & read wram`() {
        for (address in WRAM) {
            val value = 0xa4.toByte()

            bus.write(address, value)
            assertThat(bus.read(address)).isEqualTo(value)
        }
    }

    @Test
    fun `should write & read hram`() {
        for (address in HRAM) {
            val value = 0xa4.toByte()

            bus.write(address, value)
            assertThat(bus.read(address)).isEqualTo(value)
        }
    }

    @Test
    fun `should should write & rad oam`() {
        for (address in OAM) {
            bus.write(address, 0x0a)

            assertThat(bus.read(address)).isEqualTo(0x0a)
        }
    }

    @Test
    fun `should read io`() {
        every { io[any()] } returns 0x4a

        for (address in IO) {
            assertThat(bus.read(address)).isEqualTo(0x4a)
        }

        verify(exactly = 0x80) { io[any()] }
    }

    @Test
    fun `should write io`() {
        every { io[any()] = any() } just runs

        for (address in IO) {
            assertThat(bus.write(address, 0x4a))
        }

        verify(exactly = 0x80) { io[any()] = 0x4a }
    }

    @Test
    fun `reading should cycle`() {
        every { cart[any()] } returns 0x00

        bus.read(0x000)

        verify { timer.cycle(4) }
    }

    @Test
    fun `writing should cycle`() {
        bus.write(WRAM.first, 0x00)

        verify { timer.cycle(4) }
    }
}