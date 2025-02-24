package gbe4k.core.instructions.arithmetic

import gbe4k.core.Cpu
import gbe4k.core.Register

class Inc : Base {
    constructor(register: Register) : super(register)
    constructor(address: Int) : super(address)

    override fun calculate(value: Byte) = value.inc()
    override fun calculate(value: Int) = value.inc()

    override fun setFlags(newValue: Byte, previousValue: Byte, cpu: Cpu) {
        cpu.flags.z = newValue.isZero()
        cpu.flags.n = false

        // TODO calculate half-carry
        // cpu.flags.h = false
    }
}
