package io.github.corzani.nep

import io.github.corzani.nep.mappers.Mapper
import io.github.corzani.nep.mappers.mapper

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

fun hasTrainer(header: UByteArray) = header[6].isBitSet(2)
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
fun prgRomStart(header: UByteArray) = NES_HEADER_SIZE + if (hasTrainer(header)) 516 else 0
fun chrRomStart(header: UByteArray) = prgRomStart(header) + prgRomSize(header)

data class Rom(
    val prg: Memory,
    val chr: Memory,
    val mapperId: Int,
    val mapper: Mapper,
    val screenMirroring: ScreenMirroring,
    val prgSize: Int
)

enum class ScreenMirroring {
    Vertical, Horizontal, FourScreen
}

data class NesHeader(
    val prgRomSize: Int,
    val chrRomSize: Int,
    val mapper: U8,
    val mirroring: ScreenMirroring,
    val battery: Int,
    val trainer: Boolean
)

fun headerOf(memory: Memory): NesHeader = memory.let {
    check(isNesRomHeader(memory))

    NesHeader(
        prgRomSize = prgRomSize(it),
        chrRomSize = chrRomSize(it),
        mapper = mapperType(it),
        mirroring = screenMirroring(it),
        battery = 0, // TODO What is this?
        trainer = hasTrainer(it)
    )
}

fun rom(memory: UByteArray): Rom {
    val header = headerOf(memory)
    val mapperId = mapperType(memory).toInt()

    println(header)

    return Rom(
        prg = memory.copyOfRange(prgRomStart(memory), memory.size),
        prgSize = prgRomSize(memory), // Mapping purposes
        chr = memory.copyOf(chrRomSize(memory)),
        mapperId = mapperId,
        mapper = mapper(mapperId, header),
        screenMirroring = screenMirroring(memory)
    )
}
