package io.github.corzani.nep.ppu

import io.github.corzani.nep.*
import io.github.corzani.nep.ppu.registers.AddrRegister
import io.github.corzani.nep.ppu.registers.ControlRegister
import io.github.corzani.nep.ppu.registers.StatusRegister.VBLANK_STARTED
import io.github.corzani.nep.ppu.registers.vRamAddressIncrement
import io.github.corzani.nep.ppu.registers.vblankNmi

data class NameTable(val mirroring: ScreenMirroring, val idx: Int)

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
    var status: U8 = 0u,
    var scanline: Int = 0
) {
    fun tick(additionalCycles: Int): Boolean {
        cycles += additionalCycles

        if (cycles >= 341) {

            cycles -= 341
            ++scanline

            if (scanline == 241 && ctrl.vblankNmi()) {
                status = withFlag(status, VBLANK_STARTED.bitmask, true)
            }

            if (scanline >= 262) {
                scanline = 0
                status = withFlag(status, VBLANK_STARTED.bitmask, false)
                return true
            }
        }
        return false
    }

    fun getAndSetBufferWith(value: U8): U8 {
        val temp = buffer
        buffer = value
        return temp
    }

    fun ppuRead(address: U16): U8 = when (address.toInt()) { // TODO Too many toInts
        in 0..0x1FFF -> getAndSetBufferWith(chrRom[address.toInt()])
        in 0x2000..0x2FFF -> getAndSetBufferWith(vRam[getMirroredVramAddress(address).toInt()])
        in 0x3000..0x3EFF -> throw IllegalStateException("Address ${humanReadable(address)} is not supposed to be used")
        in 0x3F00..0x3FFF -> (address - 0x3F00u).let { paletteAddr -> palette[paletteAddr.toInt()] }
        else -> throw IllegalStateException("Unable to access to ${humanReadable(address)}")
    }

    fun read() = ppuRead(addressRegister.incr(ctrl.vRamAddressIncrement()))

    private fun nameTableOffSet(nameTable: NameTable): UInt = when (nameTable) {
        NameTable(ScreenMirroring.Vertical, 2),
        NameTable(ScreenMirroring.Vertical, 3),
        NameTable(ScreenMirroring.Horizontal, 3) -> 0x800u
        NameTable(ScreenMirroring.Horizontal, 1),
        NameTable(ScreenMirroring.Horizontal, 2) -> 0x400u
        else -> 0u
    }

    fun getMirroredVramAddress(address: U16): U16 {
        val mirrorVram = address and 0b10111111111111u
        val vRamIndex = mirrorVram - 0x2000u
        val nameTableIdx = (address / 0x0400u).toInt()
        return u16(vRamIndex - nameTableOffSet(NameTable(mirroring, nameTableIdx)))
    }
}
