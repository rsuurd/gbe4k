package gbe4k.core.instructions.logic

import gbe4k.core.Cpu
import gbe4k.core.Register
import gbe4k.core.instructions.arithmetic.Base.Companion.isZero

class Cp : BaseLogic {
    constructor(register: Register) : super(register)
    constructor(address: Int) : super(address)
    constructor(value: Byte) : super(value)

    override fun logic(acc: Byte, value: Byte) = acc.minus(value).toByte()

    override fun storeResult(result: Byte, cpu: Cpu) {
        // cp does not store the result in A
    }

    override fun setFlags(value: Byte, acc: Byte, cpu: Cpu) {
        cpu.flags.z = value.isZero()
        cpu.flags.n = true
        // cpu.flags.h = ???
        // cpu.flags.c = ???
    }
}