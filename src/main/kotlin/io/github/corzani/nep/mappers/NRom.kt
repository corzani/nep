package io.github.corzani.nep.mappers

import io.github.corzani.nep.*

fun memRead(prgBanks: Int): (U16) -> MappedAddress {
    val bitMask = if (prgBanks > 1) u16(0x7FFFu) else u16(0x3FFFu)
    0b00000111_11111111u
    return fun(address: U16) = when (address.toInt()) {
        in 0x8000..0xFFFF -> MappedAddress(address and bitMask, true)
        else -> MappedAddress(address, false)
    }
}

fun ppuRead(addr: U16) = ppuMemory(addr) { addr.toInt() in 0x0000..0x1FFF }
fun ppuWrite(chrBanks: Int) =
    fun(address: U16) = ppuMemory(address) { address.toInt() in 0x0000..0x1FFF && chrBanks == 0 }

fun ppuMemory(address: U16, conditionFn: (U16) -> Boolean): MappedAddress =
    MappedAddress(address, conditionFn(address))

fun nRom(nesHeader: NesHeader) = Mapper(
    readMemAddress = memRead(nesHeader.prgRomSize / PRG_PAGE_SIZE),
    writeMem = memRead(nesHeader.prgRomSize / PRG_PAGE_SIZE),
    readPpu = ::ppuRead,
    writePpu = ppuWrite(nesHeader.chrRomSize / CHR_PAGE_SIZE)
)