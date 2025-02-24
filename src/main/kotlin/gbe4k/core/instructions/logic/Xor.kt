package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.arithmetic.Base.Companion.isZero
import kotlin.experimental.or
import kotlin.experimental.xor

class Xor : BaseLogic {
    constructor(register: Register) : super(register)
    constructor(address: Int) : super(address)
    constructor(value: Byte) : super(value)

    override fun logic(a: Byte, b: Byte) = a.xor(b)

    override fun setFlags(newValue: Byte, previousValue: Byte, cpu: Cpu) {
        cpu.flags.z = newValue.isZero()
        cpu.flags.n = false
        cpu.flags.h = false
        cpu.flags.c = false
    }
}