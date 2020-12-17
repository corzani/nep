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
    val a: Pair<U8, U8>,
    val hiPtr: Boolean
)

fun AddrRegister.update(u16: U16) = AddrRegister(a = Pair(u16.hi8(), u16.lo8()), this.hiPtr)


fun addRegister() = AddrRegister(Pair(u8(0), u8(0)), true)