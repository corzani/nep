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

        test(nesRomWithHeader(lo, hi, lo, hi, lo, hi)) {
            x = regX
            y = regY

            absolute(this).let { (fetched) ->
                assertMemoryEquals(expectedAddress, fetched)
            }
            absoluteX(this).let { (fetched) ->
                assertMemoryEquals(u16(expectedAddress + regX), fetched)
            }
            absoluteY(this).let { (fetched) ->
                assertMemoryEquals(u16(expectedAddress + regY), fetched)
            }
        }
    }

    @Test
    fun `Zero Page (with X & Y)`() {
        val regX: U8 = 0x10u
        val regY: U8 = 0xABu
        val expectedAddress: U8 = 0xF0u

        test(nesRomWithHeader(expectedAddress, expectedAddress, expectedAddress)) {
            x = regX
            y = regY

            zeroPage(this).let { (fetched) ->
                assertMemoryEquals(u16(expectedAddress), fetched)
            }

            zeroPageX(this).let { (fetched) ->
                assertMemoryEquals(u16(expectedAddress + regX), fetched)
            }

            zeroPageY(this).let { (fetched) ->
                assertMemoryEquals(u16(expectedAddress + regY), fetched)
            }
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

        test(nesRomWithHeader(lo, lo)) {
            x = regX
            y = regY

            bus.write16(u16(lo), memory)

            indirectX(this).let { (fetched) ->
                assertMemoryEquals(expectedAddressIndirectX(regX), fetched)
            }

            indirectY(this).let { (fetched) ->
                assertMemoryEquals(u16(memory + regY), fetched)
            }
        }
    }
}