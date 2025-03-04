package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DaaTest : CpuTestSupport() {
    @Test
    fun `should daa nothing`() {
        stepWith(0x27)

        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should daa carry`() {
        cpu.flags.c = true

        stepWith(0x27)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0x60)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should daa carry if a is over 0x99`() {
        cpu.registers.a = 0xaa.toByte()

        stepWith(0x27)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0x10)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should daa half carry`() {
        cpu.flags.h = true

        stepWith(0x27)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0x6)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should daa with both carry and half carry`() {
        cpu.flags.h = true
        cpu.flags.c = true
        cpu.registers.a = 0xb4.toByte()

        stepWith(0x27)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0x1a)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should daa carry after subtraction`() {
        cpu.flags.n = true
        cpu.flags.c = true
        cpu.registers.a = 0x11

        stepWith(0x27)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0xb1)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isTrue()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should daa half carry after subtraction`() {
        cpu.flags.n = true
        cpu.flags.h = true
        cpu.registers.a = 0x6

        stepWith(0x27)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0x00)
        assertThat(cpu.flags.z).isTrue()
        assertThat(cpu.flags.n).isTrue()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }

    @Test
    fun `should daa half carry if a is over 0x09`() {
        cpu.registers.a = 0x0b

        stepWith(0x27)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0x11)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isFalse()
        assertThat(cpu.flags.c).isFalse()
        assertThat(timer.div).isEqualTo(4)
    }
}