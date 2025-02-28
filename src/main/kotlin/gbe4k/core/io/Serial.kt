package gbe4k.core.io

import gbe4k.core.Addressable
import gbe4k.core.Cpu.Companion.hex
import java.io.ByteArrayOutputStream

class Serial : Addressable {
    private val bytes = ByteArrayOutputStream()

    override fun get(address: Int): Byte = when (address) {
        SB -> bytes.toByteArray().lastOrNull() ?: 0x00
        SC -> 0x00
        else -> throw IllegalArgumentException("${address.hex()} is not a serial i/o address")
    }

    override fun set(address: Int, value: Byte) {
        when (address) {
            SB -> bytes.write(value.toInt())
            SC -> {}
            else -> throw IllegalArgumentException("${address.hex()} is not a serial i/o address")
        }
    }

    fun clear() {
        bytes.reset()
    }

    val contents: String get() = String(bytes.toByteArray())

    companion object {
        const val SB = 0xff01
        const val SC = 0xff02
    }
}
