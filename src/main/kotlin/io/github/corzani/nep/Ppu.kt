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
    val addressRegister: AddrRegister = AddrRegister(0u, true),
    val ctrl: ControlRegister = 0u,
    var buffer: U8 = 0u
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
fun Ppu.getAndSetBufferWith(value: U8): U8 {
    val temp = buffer
    buffer = value
    return temp
}

fun Ppu.ppuRead(address: U16): U8 = when (address.toInt()) { // TODO Too many toInts
    in 0..0x1FFF -> getAndSetBufferWith(chrRom[address.toInt()])
    in 0x2000..0x2FFF -> getMirroredVramAddress(address).toInt().let { mirroredVramAddress ->
        getAndSetBufferWith(vRam[mirroredVramAddress])
    }
    in 0x3000..0x3EFF -> throw IllegalStateException("Address ${humanReadable(address)} is not supposed to be used")
    in 0x3F00..0x3FFF -> (address - 0x3F00u).let { paletteAddr -> palette[paletteAddr.toInt()] }
    else -> throw IllegalStateException("Unable to access to ${humanReadable(address)}")
}

fun Ppu.read() = ppuRead(addressRegister.incr(ctrl.vRamAddressIncrement()))

data class NameTable(val mirroring: ScreenMirroring, val idx: Int)

fun nameTableOffSet(nameTable: NameTable): UInt = when (nameTable) {
    NameTable(ScreenMirroring.Vertical, 2),
    NameTable(ScreenMirroring.Vertical, 3),
    NameTable(ScreenMirroring.Horizontal, 3) -> 0x800u
    NameTable(ScreenMirroring.Horizontal, 1),
    NameTable(ScreenMirroring.Horizontal, 2) -> 0x400u
    else -> 0u
}

fun Ppu.getMirroredVramAddress(address: U16): U16 {
    val mirrorVram = address and 0b10111111111111u
    val vRamIndex = mirrorVram - 0x2000u
    val nameTableIdx = (address / 0x0400u).toInt()
    return u16(vRamIndex - nameTableOffSet(NameTable(mirroring, nameTableIdx)))
}