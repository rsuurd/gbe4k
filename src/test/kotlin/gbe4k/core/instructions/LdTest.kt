package gbe4k.core.instructions

import gbe4k.core.Cpu.Companion.asInt
import gbe4k.core.CpuTestSupport
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LdTest : CpuTestSupport() {
    @Test
    fun `should ld bc, d16`() {
        stepWith(0x01, 0x32, 0xaf)

        assertThat(cpu.registers.bc).isEqualTo(0xaf32)
        assertThat(cpu.timer.div).isEqualTo(12)
    }

    @Test
    fun `should ld de, d16`() {
        stepWith(0x11, 0x32, 0xaf)

        assertThat(cpu.registers.de).isEqualTo(0xaf32)
        assertThat(cpu.timer.div).isEqualTo(12)
    }

    @Test
    fun `should ld hl, d16`() {
        stepWith(0x21, 0x32, 0xaf)

        assertThat(cpu.registers.hl).isEqualTo(0xaf32)
        assertThat(cpu.timer.div).isEqualTo(12)
    }

    @Test
    fun `should ld sp, d16`() {
        stepWith(0x31, 0x32, 0xaf)

        assertThat(cpu.registers.sp).isEqualTo(0xaf32)
        assertThat(cpu.timer.div).isEqualTo(12)
    }

    @Test
    fun `should ld (bc), a`() {
        cpu.registers.a = 0x23
        cpu.registers.bc = 0x343a

        stepWith(0x02)

        verify { cpu.bus.write(0x343a, 0x23) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld (de), a`() {
        cpu.registers.a = 0x23
        cpu.registers.de = 0x343a

        stepWith(0x12)

        verify { cpu.bus.write(0x343a, 0x23) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld (hl++), a`() {
        cpu.registers.a = 0x23
        cpu.registers.hl = 0x343a

        stepWith(0x22)

        verify { cpu.bus.write(0x343a, 0x23) }
        assertThat(cpu.registers.hl).isEqualTo(0x343b)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld (hl--), a`() {
        cpu.registers.a = 0x23
        cpu.registers.hl = 0x343a

        stepWith(0x32)

        verify { cpu.bus.write(0x343a, 0x23) }
        assertThat(cpu.registers.hl).isEqualTo(0x3439)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld b, d8`() {
        stepWith(0x06, 0xa3)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0xa3)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld d, d8`() {
        stepWith(0x16, 0xa3)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0xa3)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld h, d8`() {
        stepWith(0x26, 0xa3)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0xa3)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld (hl), d8`() {
        cpu.registers.hl = 0xa3ff
        stepWith(0x36, 0xa3)

        verify { cpu.bus.write(0xa3ff, 0xa3.toByte()) }
        assertThat(cpu.timer.div).isEqualTo(12)
    }

    @Test
    fun `shoud ld (a16), sp`() {
        cpu.registers.sp = 0xffef

        stepWith(0x08, 0x00, 0x30)

        verify {
            cpu.bus.write(0x3000, 0xef.toByte())
            cpu.bus.write(0x3001, 0xff.toByte())
        }
        assertThat(cpu.timer.div).isEqualTo(20)
    }

    @Test
    fun `should ld a, (bc)`() {
        cpu.registers.bc = 0x3543

        stepWith(0x0a, 0x0b)

        verify { cpu.bus.read(0x3543) }
        assertThat(cpu.registers.a.asInt()).isEqualTo(0x0b)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld a, (de)`() {
        cpu.registers.de = 0x3543

        stepWith(0x1a, 0x0b)

        verify { cpu.bus.read(0x3543) }
        assertThat(cpu.registers.a.asInt()).isEqualTo(0x0b)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld a, (hl++)`() {
        cpu.registers.hl = 0x3543

        stepWith(0x2a, 0x0b)

        verify { cpu.bus.read(0x3543) }
        assertThat(cpu.registers.a.asInt()).isEqualTo(0x0b)
        assertThat(cpu.registers.hl).isEqualTo(0x3544)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld a, (hl--)`() {
        cpu.registers.hl = 0x3543

        stepWith(0x3a, 0x0b)

        verify { cpu.bus.read(0x3543) }
        assertThat(cpu.registers.a.asInt()).isEqualTo(0x0b)
        assertThat(cpu.registers.hl).isEqualTo(0x3542)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld c, d8`() {
        stepWith(0x0e, 0x99)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x99)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld e, d8`() {
        stepWith(0x1e, 0x99)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x99)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld l, d8`() {
        stepWith(0x2e, 0x99)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x99)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld a, d8`() {
        stepWith(0x3e, 0x99)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0x99)
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld b, b`() {
        cpu.registers.b = 0x11

        stepWith(0x40)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld b, c`() {
        cpu.registers.c = 0x11

        stepWith(0x41)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld b, d`() {
        cpu.registers.d = 0x11

        stepWith(0x42)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld b, e`() {
        cpu.registers.e = 0x11

        stepWith(0x43)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld b, h`() {
        cpu.registers.h = 0x11

        stepWith(0x44)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld b, l`() {
        cpu.registers.l = 0x11

        stepWith(0x45)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld b, (hl)`() {
        setupMemory(0xff80, 0x11)
        cpu.registers.hl = 0xff80

        stepWith(0x46)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x11)
        verify { bus.read(0xff80) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld b, a`() {
        cpu.registers.a = 0x11

        stepWith(0x47)

        assertThat(cpu.registers.b.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld c, b`() {
        cpu.registers.b = 0x11

        stepWith(0x48)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld c, c`() {
        cpu.registers.c = 0x11

        stepWith(0x49)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld c, d`() {
        cpu.registers.d = 0x11

        stepWith(0x4a)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld c, e`() {
        cpu.registers.e = 0x11

        stepWith(0x4b)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld c, h`() {
        cpu.registers.h = 0x11

        stepWith(0x4c)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld c, l`() {
        cpu.registers.l = 0x11

        stepWith(0x4d)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld c, (hl)`() {
        setupMemory(0xff80, 0x11)
        cpu.registers.hl = 0xff80

        stepWith(0x4e)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x11)
        verify { bus.read(0xff80) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld c, a`() {
        cpu.registers.a = 0x11

        stepWith(0x4f)

        assertThat(cpu.registers.c.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld d, b`() {
        cpu.registers.b = 0x11

        stepWith(0x50)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld d, c`() {
        cpu.registers.c = 0x11

        stepWith(0x51)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld d, d`() {
        cpu.registers.d = 0x11

        stepWith(0x52)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld d, e`() {
        cpu.registers.e = 0x11

        stepWith(0x53)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld d, h`() {
        cpu.registers.h = 0x11

        stepWith(0x54)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld d, l`() {
        cpu.registers.l = 0x11

        stepWith(0x55)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld d, (hl)`() {
        setupMemory(0xff80, 0x11)
        cpu.registers.hl = 0xff80

        stepWith(0x56, 0x11)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0x11)
        verify { bus.read(0xff80) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld d, a`() {
        cpu.registers.a = 0x11

        stepWith(0x57)

        assertThat(cpu.registers.d.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld e, b`() {
        cpu.registers.b = 0x11

        stepWith(0x58)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld e, c`() {
        cpu.registers.c = 0x11

        stepWith(0x59)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld e, d`() {
        cpu.registers.d = 0x11

        stepWith(0x5a)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld e, e`() {
        cpu.registers.e = 0x11

        stepWith(0x5b)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld e, h`() {
        cpu.registers.h = 0x11

        stepWith(0x5c)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld e, l`() {
        cpu.registers.l = 0x11

        stepWith(0x5d)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld e, (hl)`() {
        setupMemory(0xff80, 0x11)
        cpu.registers.hl = 0xff80

        stepWith(0x5e, 0x11)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x11)
        verify { bus.read(0xff80) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld e, a`() {
        cpu.registers.a = 0x11

        stepWith(0x5f)

        assertThat(cpu.registers.e.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld h, b`() {
        cpu.registers.b = 0x11

        stepWith(0x60)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld h, c`() {
        cpu.registers.c = 0x11

        stepWith(0x61)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld h, d`() {
        cpu.registers.d = 0x11

        stepWith(0x62)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld h, e`() {
        cpu.registers.e = 0x11

        stepWith(0x63)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld h, h`() {
        cpu.registers.h = 0x11

        stepWith(0x64)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld h, l`() {
        cpu.registers.l = 0x11

        stepWith(0x65)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld h, (hl)`() {
        setupMemory(0xff80, 0x11)
        cpu.registers.hl = 0xff80

        stepWith(0x66, 0x11)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0x11)
        verify { bus.read(0xff80) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld h, a`() {
        cpu.registers.a = 0x11

        stepWith(0x67)

        assertThat(cpu.registers.h.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld l, b`() {
        cpu.registers.b = 0x11

        stepWith(0x68)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld l, c`() {
        cpu.registers.c = 0x11

        stepWith(0x69)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld l, d`() {
        cpu.registers.d = 0x11

        stepWith(0x6a)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld l, e`() {
        cpu.registers.e = 0x11

        stepWith(0x6b)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld l, h`() {
        cpu.registers.h = 0x11

        stepWith(0x6c)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld l, l`() {
        cpu.registers.l = 0x11

        stepWith(0x6d)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld l, (hl)`() {
        setupMemory(0xff80, 0x11)
        cpu.registers.hl = 0xff80

        stepWith(0x6e)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x11)
        verify { bus.read(0xff80) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld l, a`() {
        cpu.registers.a = 0x11

        stepWith(0x6f)

        assertThat(cpu.registers.l.asInt()).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should (hl), b`() {
        cpu.registers.b = 0xc3.toByte()
        cpu.registers.hl = 0xffef

        stepWith(0x70)

        verify { bus.write(0xffef, 0xc3.toByte()) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should (hl), c`() {
        cpu.registers.c = 0xc3.toByte()
        cpu.registers.hl = 0xffef

        stepWith(0x71)

        verify { bus.write(0xffef, 0xc3.toByte()) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should (hl), d`() {
        cpu.registers.d = 0xc3.toByte()
        cpu.registers.hl = 0xffef

        stepWith(0x72)

        verify { bus.write(0xffef, 0xc3.toByte()) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should (hl), e`() {
        cpu.registers.e = 0xc3.toByte()
        cpu.registers.hl = 0xffef

        stepWith(0x73)

        verify { bus.write(0xffef, 0xc3.toByte()) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should (hl), h`() {
        cpu.registers.hl = 0xffef

        stepWith(0x74)

        verify { bus.write(0xffef, 0xff.toByte()) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should (hl), l`() {
        cpu.registers.hl = 0xffef

        stepWith(0x75)

        verify { bus.write(0xffef, 0xef.toByte()) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should (hl), a`() {
        cpu.registers.a = 0x11
        cpu.registers.hl = 0xffef

        stepWith(0x77)

        verify { bus.write(0xffef, 0x11) }
        assertThat(cpu.timer.div).isEqualTo(8)
    }

    @Test
    fun `should ld a, b`() {
        cpu.registers.b = 0x11

        stepWith(0x78)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld a, c`() {
        cpu.registers.c = 0x11

        stepWith(0x79)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld a, d`() {
        cpu.registers.d = 0x11

        stepWith(0x7a)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld a, e`() {
        cpu.registers.e = 0x11

        stepWith(0x7b)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld a, h`() {
        cpu.registers.h = 0x11

        stepWith(0x7c)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld a, l`() {
        cpu.registers.l = 0x11

        stepWith(0x7d)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ld a, (hl)`() {
        cpu.registers.hl = 0x6a0f

        stepWith(0x7e, 0x11)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(8)
        verify { bus.read(0x6a0f) }
    }

    @Test
    fun `should ld a, a`() {
        cpu.registers.a = 0x11

        stepWith(0x7f)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(4)
    }

    @Test
    fun `should ldh (a8), a`() {
        cpu.registers.a = 0x11

        stepWith(0xe0, 0x44)

        assertThat(cpu.timer.div).isEqualTo(12)
        verify { bus.write(0xff44, 0x11) }
    }

    @Test
    fun `should ldh a, (a8)`() {
        setupMemory(0xff80, 0x11)
        stepWith(0xf0, 0x80)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(12)
        verify { bus.read(0xff80) }
    }

    @Test
    fun `should ld (c), a`() {
        cpu.registers.a = 0x22
        cpu.registers.c = 0x00

        stepWith(0xe2)

        assertThat(cpu.timer.div).isEqualTo(8)
        verify { bus.write(0xff00, 0x22) }
    }

    @Test
    fun `should ldh a, (c)`() {
        setupMemory(0xff80, 0x11)
        cpu.registers.c = 0x80.toByte()

        stepWith(0xf2)

        assertThat(cpu.registers.a).isEqualTo(0x11)
        assertThat(cpu.timer.div).isEqualTo(8)
        verify { bus.read(0xff80) }
    }

    @Test
    fun `should ld (a16), a`() {
        cpu.registers.a = 0xaa.toByte()

        stepWith(0xea, 0x33, 0x66)

        assertThat(cpu.timer.div).isEqualTo(16)
        verify { bus.write(0x6633, 0xaa.toByte()) }
    }

    @Test
    fun `should ld a, (a16)`() {
        stepWith(0xfa, 0x33, 0x66, 0xaa)

        assertThat(cpu.registers.a.asInt()).isEqualTo(0xaa)
        assertThat(cpu.timer.div).isEqualTo(16)
        verify { bus.read(0x6633) }
    }

    @Test
    fun `should ld hl, sp+r8`() {
        cpu.registers.sp = 0xffef

        stepWith(0xf8, 0x9)

        assertThat(cpu.registers.hl).isEqualTo(0xfff8)
        assertThat(cpu.flags.z).isFalse()
        assertThat(cpu.flags.n).isFalse()
        assertThat(cpu.flags.h).isTrue()
        assertThat(cpu.flags.c).isFalse()
        assertThat(cpu.timer.div).isEqualTo(12)
    }

    @Test
    fun `should ld sp, hl`() {
        cpu.registers.hl = 0xffef

        stepWith(0xf9)

        assertThat(cpu.registers.sp).isEqualTo(0xffef)
        assertThat(cpu.timer.div).isEqualTo(8)
    }
}
