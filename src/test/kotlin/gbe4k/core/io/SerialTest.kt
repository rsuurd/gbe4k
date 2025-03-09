package gbe4k.core.io

import gbe4k.core.io.Serial.Companion.SB
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class SerialTest {
    @Test
    fun `should write to outputstream`() {
        ByteArrayOutputStream().use { out ->
            Serial(out)[SB] = 'C'.code.toByte()

            assertThat(out.toByteArray()).isEqualTo(byteArrayOf(67))
        }
    }
}