const val RAM_SIZE = 1024 * 64
const val CARTRIDGE_ROM_ADDRESS = 0x8000

fun initRam(ram: Ram) {
    write16(ram, 0xFFFCu, 0x8000u)
}

data class NesArch(
    val ram: Ram = Ram(RAM_SIZE).apply(::initRam),
    var accumulator: U8 = 0x00u,
    var x: U8 = 0x00u,
    var y: U8 = 0x00u,
    var stackpointer: U8 = 0x00u,
    var status: U8 = 0x00u,
    val cartSize: Int = 0,
    var pc: U16 = read16(ram, 0xFFFCu),
)

fun testArch(nesArch: NesArch) =
    testLoop(nesArch, instructionHandler(nesArch), u16(CARTRIDGE_ROM_ADDRESS + nesArch.cartSize))

// TODO Silly Implementation
fun load(program: Program): NesArch = NesArch(cartSize = program.size).also { nesArch ->
    program.forEachIndexed { index, uByte ->
        nesArch.ram[index + CARTRIDGE_ROM_ADDRESS] = uByte
    }
}

fun runAll(nesArch: NesArch) = mainLoop(nesArch, instructionHandler(nesArch))

tailrec fun mainLoop(nesArch: NesArch, runInstruction: (opcode: U8) -> Unit) {
    runInstruction(read(nesArch.ram, nesArch.pc))
    mainLoop(nesArch, runInstruction)
}

fun testLoop(
    nesArch: NesArch,
    runInstruction: (opcode: U8) -> Unit,
    runUntilPC: U16
) {
    while (nesArch.pc < runUntilPC) {
        runInstruction(read(nesArch.ram, nesArch.pc))
    }
}

// TODO toInt shouldn't be here
fun instructionHandler(nesArch: NesArch) =
    { opcode: U8 -> opcodes[opcode.toInt()].also { ++nesArch.pc }.istruction(nesArch) }

data class Op(val name: String, val istruction: (NesArch) -> Unit, val cycles: Int)

fun statusWith(vararg flags: Flag): U8 = flags.fold(u8(0)) { acc, flag -> acc or flag.bitMask }

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

fun main(args: Array<String>) {

}