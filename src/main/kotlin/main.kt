const val RAM_SIZE = 1024 * 64
const val CARTRIDGE_ROM_ADDRESS = 0x8000

data class NesArch(
    val ram: Ram = Ram(RAM_SIZE),
    var accumulator: U8 = 0x00u,
    var x: U8 = 0x00u,
    var y: U8 = 0x00u,
    var stackpointer: U8 = 0x00u,
    var pc: U16 = u16(0x0000u),
    var status: U8 = 0x00u
)

// TODO Silly Implementation
@ExperimentalUnsignedTypes
fun NesArch.run(program: Program) = NesArch().let {
    program.forEachIndexed() { index, uByte -> ram[index + CARTRIDGE_ROM_ADDRESS] = uByte }
}

data class Op(val name: String, val istruction: (NesArch) -> Unit, val cycles: Int)

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

//fun writeFlag(ram: Ram, address: Int, data: U8) = ram.set(address, data)
//fun Nes.writeFlag(address: Int, data: U8) = writeFlag(ram, address, data)

//
//fun getFlag(status: U8, flag: Flag): Boolean = status.and(flag.bitMask).toInt() > 0;
//fun Nes.getFlag(flag: Flag): Boolean = getFlag(status, flag)
//
fun retrieveFlag(status: U8, flag: Flag, value: Boolean = true) = when (value) {
    true -> status or (flag.bitMask)
    false -> status and (flag.bitMask.inv())
}

fun setFlag(nesArch: NesArch, flag: Flag, value: Boolean) = nesArch.apply {
    status = retrieveFlag(status, flag)
}

//
//fun main(args: Array<String>) {
//    val nes = Init().apply {
//        setFlag(Flag.C)
//        setFlag(Flag.N)
//    }
//    println(nes.status)
//}