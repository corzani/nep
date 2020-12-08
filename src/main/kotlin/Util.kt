typealias Ram = UByteArray
typealias Program = UByteArray
typealias U8 = UByte
typealias U16 = UShort
typealias AddressMode = (NesArch) -> U16

fun u8(value: UInt) = value.toUByte()
fun u8(value: Int) = value.toUByte()

fun u16(value: UInt) = value.toUShort()
fun u16(value: U8) = value.toUShort()

data class U16Split(val lo: U8, val hi: U8)

fun read(ram: Ram, address: U16): U8 = ram[address.toInt()]
fun NesArch.read(address: U16): U8 = read(ram, address)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun read16(ram: Ram, address: U16): U16 =
        ram[address.toInt() + 1].toUShort().rotateLeft(8) or ram[address.toInt()].toUShort()

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun U16.splitLoHi(): U16Split = U16Split(lo = (this and 0x00FFu).toUByte(), hi = this.rotateRight(8).toUByte())

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun fromLoHi(lo: U8, hi: U8): U16 = (hi).toUShort().rotateLeft(8) or lo.toUShort()

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun fromLoHiToU16(u16Split: U16Split): U16 = (u16Split.hi).toUShort().rotateLeft(8) or u16Split.lo.toUShort()

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun <T> U16.splitLoHi(fn: ((lo: U8, hi: U8) -> T)): T = this.splitLoHi().run { fn(lo, hi) }

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun write16(ram: Ram, address: U16, data: U16) = data.splitLoHi { lo: U8, hi: U8 ->
    address.toInt().let {
        ram[it] = lo
        ram[it + 1] = hi
    }
}

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun write(ram: Ram, address: U16, data: U8) = data.let { ram[address.toInt()] = it }


@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun NesArch.read16(address: U16): U16 = read16(ram, address)
