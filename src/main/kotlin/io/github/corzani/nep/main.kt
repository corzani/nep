package io.github.corzani.nep

fun runAll(nesArch: NesArch) =
    mainLoop(nesArch, instructionHandler(nesArch, opcodes()))

tailrec fun mainLoop(nesArch: NesArch, runInstruction: Instruction) {
    runInstruction(nesArch.bus.read(nesArch.pc))
    mainLoop(nesArch, runInstruction)
}

fun testLoop(
    nesArch: NesArch,
    runInstruction: Instruction,
    runUntilPC: U16
) {
    while (nesArch.pc < runUntilPC) {
        nesArch.cycles += runInstruction(nesArch.bus.read(nesArch.pc))
    }
}

// TODO toInt shouldn't be here
fun instructionHandler(nesArch: NesArch, opcodes: List<Op>) = fun(opcode: U8): Int {
    val currentInstruction = opcodes[opcode.toInt()]
    ++nesArch.pc

    // TODO... Check when I have additional cycles

    val result = currentInstruction.memory.address(nesArch)
    val opcodeHex = "(0x${humanReadable(opcode)})"

    println("$opcodeHex ${currentInstruction.name} ${result.humanReadable(nesArch.bus, false)}")

    currentInstruction.instruction(result)(nesArch) // TODO check additional Cycle
    return currentInstruction.cycles
}

fun getFlag(nesArch: NesArch, flag: Flag): Boolean = nesArch.run {
    status and flag.bitMask > 0u
}

fun onFlag(nesArch: NesArch, flag: Flag, cond: Boolean, block: () -> Unit) {
    when (getFlag(nesArch, flag) == cond) {
        true -> block()
        false -> Unit
    }
}

fun flagsOf(status: U8, reg: U8, vararg functions: (status: U8, reg: U8) -> U8) =
    functions.fold(status) { acc, fn -> fn(acc, reg) }
