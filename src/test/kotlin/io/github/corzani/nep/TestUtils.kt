package io.github.corzani.nep

import kotlin.test.asserter

sealed class Base(val base: Int, val places: Int, val prefix: String)
object Hex : Base(16, 2, "0x")
object Bin : Base(2, 8, "0b")

// TODO This should be smarter than a copy/paste
fun u8toString(value: U8, type: Base) = "${type.prefix}${value.toString(type.base).padStart(type.places, '0')}"
fun toU8HexBin(value: U8) = listOf(Hex, Bin).map { u8toString(value, it) }.joinToString(separator = " <-> ")

fun u16toString(value: U16, type: Base) = "${type.prefix}${value.toString(type.base).padStart(type.places * 2, '0')}"
fun toU16HexBin(value: U16) = listOf(Hex, Bin).map { u16toString(value, it) }.joinToString(separator = " <-> ")

fun assertMemoryEquals(expected: U8, actual: U8) = asserter.assertTrue(
    { "Expected <${toU8HexBin(expected)} >, actual <${toU8HexBin(actual)}>" },
    actual == expected
)

fun assertMemoryEquals(expected: U16, actual: U16) = asserter.assertTrue(
    { "Expected <${toU16HexBin(expected)} >, actual <${toU16HexBin(actual)}>" },
    actual == expected
)
