package io.github.corzani.nep

import kotlin.test.*

internal class NesArchTest {

    @Test
    fun `Conversion between unsigned and signed`() {
        val u8: U8 = 0xFFu
        val u16: U16 = 0x000Fu
        assertEquals(-1, toSigned(u8))
        assertEquals(u16(0x000E), incrAddress(u16, toSigned(u8)))
    }
}