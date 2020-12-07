fun zeroFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.Z, reg.toInt() == 0)

fun negativeFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.N, (reg and 0x80u) == u8(0x80))

fun immediate(nes: Nes): U16 = nes.cpu.pc

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun absolute(nes: Nes, reg: U8 = 0u): U16 = u16(read16(nes.ram, nes.cpu.pc) + reg)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun absoluteX(nes: Nes): U16 = absolute(nes, nes.cpu.x)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun absoluteY(nes: Nes): U16 = absolute(nes, nes.cpu.y)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun zeroPage(nes: Nes, reg: U8 = 0u): U16 = u16(read(nes.ram, nes.cpu.pc) + reg)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun zeroPageX(nes: Nes): U16 = zeroPage(nes, nes.cpu.x)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun zeroPageY(nes: Nes): U16 = zeroPage(nes, nes.cpu.y)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun indirectX(nes: Nes): U16 = zeroPageX(nes)
    .splitLoHi { lo: U8, hi: U8 -> fromLoHi(lo = lo, hi = u8(hi + 1u)) }

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun indirectY(nes: Nes): U16 = read(nes.ram, nes.cpu.pc)
    .let { pc -> read16(nes.ram, u16(pc)) + u16(nes.cpu.y) }
    .let(::u16)
