package io.github.corzani.nep

const val VRAM_SIZE = 64 * 1024 // TODO It should be 2K
const val ROM_SIZE = 2 * 1024 // TODO It should be 2K

enum class AccessMode {
    READ, WRITE
}

data class Bus(
    val vRam: Memory = Memory(VRAM_SIZE),
    val rom: Rom = rom(Memory(ROM_SIZE))
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

//u16(0x2000u), u16(0x2001u), u16(0x2003u), u16(0x2005u), u16(0x2006u), u16(0x4014u) -> TODO()

fun mapRomAddress(romSize: Int, address: U16) = u16(address % u16(romSize))

//TODO create constants
fun Bus.readAddress(address: U16, operation: AccessMode): MemoryLocation = when (address.toInt()) {
    in RAM..RAM_END -> MemoryLocation(vRam, address and 0b00000111_11111111u) // Check mirroring
    0x2000, 0x2001, 0x2003, 0x2005, 0x2006, 0x4014 -> when (operation) {
        AccessMode.READ -> throw NotImplementedError(
            "Write only Address ${humanReadable(address)} shouldn't be read"
        )
        AccessMode.WRITE -> TODO()
    }
    in PPU..PPU_END -> TODO()
    // TODO Check if mirror is needed
    in ROM..ROM_END -> when (operation) {
        AccessMode.READ -> MemoryLocation(rom.prg, mapRomAddress(rom.prgSize, u16(address - 0x8000u)))
        AccessMode.WRITE -> throw NotImplementedError(
            "Unable to write data to ROM. Address ${humanReadable(address)} shouldn't be written"
        )
    }
    else -> throw NotImplementedError("Address 0x${humanReadable(address)} is not in the range of accessible memory")
}

fun Bus.read(address: U16) =
    readAddress(address, AccessMode.READ).let { (memory, currentAddress) -> read(memory, currentAddress) }

fun Bus.read16(address: U16) =
    readAddress(address, AccessMode.READ).let { (memory, currentAddress) -> read16(memory, currentAddress) }

fun Bus.write(address: U16, data: U8) =
    readAddress(address, AccessMode.WRITE).let { (memory, currentAddress) -> write(memory, currentAddress, data) }

fun Bus.write16(address: U16, data: U16) =
    readAddress(address, AccessMode.WRITE).let { (memory, currentAddress) -> write16(memory, currentAddress, data) }