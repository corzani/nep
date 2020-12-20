package io.github.corzani.nep.cpu

import io.github.corzani.nep.*
import io.github.corzani.nep.ppu.Ppu

const val RAM_SIZE = 1024 * 64
const val CARTRIDGE_ROM_ADDRESS = 0x8000

typealias Instruction = (opcode: U8) -> Int

class Cpu(
    var accumulator: U8 = 0x00u,
    var x: U8 = 0x00u,
    var y: U8 = 0x00u,
    var stackPointer: U8 = 0x00u,
    var status: U8 = 0x00u,
    val cartSize: Int = 0,
    val bus: Bus,
    var pc: U16 = u16(ROM), //bus.read16(0xFFFCu),
    var cycles: Int = 0
) {
    fun incrementPcBy(value: Int) {
        pc = u16(pc + u16(value))
    }

    fun read(address: U16): U8 = bus.read(address)
    fun write(address: U16, data: U8) = bus.write(address, data)
    fun stackPush(data: U8) = stackPush(this, data)
    fun stackPush(data: U16) = stackPush(this, data)
    fun stackPop8() = stackPop8(this)
    fun stackPop16() = stackPop16(this)

    fun runTest() =
        testLoop(
            this,
            instructionHandler(this, opcodes()),
            u16(CARTRIDGE_ROM_ADDRESS + this.cartSize - NES_HEADER_SIZE)
        )

    fun getAddressFrom(addressMode: AddressMode) = addressMode.address(this)

    fun fetchFrom(addressMode: Address) =
        FetchedAddress(fetched = this.read(addressMode.address), address = addressMode.address)

    fun setFlag(flag: Flag, value: Boolean) {
        status = retrieveFlag(status, flag, value)
    }

    fun setNegativeFlag(cond: Boolean) = setFlag(Flag.N, cond)
    fun setOverflowFlag(cond: Boolean) = setFlag(Flag.V, cond)
    fun setBreakFlag(cond: Boolean) = setFlag(Flag.B, cond)
    fun setDecimalFlag(cond: Boolean) = setFlag(Flag.D, cond)
    fun setInterruptFlag(cond: Boolean) = setFlag(Flag.I, cond)
    fun setZeroFlag(cond: Boolean) = setFlag(Flag.Z, cond)
    fun setCarryFlag(cond: Boolean) = setFlag(Flag.C, cond)

    fun getFlag(flag: Flag): Boolean = this.status and flag.bitMask > 0u

    fun setFlagsFrom(reg: U8, vararg functions: (status: U8, reg: U8) -> U8) {
        status = flagsOf(status, reg, *functions)
    }

    fun start() = mainLoop(this, instructionHandler(this, opcodes()))

    fun interruptNmi() {
        stackPush(pc)
        val statusA: U8 = (status or Flag.B.bitMask) and Flag.U.bitMask.inv()

        stackPush(statusA)

        status = withFlag(status, Flag.I.bitMask, true)

        bus.tick(2)
        pc = bus.read16(0xFFFAu)
    }
}

//fun NesArch.initRam() {
//    bus.write16(0xFFFCu, 0x8000u)
//}


fun initRam(ram: Memory) {
    write16(ram, 0xFFFCu, 0x8000u)
}

// TODO Silly Implementation
fun loadFromMemory(program: Program): Cpu = rom(program.copyOf(ROM_SIZE)).let { rom ->
    Cpu(
        cartSize = program.size,
        bus = Bus(
            rom = rom,
            ppu = Ppu(
                mirroring = rom.screenMirroring,
                chrRom = rom.chr
            )
        )
    )
}

// TODO Silly Implementation
fun loadFromMemory(program: Program, block: Cpu.() -> Unit) =
    loadFromMemory(program).run {
        block(this)
    }
