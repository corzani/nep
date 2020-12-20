package io.github.corzani.nep

const val VRAM_SIZE = 64 * 1024 // TODO It should be 2K
const val ROM_SIZE = 2 * 1024 // TODO It should be 2K

enum class AccessMode {
    READ, WRITE
}

data class Bus(
    val vRam: Memory = Memory(VRAM_SIZE),
    val rom: Rom = rom(Memory(ROM_SIZE)),
    val ppu: Ppu
)

const val RAM = 0x0000
const val RAM_END = 0x1FFF

const val PPU = 0x2000
const val PPU_END = 0x3FFF

const val ROM = 0x8000
const val ROM_END = 0xFFFF

data class MemoryLocation(
    val memory: Memory,
    val address: U16
)

fun mapRomAddress(romSize: Int, address: U16) = u16(address % u16(romSize))

//TODO create constants
fun Bus.readFromBus(address: U16): U8 = when (address.toInt()) {
    in RAM..RAM_END -> read(vRam, address and 0b00000111_11111111u) // Check mirroring
    0x2000, 0x2001, 0x2003, 0x2005, 0x2006, 0x4014 -> throw NotImplementedError(
        "Write only Address ${humanReadable(address)} shouldn't be read"
    )
    0x2007 -> ppu.read()
    0x2008 -> readFromBus(address and 0b00100000_00000111u)
    in ROM..ROM_END -> read(rom.prg, mapRomAddress(rom.prgSize, u16(address - 0x8000u)))
    else -> throw NotImplementedError("Address 0x${humanReadable(address)} is not in the range of accessible memory")
//    else -> 0u
}

fun Bus.writeToBus(address: U16, data: U8): Unit = when (address.toInt()) {
    in RAM..RAM_END -> write(vRam, address and 0b00000111_11111111u, data) // Check mirroring
    0x2000 -> {
        ppu.ctrl = data
    }
    0x2006 -> {
        ppu.addressRegister = ppu.addressRegister.update(data)
    }
    0x2007 -> TODO()
    in 0x2008..PPU_END -> writeToBus(
        address and 0b00100000_00000111u, // Mirror Down address and recall
        data
    )
    in ROM..ROM_END -> throw IllegalStateException(
        "Unable to write data to ROM. Address ${humanReadable(address)} shouldn't be written"
    )
    else -> throw IllegalStateException("Address 0x${humanReadable(address)} is not in the range of accessible memory")
}

////TODO create constants
//fun Bus.readAddress(address: U16, operation: AccessMode): U8 = when (address.toInt()) {
//    in RAM..RAM_END -> read(vRam, address and 0b00000111_11111111u) // Check mirroring
//    0x2000, 0x2001, 0x2003, 0x2005, 0x2006, 0x4014 -> when (operation) {
//        AccessMode.READ -> throw NotImplementedError(
//            "Write only Address ${humanReadable(address)} shouldn't be read"
//        )
//        AccessMode.WRITE -> TODO()
//    }
//    0x2007 -> when (operation) {
//        AccessMode.READ -> 0x0u // read data
//        AccessMode.WRITE -> 0x0u // write data
//    }
//    in 0x2008..PPU_END -> readAddress(
//        address and 0b00100000_00000111u,
//        AccessMode.WRITE
//    ) // Mirror Down address and recall
//    in ROM..ROM_END -> when (operation) {
//        AccessMode.READ -> read(rom.prg, mapRomAddress(rom.prgSize, u16(address - 0x8000u)))
//        AccessMode.WRITE -> throw NotImplementedError(
//            "Unable to write data to ROM. Address ${humanReadable(address)} shouldn't be written"
//        )
//    }
//    else -> throw NotImplementedError("Address 0x${humanReadable(address)} is not in the range of accessible memory")
//}

fun Bus.read(address: U16) = readFromBus(address)

fun Bus.read16(address: U16) =
    fromLoHi(
        lo = readFromBus(address),
        hi = readFromBus(u16(address + 1u))
    )  // I am sure this will be very dodgy if misused...

fun Bus.write(address: U16, data: U8) = writeToBus(address, data)

fun Bus.write16(
    address: U16,
    data: U16
) {  // I am sure this will be the dodgiest piece of code ever written if misused...
    write(address, data.lo8())
    write(u16(address + 1u), data.hi8())
}
