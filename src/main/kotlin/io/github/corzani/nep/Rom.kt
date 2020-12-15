package io.github.corzani.nep

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

fun prgRomSize(header: UByteArray) = header[4].toInt()
fun chrRomSize(header: UByteArray) = header[5].toInt()

fun prgRomStart(header: UByteArray) = 16 + if (skipTrainer(header)) 512 else 0
fun chrRomStart(header: UByteArray) = prgRomStart(header) + prgRomSize(header)

data class Rom(
    val prg: UByteArray,
    val chr: UByteArray,
    val mapper: Int,
    val screenMirroring: ScreenMirroring
)

enum class ScreenMirroring {
    Vertical, Horizontal, FourScreen
}

fun rom(memory: UByteArray): Rom {
//    check(isNesRomHeader(memory))

    return Rom(
        prg = memory,//memory.sliceArray(IntRange(prgRomStart(memory), prgRomSize(memory))),
        chr = memory.sliceArray(IntRange(chrRomStart(memory), chrRomSize(memory))),
        mapper = mapperType(memory).toInt(),
        screenMirroring = screenMirroring(memory)
    )
}
