package gbe4k.core.mappers

import java.nio.file.Path

class Mbc3(
    data: ByteArray,
    ramSize: Int = 0,
    battery: Boolean = false,
    path: Path? = null
) : Mbc(data, ramSize, battery, path) {
    override fun selectMode(value: Byte) {
        // TODO RTC latch
    }

    override fun readRam(address: Int): Byte {
        // TODO read rtc register?
        return super.readRam(address)
    }

    override fun writeRam(address: Int, value: Byte) {
        // TODO write rtc register?
        super.writeRam(address, value)
    }
}
