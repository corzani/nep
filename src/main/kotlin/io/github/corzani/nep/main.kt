package io.github.corzani.nep

import io.github.corzani.nep.cpu.Cpu
import io.github.corzani.nep.cpu.Instruction

fun runAll(cpu: Cpu) =
    mainLoop(cpu, instructionHandler(cpu, opcodes()))

tailrec fun mainLoop(cpu: Cpu, runInstruction: Instruction) {
    runInstruction(cpu.bus.read(cpu.pc))
    mainLoop(cpu, runInstruction)
}

fun testLoop(
    cpu: Cpu,
    runInstruction: Instruction,
    runUntilPC: U16
) {
    while (cpu.pc < runUntilPC) {
        val cycles = runInstruction(cpu.bus.read(cpu.pc))
        cpu.bus.tick(cycles)
    }
}

// TODO toInt shouldn't be here
fun instructionHandler(cpu: Cpu, opcodes: List<Op>) = fun(opcode: U8): Int {
    val currentInstruction = opcodes[opcode.toInt()]
    ++cpu.pc

    // TODO... Check when I have additional cycles

    val result = currentInstruction.memory.address(cpu)
    val opcodeHex = "(0x${humanReadable(opcode)})"

    println("$opcodeHex ${currentInstruction.name} ${result.humanReadable(cpu.bus)}")

    currentInstruction.instruction(result)(cpu) // TODO check additional Cycle
    println("       PC:${humanReadable(cpu.pc)} A:${humanReadable(cpu.accumulator)} X:${humanReadable(cpu.x)} Y:${humanReadable(cpu.y)} SP:${humanReadable(cpu.stackPointer)}")
    return currentInstruction.cycles
}

fun getFlag(cpu: Cpu, flag: Flag): Boolean = cpu.run {
    status and flag.bitMask > 0u
}

fun onFlag(cpu: Cpu, flag: Flag, cond: Boolean, block: () -> Unit) {
    when (getFlag(cpu, flag) == cond) {
        true -> block()
        false -> Unit
    }
}

fun flagsOf(status: U8, reg: U8, vararg functions: (status: U8, reg: U8) -> U8) =
    functions.fold(status) { acc, fn -> fn(acc, reg) }
