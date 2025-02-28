package gbe4k.core.io

import gbe4k.core.Addressable

class Lcd : Addressable {
    // TODO additional properties
    private var ly: Byte = 0xff.toByte()

    override operator fun get(address: Int) = when (address) {
        LY -> ly
        else -> 0x00
    }

    override operator fun set(address: Int, value: Byte) = when (address) {
        LY -> ly = value
        else -> {}
    }

    companion object {
        const val LY = 0xff44
    }
}
