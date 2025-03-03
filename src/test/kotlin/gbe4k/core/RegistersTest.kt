package gbe4k.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RegistersTest {
    private val registers = Registers()

    @Test
    fun `should set and get register A`() {
        registers.a = 0x33

        assertThat(registers.a).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register B`() {
        registers.b = 0x33

        assertThat(registers.b).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register C`() {
        registers.c = 0x33

        assertThat(registers.c).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register D`() {
        registers.d = 0x33

        assertThat(registers.d).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register E`() {
        registers.e = 0x33

        assertThat(registers.e).isEqualTo(0x33)
    }

    @Test
    fun `should only set upper 4 bits and get register F`() {
        registers.f = 0x33

        assertThat(registers.f).isEqualTo(0x30)
    }

    @Test
    fun `should set and get register H`() {
        registers.h = 0x33

        assertThat(registers.h).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register L`() {
        registers.l = 0x33

        assertThat(registers.l).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register AF`() {
        registers.af = 0xffff

        assertThat(registers.af).isEqualTo(0xfff0)
        assertThat(registers.a).isEqualTo(0xff.toByte())
        assertThat(registers.f).isEqualTo(0xf0.toByte())
    }

    @Test
    fun `should set and get register BC`() {
        registers.bc = 0x3323

        assertThat(registers.bc).isEqualTo(0x3323)
        assertThat(registers.b).isEqualTo(0x33)
        assertThat(registers.c).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register DE`() {
        registers.de = 0x3323

        assertThat(registers.de).isEqualTo(0x3323)
        assertThat(registers.d).isEqualTo(0x33)
        assertThat(registers.e).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register HL`() {
        registers.hl = 0x3323

        assertThat(registers.hl).isEqualTo(0x3323)
        assertThat(registers.h).isEqualTo(0x33)
        assertThat(registers.l).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register SP`() {
        registers.sp = 0x0001

        assertThat(registers.sp).isEqualTo(0x0001)
    }
}
