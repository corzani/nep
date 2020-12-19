package io.github.corzani.nep

typealias ControlRegister = U8

enum class ControlRegisterFlags(val bitMask: U8) {
    NAMETABLE1(0b00000001u),
    NAMETABLE2(0b00000010u),
    VRAM_ADD_INCREMENT(0b00000100u),
    SPRITE_PATTERN_ADDR(0b00001000u),
    BACKROUND_PATTERN_ADDR(0b00010000u),
    SPRITE_SIZE(0b00100000u),
    MASTER_SLAVE_SELECT(0b01000000u),
    GENERATE_NMI(0b10000000u)
}

fun ControlRegister.vRamAddressIncrement() = if (this[ControlRegisterFlags.VRAM_ADD_INCREMENT.ordinal]) 32 else 1

data class Ppu(
    val chrRom: Memory,
    val palette: Memory = Memory(32),
    val vRam: Memory = Memory(2048),
    val mirroring: ScreenMirroring,
    val oamData: Memory = Memory(256),
    val addr: AddrRegister,
    val ctrl: U8
)

data class AddrRegister(
    val address: U16,
    val hiPtr: Boolean
)

fun mirrorAddress(address: U16) = if (address > 0x3FFFu) address and 0b11111111111111u else address

fun AddrRegister.update(data: U8): AddrRegister = when (hiPtr) {
    true -> (address and 0x00FFu) or u16(data).rotateLeft(8)
    false -> (address and 0xFF00u) or u16(data)
}.let(::mirrorAddress).let {
    AddrRegister(it, !hiPtr)
}

fun AddrRegister.incr(value: Int) = (address + value).let(::mirrorAddress)
fun AddrRegister.reset() = this.copy(hiPtr = true)