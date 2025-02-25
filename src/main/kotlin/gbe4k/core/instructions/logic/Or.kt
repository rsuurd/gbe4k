package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.arithmetic.Base.Companion.isZero
import kotlin.experimental.or

class Or : BaseLogic {
    constructor(register: Register) : super(register)
    constructor(address: Int) : super(address)
    constructor(value: Byte) : super(value)

    override fun logic(acc: Byte, value: Byte) = acc.or(value)

    override fun setFlags(value: Byte, acc: Byte, cpu: Cpu) {
        cpu.flags.z = value.isZero()
        cpu.flags.n = false
        cpu.flags.h = false
        cpu.flags.c = false
    }
}