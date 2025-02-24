package gbe4k.core.instructions

import gbe4k.core.CpuTestSupport
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LdTest : CpuTestSupport() {
    @Test
    fun `should ld bc, d16`() {
        stepWith(0x01, 0x00, 0xaa)

        assertThat(cpu.registers.bc).isEqualTo(0xaa00)
    }

    @Test
    fun `should ld (bc), a`() {
        every { bus.write(any(), any()) } just runs

        cpu.registers.a = 0xaa.toByte()
        cpu.registers.bc = 0xffef

        stepWith(0x02)

        verify { bus.write(0xffef, 0xaa.toByte()) }
    }

    @Test
    fun `should ld b, d8`() {
        stepWith(0x06, 0xaa)

        assertThat(cpu.registers.b).isEqualTo(0xaa.toByte())
    }

    @Test
    fun `should ld (a16), sp`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.sp = 0x0023

        stepWith(0x08, 0xaa, 0x43)

        verify {
            bus.write(0x43aa, 0x00)
            bus.write(0x43ab, 0x23)
        }
    }

    @Test
    fun `should ld a, (bc)`() {
        cpu.registers.bc = 0xa3b0

        stepWith(0x0a, 0x12)

        assertThat(cpu.registers.a).isEqualTo(0x12)

        verify { bus.read(0xa3b0) }
    }

    @Test
    fun `should ld c, d8`() {
        stepWith(0x0e, 0x12)

        assertThat(cpu.registers.c).isEqualTo(0x12)
    }

    @Test
    fun `should ld de, d16`() {
        stepWith(0x11, 0x00, 0xaa)

        assertThat(cpu.registers.de).isEqualTo(0xaa00)
    }

    @Test
    fun `should ld (de), a`() {
        every { bus.write(any(), any()) } just runs

        cpu.registers.a = 0xaa.toByte()
        cpu.registers.de = 0xffef

        stepWith(0x12)

        verify { bus.write(0xffef, 0xaa.toByte()) }
    }

    @Test
    fun `should ld d, d8`() {
        stepWith(0x16, 0xaa)

        assertThat(cpu.registers.d).isEqualTo(0xaa.toByte())
    }

    @Test
    fun `should ld a, (de)`() {
        cpu.registers.de = 0xa3b0

        stepWith(0x1a, 0x12)

        assertThat(cpu.registers.a).isEqualTo(0x12)

        verify { bus.read(0xa3b0) }
    }

    @Test
    fun `should ld e, d8`() {
        stepWith(0x1e, 0x12)

        assertThat(cpu.registers.e).isEqualTo(0x12)
    }

    @Test
    fun `should ld hl, d16`() {
        stepWith(0x21, 0x00, 0xaa)

        assertThat(cpu.registers.hl).isEqualTo(0xaa00)
    }

    @Test
    fun `should ld (hl++), a`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.a = 0x0a
        cpu.registers.hl = 0xa63c

        stepWith(0x22)

        assertThat(cpu.registers.hl).isEqualTo(0xa63d)
        verify { bus.write(0xa63c, 0x0a) }
    }

    @Test
    fun `should ld h, d8`() {
        stepWith(0x26, 0xaa)

        assertThat(cpu.registers.h).isEqualTo(0xaa.toByte())
    }

    @Test
    fun `should ld a, (hl++)`() {
        cpu.registers.hl = 0xf40c

        stepWith(0x2a, 0x64)

        assertThat(cpu.registers.a).isEqualTo(0x64)
        assertThat(cpu.registers.hl).isEqualTo(0xf40d)
        verify { bus.read(0xf40c) }
    }

    @Test
    fun `should ld l, d8`() {
        stepWith(0x2e, 0xaa)

        assertThat(cpu.registers.l).isEqualTo(0xaa.toByte())
    }

    @Test
    fun `should ld sp, d16`() {
        stepWith(0x31, 0xaa, 0x22)

        assertThat(cpu.registers.sp).isEqualTo(0x22aa)
    }

    @Test
    fun `should ld (hl--), a`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.a = 0x0a
        cpu.registers.hl = 0xa63c

        stepWith(0x32)

        assertThat(cpu.registers.hl).isEqualTo(0xa63b)
        verify { bus.write(0xa63c, 0x0a) }
    }

    @Test
    fun `should ld (hl), d8`() {
        every { bus.write(any(), any()) } just runs

        cpu.registers.hl = 0xa63c

        stepWith(0x36, 0x8)

        verify { bus.write(0xa63c, 0x08) }
    }

    @Test
    fun `should ld a, (hl--)`() {
        cpu.registers.hl = 0xa63c

        stepWith(0x3a, 0xa)

        assertThat(cpu.registers.a).isEqualTo(0xa)
        assertThat(cpu.registers.hl).isEqualTo(0xa63b)
    }

    @Test
    fun `should ld a, d8`() {
        cpu.registers.hl = 0xa63c

        stepWith(0x3e, 0xa)

        assertThat(cpu.registers.a).isEqualTo(0xa)
    }

    @Test
    fun `should ld b, b`() {
        cpu.registers.b = 0xb

        stepWith(0x40)

        assertThat(cpu.registers.b).isEqualTo(0xb)
    }

    @Test
    fun `should ld b, c`() {
        cpu.registers.c = 0xc

        stepWith(0x41)

        assertThat(cpu.registers.b).isEqualTo(0xc)
    }

    @Test
    fun `should ld b, d`() {
        cpu.registers.d = 0xd

        stepWith(0x42)

        assertThat(cpu.registers.b).isEqualTo(0xd)
    }

    @Test
    fun `should ld b, e`() {
        cpu.registers.e = 0xe

        stepWith(0x43)

        assertThat(cpu.registers.b).isEqualTo(0xe)
    }

    @Test
    fun `should ld b, h`() {
        cpu.registers.h = 0x1

        stepWith(0x44)

        assertThat(cpu.registers.b).isEqualTo(0x1)
    }

    @Test
    fun `should ld b, l`() {
        cpu.registers.l = 0x2

        stepWith(0x45)

        assertThat(cpu.registers.b).isEqualTo(0x2)
    }

    @Test
    fun `should ld b, (hl)`() {
        cpu.registers.hl = 0x22f8

        stepWith(0x46, 0x03)

        assertThat(cpu.registers.b).isEqualTo(0x3)

        verify { bus.read(0x22f8) }
    }

    @Test
    fun `should ld b, a`() {
        cpu.registers.a = 0xa

        stepWith(0x47)

        assertThat(cpu.registers.b).isEqualTo(0xa)
    }

    @Test
    fun `should ld c, b`() {
        cpu.registers.b = 0xb

        stepWith(0x48)

        assertThat(cpu.registers.c).isEqualTo(0xb)
    }

    @Test
    fun `should ld c, c`() {
        cpu.registers.c = 0xc

        stepWith(0x49)

        assertThat(cpu.registers.c).isEqualTo(0xc)
    }

    @Test
    fun `should ld c, d`() {
        cpu.registers.d = 0xd

        stepWith(0x4a)

        assertThat(cpu.registers.c).isEqualTo(0xd)
    }

    @Test
    fun `should ld c, e`() {
        cpu.registers.e = 0xe

        stepWith(0x4b)

        assertThat(cpu.registers.c).isEqualTo(0xe)
    }

    @Test
    fun `should ld c, h`() {
        cpu.registers.h = 0x1

        stepWith(0x4c)

        assertThat(cpu.registers.c).isEqualTo(0x1)
    }

    @Test
    fun `should ld c, l`() {
        cpu.registers.l = 0x2

        stepWith(0x4d)

        assertThat(cpu.registers.c).isEqualTo(0x2)
    }

    @Test
    fun `should ld c, (hl)`() {
        cpu.registers.hl = 0x22f8

        stepWith(0x4e, 0x03)

        assertThat(cpu.registers.c).isEqualTo(0x3)

        verify { bus.read(0x22f8) }
    }

    @Test
    fun `should ld c, a`() {
        cpu.registers.a = 0xa

        stepWith(0x4f)

        assertThat(cpu.registers.c).isEqualTo(0xa)
    }

    @Test
    fun `should ld d, b`() {
        cpu.registers.b = 0xb

        stepWith(0x50)

        assertThat(cpu.registers.d).isEqualTo(0xb)
    }

    @Test
    fun `should ld d, c`() {
        cpu.registers.c = 0xc

        stepWith(0x51)

        assertThat(cpu.registers.d).isEqualTo(0xc)
    }

    @Test
    fun `should ld d, d`() {
        cpu.registers.d = 0xd

        stepWith(0x52)

        assertThat(cpu.registers.d).isEqualTo(0xd)
    }

    @Test
    fun `should ld d, e`() {
        cpu.registers.e = 0xe

        stepWith(0x53)

        assertThat(cpu.registers.d).isEqualTo(0xe)
    }

    @Test
    fun `should ld d, h`() {
        cpu.registers.h = 0x1

        stepWith(0x54)

        assertThat(cpu.registers.d).isEqualTo(0x1)
    }

    @Test
    fun `should ld d, l`() {
        cpu.registers.l = 0x2

        stepWith(0x55)

        assertThat(cpu.registers.d).isEqualTo(0x2)
    }

    @Test
    fun `should ld d, (hl)`() {
        cpu.registers.hl = 0x22f8

        stepWith(0x56, 0x03)

        assertThat(cpu.registers.d).isEqualTo(0x3)

        verify { bus.read(0x22f8) }
    }

    @Test
    fun `should ld d, a`() {
        cpu.registers.a = 0xa

        stepWith(0x57)

        assertThat(cpu.registers.d).isEqualTo(0xa)
    }


    @Test
    fun `should ld e, b`() {
        cpu.registers.b = 0xb

        stepWith(0x58)

        assertThat(cpu.registers.e).isEqualTo(0xb)
    }

    @Test
    fun `should ld e, c`() {
        cpu.registers.c = 0xc

        stepWith(0x59)

        assertThat(cpu.registers.e).isEqualTo(0xc)
    }

    @Test
    fun `should ld e, d`() {
        cpu.registers.d = 0xd

        stepWith(0x5a)

        assertThat(cpu.registers.e).isEqualTo(0xd)
    }

    @Test
    fun `should ld e, e`() {
        cpu.registers.e = 0xe

        stepWith(0x5b)

        assertThat(cpu.registers.e).isEqualTo(0xe)
    }

    @Test
    fun `should ld e, h`() {
        cpu.registers.h = 0x1

        stepWith(0x5c)

        assertThat(cpu.registers.e).isEqualTo(0x1)
    }

    @Test
    fun `should ld e, l`() {
        cpu.registers.l = 0x2

        stepWith(0x5d)

        assertThat(cpu.registers.e).isEqualTo(0x2)
    }

    @Test
    fun `should ld e, (hl)`() {
        cpu.registers.hl = 0x22f8

        stepWith(0x5e, 0x03)

        assertThat(cpu.registers.e).isEqualTo(0x3)

        verify { bus.read(0x22f8) }
    }

    @Test
    fun `should ld e, a`() {
        cpu.registers.a = 0xa

        stepWith(0x5f)

        assertThat(cpu.registers.e).isEqualTo(0xa)
    }

    @Test
    fun `should ld h, b`() {
        cpu.registers.b = 0xb

        stepWith(0x60)

        assertThat(cpu.registers.h).isEqualTo(0xb)
    }

    @Test
    fun `should ld h, c`() {
        cpu.registers.c = 0xc

        stepWith(0x61)

        assertThat(cpu.registers.h).isEqualTo(0xc)
    }

    @Test
    fun `should ld h, d`() {
        cpu.registers.d = 0xd

        stepWith(0x62)

        assertThat(cpu.registers.h).isEqualTo(0xd)
    }

    @Test
    fun `should ld h, e`() {
        cpu.registers.e = 0xe

        stepWith(0x63)

        assertThat(cpu.registers.h).isEqualTo(0xe)
    }

    @Test
    fun `should ld h, h`() {
        cpu.registers.h = 0x1

        stepWith(0x64)

        assertThat(cpu.registers.h).isEqualTo(0x1)
    }

    @Test
    fun `should ld h, l`() {
        cpu.registers.l = 0x2

        stepWith(0x65)

        assertThat(cpu.registers.h).isEqualTo(0x2)
    }

    @Test
    fun `should ld h, (hl)`() {
        cpu.registers.hl = 0x22f8

        stepWith(0x66, 0x03)

        assertThat(cpu.registers.h).isEqualTo(0x3)

        verify { bus.read(0x22f8) }
    }

    @Test
    fun `should ld h, a`() {
        cpu.registers.a = 0xa

        stepWith(0x67)

        assertThat(cpu.registers.h).isEqualTo(0xa)
    }

    @Test
    fun `should ld l, b`() {
        cpu.registers.b = 0xb

        stepWith(0x68)

        assertThat(cpu.registers.l).isEqualTo(0xb)
    }

    @Test
    fun `should ld l, c`() {
        cpu.registers.c = 0xc

        stepWith(0x69)

        assertThat(cpu.registers.l).isEqualTo(0xc)
    }

    @Test
    fun `should ld l, d`() {
        cpu.registers.d = 0xd

        stepWith(0x6a)

        assertThat(cpu.registers.l).isEqualTo(0xd)
    }

    @Test
    fun `should ld l, e`() {
        cpu.registers.e = 0xe

        stepWith(0x6b)

        assertThat(cpu.registers.l).isEqualTo(0xe)
    }

    @Test
    fun `should ld l, h`() {
        cpu.registers.h = 0x1

        stepWith(0x6c)

        assertThat(cpu.registers.l).isEqualTo(0x1)
    }

    @Test
    fun `should ld l, l`() {
        cpu.registers.l = 0x2

        stepWith(0x6d)

        assertThat(cpu.registers.l).isEqualTo(0x2)
    }

    @Test
    fun `should ld l, (hl)`() {
        cpu.registers.hl = 0x22f8

        stepWith(0x6e, 0x03)

        assertThat(cpu.registers.l).isEqualTo(0x3)

        verify { bus.read(0x22f8) }
    }

    @Test
    fun `should ld l, a`() {
        cpu.registers.a = 0xa

        stepWith(0x6f)

        assertThat(cpu.registers.l).isEqualTo(0xa)
    }

    @Test
    fun `should ld (hl), b`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.b = 0xb
        cpu.registers.hl = 0xffef

        stepWith(0x70)

        verify { bus.write(0xffef, 0xb) }
    }

    @Test
    fun `should ld (hl), c`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.c = 0xc
        cpu.registers.hl = 0xffef

        stepWith(0x71)

        verify { bus.write(0xffef, 0xc) }
    }

    @Test
    fun `should ld (hl), d`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.d = 0xd
        cpu.registers.hl = 0xffef

        stepWith(0x72)

        verify { bus.write(0xffef, 0xd) }
    }

    @Test
    fun `should ld (hl), e`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.e = 0xe
        cpu.registers.hl = 0xffef

        stepWith(0x73)

        verify { bus.write(0xffef, 0xe) }
    }

    @Test
    fun `should ld (hl), h`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.hl = 0xffef

        stepWith(0x74)

        verify { bus.write(0xffef, 0xff.toByte()) }
    }

    @Test
    fun `should ld (hl), l`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.hl = 0xffef

        stepWith(0x75)

        verify { bus.write(0xffef, 0xef.toByte()) }
    }

    @Test
    fun `should ld (hl), a`() {
        every { bus.write(any(), any()) } just runs
        cpu.registers.a = 0xa
        cpu.registers.hl = 0xffef

        stepWith(0x77)

        verify { bus.write(0xffef, 0xa) }
    }

    @Test
    fun `should ld a, b`() {
        cpu.registers.b = 0xb

        stepWith(0x78)

        assertThat(cpu.registers.a).isEqualTo(0xb)
    }

    @Test
    fun `should ld a, c`() {
        cpu.registers.c = 0xc

        stepWith(0x79)

        assertThat(cpu.registers.a).isEqualTo(0xc)
    }

    @Test
    fun `should ld a, d`() {
        cpu.registers.d = 0xd

        stepWith(0x7a)

        assertThat(cpu.registers.a).isEqualTo(0xd)
    }

    @Test
    fun `should ld a, e`() {
        cpu.registers.e = 0xe

        stepWith(0x7b)

        assertThat(cpu.registers.a).isEqualTo(0xe)
    }

    @Test
    fun `should ld a, h`() {
        cpu.registers.h = 0x1

        stepWith(0x7c)

        assertThat(cpu.registers.a).isEqualTo(0x1)
    }

    @Test
    fun `should ld a, l`() {
        cpu.registers.l = 0x2

        stepWith(0x7d)

        assertThat(cpu.registers.a).isEqualTo(0x2)
    }

    @Test
    fun `should ld a, (hl)`() {
        cpu.registers.hl = 0x22f8

        stepWith(0x7e, 0x03)

        assertThat(cpu.registers.a).isEqualTo(0x3)

        verify { bus.read(0x22f8) }
    }

    @Test
    fun `should ld a, a`() {
        cpu.registers.a = 0xa

        stepWith(0x7f)

        assertThat(cpu.registers.a).isEqualTo(0xa)
    }

    @Test
    fun `should ld (a8), a`() {
        every { bus.write(any(), any()) } just runs

        cpu.registers.a = 0xa

        stepWith(0xe0, 0xee)

        verify { bus.write(0xffee, 0x0a) }
    }

    @Test
    fun `should ld (c), a`() {
        every { bus.write(any(), any()) } just runs

        cpu.registers.a = 0xa
        cpu.registers.c = 0xee.toByte()

        stepWith(0xe2)

        verify { bus.write(0xffee, 0x0a) }
    }

    @Test
    fun `should ld (a16), a`() {
        every { bus.write(any(), any()) } just runs

        cpu.registers.a = 0xa

        stepWith(0xea, 0xff, 0xfe)

        verify { bus.write(0xfeff, 0x0a) }
    }

    @Test
    fun `should ld a, (a8)`() {
        stepWith(0xf0, 0x0a, 0x23)

        assertThat(cpu.registers.a).isEqualTo(0x23)

        verify { bus.read(0xff0a) }
    }

    @Test
    fun `should ld a, (c)`() {
        cpu.registers.c = 0x0a

        stepWith(0xf2, 0x23)

        assertThat(cpu.registers.a).isEqualTo(0x23)

        verify { bus.read(0xff0a) }
    }

    @Test
    fun `should ld hl, sp+r8`() {
        cpu.registers.sp = 0x0231

        stepWith(0xf8, 0xaa)

        assertThat(cpu.registers.hl).isEqualTo(0x01db)
    }

    @Test
    fun `should ld sp, hl`() {
        cpu.registers.hl = 0x0231

        stepWith(0xf9)

        assertThat(cpu.registers.sp).isEqualTo(0x0231)
    }

    @Test
    fun `should ld a, (a16)`() {
        stepWith(0xfa, 0xaa, 0xaa, 0x21)

        assertThat(cpu.registers.a).isEqualTo(0x021)

        verify { bus.read(0xaaaa) }
    }
}
