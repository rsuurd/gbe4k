package gbe4k.core

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
abstract class CpuTestSupport {
    @MockK
    protected lateinit var bus: Bus

    @InjectMockKs
    protected lateinit var cpu: Cpu

    private fun withBytes(vararg bytes: Number, block: () -> Unit) {
        every { bus.read(any()) }.returnsMany(bytes.toList().map { it.toByte() })

        block()

        verify(exactly = bytes.size) { bus.read(any())  }
    }

    protected fun stepWith(vararg bytes: Number) {
        every { bus.write(any(), any()) } just runs

        withBytes(*bytes) {
            cpu.step()
        }
    }
}
