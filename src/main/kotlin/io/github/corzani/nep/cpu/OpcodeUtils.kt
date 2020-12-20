package io.github.corzani.nep.cpu

import io.github.corzani.nep.*

fun branch(nesArch: NesArch, address: U16, flag: Flag, cond: Boolean) = onFlag(nesArch, flag, cond) {
    ++nesArch.cycles
    val jump = nesArch.bus.read(address)
    val jumpTo = nesArch.pc + jump + 1u
    val additionalTickRequired = ((nesArch.pc + 1u) and 0xFF00u) != (jumpTo and 0xFF00u)

    if (additionalTickRequired) {
        ++nesArch.cycles
    }
    nesArch.pc = u16(jumpTo)
}

fun NesArch.branchOnFlag(addressMode: Address, flag: Flag, cond: Boolean) =
    branch(this, addressMode.address, flag, cond)