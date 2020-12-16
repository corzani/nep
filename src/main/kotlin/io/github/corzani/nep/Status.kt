package io.github.corzani.nep

fun statusWith(vararg flags: Flag): U8 = flags.fold(u8(0)) { acc, flag -> acc or flag.bitMask }

enum class Flag(val mnemonic: String, val bitMask: U8) {
    C("Carry", 0x01u),
    Z("Zero", 0x02u),
    I("Interrupt", 0x04u),
    D("Decimal", 0x08u),
    B("Break", 0x10u),
    U("Unused", 0x20u),
    V("Overflow", 0x40u),
    N("Negative", 0x80u)
}

fun retrieveFlag(status: U8, flag: Flag, value: Boolean = true) = when (value) {
    true -> status or (flag.bitMask)
    false -> status and (flag.bitMask.inv())
}

fun zeroFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.Z, reg == u8(0))

fun negativeFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.N, reg[7])

fun overflowFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.V, reg[6])


fun NesArch.setFlag(flag: Flag, value: Boolean) {
    status = retrieveFlag(status, flag, value)
}

fun NesArch.setNegativeFlag(cond: Boolean) = setFlag(Flag.N, cond)
fun NesArch.setOverflowFlag(cond: Boolean) = setFlag(Flag.V, cond)
fun NesArch.setBreakFlag(cond: Boolean) = setFlag(Flag.B, cond)
fun NesArch.setDecimalFlag(cond: Boolean) = setFlag(Flag.D, cond)
fun NesArch.setInterruptFlag(cond: Boolean) = setFlag(Flag.I, cond)
fun NesArch.setZeroFlag(cond: Boolean) = setFlag(Flag.Z, cond)
fun NesArch.setCarryFlag(cond: Boolean) = setFlag(Flag.C, cond)
