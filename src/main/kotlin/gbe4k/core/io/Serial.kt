package gbe4k.core.io

import gbe4k.core.Addressable
import gbe4k.core.Cpu.Companion.hex
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class Serial(private val out: OutputStream = ByteArrayOutputStream()) : Addressable {
    override fun get(address: Int): Byte = when (address) {
        SB -> 0x00
        SC -> 0x00
        else -> throw IllegalArgumentException("${address.hex()} is not a serial i/o address")
    }

    override fun set(address: Int, value: Byte) {
        when (address) {
            SB -> out.write(value.toInt())
            SC -> {}
            else -> throw IllegalArgumentException("${address.hex()} is not a serial i/o address")
        }
    }

    companion object {
        const val SB = 0xff01
        const val SC = 0xff02
    }
}
