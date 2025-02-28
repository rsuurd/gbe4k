package gbe4k.core.instructions.control

import gbe4k.core.Cpu.Companion.hex

class Rst(address: Int) : Call(address) {
    override fun toString(): String = "REST ${address.hex()}"
}
