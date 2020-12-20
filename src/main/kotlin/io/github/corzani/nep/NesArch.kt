package io.github.corzani.nep

const val RAM_SIZE = 1024 * 64
const val CARTRIDGE_ROM_ADDRESS = 0x8000

typealias Instruction = (opcode: U8) -> Int

class NesArch(
    var accumulator: U8 = 0x00u,
    var x: U8 = 0x00u,
    var y: U8 = 0x00u,
    var stackPointer: U8 = 0x00u,
    var status: U8 = 0x00u,
    val cartSize: Int = 0,
    val bus: Bus,
    var pc: U16 = u16(ROM), //bus.read16(0xFFFCu),
    var cycles: Int = 0
)

fun NesArch.initRam() {
    bus.write16(0xFFFCu, 0x8000u)
}

fun NesArch.incrementPcBy(value: Int) {
    pc = u16(pc + u16(value))
}

fun initRam(ram: Memory) {
    write16(ram, 0xFFFCu, 0x8000u)
}

fun NesArch.read16(address: U16): U16 = bus.read16(address)

fun NesArch.write16(address: U16, data: U16) = bus.write16(address, data)

fun NesArch.read(address: U16): U8 = bus.read(address)
fun NesArch.write(address: U16, data: U8) = bus.write(address, data)

fun NesArch.stackPush(data: U8) = stackPush(this, data)
fun NesArch.stackPush(data: U16) = stackPush(this, data)

fun NesArch.stackPop8() = stackPop8(this)
fun NesArch.stackPop16() = stackPop16(this)

fun NesArch.runTest() =
    testLoop(
        this,
        instructionHandler(this, opcodes()),
        u16(CARTRIDGE_ROM_ADDRESS + this.cartSize - NES_HEADER_SIZE)
    )

// TODO Silly Implementation
fun loadFromMemory(program: Program): NesArch =
    NesArch(
        cartSize = program.size,
        bus = Bus(
            rom = rom(program.copyOf(ROM_SIZE)),
            ppu = Ppu(
                mirroring = ScreenMirroring.Horizontal,
                chrRom = Memory(4)
            )
        )
    )

// TODO Silly Implementation
fun loadFromMemory(program: Program, block: NesArch.() -> Unit) =
    loadFromMemory(program).run {
        block(this)
    }

fun NesArch.getAddressFrom(addressMode: AddressMode) = addressMode.address(this)

fun NesArch.fetchFrom(addressMode: Address) =
    FetchedAddress(fetched = this.read(addressMode.address), address = addressMode.address)

fun NesArch.setFlag(flag: Flag, value: Boolean) {
    status = retrieveFlag(status, flag, value)
}

fun NesArch.setNegativeFlag(cond: Boolean) = setFlag(Flag.N, cond)
fun NesArch.setOverflowFlag(cond: Boolean) = setFlag(Flag.V, cond)
fun NesArch.setBreakFlag(cond: Boolean) = setFlag(Flag.B, cond)
fun NesArch.setDecimalFlag(cond: Boolean) = setFlag(Flag.D, cond)
fun NesArch.setInterruptFlag(cond: Boolean) = setFlag(Flag.I, cond)
fun NesArch.setZeroFlag(cond: Boolean) = setFlag(Flag.Z, cond)
fun NesArch.setCarryFlag(cond: Boolean) = setFlag(Flag.C, cond)

fun NesArch.getFlag(flag: Flag): Boolean = this.status and flag.bitMask > 0u

fun NesArch.setFlagsFrom(reg: U8, vararg functions: (status: U8, reg: U8) -> U8) {
    status = flagsOf(status, reg, *functions)
}

fun NesArch.start() =
    mainLoop(this, instructionHandler(this, opcodes()))
