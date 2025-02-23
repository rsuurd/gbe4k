package gbe4k.core

import gbe4k.core.Register.A
import gbe4k.core.Register.AF
import gbe4k.core.Register.B
import gbe4k.core.Register.BC
import gbe4k.core.Register.C
import gbe4k.core.Register.D
import gbe4k.core.Register.DE
import gbe4k.core.Register.E
import gbe4k.core.Register.F
import gbe4k.core.Register.H
import gbe4k.core.Register.HL
import gbe4k.core.Register.L
import gbe4k.core.Register.SP
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class RegistersTest {
    private val registers = Registers()

    @Test
    fun `should set and get register A`() {
        registers.a = 0x33

        assertThat(registers.a).isEqualTo(0x33)
        assertThat(registers[A]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register A by name`() {
        registers[A] = 0x33

        assertThat(registers.a).isEqualTo(0x33)
        assertThat(registers[A]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register B`() {
        registers.b = 0x33

        assertThat(registers.b).isEqualTo(0x33)
        assertThat(registers[B]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register B by name`() {
        registers[B] = 0x33

        assertThat(registers.b).isEqualTo(0x33)
        assertThat(registers[B]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register C`() {
        registers.c = 0x33

        assertThat(registers.c).isEqualTo(0x33)
        assertThat(registers[C]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register C by name`() {
        registers[C] = 0x33

        assertThat(registers.c).isEqualTo(0x33)
        assertThat(registers[C]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register D`() {
        registers.d = 0x33

        assertThat(registers.d).isEqualTo(0x33)
        assertThat(registers[D]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register D by name`() {
        registers[D] = 0x33

        assertThat(registers.d).isEqualTo(0x33)
        assertThat(registers[D]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register E`() {
        registers.e = 0x33

        assertThat(registers.e).isEqualTo(0x33)
        assertThat(registers[E]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register E by name`() {
        registers[E] = 0x33

        assertThat(registers.e).isEqualTo(0x33)
        assertThat(registers[E]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register F`() {
        registers.f = 0x33

        assertThat(registers.f).isEqualTo(0x33)
        assertThat(registers[F]).isEqualTo(0x33)
    }

    @Test
    fun `should not set and get register F by name`() {
        registers[F] = 0x33

        assertThat(registers.f).isEqualTo(0x33)
        assertThat(registers[F]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register H`() {
        registers.h = 0x33

        assertThat(registers.h).isEqualTo(0x33)
        assertThat(registers[H]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register H by name`() {
        registers[H] = 0x33

        assertThat(registers.h).isEqualTo(0x33)
        assertThat(registers[H]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register L`() {
        registers.l = 0x33

        assertThat(registers.l).isEqualTo(0x33)
        assertThat(registers[L]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register L by name`() {
        registers[L] = 0x33

        assertThat(registers.l).isEqualTo(0x33)
        assertThat(registers[L]).isEqualTo(0x33)
    }

    @Test
    fun `should set and get register AF`() {
        registers.af = 0x3323

        assertThat(registers.af).isEqualTo(0x3323)
        assertThat(registers[AF]).isEqualTo(0x3323)
        assertThat(registers.a).isEqualTo(0x33)
        assertThat(registers.f).isEqualTo(0x23)
        assertThat(registers[A]).isEqualTo(0x33)
        assertThat(registers[F]).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register AF by name`() {
        registers[AF] = 0x3323

        assertThat(registers.af).isEqualTo(0x3323)
        assertThat(registers[AF]).isEqualTo(0x3323)
        assertThat(registers.a).isEqualTo(0x33)
        assertThat(registers.f).isEqualTo(0x23)
        assertThat(registers[A]).isEqualTo(0x33)
        assertThat(registers[F]).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register BC`() {
        registers.bc = 0x3323

        assertThat(registers.bc).isEqualTo(0x3323)
        assertThat(registers[BC]).isEqualTo(0x3323)
        assertThat(registers.b).isEqualTo(0x33)
        assertThat(registers.c).isEqualTo(0x23)
        assertThat(registers[B]).isEqualTo(0x33)
        assertThat(registers[C]).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register BC by name`() {
        registers[BC] = 0x3323

        assertThat(registers.bc).isEqualTo(0x3323)
        assertThat(registers[BC]).isEqualTo(0x3323)
        assertThat(registers.b).isEqualTo(0x33)
        assertThat(registers.c).isEqualTo(0x23)
        assertThat(registers[B]).isEqualTo(0x33)
        assertThat(registers[C]).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register DE`() {
        registers.de = 0x3323

        assertThat(registers.de).isEqualTo(0x3323)
        assertThat(registers[DE]).isEqualTo(0x3323)
        assertThat(registers.d).isEqualTo(0x33)
        assertThat(registers.e).isEqualTo(0x23)
        assertThat(registers[D]).isEqualTo(0x33)
        assertThat(registers[E]).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register DE by name`() {
        registers[DE] = 0x3323

        assertThat(registers.de).isEqualTo(0x3323)
        assertThat(registers[DE]).isEqualTo(0x3323)
        assertThat(registers.d).isEqualTo(0x33)
        assertThat(registers.e).isEqualTo(0x23)
        assertThat(registers[D]).isEqualTo(0x33)
        assertThat(registers[E]).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register HL`() {
        registers.hl = 0x3323

        assertThat(registers.hl).isEqualTo(0x3323)
        assertThat(registers[HL]).isEqualTo(0x3323)
        assertThat(registers.h).isEqualTo(0x33)
        assertThat(registers.l).isEqualTo(0x23)
        assertThat(registers[H]).isEqualTo(0x33)
        assertThat(registers[L]).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register HL by name`() {
        registers[HL] = 0x3323

        assertThat(registers.hl).isEqualTo(0x3323)
        assertThat(registers[HL]).isEqualTo(0x3323)
        assertThat(registers.h).isEqualTo(0x33)
        assertThat(registers.l).isEqualTo(0x23)
        assertThat(registers[H]).isEqualTo(0x33)
        assertThat(registers[L]).isEqualTo(0x23)
    }

    @Test
    fun `should set and get register SP`() {
        registers.sp = 0x0001

        assertThat(registers.sp).isEqualTo(0x0001)
        assertThat(registers[SP]).isEqualTo(0x0001)
    }

    @Test
    fun `should set and get register SP by name`() {
        registers[SP] = 0x0001

        assertThat(registers.sp).isEqualTo(0x0001)
        assertThat(registers[SP]).isEqualTo(0x0001)
    }
}