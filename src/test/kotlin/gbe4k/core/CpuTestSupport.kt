package gbe4k.core

import gbe4k.core.io.Dma
import gbe4k.core.io.Interrupts
import gbe4k.core.io.Io
import gbe4k.core.io.Joypad
import gbe4k.core.io.Lcd
import gbe4k.core.io.Serial
import gbe4k.core.io.Timer
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
abstract class CpuTestSupport {
    @MockK
    protected lateinit var cart: Cart

    protected lateinit var bus: Bus
    protected lateinit var dma: Dma
    protected lateinit var timer: Timer
    protected lateinit var interrupts: Interrupts

    protected lateinit var cpu: Cpu

    @BeforeEach
    fun `create cpu`() {
        dma = spyk(Dma())
        interrupts = Interrupts()
        timer = spyk(Timer(interrupts))
        timer.div = 0x00
        bus = spyk(Bus(cart, Io(Joypad(), Serial(), timer, Lcd(dma, interrupts), interrupts)))

        cpu = Cpu(bus, timer, interrupts)

        // reset registers for tests
        cpu.registers.af = 0x000
        cpu.registers.bc = 0x000
        cpu.registers.de = 0x000
        cpu.registers.hl = 0x000
    }

    private fun withBytes(vararg bytes: Number, block: () -> Unit) {
        every { cart[any()] }.returnsMany(bytes.toList().map { it.toByte() })

        block()

        verify { cart[any()] }
    }

    protected fun stepWith(vararg bytes: Number) {
        withBytes(*bytes) {
            cpu.step()
        }
    }

    protected fun setupMemory(address: Int, value: Byte) {
        cpu.bus.write(address, value)
        timer.div = 0 // reset the timer
    }

    protected fun read(address: Int) = cpu.bus.read(address).also {
        timer.div = 0
    }
}
