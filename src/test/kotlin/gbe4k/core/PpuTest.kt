package gbe4k.core

import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Cpu.Companion.setBit
import gbe4k.core.io.Dma
import gbe4k.core.io.Interrupts
import gbe4k.core.io.Lcd
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PpuTest {
    @MockK
    private lateinit var bus: Bus

    @MockK
    private lateinit var interrupts: Interrupts

    private lateinit var ppu: Ppu

    @BeforeEach
    fun `create ppu`() {
        every { bus[any()] } returns 0xff.toByte()
        every { bus.oam } returns Oam()
        every { interrupts.request(any()) } just runs

        ppu = Ppu(bus, Lcd(Dma(), interrupts), interrupts)
    }

    @Test
    fun `should scan oam for first 80 dots`() {
        repeat(79) {
            ppu.update(1)

            assertThat(ppu.lcd.stat.ppuMode).isEqualTo(Ppu.Mode.OAM_SCAN)
        }
    }

    @Test
    fun `should draw between dots 80 and 252 `() {
        ppu.update(80)

        repeat(171) {
            ppu.update(1)

            assertThat(ppu.lcd.stat.ppuMode).isEqualTo(Ppu.Mode.DRAWING)
        }
    }

    @Test
    fun `should hblank between dots 289 and 456`() {
        ppu.update(289)

        repeat(166) {
            ppu.update(1)

            assertThat(ppu.lcd.stat.ppuMode).isEqualTo(Ppu.Mode.HBLANK)
        }
    }

    @Test
    fun `should move to the next line`() {
        ppu.update(456)

        assertThat(ppu.lcd.ly).isEqualTo(1)
        assertThat(ppu.lcd.stat.ppuMode).isEqualTo(Ppu.Mode.OAM_SCAN)
    }

    @Test
    fun `should request vblank`() {
        ppu.lcd.ly = 143
        ppu.update(456)

        verify(exactly = 1) { interrupts.request(Interrupts.Interrupt.VBLANK) }
        assertThat(ppu.lcd.stat.ppuMode).isEqualTo(Ppu.Mode.VBLANK)
    }

    @Test
    fun `should inform listeners on vblank once`() {
        val vBlankListener = mockk<Ppu.VBlankListener>()
        every { vBlankListener.onVBlank(any()) } just runs

        ppu.addVBlankListener(vBlankListener)

        for (ly in 143..153) {
            ppu.lcd.ly = ly
            ppu.update(456)
        }

        verify(exactly = 1) { vBlankListener.onVBlank(any()) }
    }

    @Test
    fun `should vblank last 10 lines`() {
        ppu.lcd.ly = 143
        ppu.update(456)

        repeat(4559) {
            ppu.update(1)

            assertThat(ppu.lcd.stat.ppuMode).isEqualTo(Ppu.Mode.VBLANK)
        }

        assertThat(ppu.lcd.ly).isEqualTo(153)
    }

    @Test
    fun `should go back to top after vblank`() {
        ppu.lcd.stat.ppuMode = Ppu.Mode.VBLANK
        ppu.lcd.ly = 153
        ppu.update(456)

        assertThat(ppu.lcd.ly).isEqualTo(0)
        assertThat(ppu.lcd.stat.ppuMode).isEqualTo(Ppu.Mode.OAM_SCAN)
    }

    @Test
    fun `should request stat when switching modes when enabled`() {
        ppu.lcd.stat.value = 0x00.toByte()
            .setBit(true, 3) // select stat for all modes
            .setBit(true, 4)
            .setBit(true, 5)

        repeat(456) {
            ppu.update(1)
        }

        verify(exactly = 3) { interrupts.request(Interrupts.Interrupt.STAT) }
    }

    @Test
    fun `should not request stat when switching modes when disabled`() {
        ppu.lcd.stat.value = 0x00

        repeat(456) {
            ppu.update(1)
        }

        verify(exactly = 0) { interrupts.request(Interrupts.Interrupt.STAT) }
    }

    @Test
    fun `should request stat when ly == lyc when enabled`() {
        ppu.lcd.stat.value = 0x00.toByte().setBit(true, 6)
        ppu.lcd.lyc = 90
        ppu.lcd.ly = 90

        verify(exactly = 1) { interrupts.request(Interrupts.Interrupt.STAT) }
        assertThat(ppu.lcd.stat.lycSelected).isTrue()
    }

    @Test
    fun `should not request stat when ly == lyc when disabled`() {
        ppu.lcd.stat.value = 0x00
        ppu.lcd.ly = 89
        ppu.lcd.lyc = 90

        ppu.update(456)

        verify(exactly = 0) { interrupts.request(Interrupts.Interrupt.VBLANK) }
        assertThat(ppu.lcd.stat.lycSelected).isFalse()
    }
}
