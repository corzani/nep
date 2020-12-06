typealias Ram = UByteArray
typealias U8 = UByte
typealias U16 = UShort

data class U16(val lo: U8, val hi: U8)

const val RAM_SIZE = 1024 * 64

data class CPU(
        var accumulator: U8,
        var x: U8,
        var y: U8,
        var stackpointer: U8,
        var pc: U16,
        var status: U8
)

data class Nes(
        val ram: Ram,
        val cpu: CPU
)

data class loHi16(val lo: U8, val hi: U8)

data class Opcode(val name: String, val istruction: (Nes) -> Nes, val memorySomething: (Nes) -> Nes, val cycles: Int)

//fun Immediate(nes: Nes) = nes.cpu.pc
//fun ZeroPage(nes: Nes) = read16(nes.ram, nes.cpu.pc)
//fun ZeroPageX(nes: Nes) = ZeroPage(nes)

//val a = Opcode("BRK", BRK, ::IMM, 7)

//
//const opcodes = listOf(
//Opcode("BRK", BRK, ::IMM, 7), Opcode("ORA", ORA, IZX, 6), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 3), Opcode("ORA", ORA, ZP0, 3), Opcode("ASL", ASL, ZP0, 5), Opcode("???", XXX, IMP, 5), Opcode("PHP", PHP, IMP, 3), Opcode("ORA", ORA, IMM, 2), Opcode("ASL", ASL, IMP, 2), Opcode("???", XXX, IMP, 2), Opcode("???", NOP, IMP, 4), Opcode("ORA", ORA, ABS, 4), Opcode("ASL", ASL, ABS, 6), Opcode("???", XXX, IMP, 6),
//Opcode("BPL", BPL, REL, 2), Opcode("ORA", ORA, IZY, 5), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 4), Opcode("ORA", ORA, ZPX, 4), Opcode("ASL", ASL, ZPX, 6), Opcode("???", XXX, IMP, 6), Opcode("CLC", CLC, IMP, 2), Opcode("ORA", ORA, ABY, 4), Opcode("???", NOP, IMP, 2), Opcode("???", XXX, IMP, 7), Opcode("???", NOP, IMP, 4), Opcode("ORA", ORA, ABX, 4), Opcode("ASL", ASL, ABX, 7), Opcode("???", XXX, IMP, 7),
//Opcode("JSR", JSR, ABS, 6), Opcode("AND", AND, IZX, 6), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("BIT", BIT, ZP0, 3), Opcode("AND", AND, ZP0, 3), Opcode("ROL", ROL, ZP0, 5), Opcode("???", XXX, IMP, 5), Opcode("PLP", PLP, IMP, 4), Opcode("AND", AND, IMM, 2), Opcode("ROL", ROL, IMP, 2), Opcode("???", XXX, IMP, 2), Opcode("BIT", BIT, ABS, 4), Opcode("AND", AND, ABS, 4), Opcode("ROL", ROL, ABS, 6), Opcode("???", XXX, IMP, 6),
//Opcode("BMI", BMI, REL, 2), Opcode("AND", AND, IZY, 5), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 4), Opcode("AND", AND, ZPX, 4), Opcode("ROL", ROL, ZPX, 6), Opcode("???", XXX, IMP, 6), Opcode("SEC", SEC, IMP, 2), Opcode("AND", AND, ABY, 4), Opcode("???", NOP, IMP, 2), Opcode("???", XXX, IMP, 7), Opcode("???", NOP, IMP, 4), Opcode("AND", AND, ABX, 4), Opcode("ROL", ROL, ABX, 7), Opcode("???", XXX, IMP, 7),
//Opcode("RTI", RTI, IMP, 6), Opcode("EOR", EOR, IZX, 6), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 3), Opcode("EOR", EOR, ZP0, 3), Opcode("LSR", LSR, ZP0, 5), Opcode("???", XXX, IMP, 5), Opcode("PHA", PHA, IMP, 3), Opcode("EOR", EOR, IMM, 2), Opcode("LSR", LSR, IMP, 2), Opcode("???", XXX, IMP, 2), Opcode("JMP", JMP, ABS, 3), Opcode("EOR", EOR, ABS, 4), Opcode("LSR", LSR, ABS, 6), Opcode("???", XXX, IMP, 6),
//Opcode("BVC", BVC, REL, 2), Opcode("EOR", EOR, IZY, 5), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 4), Opcode("EOR", EOR, ZPX, 4), Opcode("LSR", LSR, ZPX, 6), Opcode("???", XXX, IMP, 6), Opcode("CLI", CLI, IMP, 2), Opcode("EOR", EOR, ABY, 4), Opcode("???", NOP, IMP, 2), Opcode("???", XXX, IMP, 7), Opcode("???", NOP, IMP, 4), Opcode("EOR", EOR, ABX, 4), Opcode("LSR", LSR, ABX, 7), Opcode("???", XXX, IMP, 7),
//Opcode("RTS", RTS, IMP, 6), Opcode("ADC", ADC, IZX, 6), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 3), Opcode("ADC", ADC, ZP0, 3), Opcode("ROR", ROR, ZP0, 5), Opcode("???", XXX, IMP, 5), Opcode("PLA", PLA, IMP, 4), Opcode("ADC", ADC, IMM, 2), Opcode("ROR", ROR, IMP, 2), Opcode("???", XXX, IMP, 2), Opcode("JMP", JMP, IND, 5), Opcode("ADC", ADC, ABS, 4), Opcode("ROR", ROR, ABS, 6), Opcode("???", XXX, IMP, 6),
//Opcode("BVS", BVS, REL, 2), Opcode("ADC", ADC, IZY, 5), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 4), Opcode("ADC", ADC, ZPX, 4), Opcode("ROR", ROR, ZPX, 6), Opcode("???", XXX, IMP, 6), Opcode("SEI", SEI, IMP, 2), Opcode("ADC", ADC, ABY, 4), Opcode("???", NOP, IMP, 2), Opcode("???", XXX, IMP, 7), Opcode("???", NOP, IMP, 4), Opcode("ADC", ADC, ABX, 4), Opcode("ROR", ROR, ABX, 7), Opcode("???", XXX, IMP, 7),
//Opcode("???", NOP, IMP, 2), Opcode("STA", STA, IZX, 6), Opcode("???", NOP, IMP, 2), Opcode("???", XXX, IMP, 6), Opcode("STY", STY, ZP0, 3), Opcode("STA", STA, ZP0, 3), Opcode("STX", STX, ZP0, 3), Opcode("???", XXX, IMP, 3), Opcode("DEY", DEY, IMP, 2), Opcode("???", NOP, IMP, 2), Opcode("TXA", TXA, IMP, 2), Opcode("???", XXX, IMP, 2), Opcode("STY", STY, ABS, 4), Opcode("STA", STA, ABS, 4), Opcode("STX", STX, ABS, 4), Opcode("???", XXX, IMP, 4),
//Opcode("BCC", BCC, REL, 2), Opcode("STA", STA, IZY, 6), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 6), Opcode("STY", STY, ZPX, 4), Opcode("STA", STA, ZPX, 4), Opcode("STX", STX, ZPY, 4), Opcode("???", XXX, IMP, 4), Opcode("TYA", TYA, IMP, 2), Opcode("STA", STA, ABY, 5), Opcode("TXS", TXS, IMP, 2), Opcode("???", XXX, IMP, 5), Opcode("???", NOP, IMP, 5), Opcode("STA", STA, ABX, 5), Opcode("???", XXX, IMP, 5), Opcode("???", XXX, IMP, 5),
//Opcode("LDY", LDY, IMM, 2), Opcode("LDA", LDA, IZX, 6), Opcode("LDX", LDX, IMM, 2), Opcode("???", XXX, IMP, 6), Opcode("LDY", LDY, ZP0, 3), Opcode("LDA", LDA, ZP0, 3), Opcode("LDX", LDX, ZP0, 3), Opcode("???", XXX, IMP, 3), Opcode("TAY", TAY, IMP, 2), Opcode("LDA", LDA, IMM, 2), Opcode("TAX", TAX, IMP, 2), Opcode("???", XXX, IMP, 2), Opcode("LDY", LDY, ABS, 4), Opcode("LDA", LDA, ABS, 4), Opcode("LDX", LDX, ABS, 4), Opcode("???", XXX, IMP, 4),
//Opcode("BCS", BCS, REL, 2), Opcode("LDA", LDA, IZY, 5), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 5), Opcode("LDY", LDY, ZPX, 4), Opcode("LDA", LDA, ZPX, 4), Opcode("LDX", LDX, ZPY, 4), Opcode("???", XXX, IMP, 4), Opcode("CLV", CLV, IMP, 2), Opcode("LDA", LDA, ABY, 4), Opcode("TSX", TSX, IMP, 2), Opcode("???", XXX, IMP, 4), Opcode("LDY", LDY, ABX, 4), Opcode("LDA", LDA, ABX, 4), Opcode("LDX", LDX, ABY, 4), Opcode("???", XXX, IMP, 4),
//Opcode("CPY", CPY, IMM, 2), Opcode("CMP", CMP, IZX, 6), Opcode("???", NOP, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("CPY", CPY, ZP0, 3), Opcode("CMP", CMP, ZP0, 3), Opcode("DEC", DEC, ZP0, 5), Opcode("???", XXX, IMP, 5), Opcode("INY", INY, IMP, 2), Opcode("CMP", CMP, IMM, 2), Opcode("DEX", DEX, IMP, 2), Opcode("???", XXX, IMP, 2), Opcode("CPY", CPY, ABS, 4), Opcode("CMP", CMP, ABS, 4), Opcode("DEC", DEC, ABS, 6), Opcode("???", XXX, IMP, 6),
//Opcode("BNE", BNE, REL, 2), Opcode("CMP", CMP, IZY, 5), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 4), Opcode("CMP", CMP, ZPX, 4), Opcode("DEC", DEC, ZPX, 6), Opcode("???", XXX, IMP, 6), Opcode("CLD", CLD, IMP, 2), Opcode("CMP", CMP, ABY, 4), Opcode("NOP", NOP, IMP, 2), Opcode("???", XXX, IMP, 7), Opcode("???", NOP, IMP, 4), Opcode("CMP", CMP, ABX, 4), Opcode("DEC", DEC, ABX, 7), Opcode("???", XXX, IMP, 7),
//Opcode("CPX", CPX, IMM, 2), Opcode("SBC", SBC, IZX, 6), Opcode("???", NOP, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("CPX", CPX, ZP0, 3), Opcode("SBC", SBC, ZP0, 3), Opcode("INC", INC, ZP0, 5), Opcode("???", XXX, IMP, 5), Opcode("INX", INX, IMP, 2), Opcode("SBC", SBC, IMM, 2), Opcode("NOP", NOP, IMP, 2), Opcode("???", SBC, IMP, 2), Opcode("CPX", CPX, ABS, 4), Opcode("SBC", SBC, ABS, 4), Opcode("INC", INC, ABS, 6), Opcode("???", XXX, IMP, 6),
//Opcode("BEQ", BEQ, REL, 2), Opcode("SBC", SBC, IZY, 5), Opcode("???", XXX, IMP, 2), Opcode("???", XXX, IMP, 8), Opcode("???", NOP, IMP, 4), Opcode("SBC", SBC, ZPX, 4), Opcode("INC", INC, ZPX, 6), Opcode("???", XXX, IMP, 6), Opcode("SED", SED, IMP, 2), Opcode("SBC", SBC, ABY, 4), Opcode("NOP", NOP, IMP, 2), Opcode("???", XXX, IMP, 7), Opcode("???", NOP, IMP, 4), Opcode("SBC", SBC, ABX, 4), Opcode("INC", INC, ABX, 7), Opcode("???", XXX, IMP, 7),
//)

