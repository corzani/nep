package io.github.corzani.nep

const val VRAM_SIZE = 64 * 1024 // TODO It should be 2K
const val ROM_SIZE = 2 * 1024 // TODO It should be 2K


data class Bus(
    val vRam: Memory = Memory(VRAM_SIZE),
    val rom: Rom = rom(Memory(ROM_SIZE))
)

const val RAM = 0x0000u
const val RAM_END = 0x1FFFu

const val PPU = 0x2000u
const val PPU_END = 0x3FFFu

const val ROM = 0x8000u
const val ROM_END = 0xFFFFu

//TODO create constants
fun Bus.read(address: U16) = when (address) {
    in (RAM..RAM_END) -> read(vRam, address and 0b00000111_11111111u) // Check mirroring
    in (PPU..PPU_END) -> TODO()
    // TODO Check if mirror is needed
    in (ROM..ROM_END) -> read(rom.prg, u16(address - 0x8000u))
    else -> TODO()
}

fun Bus.write(address: U16, data: U8) = when (address) {
    in (RAM..RAM_END) -> write(vRam, address and 0b00000111_11111111u, data) // Check mirroring
    in (PPU..PPU_END) -> TODO()
    in (ROM..ROM_END) -> throw IllegalStateException()
    else -> TODO()
}

fun Bus.read16(address: U16) = when (address) {
    in (RAM..RAM_END) -> read16(vRam, address and 0b00000111_11111111u) // Check mirroring
    in (PPU..PPU_END) -> TODO()
    in (ROM..ROM_END) -> read16(rom.prg, u16(address - 0x8000u))
    else -> TODO()
}

fun Bus.write16(address: U16, data: U16) = when (address) {
    in (RAM..RAM_END) -> write16(vRam, address and 0b00000111_11111111u, data) // Check mirroring
    in (PPU..PPU_END) -> TODO()
    in (ROM..ROM_END) -> throw IllegalStateException()
    else -> TODO()
}
