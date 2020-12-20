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
    retrieveFlag(status, Flag.N, reg[Flag.N.ordinal])

fun overflowFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.V, reg[Flag.V.ordinal])

fun interruptDisableFlag(status: U8, reg: U8): U8 =
    retrieveFlag(status, Flag.I, reg[Flag.I.ordinal])

fun withFlag(status: U8, bitMask: U8, value: Boolean = true) = when (value) {
    true -> status or bitMask
    false -> status and (bitMask.inv())
}