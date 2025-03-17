package gbe4k.core

class Ram(private val range: IntRange) : Iterable<Byte> {
    private val data = ByteArray((range.last - range.first) + 1)

    operator fun get(address: Int) = data[address.index()]
    operator fun set(address: Int, value: Byte) {
        data[address.index()] = value
    }

    private fun Int.index() = this - range.first
    override fun iterator(): Iterator<Byte> = data.iterator()

    fun copyFrom(bytes: ByteArray) = bytes.copyInto(data)
}
