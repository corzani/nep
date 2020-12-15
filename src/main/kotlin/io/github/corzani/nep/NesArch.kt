package io.github.corzani.nep

const val RAM_SIZE = 1024 * 64
const val CARTRIDGE_ROM_ADDRESS = 0x8000

typealias Instruction = (opcode: U8) -> Int

class NesArch(
    val ram: Ram = Ram(RAM_SIZE).apply(::initRam),
    var accumulator: U8 = 0x00u,
    var x: U8 = 0x00u,
    var y: U8 = 0x00u,
    var stackPointer: U8 = 0x00u,
    var status: U8 = 0x00u,
    val cartSize: Int = 0,
    var pc: U16 = read16(ram, 0xFFFCu),
    var cycles: Int = 0
)

fun NesArch.initRam() {
    write16(ram, 0xFFFCu, 0x8000u)
}

fun NesArch.incrementPcBy(value: Int) {
    pc = u16(pc + u16(value))
}

fun initRam(ram: Ram) {
    write16(ram, 0xFFFCu, 0x8000u)
}

fun NesArch.read16(address: U16): U16 = read16(ram, address)

fun NesArch.write16(address: U16, data: U16) = write16(ram, address, data)

fun NesArch.read(address: U16): U8 = read(ram, address)
fun NesArch.write(address: U16, data: U8) = write(ram, address, data)

fun NesArch.stackPush(data: U8) = stackPush(this, data)
fun NesArch.stackPush(data: U16) = stackPush(this, data)

fun NesArch.stackPop8() = stackPop8(this)
fun NesArch.stackPop16() = stackPop16(this)

fun NesArch.runTest() = testLoop(this, instructionHandler(opcodes(), this), u16(CARTRIDGE_ROM_ADDRESS + this.cartSize))

// TODO Silly Implementation
fun loadFromMemory(program: Program): NesArch = NesArch(cartSize = program.size).also { nesArch ->
    program.forEachIndexed { index, uByte ->
        nesArch.ram[index + CARTRIDGE_ROM_ADDRESS] = uByte
    }
}

// TODO Silly Implementation
fun loadFromMemory(program: Program, block: NesArch.() -> Unit) =
    NesArch(cartSize = program.size).also { nesArch ->
        program.forEachIndexed { index, uByte ->
            nesArch.ram[index + CARTRIDGE_ROM_ADDRESS] = uByte
        }
        block(nesArch)
    }

fun NesArch.getAddressFrom(addressMode: AddressMode) = addressMode.address(this)

fun NesArch.fetchFrom(addressMode: Address) =
    FetchedAddress(fetched = this.read(addressMode.address), address = addressMode.address)

fun createNes(block: NesArch.() -> Unit) = block(NesArch())
