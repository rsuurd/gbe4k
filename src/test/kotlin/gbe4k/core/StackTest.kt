package gbe4k.core

import gbe4k.core.Register.AF
import gbe4k.core.Register.BC
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(MockKExtension::class)
class StackTest {
    @MockK
    private lateinit var bus: Bus

    private val registers = Registers()

    private lateinit var stack: Stack

    @BeforeEach
    fun `create stack`() {
        stack = Stack(bus, registers)
    }

    @Test
    fun `should push value on the stack`() {
        every { bus.write(any(), any()) } just runs

        registers.sp = 0x3000

        stack.push(0xffaa)

        verify {
            bus.write(0x2fff, 0xff.toByte())
            bus.write(0x2ffe, 0xaa.toByte())
        }
    }

    @Test
    fun `should pop value from the stack`() {
        every { bus.read(any()) }.returnsMany(0x1a, 0x12)

        registers.sp = 0x3000

        val result = stack.pop()

        assertThat(registers.sp).isEqualTo(0x3002)
        assertThat(result).isEqualTo(0x121a)
    }

    @ParameterizedTest
    @EnumSource(names = ["BC", "DE", "HL", "AF"])
    fun `should push register on the stack`(register: Register) {
        every { bus.write(any(), any()) } just runs

        registers[register] = 0x1f00
        registers.sp = 0x3000

        stack.push(register)

        verify {
            bus.write(0x2fff, 0x1f.toByte())
            bus.write(0x2ffe, 0x00.toByte())
        }
    }

    @Test
    fun `should pop value to register`() {
        every { bus.read(any()) }.returnsMany( 0x1a, 0x12)

        registers.sp = 0x3000

        stack.pop(BC)

        assertThat(registers.sp).isEqualTo(0x3002)
        assertThat(registers.bc).isEqualTo(0x121a)
    }
}