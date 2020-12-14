package io.github.corzani.nep

sealed class AddressMode(val name: String, val address: (NesArch) -> Address)
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

fun readZeroPageAddress(ram: Ram, pc: U16, reg: U8) = u16(read(ram, pc) + reg)
fun readAbsoluteAddress(ram: Ram, pc: U16, reg: U8) = u16(read16(ram, pc) + reg)

fun immediate(nesArch: NesArch): Address =
    Address(address = nesArch.pc, pageCrossed = false, length = 1).also {
        nesArch.incrementPcBy(it.length)
    }

// TODO Check
fun implied(nesArch: NesArch): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1).also {
        nesArch.incrementPcBy(it.length)
    }

fun relative(nesArch: NesArch): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1).also {
        nesArch.incrementPcBy(it.length)
    }

fun indirect(nesArch: NesArch): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1).also {
        nesArch.incrementPcBy(it.length)
    }

fun absolute(nesArch: NesArch): Address = Address(
    address = readAbsoluteAddress(nesArch.ram, nesArch.pc, 0u),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
}

fun absoluteX(nesArch: NesArch): Address = Address(
    readAbsoluteAddress(nesArch.ram, nesArch.pc, nesArch.x),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
}

fun absoluteY(nesArch: NesArch): Address = Address(
    readAbsoluteAddress(nesArch.ram, nesArch.pc, nesArch.y),
    pageCrossed = false,
    length = 2
).also {
    nesArch.incrementPcBy(it.length)
}

fun zeroPage(nesArch: NesArch): Address = Address(
    address = readZeroPageAddress(nesArch.ram, nesArch.pc, 0u),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}

fun zeroPageX(nesArch: NesArch): Address = Address(
    address = readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.x),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}

fun zeroPageY(nesArch: NesArch): Address = Address(
    address = readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.y),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}

fun indirectX(nesArch: NesArch): Address = Address(
    address = readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.x).splitLoHi { lo: U8, hi: U8 ->
        fromLoHi(lo = lo, hi = u8(hi + 1u))
    },
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}

fun indirectY(nesArch: NesArch): Address = Address(
    address = read(nesArch.ram, nesArch.pc)
        .let { address: U8 -> read16(nesArch.ram, u16(address)) + u16(nesArch.y) }
        .let(::u16),
    pageCrossed = false,
    length = 1
).also {
    nesArch.incrementPcBy(it.length)
}
