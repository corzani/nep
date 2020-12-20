package io.github.corzani.nep.cpu

import io.github.corzani.nep.*

fun branch(cpu: Cpu, address: U16, flag: Flag, cond: Boolean) = onFlag(cpu, flag, cond) {
    ++cpu.cycles
    val jump = cpu.bus.read(address)
    val jumpTo = cpu.pc + jump + 1u
    val additionalTickRequired = ((cpu.pc + 1u) and 0xFF00u) != (jumpTo and 0xFF00u)

    if (additionalTickRequired) {
        ++cpu.cycles
    }
    cpu.pc = u16(jumpTo)
}

fun Cpu.branchOnFlag(addressMode: Address, flag: Flag, cond: Boolean) =
    branch(this, addressMode.address, flag, cond)