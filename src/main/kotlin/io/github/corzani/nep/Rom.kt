package io.github.corzani.nep

const val NES_HEADER_SIZE = 16

const val PRG_PAGE_SIZE = 16 * 1024
const val CHR_PAGE_SIZE = 8 * 1024

val testRomHeader = mem(
    0X4eU, 0X45U, 0X53U, 0X1AU, // Magic
    0X01U, 0X01U, 0X00U, 0X00U, 0X00U, 0X00U, // Something useful here
    0X00U, 0X00U, 0X00U, 0X00U, 0X00U, 0X00U // Reserved Unused
)

fun isNesRomHeader(header: UByteArray) =
    header.slice(IntRange(0, 3)) == ubyteArrayOf(0x4Eu, 0x45u, 0x53u, 0x1Au).toList()

fun skipTrainer(header: UByteArray) = header[6].isBitSet(2)
fun mapperType(header: UByteArray) = (header[7] and 0xF0u) or (header[6].rotateRight(4))
fun fourScreen(header: UByteArray) = header[6].isBitSet(3)
fun verticalMirroring(header: UByteArray) = header[6].isBitSet(0)
fun screenMirroring(header: UByteArray): ScreenMirroring {
    val fourScreen = fourScreen(header)
    val verticalMirroring = verticalMirroring(header)

    return when {
        fourScreen -> ScreenMirroring.FourScreen
        verticalMirroring -> ScreenMirroring.Vertical
        else -> ScreenMirroring.Horizontal
    }
}

fun prgRomSize(header: UByteArray) = header[4].toInt() * PRG_PAGE_SIZE
fun chrRomSize(header: UByteArray) = header[5].toInt() * CHR_PAGE_SIZE
fun prgRomStart(header: UByteArray) = NES_HEADER_SIZE + if (skipTrainer(header)) 516 else 0
fun chrRomStart(header: UByteArray) = prgRomStart(header) + prgRomSize(header)

data class Rom(
    val prg: Memory,
    val chr: Memory,
    val mapper: Int,
    val screenMirroring: ScreenMirroring,
    val prgSize: Int
)

enum class ScreenMirroring {
    Vertical, Horizontal, FourScreen
}

fun rom(memory: UByteArray): Rom {
    check(isNesRomHeader(memory))

    val rom = Rom(
        prg = memory.copyOfRange(prgRomStart(memory), memory.size),
        prgSize = prgRomSize(memory), // Mapping purposes
        chr = memory.copyOf(chrRomSize(memory)),
        mapper = mapperType(memory).toInt(),
        screenMirroring = screenMirroring(memory)
    )
    return rom
}
