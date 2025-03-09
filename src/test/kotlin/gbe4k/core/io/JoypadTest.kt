package gbe4k.core.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JoypadTest {
    private val joypad = Joypad()

    @Test
    fun `should indicate nothing pressed when no mode selected`() {
        joypad.start = true
        joypad.select = true
        joypad.a = true
        joypad.b = true
        joypad.up = true
        joypad.down = true
        joypad.left = true
        joypad.right = true

        joypad.value = 0b00110000

        assertThat(joypad.value).isEqualTo(0b00111111)
    }

    @Test
    fun `should indicate buttons pressed`() {
        joypad.start = true
        joypad.select = true
        joypad.a = true
        joypad.b = true

        // select button mode
        joypad.value = 0b00010000

        assertThat(joypad.value).isEqualTo(0b00010000)
    }

    @Test
    fun `should indicate buttons not pressed`() {
        // select button mode
        joypad.value = 0b00010000

        assertThat(joypad.value).isEqualTo(0b00011111)
    }

    @Test
    fun `should indicate dpad pressed`() {
        joypad.up = true
        joypad.down = true
        joypad.left = true
        joypad.right = true

        // select dpad mode
        joypad.value = 0b00100000

        assertThat(joypad.value).isEqualTo(0b00100000)
    }

    @Test
    fun `should indicate buttons are not pressed`() {
        // select dpad mode
        joypad.value = 0b00100000

        assertThat(joypad.value).isEqualTo(0b00101111)
    }
}