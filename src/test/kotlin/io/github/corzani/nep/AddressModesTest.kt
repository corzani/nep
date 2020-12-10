package io.github.corzani.nep

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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

        memoryOf(lo, hi)
            .let(::loadFromMemory)
            .run {
                x = regX
                y = regY

                assertEquals(expectedAddress, absolute(this))
                assertEquals(u16(expectedAddress + regX), absoluteX(this))
                assertEquals(u16(expectedAddress + regY), absoluteY(this))
            }
    }

    @Test
    fun `Zero Page (with X & Y)`() {
        val regX: U8 = 0x10u
        val regY: U8 = 0xABu
        val expectedAddress: U8 = 0xF0u

        memoryOf(expectedAddress)
            .let(::loadFromMemory)
            .run {
                x = regX
                y = regY

                assertEquals(u16(expectedAddress), zeroPage(this))
                assertEquals(u16(expectedAddress + regX), zeroPageX(this))
                assertEquals(u16(expectedAddress + regY), zeroPageY(this))
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

        memoryOf(lo, hi)
            .let(::loadFromMemory)
            .run {
                x = regX
                y = regY

                write16(u16(lo), memory)

                assertEquals(expectedAddressIndirectX(regX), indirectX(this))
                assertEquals(u16(memory + regY), indirectY(this))
            }
    }
}