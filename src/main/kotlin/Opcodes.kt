fun flagsOf(status: U8, reg: U8, vararg functions: (status: U8, reg: U8) -> U8) =
        functions.fold(status) { acc, fn -> fn(acc, reg) }

fun lda(mode: AddressMode) = { nesArch: NesArch ->
    read(nesArch.ram, mode(nesArch)).let { data ->
        nesArch.accumulator = data
    }
    nesArch.status = flagsOf(nesArch.status, nesArch.accumulator, ::zeroFlag, ::negativeFlag)
}

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun sta(addressMode: AddressMode) = { nesArch: NesArch ->
    write(nesArch.ram, addressMode(nesArch), nesArch.accumulator)
}

fun tax(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.x = nesArch.accumulator
    nesArch.status = flagsOf(nesArch.status, nesArch.x, ::zeroFlag, ::negativeFlag)
}

fun adc(addressMode: AddressMode) = { nesArch: NesArch -> }
fun and(addressMode: AddressMode) = { nesArch: NesArch -> }
fun asl(addressMode: AddressMode) = { nesArch: NesArch -> }
fun bcc(addressMode: AddressMode) = { nesArch: NesArch -> }
fun bcs(addressMode: AddressMode) = { nesArch: NesArch -> }
fun beq(addressMode: AddressMode) = { nesArch: NesArch -> }
fun bit(addressMode: AddressMode) = { nesArch: NesArch -> }
fun bmi(addressMode: AddressMode) = { nesArch: NesArch -> }
fun bne(addressMode: AddressMode) = { nesArch: NesArch -> }
fun bpl(addressMode: AddressMode) = { nesArch: NesArch -> }
fun brk(addressMode: AddressMode) = { nesArch: NesArch -> }
fun bvc(addressMode: AddressMode) = { nesArch: NesArch -> }
fun bvs(addressMode: AddressMode) = { nesArch: NesArch -> }
fun clc(addressMode: AddressMode) = { nesArch: NesArch -> }
fun cld(addressMode: AddressMode) = { nesArch: NesArch -> }
fun cli(addressMode: AddressMode) = { nesArch: NesArch -> }
fun clv(addressMode: AddressMode) = { nesArch: NesArch -> }
fun cmp(addressMode: AddressMode) = { nesArch: NesArch -> }
fun cpx(addressMode: AddressMode) = { nesArch: NesArch -> }
fun cpy(addressMode: AddressMode) = { nesArch: NesArch -> }
fun dec(addressMode: AddressMode) = { nesArch: NesArch -> }
fun dex(addressMode: AddressMode) = { nesArch: NesArch -> }
fun dey(addressMode: AddressMode) = { nesArch: NesArch -> }
fun eor(addressMode: AddressMode) = { nesArch: NesArch -> }
fun inc(addressMode: AddressMode) = { nesArch: NesArch -> }
fun inx(addressMode: AddressMode) = { nesArch: NesArch -> }
fun iny(addressMode: AddressMode) = { nesArch: NesArch -> }
fun jmp(addressMode: AddressMode) = { nesArch: NesArch -> }
fun jsr(addressMode: AddressMode) = { nesArch: NesArch -> }
fun ldx(addressMode: AddressMode) = { nesArch: NesArch -> }
fun ldy(addressMode: AddressMode) = { nesArch: NesArch -> }
fun lsr(addressMode: AddressMode) = { nesArch: NesArch -> }
fun nop(addressMode: AddressMode) = { nesArch: NesArch -> }
fun ora(addressMode: AddressMode) = { nesArch: NesArch -> }
fun pha(addressMode: AddressMode) = { nesArch: NesArch -> }
fun php(addressMode: AddressMode) = { nesArch: NesArch -> }
fun pla(addressMode: AddressMode) = { nesArch: NesArch -> }
fun plp(addressMode: AddressMode) = { nesArch: NesArch -> }
fun rol(addressMode: AddressMode) = { nesArch: NesArch -> }
fun ror(addressMode: AddressMode) = { nesArch: NesArch -> }
fun rti(addressMode: AddressMode) = { nesArch: NesArch -> }
fun rts(addressMode: AddressMode) = { nesArch: NesArch -> }
fun sbc(addressMode: AddressMode) = { nesArch: NesArch -> }
fun sec(addressMode: AddressMode) = { nesArch: NesArch -> }
fun sed(addressMode: AddressMode) = { nesArch: NesArch -> }
fun sei(addressMode: AddressMode) = { nesArch: NesArch -> }
fun stx(addressMode: AddressMode) = { nesArch: NesArch -> }
fun sty(addressMode: AddressMode) = { nesArch: NesArch -> }
fun tsx(addressMode: AddressMode) = { nesArch: NesArch -> }
fun tay(addressMode: AddressMode) = { nesArch: NesArch -> }
fun txa(addressMode: AddressMode) = { nesArch: NesArch -> }
fun txs(addressMode: AddressMode) = { nesArch: NesArch -> }
fun tya(addressMode: AddressMode) = { nesArch: NesArch -> }
fun xxx(addressMode: AddressMode) = { nesArch: NesArch -> }