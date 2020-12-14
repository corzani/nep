package io.github.corzani.nep

typealias AddressModeFn = (NesArch) -> Address

sealed class AddressMode(val name: String, val address: AddressModeFn)

fun addressFn(fn: AddressModeFn): AddressModeFn = { nesArch ->
    fn(nesArch).also { address ->
        nesArch.incrementPcBy(address.length)
    }
}

fun readZeroPageAddress(ram: Ram, pc: U16, reg: U8) = u16(read(ram, pc) + reg)
fun readAbsoluteAddress(ram: Ram, pc: U16, reg: U8) = u16(read16(ram, pc) + reg)
fun immediate(nesArch: NesArch): Address = Address(address = nesArch.pc, pageCrossed = false, length = 1)

// TODO Check
fun implied(nesArch: NesArch): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1)

fun relative(nesArch: NesArch): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1)

fun indirect(nesArch: NesArch): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1)

fun absolute(nesArch: NesArch): Address = Address(
    address = readAbsoluteAddress(nesArch.ram, nesArch.pc, 0u),
    pageCrossed = false,
    length = 2
)

fun absoluteX(nesArch: NesArch): Address = Address(
    readAbsoluteAddress(nesArch.ram, nesArch.pc, nesArch.x),
    pageCrossed = false,
    length = 2
)

fun absoluteY(nesArch: NesArch): Address = Address(
    readAbsoluteAddress(nesArch.ram, nesArch.pc, nesArch.y),
    pageCrossed = false,
    length = 2
)

fun zeroPage(nesArch: NesArch): Address = Address(
    address = readZeroPageAddress(nesArch.ram, nesArch.pc, 0u),
    pageCrossed = false,
    length = 1
)

fun zeroPageX(nesArch: NesArch): Address = Address(
    address = readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.x),
    pageCrossed = false,
    length = 1
)

fun zeroPageY(nesArch: NesArch): Address = Address(
    address = readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.y),
    pageCrossed = false,
    length = 1
)

fun indirectX(nesArch: NesArch): Address = Address(
    address = readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.x).splitLoHi { lo: U8, hi: U8 ->
        fromLoHi(lo = lo, hi = u8(hi + 1u))
    },
    pageCrossed = false,
    length = 1
)

fun indirectY(nesArch: NesArch): Address = Address(
    address = read(nesArch.ram, nesArch.pc)
        .let { address: U8 -> read16(nesArch.ram, u16(address)) + u16(nesArch.y) }
        .let(::u16),
    pageCrossed = false,
    length = 1
)

object Immediate : AddressMode("Immediate", addressFn(::immediate))
object Implied : AddressMode("Implied", addressFn(::implied))
object Relative : AddressMode("Relative", addressFn(::relative))
object Indirect : AddressMode("Indirect", addressFn(::indirect))
object ZeroPage : AddressMode("ZeroPage", addressFn(::zeroPage))
object ZeroPageX : AddressMode("ZeroPage X", addressFn(::zeroPageX))
object ZeroPageY : AddressMode("ZeroPage Y", addressFn(::zeroPageY))
object Absolute : AddressMode("Absolute", addressFn(::absolute))
object AbsoluteX : AddressMode("Absolute X", addressFn(::absoluteX))
object AbsoluteY : AddressMode("Absolute Y", addressFn(::absoluteY))
object IndirectX : AddressMode("Indirect X", addressFn(::indirectX))
object IndirectY : AddressMode("Indirect Y", addressFn(::indirectY))
