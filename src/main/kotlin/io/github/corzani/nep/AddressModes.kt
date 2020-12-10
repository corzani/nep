package io.github.corzani.nep

fun zeroFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.Z, reg.toInt() == 0)

fun negativeFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.N, (reg and 0x80u) == u8(0x80))

fun immediate(nesArch: NesArch): U16 = nesArch.pc

// TODO Check
fun implied(nesArch: NesArch): U16 = nesArch.pc
fun relative(nesArch: NesArch): U16 = nesArch.pc
fun indirect(nesArch: NesArch): U16 = nesArch.pc

fun absolute(nesArch: NesArch, reg: U8 = 0u): U16 = u16(read16(nesArch.ram, nesArch.pc) + reg)

fun absoluteX(nesArch: NesArch): U16 = absolute(nesArch, nesArch.x)

fun absoluteY(nesArch: NesArch): U16 = absolute(nesArch, nesArch.y)

fun zeroPage(nesArch: NesArch, reg: U8 = 0u): U16 = u16(read(nesArch.ram, nesArch.pc) + reg)

fun zeroPageX(nesArch: NesArch): U16 = zeroPage(nesArch, nesArch.x)

fun zeroPageY(nesArch: NesArch): U16 = zeroPage(nesArch, nesArch.y)

fun indirectX(nesArch: NesArch): U16 = zeroPageX(nesArch)
    .splitLoHi { lo: U8, hi: U8 -> fromLoHi(lo = lo, hi = u8(hi + 1u)) }

fun indirectY(nesArch: NesArch): U16 = read(nesArch.ram, nesArch.pc)
    .let { address: U8 -> read16(nesArch.ram, u16(address)) + u16(nesArch.y) }
    .let(::u16)
