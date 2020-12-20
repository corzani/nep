package io.github.corzani.nep.ppu

import io.github.corzani.nep.*
import io.github.corzani.nep.ppu.registers.AddrRegister
import io.github.corzani.nep.ppu.registers.ControlRegister
import io.github.corzani.nep.ppu.registers.incr
import io.github.corzani.nep.ppu.registers.vRamAddressIncrement

data class Ppu(
    val chrRom: Memory,
    val palette: Memory = Memory(32),
    val vRam: Memory = Memory(2048),
    val mirroring: ScreenMirroring,
    val oamData: Memory = Memory(256),
    var addressRegister: AddrRegister = AddrRegister(0u, true),
    var ctrl: ControlRegister = 0u,
    var buffer: U8 = 0u,
    var cycles: Int = 0,
//    var status: StatusRegister,
    var scanline: Int = 0
)

fun Ppu.tick(i: Int) {
    cycles += i
    if (cycles >= 341) {

        cycles -= 341
        ++scanline

//        if (scanline == 241 and ctrl.vblankNmi()) {
//            stat
//        }
    }
}

//private fun ControlRegister.vblankNmi(): Boolean {
//
//}

fun Ppu.getAndSetBufferWith(value: U8): U8 {
    val temp = buffer
    buffer = value
    return temp
}

fun Ppu.ppuRead(address: U16): U8 = when (address.toInt()) { // TODO Too many toInts
    in 0..0x1FFF -> getAndSetBufferWith(chrRom[address.toInt()])
    in 0x2000..0x2FFF -> getAndSetBufferWith(vRam[getMirroredVramAddress(address).toInt()])
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