package io.github.corzani.nep

import io.github.corzani.nep.ppu.registers.AddrRegister
import org.junit.Test
import kotlin.test.assertEquals

internal class PpuTest {

    @Test
    fun `Update PPU Address Register`() {
        AddrRegister(0x0102u, true)
            .update(0x05u)
            .also {
                assertEquals(AddrRegister(0x0502u, false), it)
            }.update(0x07u).also {
                assertEquals(AddrRegister(0x0507u, true), it)
            }
        AddrRegister(0x0102u, true)
            .update(0xFFu)
            .also {
                assertEquals(AddrRegister(0x3F02u, false), it)
            }

    }

}