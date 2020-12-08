fun zeroFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.Z, reg.toInt() == 0)

fun negativeFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.N, (reg and 0x80u) == u8(0x80))

fun immediate(nesArch: NesArch): U16 = nesArch.pc

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun absolute(nesArch: NesArch, reg: U8 = 0u): U16 = u16(read16(nesArch.ram, nesArch.pc) + reg)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun absoluteX(nesArch: NesArch): U16 = absolute(nesArch, nesArch.x)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun absoluteY(nesArch: NesArch): U16 = absolute(nesArch, nesArch.y)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun zeroPage(nesArch: NesArch, reg: U8 = 0u): U16 = u16(read(nesArch.ram, nesArch.pc) + reg)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun zeroPageX(nesArch: NesArch): U16 = zeroPage(nesArch, nesArch.x)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun zeroPageY(nesArch: NesArch): U16 = zeroPage(nesArch, nesArch.y)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun indirectX(nesArch: NesArch): U16 = zeroPageX(nesArch)
    .splitLoHi { lo: U8, hi: U8 -> fromLoHi(lo = lo, hi = u8(hi + 1u)) }

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun indirectY(nesArch: NesArch): U16 = read(nesArch.ram, nesArch.pc)
    .let { pc -> read16(nesArch.ram, u16(pc)) + u16(nesArch.y) }
    .let(::u16)
