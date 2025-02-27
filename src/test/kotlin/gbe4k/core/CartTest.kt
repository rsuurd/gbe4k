package gbe4k.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipInputStream
import kotlin.io.path.exists

class CartTest {
    @BeforeEach
    fun `download test roms`() {
        with(Paths.get(".roms")) {
            if (!exists() || toFile().listFiles()?.isEmpty() == true) {
                ZipInputStream(
                    URI("https://github.com/retrio/gb-test-roms/archive/refs/heads/master.zip").toURL().openStream()
                ).use { zip ->
                    generateSequence { zip.nextEntry }.forEach { entry ->
                        val filename = entry.name.removePrefix("gb-test-roms-master/")

                        if (entry.isDirectory) {
                            Files.createDirectories(resolve(filename))
                        } else {
                            Files.write(resolve(filename), zip.readAllBytes())
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `should load cart`() {
        val cart = Cart.load(Paths.get(".roms/cpu_instrs/cpu_instrs.gb"))

        assertThat(cart.title).isEqualTo("CPU_INSTRS")
        assertThat(cart.size).isEqualTo(64)
        assertThat(cart[0x0000]).isEqualTo(0x3c)
    }
}
