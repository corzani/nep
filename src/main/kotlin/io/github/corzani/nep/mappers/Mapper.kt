package io.github.corzani.nep.mappers

import io.github.corzani.nep.NesHeader
import io.github.corzani.nep.U16

data class MappedAddress(val address: U16, val pageEx: Boolean)

typealias MapperFn = (U16) -> MappedAddress

data class Mapper(
    val readMemAddress: MapperFn,
    val writeMem: MapperFn,
    val readPpu: MapperFn,
    val writePpu: MapperFn
)

fun mapper(mapper: Int, nesHeader: NesHeader) = when (mapper) {
    0 -> nRom(nesHeader)
    else -> TODO("Mapper not implemented... Yet...")
}