package io.github.corzani.nep

import kotlin.test.Test
import kotlin.test.assertEquals
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
        assertEquals(u8(0b00000010), flagsOf(status, u8(0b00000000), ::zeroFlag))

        assertEquals(u8(0b10000000), flagsOf(status, u8(0b10000000), ::negativeFlag))
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
    fun `Write 16 Bit Unsigned Short Int to Ram`() {
        val ram = mem(0xABu, 0xB6u, 0x23u, 0xE3u, 0x45u)
        val expectedRam = mem(0xABu, 0xB6u, 0x12u, 0xF0u, 0x45u)

        write16(ram, 2u, 0xF012u)
        assertEquals(expectedRam.toList(), ram.toList())
    }

    @Test
    fun `Test 1()`() = test(
        nesRomWithHeader(0xa9u, 0xc0u, 0xaau, 0xe8u)
    ) {
        runTest()
        assertMemoryEquals(x, 0xc1u)
    }

    @Test
    fun `Test 2()`() = test(
        nesRomWithHeader(0xe8u, 0xe8u)
    ) {
        x = 0xFFu
        runTest()
        assertMemoryEquals(x, 0x01u)
    }

    @Test
    fun `Test 4`() {
        assertEquals(
            ubyteArrayOf(0u, 5u, 10u, 15u, 20u).toList(),
            (ubyteArrayOf(0u, 5u, 10u) + ubyteArrayOf(15u, 20u)).toList()
        )

    }
}