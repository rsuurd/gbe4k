package gbe4k.core.mappers

import java.nio.file.Path

class Mbc3(data: ByteArray, ramSize: Int = 0, battery: Boolean = false, path: Path? = null) : Mbc(data, ramSize, battery, path) {
    override fun selectMode(value: Byte) {
        // TODO clock stuff
    }
}
