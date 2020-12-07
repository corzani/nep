import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
class MainTest {

    @Test
    fun `16 Bit Memory Read`() = assertEquals(46763u, read16(ubyteArrayOf(0xABu, 0xB6u), 0u))

    @Test
    fun `8 Bit Memory Read`() = assertEquals(171u, read(ubyteArrayOf(0xABu, 0xB6u), 0u))

    @Test
    fun `Split 16 Bit Unsigned Short Int to HI and LO set of bits`() = (0xABCDu).toUShort().splitLoHi().run {
        assertEquals(U16Split(0xCDu, 0xABu), this)
    }

    @Test
    fun `Set Flags`() {
        val status = u8(0b00000000)
        flagsOf(status, u8(0b00000000), ::zeroFlag).let { actual ->
            assertEquals(u8(0b00000010), actual)
        }

        flagsOf(status, u8(0b10000000), ::negativeFlag).let { actual ->
            assertEquals(u8(0b10000000), actual)
        }
    }

    @Test
    fun `U8 & U16 equity`() {
        assert(u8(0) == u8(0))
        assert(u8(10) != u8(0))
    }

    @Test
    fun `Write 16 Bit Unsigned Short Int to Ram`() {
        val ram = ubyteArrayOf(0xABu, 0xB6u, 0x23u, 0xE3u, 0x45u)
        val expectedRam = ubyteArrayOf(0xABu, 0xB6u, 0x12u, 0xF0u, 0x45u)

        write16(ram, 2u, 0xF012u)
        assertEquals(expectedRam.toList(), ram.toList())
    }
}