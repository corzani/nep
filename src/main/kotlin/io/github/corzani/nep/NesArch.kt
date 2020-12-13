package io.github.corzani.nep

const val RAM_SIZE = 1024 * 64
const val CARTRIDGE_ROM_ADDRESS = 0x8000

class NesArch(
    val ram: Ram = Ram(RAM_SIZE).apply(::initRam),
    var accumulator: U8 = 0x00u,
    var x: U8 = 0x00u,
    var y: U8 = 0x00u,
    var stackpointer: U8 = 0x00u,
    var status: U8 = 0x00u,
    val cartSize: Int = 0,
    var pc: U16 = read16(ram, 0xFFFCu),
    var cycles: Int = 0
)

fun NesArch.initRam() {
    write16(ram, 0xFFFCu, 0x8000u)
}

// TODO idea? Or maybe not

//fun NesArch.fetch(fn: (NesArch, Address.() -> Unit) -> Unit) = fn(this) {
//
//}

fun NesArch.incrementPcBy(value: Int) {
    pc = u16(pc + u16(value))
}

fun initRam(ram: Ram) {
    write16(ram, 0xFFFCu, 0x8000u)
}

fun createNes(block: NesArch.() -> Unit) = block(NesArch())

fun NesArch.test() = testLoop(this, instructionHandler(this), u16(CARTRIDGE_ROM_ADDRESS + this.cartSize))

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

data class FetchedAddress(val fetched: U8, val address: U16)
fun NesArch.fetchFrom(addressMode: AddressMode) =
    addressMode.address(this).address.let {
        FetchedAddress(fetched = this.read(it), address = it)
    }

