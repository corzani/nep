package io.github.corzani.nep

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    fun `U8 & U16 is bit set`() {
        assertTrue(u16(0x0001u).isBitSet(0))
        assertTrue(u8(0b00000001u).isBitSet(0))
        assertTrue(u16(0x0002u).isBitSet(1))
        assertTrue(u8(0b00000100u).isBitSet(2))
    }

    @Test
    fun `Substraction on U16`() {
        println(u16(50) - u16(90))
    }

    @Test
    fun `Write 16 Bit Unsigned Short Int to Ram`() {
        val ram = mem(0xABu, 0xB6u, 0x23u, 0xE3u, 0x45u)
        val expectedRam = mem(0xABu, 0xB6u, 0x12u, 0xF0u, 0x45u)

        write16(ram, 2u, 0xF012u)
        assertEquals(expectedRam.toList(), ram.toList())
    }

}