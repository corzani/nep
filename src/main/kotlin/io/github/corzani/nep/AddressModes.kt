package io.github.corzani.nep

import io.github.corzani.nep.cpu.Cpu

typealias AddressModeFn = (Cpu) -> Address

sealed class AddressMode(val address: AddressModeFn)

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

fun addressFn(fn: AddressModeFn): AddressModeFn = { cpu ->
    fn(cpu).also { address ->
        cpu.incrementPcBy(address.length)
    }
}

fun readZeroPageAddress(bus: Bus, pc: U16, reg: U8) = bus.read(pc).let {
    (u16(it) to u16(it + reg))
}

fun readAbsoluteAddress(bus: Bus, pc: U16, reg: U8) = bus.read16(pc).let {
    (it to u16(it + reg))
}

fun immediate(cpu: Cpu): Address = Address(
    address = cpu.pc,
    pageCrossed = false,
    length = 1,
    origin = cpu.pc,
    type = AddressType.Immediate

)

// TODO Check
fun implied(cpu: Cpu): Address =
    Address(
        cpu.pc, pageCrossed = false, length = 0, origin = cpu.pc, type = AddressType.Implied
    )

fun relative(cpu: Cpu): Address =
    Address(cpu.pc, pageCrossed = false, length = 1, origin = cpu.pc, type = AddressType.Relative)

fun indirect(cpu: Cpu): Address = cpu.bus.read16(cpu.pc).let { origin ->

    // TODO Check Page Boundary Bug
    val validAddress = if (origin.lo8() == u8(0xFFu)) {
        fromLoHi(lo = cpu.bus.read(origin), hi = cpu.bus.read(origin and 0xFF00u))
    } else {
        cpu.bus.read16(origin)
    }

    Address(
        address = validAddress,
        pageCrossed = false,
        length = 2,
        origin = origin,
        type = AddressType.Indirect
    )
}

fun absolute(cpu: Cpu): Address = readAbsoluteAddress(cpu.bus, cpu.pc, 0u).let { (origin, address) ->
    Address(
        address = address,
        pageCrossed = false,
        length = 2,
        origin = origin,
        type = AddressType.Absolute
    )
}

fun absoluteX(cpu: Cpu): Address =
    readAbsoluteAddress(cpu.bus, cpu.pc, cpu.x).let { (origin, address) ->
        Address(
            address = address,
            pageCrossed = false,
            length = 2,
            origin = origin,
            type = AddressType.AbsoluteX
        )
    }

fun absoluteY(cpu: Cpu): Address =
    readAbsoluteAddress(cpu.bus, cpu.pc, cpu.y).let { (origin, address) ->

        Address(
            address = address,
            pageCrossed = false,
            length = 2,
            origin = origin,
            type = AddressType.AbsoluteY
        )
    }

fun zeroPage(cpu: Cpu): Address =
    readZeroPageAddress(cpu.bus, cpu.pc, 0u).let { (origin, address) ->
        Address(
            address = address,
            pageCrossed = false,
            length = 1,
            origin = origin,
            type = AddressType.ZeroPage
        )
    }

fun zeroPageX(cpu: Cpu): Address =
    readZeroPageAddress(cpu.bus, cpu.pc, cpu.x).let { (origin, address) ->
        Address(
            address = address,
            pageCrossed = false,
            length = 1,
            origin = origin,
            type = AddressType.ZeroPageX
        )
    }

fun zeroPageY(cpu: Cpu): Address =
    readZeroPageAddress(cpu.bus, cpu.pc, cpu.y).let { (origin, address) ->
        Address(
            address = address,
            pageCrossed = false,
            length = 1,
            origin = origin,
            type = AddressType.ZeroPageY
        )
    }

fun indirectX(cpu: Cpu): Address =
    readZeroPageAddress(cpu.bus, cpu.pc, cpu.x).let { (origin, address) ->
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

fun indirectY(cpu: Cpu): Address =
    cpu.bus.read(cpu.pc).let {
        Address(
            origin = u16(it),
            address = it
                .let { address: U8 -> cpu.bus.read16(u16(address)) + u16(cpu.y) }
                .let(::u16),
            pageCrossed = false,
            length = 1,
            type = AddressType.IndirectY
        )
    }
