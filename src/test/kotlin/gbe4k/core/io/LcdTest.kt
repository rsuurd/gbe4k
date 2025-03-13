package gbe4k.core.io

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.Ppu
import gbe4k.core.io.Dma.Companion.DMA_TRANSFER
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
}
