package gbe4k.core

interface Addressable {
    operator fun get(address: Int): Byte

    operator fun set(address: Int, value: Byte)
}
