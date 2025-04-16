package gbe4k.core

import gbe4k.core.Bus.Companion.CART_DATA
import gbe4k.core.Bus.Companion.CART_RAM
import gbe4k.core.Bus.Companion.HRAM
import gbe4k.core.Bus.Companion.IO
import gbe4k.core.Bus.Companion.VRAM
import gbe4k.core.Bus.Companion.WRAM
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.Oam.Companion.OAM
import gbe4k.core.boot.BootRom
import gbe4k.core.boot.BootRom.Companion.BOOTROM
import gbe4k.core.boot.BootRom.Companion.BOOTROM_BANK
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
    private lateinit var bootrom: BootRom

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
        every { bootrom.booting } returns false
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
    fun `should read cart ram`() {
        every { cart[any()] } returns 0x00

        for (address in CART_RAM) {
            bus.read(address)
        }

        verify(exactly = 0x2000) { cart[any()] }
    }

    @Test
    fun `should write cart ram`() {
        every { cart[any()] = any() } just runs

        for (address in CART_RAM) {
            bus.write(address, 0xa)
        }

        verify(exactly = 0x2000) { cart[any()] = 0xa }
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

        for (address in IO.filter { it != BOOTROM_BANK }) {
            assertThat(bus.write(address, 0x4a))
        }

        verify(exactly = 0x7f) { io[any()] = 0x4a }
    }

    @Test
    fun `should read from boot rom while booting`() {
        every { bootrom.booting } returns true
        every { bootrom[any()] } returns 0x3

        for (address in BOOTROM) {
            assertThat(bus.read(address)).isEqualTo(0x3)
        }

        verify(exactly = 0x100) { bootrom[any()] }
    }

    @Test
    fun `should write to boot rom bank`() {
        every { bootrom[BOOTROM_BANK] = any() } just runs

        bus.write(BOOTROM_BANK, 1)

        verify(exactly = 1) { bootrom[BOOTROM_BANK] = 1}
    }

    @Test
    fun `should read from cart when boot is complete`() {
        every { bootrom.booting } returns false
        every { cart[any()] } returns 0xc

        for (address in BOOTROM) {
            assertThat(bus.read(address)).isEqualTo(0xc)
        }

        verify(exactly = 0) { bootrom[any()] }
        verify(exactly = 0x100) { cart[any()] }
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