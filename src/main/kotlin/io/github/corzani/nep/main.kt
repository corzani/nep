package io.github.corzani.nep

fun runAll(nesArch: NesArch) = mainLoop(nesArch, instructionHandler(opcodes(), nesArch))

fun NesArch.start() = mainLoop(this, instructionHandler(opcodes(), this))

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
fun instructionHandler(opcodes: List<Op>, nesArch: NesArch) = fun(opcode: U8): Int {
    val currentInstruction = opcodes[opcode.toInt()]
    ++nesArch.pc
    // TODO... Check when I have additional cycles

    val result = currentInstruction.memory.address(nesArch)
    val opcodeHex = "(0x${humanReadable(opcode)})"

    println("$opcodeHex ${currentInstruction.name} ${result.humanReadable(nesArch.ram, false)}")

    currentInstruction.instruction(result)(nesArch) // TODO check additional Cycle
    return currentInstruction.cycles
}

typealias InstructionFn = (NesArch) -> Boolean

data class Op(val name: String, val instruction: (Address) -> InstructionFn, val memory: AddressMode, val cycles: Int)

@JvmName("getFlagFn")
fun getFlag(nesArch: NesArch, flag: Flag): Boolean = nesArch.run {
    status and flag.bitMask > 0u
}

fun NesArch.getFlag(flag: Flag): Boolean = this.status and flag.bitMask > 0u
fun getFlagOf(reg: U8, flag: Flag): Boolean = reg and flag.bitMask > 0u
fun onFlag(nesArch: NesArch, flag: Flag, cond: Boolean, block: () -> Unit) =
    when (((nesArch.status and flag.bitMask) > 0u) == cond) {
        true -> block()
        false -> Unit
    }

fun flagsOf(status: U8, reg: U8, vararg functions: (status: U8, reg: U8) -> U8) =
    functions.fold(status) { acc, fn -> fn(acc, reg) }

fun NesArch.setFlagFrom(reg: U8, flag: Flag) = this.setFlag(flag, getFlagOf(reg, flag))

fun NesArch.setFlagsFrom(reg: U8, vararg functions: (status: U8, reg: U8) -> U8) {
    status = flagsOf(status, reg, *functions)
}
