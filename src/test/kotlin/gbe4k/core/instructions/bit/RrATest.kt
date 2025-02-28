package gbe4k.core.instructions.bit

import gbe4k.core.Bus
import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.hex
import gbe4k.core.io.Interrupts
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RrATest {
    @Test
    fun x() {
        val bus = mockk<Bus>()
        val interupts = Interrupts()
        val cpu = Cpu(bus, interupts)

        cpu.registers.a = 0x00
        cpu.flags.c = false

        RrA.execute(cpu)

        assertThat(cpu.registers.a.hex()).isEqualTo(0x00.toByte().hex())
        assertThat(cpu.flags.z).isTrue()
    }
}