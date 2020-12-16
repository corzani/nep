package io.github.corzani.nep

data class Ppu(
    val chrRom: Memory,
    val palette: Memory = Memory(32),
    val vRam: Memory = Memory(2048),
    val mirroring: ScreenMirroring,
    val oamData: Memory = Memory(256)
)
