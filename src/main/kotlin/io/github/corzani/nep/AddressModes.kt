package io.github.corzani.nep

typealias AddressModeFn = (NesArch) -> Address

sealed class AddressMode(val address: AddressModeFn)

fun addressFn(fn: AddressModeFn): AddressModeFn = { nesArch ->
    fn(nesArch).also { address ->
        nesArch.incrementPcBy(address.length)
    }
}

fun readZeroPageAddress(ram: Ram, pc: U16, reg: U8) = read(ram, pc).let {
    (u16(it) to u16(it + reg))
}

fun readAbsoluteAddress(ram: Ram, pc: U16, reg: U8) = read16(ram, pc).let {
    (it to u16(it + reg))
}

fun immediate(nesArch: NesArch): Address = Address(
    address = nesArch.pc,
    pageCrossed = false,
    length = 1,
    origin = nesArch.pc,
    type = AddressType.Immediate

)

// TODO Check
fun implied(nesArch: NesArch): Address =
    Address(
        nesArch.pc, pageCrossed = false, length = 0, origin = nesArch.pc, type = AddressType.Implied
    )

fun relative(nesArch: NesArch): Address =
    Address(nesArch.pc, pageCrossed = false, length = 1, origin = nesArch.pc, type = AddressType.Relative)

fun indirect(nesArch: NesArch): Address = read16(nesArch.ram, nesArch.pc).let { origin ->

    // TODO Check Page Boundary Bug
    val validAddress = if (origin.lo8() == u8(0xFFu)) {
        fromLoHi(lo = read(nesArch.ram, origin), hi = read(nesArch.ram, origin and 0xFF00u))
    } else {
        read16(nesArch.ram, origin)
    }

    Address(
        address = validAddress,
        pageCrossed = false,
        length = 2,
        origin = origin,
        type = AddressType.Indirect
    )
}

fun absolute(nesArch: NesArch): Address = readAbsoluteAddress(nesArch.ram, nesArch.pc, 0u).let { (origin, address) ->
    Address(
        address = address,
        pageCrossed = false,
        length = 2,
        origin = origin,
        type = AddressType.Absolute
    )
}

fun absoluteX(nesArch: NesArch): Address =
    readAbsoluteAddress(nesArch.ram, nesArch.pc, nesArch.x).let { (origin, address) ->
        Address(
            address = address,
            pageCrossed = false,
            length = 2,
            origin = origin,
            type = AddressType.AbsoluteX
        )
    }

fun absoluteY(nesArch: NesArch): Address =
    readAbsoluteAddress(nesArch.ram, nesArch.pc, nesArch.y).let { (origin, address) ->

        Address(
            address = address,
            pageCrossed = false,
            length = 2,
            origin = origin,
            type = AddressType.AbsoluteY
        )
    }

fun zeroPage(nesArch: NesArch): Address =
    readZeroPageAddress(nesArch.ram, nesArch.pc, 0u).let { (origin, address) ->
        Address(
            address = address,
            pageCrossed = false,
            length = 1,
            origin = origin,
            type = AddressType.ZeroPage
        )
    }

fun zeroPageX(nesArch: NesArch): Address =
    readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.x).let { (origin, address) ->
        Address(
            address = address,
            pageCrossed = false,
            length = 1,
            origin = origin,
            type = AddressType.ZeroPageX
        )
    }

fun zeroPageY(nesArch: NesArch): Address =
    readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.y).let { (origin, address) ->
        Address(
            address = address,
            pageCrossed = false,
            length = 1,
            origin = origin,
            type = AddressType.ZeroPageY

        )
    }

fun indirectX(nesArch: NesArch): Address =
    readZeroPageAddress(nesArch.ram, nesArch.pc, nesArch.x).let { (origin, address) ->
        Address(
            address = address.splitLoHi { lo: U8, hi: U8 ->
                fromLoHi(lo = lo, hi = u8(hi + 1u))
            },
            pageCrossed = false,
            length = 1,
            origin = origin,
            type = AddressType.IndirectX

        )
    }

fun indirectY(nesArch: NesArch): Address =
    read(nesArch.ram, nesArch.pc).let {
        Address(
            origin = u16(it),
            address = it
                .let { address: U8 -> read16(nesArch.ram, u16(address)) + u16(nesArch.y) }
                .let(::u16),
            pageCrossed = false,
            length = 1,
            type = AddressType.IndirectY
        )
    }

object Immediate : AddressMode(addressFn(::immediate))
object Implied : AddressMode(addressFn(::implied))
object Relative : AddressMode(addressFn(::relative))
object Indirect : AddressMode(addressFn(::indirect))
object ZeroPage : AddressMode(addressFn(::zeroPage))
object ZeroPageX : AddressMode(addressFn(::zeroPageX))
object ZeroPageY : AddressMode(addressFn(::zeroPageY))
object Absolute : AddressMode(addressFn(::absolute))
object AbsoluteX : AddressMode(addressFn(::absoluteX))
object AbsoluteY : AddressMode(addressFn(::absoluteY))
object IndirectX : AddressMode(addressFn(::indirectX))
object IndirectY : AddressMode(addressFn(::indirectY))


fun addressToString(address: U16) = "\$${address.toString(16).padStart(4, '0')}".toUpperCase()
fun addressToString(address: U8) = "\$${address.toString(16).padStart(2, '0')}".toUpperCase()

fun Address.addressToString() = when (this.type) {
    AddressType.Immediate -> "#${addressToString(address.lo8())}"
    AddressType.Implied -> ""
    AddressType.Relative -> addressToString(address)
    AddressType.Indirect -> "(${addressToString(address)})"
    AddressType.ZeroPage -> addressToString(address.lo8())
    AddressType.ZeroPageX -> "${addressToString(address.lo8())},X"
    AddressType.ZeroPageY -> "${addressToString(address.lo8())},Y"
    AddressType.Absolute -> addressToString(address)
    AddressType.AbsoluteX -> "${addressToString(address)},X"
    AddressType.AbsoluteY -> "${addressToString(address)},Y"
    AddressType.IndirectX -> "(${addressToString(address.lo8())},X)"
    AddressType.IndirectY -> "(${addressToString(address.lo8())},Y)"
}