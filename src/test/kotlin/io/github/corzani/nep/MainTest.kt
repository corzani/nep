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
    fun `Mem Madness()`() {
        val nesArch = mem(
            0x20u,
            0x06u,
            0x06u,
            0x20u,
            0x38u,
            0x06u,
            0x20u,
            0x0du,
            0x06u,
            0x20u,
            0x2au,
            0x06u,
            0x60u,
            0xa9u,
            0x02u,
            0x85u,
            0x02u,
            0xa9u,
            0x04u,
            0x85u,
            0x03u,
            0xa9u,
            0x11u,
            0x85u,
            0x10u,
            0xa9u,
            0x10u,
            0x85u,
            0x12u,
            0xa9u,
            0x0fu,
            0x85u,
            0x14u,
            0xa9u,
            0x04u,
            0x85u,
            0x11u,
            0x85u,
            0x13u,
            0x85u,
            0x15u,
            0x60u,
            0xa5u,
            0xfeu,
            0x85u,
            0x00u,
            0xa5u,
            0xfeu,
            0x29u,
            0x03u,
            0x18u,
            0x69u,
            0x02u,
            0x85u,
            0x01u,
            0x60u,
            0x20u,
            0x4du,
            0x06u,
            0x20u,
            0x8du,
            0x06u,
            0x20u,
            0xc3u,
            0x06u,
            0x20u,
            0x19u,
            0x07u,
            0x20u,
            0x20u,
            0x07u,
            0x20u,
            0x2du,
            0x07u,
            0x4cu,
            0x38u,
            0x06u,
            0xa5u,
            0xffu,
            0xc9u,
            0x77u,
            0xf0u,
            0x0du,
            0xc9u,
            0x64u,
            0xf0u,
            0x14u,
            0xc9u,
            0x73u,
            0xf0u,
            0x1bu,
            0xc9u,
            0x61u,
            0xf0u,
            0x22u,
            0x60u,
            0xa9u,
            0x04u,
            0x24u,
            0x02u,
            0xd0u,
            0x26u,
            0xa9u,
            0x01u,
            0x85u,
            0x02u,
            0x60u,
            0xa9u,
            0x08u,
            0x24u,
            0x02u,
            0xd0u,
            0x1bu,
            0xa9u,
            0x02u,
            0x85u,
            0x02u,
            0x60u,
            0xa9u,
            0x01u,
            0x24u,
            0x02u,
            0xd0u,
            0x10u,
            0xa9u,
            0x04u,
            0x85u,
            0x02u,
            0x60u,
            0xa9u,
            0x02u,
            0x24u,
            0x02u,
            0xd0u,
            0x05u,
            0xa9u,
            0x08u,
            0x85u,
            0x02u,
            0x60u,
            0x60u,
            0x20u,
            0x94u,
            0x06u,
            0x20u,
            0xa8u,
            0x06u,
            0x60u,
            0xa5u,
            0x00u,
            0xc5u,
            0x10u,
            0xd0u,
            0x0du,
            0xa5u,
            0x01u,
            0xc5u,
            0x11u,
            0xd0u,
            0x07u,
            0xe6u,
            0x03u,
            0xe6u,
            0x03u,
            0x20u,
            0x2au,
            0x06u,
            0x60u,
            0xa2u,
            0x02u,
            0xb5u,
            0x10u,
            0xc5u,
            0x10u,
            0xd0u,
            0x06u,
            0xb5u,
            0x11u,
            0xc5u,
            0x11u,
            0xf0u,
            0x09u,
            0xe8u,
            0xe8u,
            0xe4u,
            0x03u,
            0xf0u,
            0x06u,
            0x4cu,
            0xaau,
            0x06u,
            0x4cu,
            0x35u,
            0x07u,
            0x60u,
            0xa6u,
            0x03u,
            0xcau,
            0x8au,
            0xb5u,
            0x10u,
            0x95u,
            0x12u,
            0xcau,
            0x10u,
            0xf9u,
            0xa5u,
            0x02u,
            0x4au,
            0xb0u,
            0x09u,
            0x4au,
            0xb0u,
            0x19u,
            0x4au,
            0xb0u,
            0x1fu,
            0x4au,
            0xb0u,
            0x2fu,
            0xa5u,
            0x10u,
            0x38u,
            0xe9u,
            0x20u,
            0x85u,
            0x10u,
            0x90u,
            0x01u,
            0x60u,
            0xc6u,
            0x11u,
            0xa9u,
            0x01u,
            0xc5u,
            0x11u,
            0xf0u,
            0x28u,
            0x60u,
            0xe6u,
            0x10u,
            0xa9u,
            0x1fu,
            0x24u,
            0x10u,
            0xf0u,
            0x1fu,
            0x60u,
            0xa5u,
            0x10u,
            0x18u,
            0x69u,
            0x20u,
            0x85u,
            0x10u,
            0xb0u,
            0x01u,
            0x60u,
            0xe6u,
            0x11u,
            0xa9u,
            0x06u,
            0xc5u,
            0x11u,
            0xf0u,
            0x0cu,
            0x60u,
            0xc6u,
            0x10u,
            0xa5u,
            0x10u,
            0x29u,
            0x1fu,
            0xc9u,
            0x1fu,
            0xf0u,
            0x01u,
            0x60u,
            0x4cu,
            0x35u,
            0x07u,
            0xa0u,
            0x00u,
            0xa5u,
            0xfeu,
            0x91u,
            0x00u,
            0x60u,
            0xa6u,
            0x03u,
            0xa9u,
            0x00u,
            0x81u,
            0x10u,
            0xa2u,
            0x00u,
            0xa9u,
            0x01u,
            0x81u,
            0x10u,
            0x60u,
            0xa2u,
            0x00u,
            0xeau,
            0xeau,
            0xcau,
            0xd0u,
            0xfbu,
            0x60u
        ).let(::loadFromMemory)

        nesArch.runTest()
    }

    @Test
    fun `Test 1()`() = test(
        mem(0xa9u, 0xc0u, 0xaau, 0xe8u, 0x00u)
    ) {
        runTest()
        assertMemoryEquals(x, 0xc1u)
    }

    @Test
    fun `Test 2()`() = test(
        mem(0xe8u, 0xe8u, 0x00u)
    ) {
        x = 0xFFu
        runTest()
        assertMemoryEquals(x, 0x01u)
    }

    @Test
    fun `Test 3()`() =
        assertEquals(ubyteArrayOf(0u, 1u, 2u, 3u, 4u, 5u).slice(IntRange(0, 3)), ubyteArrayOf(1u).toList())
}