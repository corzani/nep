package io.github.corzani.nep

fun runAll(nesArch: NesArch) = mainLoop(nesArch, instructionHandler(nesArch))

fun NesArch.start() = mainLoop(this, instructionHandler(this))

tailrec fun mainLoop(nesArch: NesArch, runInstruction: Instruction) {
    runInstruction(read(nesArch.ram, nesArch.pc))
    mainLoop(nesArch, runInstruction)
}

fun testLoop(
    nesArch: NesArch,
    runInstruction: Instruction,
    runUntilPC: U16
) {
    while (nesArch.pc < runUntilPC) {
        nesArch.cycles += runInstruction(read(nesArch.ram, nesArch.pc))
    }
}

// TODO toInt shouldn't be here
fun instructionHandler(nesArch: NesArch) = fun(opcode: U8): Int {
    val currentInstruction = opcodes[opcode.toInt()]
    ++nesArch.pc
    // TODO... Check when I have additional cycles
    currentInstruction.instruction(nesArch) // TODO check additional Cycle
    return currentInstruction.cycles
}

data class Op(val name: String, val instruction: (NesArch) -> Boolean, val cycles: Int)

fun statusWith(vararg flags: Flag): U8 = flags.fold(u8(0)) { acc, flag -> acc or flag.bitMask }

enum class Flag(val bitMask: U8) {
    C(0x01u),
    Z(0x02u),
    I(0x04u),
    D(0x08u),
    B(0x10u),
    U(0x20u),
    V(0x40u),
    N(0x80u)
}


//fun writeFlag(ram: Ram, address: Int, data: U8) = ram.set(address, data)
//fun Nes.writeFlag(address: Int, data: U8) = writeFlag(ram, address, data)

//
//fun getFlag(status: U8, flag: io.github.corzani.nep.Flag): Boolean = status.and(flag.bitMask).toInt() > 0;
//fun Nes.getFlag(flag: io.github.corzani.nep.Flag): Boolean = getFlag(status, flag)
//

fun retrieveFlag(status: U8, flag: Flag, value: Boolean = true) = when (value) {
    true -> status or (flag.bitMask)
    false -> status and (flag.bitMask.inv())
}

fun setFlag(nesArch: NesArch, flag: Flag, value: Boolean) = nesArch.apply {
    status = retrieveFlag(status, flag)
}

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

fun getFlag(nesArch: NesArch, flag: Flag): Boolean = nesArch.run {
    status and flag.bitMask > 0u
}

fun onFlag(nesArch: NesArch, flag: Flag, cond: Boolean, block: () -> Unit) =
    when (((nesArch.status and flag.bitMask) > 0u) == cond) {
        true -> block()
        false -> Unit
    }
