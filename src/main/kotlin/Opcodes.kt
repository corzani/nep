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