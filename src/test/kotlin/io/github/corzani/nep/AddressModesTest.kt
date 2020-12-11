package io.github.corzani.nep

import kotlin.test.Test

internal class AddressModesTest {

    @Test
    fun immediate() {
    }

    @Test
    fun implied() {
    }

    @Test
    fun relative() {
    }

    @Test
    fun indirect() {
    }

    @Test
    fun `Absolute (with X & Y)`() {
        val regX: U8 = 0x10u
        val regY: U8 = 0xABu
        val hi: U8 = 0x10u
        val lo: U8 = 0xF0u
        val expectedAddress = fromLoHi(lo = lo, hi = hi)

        loadFromMemory(mem(lo, hi)) {
            x = regX
            y = regY

            assertMemoryEquals(expectedAddress, absolute(this).fetched)
            assertMemoryEquals(u16(expectedAddress + regX), absoluteX(this).fetched)
            assertMemoryEquals(u16(expectedAddress + regY), absoluteY(this).fetched)
        }
    }

    @Test
    fun `Zero Page (with X & Y)`() {
        val regX: U8 = 0x10u
        val regY: U8 = 0xABu
        val expectedAddress: U8 = 0xF0u

        loadFromMemory(mem(expectedAddress)) {
            x = regX
            y = regY

            assertMemoryEquals(u16(expectedAddress), zeroPage(this).fetched)
            assertMemoryEquals(u16(expectedAddress + regX), zeroPageX(this).fetched)
            assertMemoryEquals(u16(expectedAddress + regY), zeroPageY(this).fetched)
        }
    }

    @Test
    fun `Indirect (X & Y)`() {
        val regX: U8 = 0x02u
        val regY: U8 = 0x05u
        val hi: U8 = 0x01u
        val lo: U8 = 0xC0u
        val expectedAddressIndirectX = { reg: U8 -> fromLoHi(lo = u8(lo + reg), hi = hi) }
        val memory: U16 = fromLoHi(lo, hi)

        loadFromMemory(mem(lo, hi)) {
            x = regX
            y = regY

            write16(u16(lo), memory)

            assertMemoryEquals(expectedAddressIndirectX(regX), indirectX(this).fetched)
            assertMemoryEquals(u16(memory + regY), indirectY(this).fetched)
        }
    }
}