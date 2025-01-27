package io.github.corzani.nep

import kotlin.test.Test
import kotlin.test.assertEquals

internal class CpuTest {

    @Test
    fun `Conversion between unsigned and signed`() {
        val u8: U8 = 0xFFu
        val u16: U16 = 0x000Fu
        assertEquals(-1, toSigned(u8))
        assertEquals(u16(0x000E), incrementAddress(u16, toSigned(u8)))
    }

    @Test
    fun `Subtraction clue`() {
        val u8: U8 = 0x00u
        val temp = u16(u8 - 1u)
        assertEquals(0xFF, temp.lo8().toInt())

        var u8Var: U8 = 0x00u
        --u8Var
        assertEquals(0xFFu, u8Var)
    }
}