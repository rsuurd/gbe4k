package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Register

class Dec : Base {
    constructor(register: Register) : super(register)
    constructor(address: Int) : super(address)

    override fun calculate(value: Byte) = value.dec()
    override fun calculate(value: Int) = value.dec()

    override fun setFlags(newValue: Byte, previousValue: Byte, cpu: Cpu) {
        cpu.flags.z = newValue.isZero()
        cpu.flags.n = true

        // TODO calculate half-carry
        // cpu.flags.h = false
    }
}
