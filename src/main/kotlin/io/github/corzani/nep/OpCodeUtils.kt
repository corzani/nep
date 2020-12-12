package io.github.corzani.nep

fun branch(nesArch: NesArch, address: U16, flag: Flag, cond: Boolean) = onFlag(nesArch, flag, cond) {
    ++nesArch.cycles
    val jump = toSigned(read(nesArch.ram, address))
    val jumpTo = incrAddress(nesArch.pc, jump) + 1u
    val additionalTickRequired = ((nesArch.pc + 1u) and 0xFF00u) != (jumpTo and 0xFF00u)

    if (additionalTickRequired) {
        ++nesArch.cycles
    }
    nesArch.pc = u16(jumpTo)
}

fun branchOnFlag(nesArch: NesArch, addressMode: AddressMode, flag: Flag, cond: Boolean) =
    addressMode.address(nesArch).let { (fetched) -> branch(nesArch, fetched, flag, cond) }