fun flagsOf(status: U8, reg: U8, vararg functions: (status: U8, reg: U8) -> U8) =
    functions.fold(status) { acc, fn -> fn(acc, reg) }

fun lda(mode: AddressMode) = { nes: Nes ->
    read(nes.ram, mode(nes)).let { data ->
        nes.cpu.accumulator = data
    }.also {
        nes.cpu.status = flagsOf(nes.cpu.status, nes.cpu.accumulator, ::zeroFlag, ::negativeFlag)
    }
}