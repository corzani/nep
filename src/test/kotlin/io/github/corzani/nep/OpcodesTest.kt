package io.github.corzani.nep

import kotlin.test.*

internal class OpcodesTest {

    @Test
    fun adc() {}

    @Test
    fun and() = mem(0x29u, 0b10101010u)
        .let(::loadFromMemory)
        .run {
            accumulator = 0b10001101u
            runTest()

            assertEquals(u8(0b10101010u and 0b10001101u), accumulator)
            assertEquals(statusWith(Flag.N), status)
        }

    @Test
    fun asl() {
    }

    @Test
    fun bcc() {
    }

    @Test
    fun bcs() {
    }

    @Test
    fun beq() {
    }

    @Test
    fun bit() {
    }

    @Test
    fun bmi() {
    }

    @Test
    fun bne() {
    }

    @Test
    fun bpl() {
    }

    @Test
    fun brk() {
    }

    @Test
    fun bvc() {
    }

    @Test
    fun bvs() {
    }

    @Test
    fun clc() {
    }

    @Test
    fun cld() {
    }

    @Test
    fun cli() {
    }

    @Test
    fun clv() {
    }

    @Test
    fun cmp() {
    }

    @Test
    fun cpx() {
    }

    @Test
    fun cpy() {
    }

    @Test
    fun dec() {
    }

    @Test
    fun dex() {
    }

    @Test
    fun dey() {
    }

    @Test
    fun eor() {
    }

    @Test
    fun inc() {
    }

    @Test
    fun inx() {
    }

    @Test
    fun iny() {
    }

    @Test
    fun jmp() {
    }

    @Test
    fun jsr() {
    }

    @Test
    fun `LDA from memory`() = mem(0xa5u, 0x10u)
        .let(::loadFromMemory)
        .run {
            write(0x10u, 0xF5u)
            runTest()

            assertEquals(0xF5u, accumulator)
            assertEquals(statusWith(Flag.N), status)
        }

    @Test
    fun ldx() {
    }

    @Test
    fun ldy() {
    }

    @Test
    fun lsr() {
    }

    @Test
    fun nop() {
    }

    @Test
    fun ora() {
    }

    @Test
    fun pha() {
    }

    @Test
    fun php() {
    }

    @Test
    fun pla() {
    }

    @Test
    fun plp() {
    }

    @Test
    fun rol() {
    }

    @Test
    fun ror() {
    }

    @Test
    fun rti() {
    }

    @Test
    fun rts() {
    }

    @Test
    fun sbc() {
    }

    @Test
    fun sec() {
    }

    @Test
    fun sed() {
    }

    @Test
    fun sei() {
    }

    @Test
    fun sta() {
    }

    @Test
    fun stx() {
    }

    @Test
    fun sty() {
    }

    @Test
    fun tsx() {
    }

    @Test
    fun tay() {
    }

    @Test
    fun tax() {
    }

    @Test
    fun txa() {
    }

    @Test
    fun txs() {
    }

    @Test
    fun tya() {
    }

    @Test
    fun xxx() {
    }
}