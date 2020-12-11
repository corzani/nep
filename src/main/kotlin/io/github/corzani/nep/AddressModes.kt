package io.github.corzani.nep

sealed class AddressMode(val name: String, val fn: AddressModeFn)
object Immediate : AddressMode("Immediate", ::immediate)
object Implied : AddressMode("Implied", ::implied)
object Relative : AddressMode("Relative", ::relative)
object Indirect : AddressMode("Indirect", ::indirect)
object Absolute : AddressMode("Absolute", ::absolute)
object AbsoluteX : AddressMode("Absolute X", ::absoluteX)
object AbsoluteY : AddressMode("Absolute Y", ::absoluteY)
object ZeroPage : AddressMode("ZeroPage", ::zeroPage)
object ZeroPageX : AddressMode("ZeroPage X", ::zeroPageX)
object ZeroPageY : AddressMode("ZeroPage Y", ::zeroPageY)
object IndirectX : AddressMode("Indirect X", ::indirectX)
object IndirectY : AddressMode("Indirect Y", ::indirectY)

fun zeroFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.Z, reg.toInt() == 0)

fun negativeFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.N, (reg and 0x80u) == u8(0x80))

fun immediate(nesArch: NesArch): Address = Address(fetched = nesArch.pc, pageCrossed = false, length = 1).also {
    nesArch.incrementPcBy(it.length)
}

fun zp(ram: Ram, pc: U16, reg: U8) = u16(read(ram, pc) + reg)
fun ab(ram: Ram, pc: U16, reg: U8) = u16(read16(ram, pc) + reg)

// TODO Check
fun implied(nesArch: NesArch): Address = Address(nesArch.pc, pageCrossed = false, length = 1).also {
    nesArch.incrementPcBy(it.length)
}


fun relative(nesArch: NesArch): Address = Address(nesArch.pc, pageCrossed = false, length = 1).also {
    nesArch.incrementPcBy(it.length)
}


fun indirect(nesArch: NesArch): Address = Address(nesArch.pc, pageCrossed = false, length = 1).also {
    nesArch.incrementPcBy(it.length)
}


fun absolute(nesArch: NesArch, reg: U8 = 0u): Address = Address(
    fetched = ab(nesArch.ram, nesArch.pc, reg),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
}

fun absoluteX(nesArch: NesArch): Address = Address(
    ab(nesArch.ram, nesArch.pc, nesArch.x),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
}


fun absoluteY(nesArch: NesArch): Address = Address(
    ab(nesArch.ram, nesArch.pc, nesArch.y),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
}


fun zeroPage(nesArch: NesArch): Address = Address(
    fetched = zp(nesArch.ram, nesArch.pc, 0u),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}


fun zeroPageX(nesArch: NesArch): Address = Address(
    fetched = zp(nesArch.ram, nesArch.pc, nesArch.x),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}


fun zeroPageY(nesArch: NesArch): Address = Address(
    fetched = zp(nesArch.ram, nesArch.pc, nesArch.y),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}


fun indirectX(nesArch: NesArch): Address = Address(
    fetched = zp(nesArch.ram, nesArch.pc, nesArch.x).splitLoHi { lo: U8, hi: U8 ->
        fromLoHi(lo = lo, hi = u8(hi + 1u))
    },
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}

fun indirectY(nesArch: NesArch): Address = Address(
    fetched = read(nesArch.ram, nesArch.pc)
        .let { address: U8 -> read16(nesArch.ram, u16(address)) + u16(nesArch.y) }
        .let(::u16),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}


