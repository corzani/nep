package io.github.corzani.nep.ppu.registers

import io.github.corzani.nep.U8

enum class StatusRegister(val bitmask: U8) {
    NOTUSED (0b0000_0001u),
    NOTUSED2(0b0000_0010u),
    NOTUSED3(0b0000_0100u),
    NOTUSED4(0b0000_1000u),
    NOTUSED5(0b0001_0000u),
    SPRITE_OVERFLOW(0b0010_0000u),
    SPRITE_ZERO_HIT(0b0100_0000u),
    VBLANK_STARTED(0b1000_0000u)
}
