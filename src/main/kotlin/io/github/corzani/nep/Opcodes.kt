package io.github.corzani.nep

fun adc(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun and(addressMode: AddressMode) = { nesArch: NesArch ->
    val (fetched, pageCrossed) = addressMode(nesArch)

    read(nesArch.ram, fetched).let { address ->
        nesArch.accumulator = nesArch.accumulator and address
        nesArch.status = flagsOf(nesArch.status, nesArch.accumulator, ::zeroFlag, ::negativeFlag)
    }
    0
}

fun asl(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun bcc(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun bcs(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun beq(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun bit(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun bmi(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun bne(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun bpl(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun brk(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun bvc(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun bvs(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun clc(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun cld(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun cli(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun clv(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun cmp(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun cpx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun cpy(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun dec(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun dex(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun dey(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun eor(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun inc(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun inx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun iny(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun jmp(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun jsr(addressMode: AddressMode) = { nesArch: NesArch -> 0 }

fun lda(addressMode: AddressMode) = { nesArch: NesArch ->
    val (fetched, pageCrossed) = addressMode(nesArch)

    read(nesArch.ram, fetched).let { data -> nesArch.accumulator = data }
    nesArch.status = flagsOf(nesArch.status, nesArch.accumulator, ::zeroFlag, ::negativeFlag)
    0
}

fun ldx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun ldy(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun lsr(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun nop(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun ora(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun pha(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun php(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun pla(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun plp(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun rol(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun ror(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun rti(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun rts(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun sbc(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun sec(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun sed(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun sei(addressMode: AddressMode) = { nesArch: NesArch -> 0 }

fun sta(addressMode: AddressMode) = { nesArch: NesArch ->
    val (fetched, pageCrossed) = addressMode(nesArch)

    write(nesArch.ram, fetched, nesArch.accumulator)
    0
}

fun stx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun sty(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun tsx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun tay(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun tax(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.x = nesArch.accumulator
    nesArch.status = flagsOf(nesArch.status, nesArch.x, ::zeroFlag, ::negativeFlag)
    0
}

fun txa(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun txs(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun tya(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun xxx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }