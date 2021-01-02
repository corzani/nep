package io.github.corzani.nep.ppu.registers

import io.github.corzani.nep.U16
import io.github.corzani.nep.U8
import io.github.corzani.nep.plus
import io.github.corzani.nep.u16

data class AddrRegister(
    val address: U16,
    val hiPtr: Boolean
) {

    private fun mirrorAddress(address: U16) = if (address > 0x3FFFu) address and 0b11111111111111u else address

    fun update(data: U8): AddrRegister = when (hiPtr) {
        true -> (address and 0x00FFu) or u16(data).rotateLeft(8)
        false -> (address and 0xFF00u) or u16(data)
    }.let(::mirrorAddress).let {
        AddrRegister(it, !hiPtr)
    }

    fun incr(value: Int): U16 = (address + value).let(::mirrorAddress)

    fun reset() = this.copy(hiPtr = true)
}
