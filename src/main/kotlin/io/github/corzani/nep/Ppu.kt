package io.github.corzani.nep

data class Ppu(
    val chr: Memory,
    val palette: Memory = Memory(32),
    val vRam: Memory = Memory(2048),
    val mirroring: ScreenMirroring,
    val oamData: Memory = Memory(256)
)
