package io.github.corzani.nep

typealias Ram = UByteArray
typealias Program = UByteArray
typealias U8 = UByte
typealias U16 = UShort

data class Address(val address: U16, val pageCrossed: Boolean, val length: Int)
data class FetchedAddress(val fetched: U8, val address: U16)
data class U16Split(val lo: U8, val hi: U8)

operator fun U8.get(i: Int): Boolean = this.isBitSet(i)
operator fun U16.get(i: Int): Boolean = this.isBitSet(i)

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

fun mem(vararg bytes: U8) = ubyteArrayOf(*bytes)

fun incrAddress(address: U16, increment: Byte): U16 =
    u16(address.toInt() + increment.toInt())

fun toSigned(u8: U8): Byte = u8.toByte()

fun read(ram: Ram, address: U16): U8 = ram[address.toInt()]
fun write(ram: Ram, address: U16, data: U8) = data.let { ram[address.toInt()] = it }

fun read16(ram: Ram, address: U16): U16 =
    u16(ram[address.toInt() + 1]).rotateLeft(8) or u16(ram[address.toInt()])

fun fromLoHi(lo: U8, hi: U8): U16 = u16(hi).rotateLeft(8) or u16(lo)

fun <T> U16.splitLoHi(fn: ((lo: U8, hi: U8) -> T)): T = this.splitLoHi().run { fn(lo, hi) }

fun write16(ram: Ram, address: U16, data: U16) = data.splitLoHi { lo: U8, hi: U8 ->
    address.toInt().let {
        ram[it] = lo
        ram[it + 1] = hi
    }
}

fun stackPush(nesArch: NesArch, data: U8) = nesArch.run {
    write(ram, u16(0x0100u + --stackpointer), data)
}

fun stackPush(nesArch: NesArch, data: U16) = nesArch.run {
    stackpointer = u8(stackpointer - 2u)
    write16(u16(0x0100u + this.stackpointer), data)
}

fun stackPop8(nesArch: NesArch) = nesArch.run { read(u16(0x0100u + stackpointer++)) }

fun stackPop16(nesArch: NesArch): U16 = nesArch.run {
    stackpointer = u8(stackpointer + 2u)
    return read16(u16(0x0100u + this.stackpointer))
}