//fun Init() = Nes(ram = Ram(RAM_SIZE), CPU(0u, 0u, 0u, 0u, 0, 0u))

enum class Flag(val bitMask: U8) {
    C(0x01u),
    Z(0x02u),
    I(0x04u),
    D(0x08u),
    B(0x10u),
    U(0x20u),
    V(0x40u),
    N(0x80u),
}


fun read(ram: Ram, address: U16): U8 = ram[address.toInt()]
fun Nes.read(address: U16): U8 = read(ram, address)

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun read16(ram: Ram, address: U16): U16 = ram[address.toInt() + 1].toUShort().rotateLeft(8) or ram[address.toInt()].toUShort()

fun toLoHiByte(data: U16) =

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun write16(ram: Ram, address: U16, data: U16) =

//ram[address.toInt() + 1].toUShort().rotateLeft(8) or ram[address.toInt()].toUShort()

@ExperimentalStdlibApi
fun Nes.read16(address: U16): U16 = read16(ram, address)

fun writeFlag(ram: Ram, address: Int, data: U8) = ram.set(address, data)
fun Nes.writeFlag(address: Int, data: U8) = writeFlag(ram, address, data)

//
//fun getFlag(status: U8, flag: Flag): Boolean = status.and(flag.bitMask).toInt() > 0;
//fun Nes.getFlag(flag: Flag): Boolean = getFlag(status, flag)
//
//fun retrieveFlag(status: U8, flag: Flag, clear: Boolean = true) = when (clear) {
//    true -> status or (flag.bitMask)
//    false -> status and (flag.bitMask.inv())
//}
//
//fun Nes.setFlag(flag: Flag, clear: Boolean = true) = this.apply {
//    status = retrieveFlag(status, flag, clear)
//}
//
//fun main(args: Array<String>) {
//    val nes = Init().apply {
//        setFlag(Flag.C)
//        setFlag(Flag.N)
//    }
//    println(nes.status)
//}