package io.github.corzani.nep

sealed class AddressMode(val name: String, val address: (NesArch, Address.() -> Unit) -> Address)
object Immediate : AddressMode("Immediate", ::immediate)
object Implied : AddressMode("Implied", ::implied)
object Relative : AddressMode("Relative", ::relative)
object Indirect : AddressMode("Indirect", ::indirect)
object ZeroPage : AddressMode("ZeroPage", ::zeroPage)
object ZeroPageX : AddressMode("ZeroPage X", ::zeroPageX)
object ZeroPageY : AddressMode("ZeroPage Y", ::zeroPageY)
object Absolute : AddressMode("Absolute", ::absolute)
object AbsoluteX : AddressMode("Absolute X", ::absoluteX)
object AbsoluteY : AddressMode("Absolute Y", ::absoluteY)
object IndirectX : AddressMode("Indirect X", ::indirectX)
object IndirectY : AddressMode("Indirect Y", ::indirectY)

fun zp(ram: Ram, pc: U16, reg: U8) = u16(read(ram, pc) + reg)
fun ab(ram: Ram, pc: U16, reg: U8) = u16(read16(ram, pc) + reg)

fun zeroFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.Z, reg.toInt() == 0)

fun negativeFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.N, (reg and 0x80u) == u8(0x80))

fun immediate(nesArch: NesArch, fn: Address.() -> Unit): Address =
    Address(fetched = nesArch.pc, pageCrossed = false, length = 1).also {
        nesArch.incrementPcBy(it.length)
        fn(it)
    }


// TODO Check
fun implied(nesArch: NesArch, fn: Address.() -> Unit): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1).also {
        nesArch.incrementPcBy(it.length)
        fn(it)
    }

fun relative(nesArch: NesArch, fn: Address.() -> Unit): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1).also {
        nesArch.incrementPcBy(it.length)
        fn(it)
    }

fun indirect(nesArch: NesArch, fn: Address.() -> Unit): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1).also {
        nesArch.incrementPcBy(it.length)
        fn(it)
    }

fun absolute(nesArch: NesArch, fn: Address.() -> Unit): Address = Address(
    fetched = ab(nesArch.ram, nesArch.pc, 0u),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
    fn(it)
}

fun absoluteX(nesArch: NesArch, fn: Address.() -> Unit): Address = Address(
    ab(nesArch.ram, nesArch.pc, nesArch.x),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
    fn(it)
}

fun absoluteY(nesArch: NesArch, fn: Address.() -> Unit): Address = Address(
    ab(nesArch.ram, nesArch.pc, nesArch.y),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
    fn(it)
}

fun zeroPage(nesArch: NesArch, fn: Address.() -> Unit): Address = Address(
    fetched = zp(nesArch.ram, nesArch.pc, 0u),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
    fn(it)
}

fun zeroPageX(nesArch: NesArch, fn: Address.() -> Unit): Address = Address(
    fetched = zp(nesArch.ram, nesArch.pc, nesArch.x),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
    fn(it)
}

fun zeroPageY(nesArch: NesArch, fn: Address.() -> Unit): Address = Address(
    fetched = zp(nesArch.ram, nesArch.pc, nesArch.y),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
    fn(it)
}

fun indirectX(nesArch: NesArch, fn: Address.() -> Unit): Address = Address(
    fetched = zp(nesArch.ram, nesArch.pc, nesArch.x).splitLoHi { lo: U8, hi: U8 ->
        fromLoHi(lo = lo, hi = u8(hi + 1u))
    },
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
    fn(it)
}

fun indirectY(nesArch: NesArch, fn: Address.() -> Unit): Address = Address(
    fetched = read(nesArch.ram, nesArch.pc)
        .let { address: U8 -> read16(nesArch.ram, u16(address)) + u16(nesArch.y) }
        .let(::u16),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
    fn(it)
}
