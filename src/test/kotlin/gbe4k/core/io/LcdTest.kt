package gbe4k.core.io

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Ppu
import gbe4k.core.io.Dma.Companion.DMA_TRANSFER
import gbe4k.core.io.Lcd.Companion.BG_PALETTE
import gbe4k.core.io.Lcd.Companion.OBJ_PALETTE_0
import gbe4k.core.io.Lcd.Companion.OBJ_PALETTE_1
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(MockKExtension::class)
class LcdTest {
    @MockK
    private lateinit var dma: Dma

    @MockK
    private lateinit var interrupts: Interrupts

    @InjectMockKs
    private lateinit var lcd: Lcd

    @Test
    fun `should initiate dma`() {
        every { dma.start(any()) } just runs

        lcd[DMA_TRANSFER] = 0xc0.toByte()

        verify { dma.start(0xc0.toByte()) }
    }

    @Test
    fun `should read ly`() {
        lcd.ly = 0x90

        assertThat(lcd.ly).isEqualTo(0x90)
        assertThat(lcd[Lcd.LY].asInt()).isEqualTo(0x90)
    }

    @ParameterizedTest
    @EnumSource
    fun `should set and get mode`(mode: Ppu.Mode) {
        lcd.stat.lyEqLyc = false
        lcd.stat.ppuMode = mode

        assertThat(lcd.stat.value).isEqualTo(mode.ordinal.toByte())
        assertThat(lcd.stat.ppuMode).isEqualTo(mode)
    }

    @Test
    fun `should set and get ly == lyc`() {
        lcd.stat.lyEqLyc = true

        assertThat(lcd.stat.value).isEqualTo(0b00000100)
    }

    @Test
    fun `should enable selection`() {
        lcd.stat.ppuMode = Ppu.Mode.HBLANK
        lcd.stat.lyEqLyc = false

        lcd.stat.value = 0xff.toByte()

        assertThat(lcd.stat.value).isEqualTo(0b11111000.toByte())
        assertThat(lcd.stat.isSelected(Ppu.Mode.HBLANK)).isTrue()
        assertThat(lcd.stat.isSelected(Ppu.Mode.VBLANK)).isTrue()
        assertThat(lcd.stat.isSelected(Ppu.Mode.OAM_SCAN)).isTrue()
        assertThat(lcd.stat.isSelected(Ppu.Mode.DRAWING)).isFalse()
        assertThat(lcd.stat.lycSelected).isTrue()
    }

    @Test
    fun `changing ly should cause interrupt if requested`() {
        every { interrupts.request(any()) } just runs

        lcd.stat.value = 0b01000000

        lcd.lyc = 3
        lcd.ly = 3

        verify { interrupts.request(Interrupts.Interrupt.STAT) }
    }

    @Test
    fun `should set background palette`() {
        lcd[BG_PALETTE] = 0x12

        assertThat(lcd.bgPalette).isEqualTo(0x12)
    }

    @Test
    fun `should set object palettes`() {
        lcd[OBJ_PALETTE_0] = 0x12
        lcd[OBJ_PALETTE_1] = 0x13

        assertThat(lcd.objPalette0).isEqualTo(0x12)
        assertThat(lcd.objPalette1).isEqualTo(0x13)
    }
}
