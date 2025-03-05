package gbe4k.core.io

import gbe4k.core.Cpu
import gbe4k.core.Cpu.Companion.isBitSet
import gbe4k.core.Cpu.Companion.setBit

class Interrupts(var `if`: Byte = 0, var ie: Byte = 0) {
    var ime = false
    var enableIme = false

    fun handle(cpu: Cpu): Boolean {
        if (enableIme) {
            ime = true
            enableIme = false
        }

        return Interrupt.entries.firstOrNull { interrupt ->
            ie.isEnabled(interrupt) && `if`.isRequested(interrupt)
        }?.let { interrupt ->
            if (ime) {
                ime = false
                `if` = `if`.setBit(false, interrupt.ordinal)

                // TODO cpu.call() (see JumpInstructions)
                cpu.stack.push(cpu.pc)
                cpu.pc = interrupt.address
                cpu.cycle(12)
            }
        } != null
    }

    fun request(interrupt: Interrupt) {
        `if` = `if`.setBit(true, interrupt.ordinal)
    }

    private fun Byte.isEnabled(interrupt: Interrupt) = isBitSet(interrupt.ordinal)
    private fun Byte.isRequested(interrupt: Interrupt) = isBitSet(interrupt.ordinal)

    enum class Interrupt(val address: Int) {
        VBLANK(0x40),
        STAT(0x48),
        TIMER(0x50),
        SERIAL(0x58),
        JOYPAD(0x60),
    }
}
