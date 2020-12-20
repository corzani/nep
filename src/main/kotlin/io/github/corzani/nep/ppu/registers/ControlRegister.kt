package io.github.corzani.nep.ppu.registers

import io.github.corzani.nep.U8
import io.github.corzani.nep.get

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
