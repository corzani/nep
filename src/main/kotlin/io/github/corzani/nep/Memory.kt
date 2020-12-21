package io.github.corzani.nep

import io.github.corzani.nep.cpu.Cpu

typealias Memory = UByteArray
typealias Program = UByteArray
typealias U8 = UByte
typealias U16 = UShort

data class Address(
    val address: U16,
    val pageCrossed: Boolean,
    val length: Int,
    val origin: U16,
    val type: AddressType
) {
    fun humanReadable(bus: Bus): String {
        return when (this.type) {
            AddressType.Immediate -> bus.read(origin)
                .let { value -> "#\$${humanReadable(value)} => Imm ${humanReadable(value)}:${value.toInt()}" }
            AddressType.Implied -> ""
            AddressType.Relative -> "\$${to6502Notation(origin)} => Addr ${translate(address)}"
            AddressType.Indirect -> "(\$${to6502Notation(origin)}) => Addr ${translate(address)}"
            AddressType.ZeroPage -> "\$${humanReadable(origin.lo8())} => Addr ${translate(address)}"
            AddressType.ZeroPageX -> "\$${humanReadable(origin.lo8())},X => Addr ${translate(address)}"
            AddressType.ZeroPageY -> "\$${humanReadable(origin.lo8())},Y => Addr ${translate(address)}"
            AddressType.Absolute -> "\$${to6502Notation(origin)} => Addr ${translate(address)}"
            AddressType.AbsoluteX -> "\$${to6502Notation(origin)},X => Addr ${translate(address)}"
            AddressType.AbsoluteY -> "\$${to6502Notation(origin)},Y => Addr ${translate(address)}"
            AddressType.IndirectX -> "(\$${humanReadable(origin.lo8())},X) => Addr ${translate(address)}"
            AddressType.IndirectY -> "(\$${humanReadable(origin.lo8())},Y) => Addr ${translate(address)}"
        }
    }
}

enum class AddressType(val addressModeName: String) {
    Immediate("Immediate"),
    Implied("Implied"),
    Relative("Relative"),
    Indirect("Indirect"),
    ZeroPage("ZeroPage"),
    ZeroPageX("ZeroPage X"),
    ZeroPageY("ZeroPage Y"),
    Absolute("Absolute"),
    AbsoluteX("Absolute X"),
    AbsoluteY("Absolute Y"),
    IndirectX("Indirect X"),
    IndirectY("Indirect Y")
}

data class FetchedAddress(val fetched: U8, val address: U16)
data class U16Split(val lo: U8, val hi: U8)

operator fun U8.get(i: Int): Boolean = this.isBitSet(i)
operator fun U16.get(i: Int): Boolean = this.isBitSet(i)

operator fun Memory.get(i: U16): U8 = this[i.toInt()]

fun u8(value: UInt) = value.toUByte()
fun u8(value: Int) = value.toUByte()

fun u16(value: Int) = value.toUShort()
fun u16(value: UInt) = value.toUShort()
fun u16(value: U8) = value.toUShort()

// TODO Fix toUByte
fun U16.splitLoHi(): U16Split = U16Split(lo = (this and 0x00FFu).toUByte(), hi = this.rotateRight(8).toUByte())
fun U16.lo8(): U8 = (this and 0x00FFu).toUByte()
fun U16.hi8(): U8 = this.rotateRight(8).toUByte()

fun U16.isBitSet(ind: Int) = (this and u16(1u).rotateLeft(ind)) > 0u
fun U8.isBitSet(ind: Int) = (this and u8(1u).rotateLeft(ind)) > 0u

operator fun U16.plus(value: U16): U16 = u16(this + value)
operator fun U16.plus(value: Int): U16 = u16(this + u16(value))

fun mem(vararg bytes: U8) = ubyteArrayOf(*bytes)
fun nesRomWithHeader(vararg bytes: U8) = testRomHeader + mem(*bytes)

fun incrementAddress(address: U16, increment: Byte): U16 =
    u16(address.toInt() + increment.toInt())

fun toSigned(u8: U8): Byte = u8.toByte()

fun read(ram: Memory, address: U16): U8 = ram[address.toInt()]
fun write(ram: Memory, address: U16, data: U8) = data.let { ram[address.toInt()] = it }

fun read16(ram: Memory, address: U16): U16 =
    u16(ram[address.toInt() + 1]).rotateLeft(8) or u16(ram[address.toInt()])

fun fromLoHi(lo: U8, hi: U8): U16 = u16(hi).rotateLeft(8) or u16(lo)

fun <T> U16.splitLoHi(fn: ((lo: U8, hi: U8) -> T)): T = this.splitLoHi().run { fn(lo, hi) }

fun write16(ram: Memory, address: U16, data: U16) = data.splitLoHi { lo: U8, hi: U8 ->
    address.toInt().let {
        ram[it] = lo
        ram[it + 1] = hi
    }
}

fun stackPush(cpu: Cpu, data: U8) = cpu.run { write(u16(0x0100u + --stackPointer), data) }

fun stackPush(cpu: Cpu, data: U16) = cpu.run {
    stackPointer = u8(stackPointer - 2u)
    data.splitLoHi { lo: U8, hi: U8 ->
        (u16(0x0100u + this.stackPointer)).let {
            cpu.bus.write(it, lo)
            cpu.bus.write(u16(it + 1u), hi)
        }
    }
}

fun stackPop8(cpu: Cpu) = cpu.run { read(u16(0x0100u + stackPointer++)) }

fun stackPop16(cpu: Cpu): U16 = cpu.run {
    stackPointer = u8(stackPointer + 2u)

    u16(0x0100u + this.stackPointer).let {
        fromLoHi(lo = bus.read(it), hi = bus.read(u16(it + 1u)))
    }
}

fun humanReadable(address: U16) = address.toString(16).padStart(4, '0').toUpperCase()
fun humanReadable(address: U8) = address.toString(16).padStart(2, '0').toUpperCase()

fun to6502Notation(address: U16) = address.splitLoHi { lo, hi -> "${humanReadable(lo)}${humanReadable(hi)}" }

fun translate(address: U16) = "0x${humanReadable(address)}:${address.toInt()}"
