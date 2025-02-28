package gbe4k.core

import gbe4k.core.io.Interrupts
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
abstract class CpuTestSupport {
    @MockK
    protected lateinit var bus: Bus

    protected lateinit var interrupts: Interrupts

    protected lateinit var cpu: Cpu

    @BeforeEach
    fun `create cpu`() {
        interrupts = Interrupts()

        cpu = Cpu(bus, interrupts)

        // reset flags for tests
        cpu.registers.af = 0
        cpu.registers.bc = 0
        cpu.registers.de = 0
        cpu.registers.hl = 0
    }

    private fun withBytes(vararg bytes: Number, block: () -> Unit) {
        every { bus.read(any()) }.returnsMany(bytes.toList().map { it.toByte() })

        block()

        verify { bus.read(any())  }
    }

    protected fun stepWith(vararg bytes: Number) {
        every { bus.write(any(), any()) } just runs

        withBytes(*bytes) {
            cpu.step()
        }
    }
}
