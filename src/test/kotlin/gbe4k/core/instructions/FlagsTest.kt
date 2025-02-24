package gbe4k.core.instructions

import gbe4k.core.Registers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class FlagsTest {
    private val flags = Flags(Registers())

    @ParameterizedTest
    @ArgumentsSource(FlagsProvider::class)
    fun `setting zero flag should not change other flags`(n: Boolean, h: Boolean, c: Boolean) {
        setupFlags(false, n, h, c)

        flags.z = true
        assertFlags(true, n, h, c)


        flags.z = false
        assertFlags(false, n, h, c)
    }

    @ParameterizedTest
    @ArgumentsSource(FlagsProvider::class)
    fun `setting negative flag should not change other flags`(z: Boolean, h: Boolean, c: Boolean) {
        setupFlags(z, false, h, c)

        flags.n = true
        assertFlags(z, true, h, c)


        flags.n = false
        assertFlags(z, false, h, c)
    }

    @ParameterizedTest
    @ArgumentsSource(FlagsProvider::class)
    fun `setting half carry flag should not change other flags`(z: Boolean, n: Boolean, c: Boolean) {
        setupFlags(z, n, false, c)

        flags.h = true
        assertFlags(z, n, true, c)


        flags.h = false
        assertFlags(z, n, false, c)
    }

    @ParameterizedTest
    @ArgumentsSource(FlagsProvider::class)
    fun `setting carry flag should not change other flags`(z: Boolean, n: Boolean, h: Boolean) {
        setupFlags(z, n, h, false)

        flags.c = true
        assertFlags(z, n, h, true)


        flags.c = false
        assertFlags(z, n, h, false)
    }

    private fun setupFlags(z: Boolean, n: Boolean, h: Boolean, c: Boolean) {
        flags.z = z
        flags.n = n
        flags.h = h
        flags.c = c
    }

    private fun assertFlags(z: Boolean, n: Boolean, h: Boolean, c: Boolean) {
        assertThat(flags.z).isEqualTo(z)
        assertThat(flags.n).isEqualTo(n)
        assertThat(flags.h).isEqualTo(h)
        assertThat(flags.c).isEqualTo(c)
    }

    class FlagsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return listOf(true, false).flatMap { a ->
                listOf(true, false).flatMap { b ->
                    listOf(true, false).map { c ->
                        Arguments.of(a, b, c)
                    }
                }
            }.stream()
        }
    }
}
