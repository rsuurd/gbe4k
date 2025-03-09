package gbe4k.core.io

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.io.Interrupts.Interrupt.TIMER
import gbe4k.core.io.Timer.Companion.DIV
import gbe4k.core.io.Timer.Companion.TAC
import gbe4k.core.io.Timer.Companion.TIMA
import gbe4k.core.io.Timer.Companion.TMA
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
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.absoluteValue

@ExtendWith(MockKExtension::class)
class TimerTest {
    @MockK
    private lateinit var interrupts: Interrupts

    @InjectMockKs
    private lateinit var timer: Timer

    @Test
    fun `should reset div when written`() {
        timer.div = 0x34
        timer[DIV] = 0x42
        assertThat(timer.div).isEqualTo(0x00)
    }

    @ParameterizedTest
    @ValueSource(ints = [TIMA, TMA, TAC])
    fun `should access registers`(address: Int) {
        timer[address] = 0x42

        assertThat(timer[address]).isEqualTo(0x42)
    }

    @Test
    fun `should tick div`() {
        timer.tick()

        assertThat(timer.div).isEqualTo(0xac01)
        assertThat(timer[DIV]).isEqualTo(0x01)
    }

    @ParameterizedTest
    @ValueSource(bytes = [0x04, 0x05, 0x06, 0x07])
    fun `should increment tima`(control: Byte) {
        timer.tac = control

        for (t in 0..timer.frequency) {
            timer.tick()
        }

        assertThat(timer.tima).isEqualTo(0x01)
    }

    @ParameterizedTest
    @ValueSource(bytes = [0x00, 0x01, 0x02, 0x03])
    fun `should not increment tima when disabled`(control: Byte) {
        timer.tac = control

        assertThat(timer.enabled).isFalse()

        for (t in 0..timer.frequency) {
            timer.tick()
        }

        assertThat(timer.tima).isEqualTo(0x00)
    }

    @Test
    fun `should clock select`() {
        val frequencies = listOf(1024, 16, 64, 256)

        frequencies.forEachIndexed { tac, frequency ->
            timer.tac = tac.toByte()

            assertThat(timer.frequency).isEqualTo(frequency)
        }
    }

    @Test
    fun `should request interrupt when tima overflows`() {
        every { interrupts.request(any()) } just runs

        timer.enabled = true
        timer.div = 0x3ff
        timer.tima = 0xff.toByte()

        timer.tick()

        verify { interrupts.request(TIMER) }

        assertThat(timer.tima).isEqualTo(0x00)
        assertThat(timer.div).isEqualTo(0x0400)
    }

    @Test
    fun `should reset tima to tma when tima overflows`() {
        every { interrupts.request(any()) } just runs

        timer.enabled = true
        timer.div = 0xacff
        timer.tma = 0xff.toByte()
        timer.tima = 0xff.toByte()

        timer.tick()

        assertThat(timer.tima.asInt()).isEqualTo(0xff)
    }

    @Test
    fun `should track cycles`() {
        val result = timer.track {
            timer.tick()
        }

        assertThat(result).isEqualTo(1)
    }
}
