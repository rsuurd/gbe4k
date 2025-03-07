package gbe4k.core.io

import gbe4k.core.Cpu.Companion.asInt
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
        lcd[Lcd.LY] = 0x90.toByte()

        assertThat(lcd.ly.asInt()).isEqualTo(0x90)
        assertThat(lcd[Lcd.LY].asInt()).isEqualTo(0x90)
    }
}