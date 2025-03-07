package gbe4k.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OamTest {
    private val oam = Oam()

    @Test
    fun `should create entries from bytes`() {
        assertThat(oam.entries.size).isEqualTo(40)
    }

    @Test
    fun `entry should have properties`() {
        oam[0xfe00] = 0x10
        oam[0xfe01] = 0x14
        oam[0xfe02] = 0x01
        oam[0xfe03] = 0xf0.toByte()

        val entry = oam.entries.first()

        assertThat(entry.x).isEqualTo(0x14)
        assertThat(entry.y).isEqualTo(0x10)
        assertThat(entry.tile).isEqualTo(0x01)
        assertThat(entry.cgbPalette).isEqualTo(0x00)
        assertThat(entry.bank).isEqualTo(0x00)
        assertThat(entry.palette).isEqualTo(0x01)
        assertThat(entry.xFlip).isTrue()
        assertThat(entry.yFlip).isTrue()
        assertThat(entry.priority).isTrue()
    }
}
