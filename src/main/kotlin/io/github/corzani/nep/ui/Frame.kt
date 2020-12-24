package io.github.corzani.nep.ui

import io.github.corzani.nep.Memory
import io.github.corzani.nep.U8
import io.github.corzani.nep.get

typealias RGB = Triple<U8, U8, U8>

val a = arrayListOf(
    RGB(0x80u, 0x80u, 0x80u),
    RGB(0x00u, 0x3Du, 0xA6u),
    RGB(0x00u, 0x12u, 0xB0u),
    RGB(0x44u, 0x00u, 0x96u),
    RGB(0xA1u, 0x00u, 0x5Eu),
    RGB(0xC7u, 0x00u, 0x28u),
    RGB(0xBAu, 0x06u, 0x00u),
    RGB(0x8Cu, 0x17u, 0x00u),
    RGB(0x5Cu, 0x2Fu, 0x00u),
    RGB(0x10u, 0x45u, 0x00u),
    RGB(0x05u, 0x4Au, 0x00u),
    RGB(0x00u, 0x47u, 0x2Eu),
    RGB(0x00u, 0x41u, 0x66u),
    RGB(0x00u, 0x00u, 0x00u),
    RGB(0x05u, 0x05u, 0x05u),
    RGB(0x05u, 0x05u, 0x05u),
    RGB(0xC7u, 0xC7u, 0xC7u),
    RGB(0x00u, 0x77u, 0xFFu),
    RGB(0x21u, 0x55u, 0xFFu),
    RGB(0x82u, 0x37u, 0xFAu),
    RGB(0xEBu, 0x2Fu, 0xB5u),
    RGB(0xFFu, 0x29u, 0x50u),
    RGB(0xFFu, 0x22u, 0x00u),
    RGB(0xD6u, 0x32u, 0x00u),
    RGB(0xC4u, 0x62u, 0x00u),
    RGB(0x35u, 0x80u, 0x00u),
    RGB(0x05u, 0x8Fu, 0x00u),
    RGB(0x00u, 0x8Au, 0x55u),
    RGB(0x00u, 0x99u, 0xCCu),
    RGB(0x21u, 0x21u, 0x21u),
    RGB(0x09u, 0x09u, 0x09u),
    RGB(0x09u, 0x09u, 0x09u),
    RGB(0xFFu, 0xFFu, 0xFFu),
    RGB(0x0Fu, 0xD7u, 0xFFu),
    RGB(0x69u, 0xA2u, 0xFFu),
    RGB(0xD4u, 0x80u, 0xFFu),
    RGB(0xFFu, 0x45u, 0xF3u),
    RGB(0xFFu, 0x61u, 0x8Bu),
    RGB(0xFFu, 0x88u, 0x33u),
    RGB(0xFFu, 0x9Cu, 0x12u),
    RGB(0xFAu, 0xBCu, 0x20u),
    RGB(0x9Fu, 0xE3u, 0x0Eu),
    RGB(0x2Bu, 0xF0u, 0x35u),
    RGB(0x0Cu, 0xF0u, 0xA4u),
    RGB(0x05u, 0xFBu, 0xFFu),
    RGB(0x5Eu, 0x5Eu, 0x5Eu),
    RGB(0x0Du, 0x0Du, 0x0Du),
    RGB(0x0Du, 0x0Du, 0x0Du),
    RGB(0xFFu, 0xFFu, 0xFFu),
    RGB(0xA6u, 0xFCu, 0xFFu),
    RGB(0xB3u, 0xECu, 0xFFu),
    RGB(0xDAu, 0xABu, 0xEBu),
    RGB(0xFFu, 0xA8u, 0xF9u),
    RGB(0xFFu, 0xABu, 0xB3u),
    RGB(0xFFu, 0xD2u, 0xB0u),
    RGB(0xFFu, 0xEFu, 0xA6u),
    RGB(0xFFu, 0xF7u, 0x9Cu),
    RGB(0xD7u, 0xE8u, 0x95u),
    RGB(0xA6u, 0xEDu, 0xAFu),
    RGB(0xA2u, 0xF2u, 0xDAu),
    RGB(0x99u, 0xFFu, 0xFCu),
    RGB(0xDDu, 0xDDu, 0xDDu),
    RGB(0x11u, 0x11u, 0x11u),
    RGB(0x11u, 0x11u, 0x11u)
)

data class Frame(
    val width: Int = 256,
    val height: Int = 240,
    var data: Memory = Memory(width * height * 3)
) {
    fun setPixel(x: Int, y: Int, pixel: RGB) {
        val base = y * 3 * width + x * 3
        if (base + 2 < data.size) {
            data[base] = pixel.first
            data[base + 1] = pixel.second
            data[base + 2] = pixel.third
        }
    }
}

fun showTitle(chrRom: Memory, bank: U8, tileN: Int): List<U8> {
    check(bank <= 1u)

    val bankIdx = (bank * 0x1000u).toInt()

    val tile = chrRom.copyOfRange((bankIdx + tileN * 16), (bankIdx + tileN * 16) + 16)

    return (0..7).flatMap { y ->
        val current = tile[y]
        val currentA = tile[y + 8]
        mix(a, current, currentA).map {
            listOf(it.first, it.second, it.third)
        }.flatten()
    }
}

fun mix(palette: ArrayList<RGB>, current: U8, currentA: U8): List<RGB> = (0..7).map { index ->
    when (Pair(current[index], currentA[index])) {
        Pair(first = false, second = false) -> palette[1]
        Pair(first = true, second = false) -> palette[0x23]
        Pair(first = false, second = true) -> palette[0x27]
        Pair(first = true, second = true) -> palette[0x30]
        else -> throw IllegalStateException("This color is Awful, I won't print it")
    }
}



