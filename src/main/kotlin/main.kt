typealias Ram = UByteArray
typealias U8 = UByte

data class U16(val lo: U8, val hi: U8)

const val RAM_SIZE = 1024 * 64

data class Nes(
        val ram: Ram,
        var accumulator: U8,
        var x: U8,
        var y: U8,
        var stackpointer: U8,
        var pc: Int,
        var status: U8,
)


fun Init() = Nes(ram = Ram(RAM_SIZE), 0u, 0u, 0u, 0u, 0, 0u)

enum class Flag(val bitMask: U8) {
    C(0x01u),
    Z(0x02u),
    I(0x04u),
    D(0x08u),
    B(0x10u),
    U(0x20u),
    V(0x40u),
    N(0x80u),
}

fun read(ram: Ram, address: Int): U8 = ram[address]
fun Nes.read(address: Int): U8 = read(ram, address)

fun read16(ram: Ram, address: Int): U16 = U16(lo = ram[address], hi = ram[address + 1])
fun Nes.read16(address: Int): U16 = read16(ram, address)

fun writeFlag(ram: Ram, address: Int, data: U8) = ram.set(address, data)
fun Nes.writeFlag(address: Int, data: U8) = writeFlag(ram, address, data)

fun getFlag(status: U8, flag: Flag): Boolean = status.and(flag.bitMask).toInt() > 0;
fun Nes.getFlag(flag: Flag): Boolean = getFlag(status, flag)

fun retrieveFlag(status: U8, flag: Flag, clear: Boolean = true) = when (clear) {
    true -> status.or(flag.bitMask)
    false -> status.and(flag.bitMask.inv())
}

fun Nes.setFlag(flag: Flag, clear: Boolean = true) = this.apply {
    status = retrieveFlag(status, flag, clear)
}

fun main(args: Array<String>) {
    val nes = Init().apply {
        setFlag(Flag.C)
        setFlag(Flag.N)
    }
    println(nes.status)
}