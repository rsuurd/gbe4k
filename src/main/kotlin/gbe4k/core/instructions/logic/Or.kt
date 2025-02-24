package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.arithmetic.Base.Companion.isZero
import kotlin.experimental.or

class Or : BaseLogic {
    constructor(register: Register) : super(register)
    constructor(address: Int) : super(address)
    constructor(value: Byte) : super(value)

    override fun logic(a: Byte, b: Byte) = a.or(b)

    override fun setFlags(newValue: Byte, previousValue: Byte, cpu: Cpu) {
        cpu.flags.z = newValue.isZero()
        cpu.flags.n = false
        cpu.flags.h = false
        cpu.flags.c = false
    }
}