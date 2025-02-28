package gbe4k.core.io

import gbe4k.core.io.Io.Companion.INTERRUPT_ENABLE
import gbe4k.core.io.Io.Companion.INTERRUPT_FLAG
import gbe4k.core.io.Io.Companion.LCD
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
class IoTest {
    @MockK
    private lateinit var serial: Serial

    @MockK
    private lateinit var lcd: Lcd

    @MockK
    private lateinit var interrupts: Interrupts

    @InjectMockKs
    private lateinit var io: Io

    @Test
    fun `should read lcd`() {
        every { lcd[any()] } returns 0x00

        for (address in LCD) {
            assertThat(lcd[address]).isEqualTo(0x00)
        }

        verify(exactly = 0xc) { lcd[any()] }
    }

    @Test
    fun `should write lcd`() {
        every { lcd[any()] = any() } just runs

        for (address in LCD) {
            lcd[address] = 0x00
        }

        verify(exactly = 0xc) { lcd[any()] = 0x00 }
    }

    @Test
    fun `should read interrupt enable`() {
        every { interrupts.ie } returns 0x0f

        assertThat(io[INTERRUPT_ENABLE]).isEqualTo(0x0f)
    }

    @Test
    fun `should write interrupt enable`() {
        every { interrupts.ie = 0xf } just runs

        io[INTERRUPT_ENABLE] = 0xf

        verify { interrupts.ie = 0xf }
    }

    @Test
    fun `should read interrupt flags`() {
        every { interrupts.`if` } returns 0x0f

        assertThat(io[INTERRUPT_FLAG]).isEqualTo(0x0f)
    }

    @Test
    fun `should write interrupt flags`() {
        every { interrupts.`if` = 0xf } just runs

        io[INTERRUPT_FLAG] = 0xf

        verify { interrupts.`if` = 0xf }
    }
}
