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
@ExperimentalStdlibApi
@ExperimentalUnsignedTypes
fun runProgram(program: Program) = NesArch().let { nesArch ->
    program.forEachIndexed() { index, uByte -> nesArch.ram[index + CARTRIDGE_ROM_ADDRESS] = uByte }

    val runInstruction = attach(nesArch)
    mainLoop(nesArch, runInstruction)
}

tailrec fun mainLoop(nesArch: NesArch, runInstruction: (opcode: U8) -> Unit) {
    runInstruction(read(nesArch.ram, nesArch.pc))
    mainLoop(nesArch, runInstruction)
}

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
// TODO toInt shouldn't be here
fun attach(nesArch: NesArch) = { opcode: U8 -> opcodes[opcode.toInt()].istruction(nesArch) }

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
@ExperimentalUnsignedTypes
fun retrieveFlag(status: U8, flag: Flag, value: Boolean = true) = when (value) {
    true -> status or (flag.bitMask)
    false -> status and (flag.bitMask.inv())
}

@ExperimentalUnsignedTypes
fun setFlag(nesArch: NesArch, flag: Flag, value: Boolean) = nesArch.apply {
    status = retrieveFlag(status, flag)
}

@ExperimentalStdlibApi
fun main(args: Array<String>) {
    val program : Program= UByteArray(3)
    runProgram(program)
}