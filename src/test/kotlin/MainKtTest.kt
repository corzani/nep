import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
class MainKtTest {

    @Test
    fun `16 Bit Memory Read`() = assertEquals(46763u, read16(ubyteArrayOf(0xABu, 0xB6u), 0u))

    @Test
    fun `8 Bit Memory Read`() = assertEquals(171u, read(ubyteArrayOf(0xABu, 0xB6u), 0u))

}