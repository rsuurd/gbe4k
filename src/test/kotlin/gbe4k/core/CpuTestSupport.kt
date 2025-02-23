package gbe4k.core

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
abstract class CpuTestSupport {
    @MockK
    protected lateinit var bus: Bus

    @InjectMockKs
    protected lateinit var cpu: Cpu

    protected fun withBytes(vararg bytes: Number, block: () -> Unit) {
        every { bus.read(any()) }.returnsMany(bytes.toList().map { it.toByte() })

        block()

        verify(exactly = bytes.size) { bus.read(any())  }
    }
}
