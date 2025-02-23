package gbe4k.core

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BusTest {
    @MockK
    private lateinit var cart: Cart

    @InjectMockKs
    private lateinit var bus: Bus

    @Test
    fun `should read from cart`() {
        every { cart.read(any()) } returns 0x00

        for (address in Bus.CART_DATA) {
            bus.read(address)
        }

        verify(exactly = 0x8000) { cart.read(any()) }
    }
}