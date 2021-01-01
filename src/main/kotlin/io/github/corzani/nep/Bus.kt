package io.github.corzani.nep

import io.github.corzani.nep.mappers.MapperFn
import io.github.corzani.nep.ppu.Ppu

const val VRAM_SIZE = 64 * 1024 // TODO It should be 2K
const val ROM_SIZE = 2 * 1024 // TODO It should be 2K

const val RAM = 0x0000
const val RAM_END = 0x1FFF

const val PPU = 0x2000
const val PPU_END = 0x3FFF

const val ROM = 0x8000
const val ROM_END = 0xFFFF

data class Bus(
    val vRam: Memory = Memory(VRAM_SIZE),
    val rom: Rom = rom(Memory(ROM_SIZE)),
    val ppu: Ppu,
    var cycles: Int = 0,
    val mapperFn: MapperFn = { address -> address }
) {

    private fun mapRomAddress(romSize: Int, address: U16) = u16(address % u16(romSize))

    fun tick(additionalCycles: Int) {
        cycles += additionalCycles
        ppu.tick(additionalCycles * 6)
    }

    //TODO create constants
    private fun readFromBus(address: U16): U8 = when (address.toInt()) {
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

    private fun writeToBus(address: U16, data: U8): Unit = when (address.toInt()) {
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

    fun read(address: U16) = readFromBus(address)

    fun read16(address: U16) =
        fromLoHi(
            lo = readFromBus(address),
            hi = readFromBus(u16(address + 1u))
        )  // I am sure this will be very dodgy if misused...

    fun write(address: U16, data: U8) = writeToBus(address, data)

    @Deprecated("This call doesn't use mappers, do not use it or expect the worst...")
    fun write16(
        address: U16,
        data: U16
    ) {  // I am sure this will be the dodgiest piece of code ever written if misused...
        write(address, data.lo8())
        write(u16(address + 1u), data.hi8())
    }
}
