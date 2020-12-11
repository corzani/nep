package io.github.corzani.nep.bdd.cpu

import io.cucumber.java8.En
import io.cucumber.java8.PendingException
import io.github.corzani.nep.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

var lastInstance: NesArch? = null

sealed class Register()
object Accumulator : Register()
object X : Register()
object Y : Register()

class BddRules : En {

    init {
        ParameterType("register", "Accumulator|X|Y") { register ->
            when (register) {
                "Accumulator" -> Accumulator
                "X" -> X
                "Y" -> Y
                else -> throw PendingException()
            }
        }

        ParameterType("binOrHex", "0[xX][0-9a-fA-F]+|0[bB][0-1]+") { num ->
            when {
                num.toLowerCase().startsWith("0x") -> Integer.decode(num)
                num.toLowerCase().startsWith("0b") -> 0
                else -> throw PendingException()
            }
        }

        ParameterType("flag", "C|Z|I|D|B|U|V|N") { flag ->
            when (flag) {
                "C" -> Flag.C
                "Z" -> Flag.Z
                "I" -> Flag.I
                "D" -> Flag.D
                "B" -> Flag.B
                "U" -> Flag.U
                "V" -> Flag.V
                "N" -> Flag.N
                else -> throw PendingException()
            }
        }

        ParameterType("enableable", "ENABLED|DISABLED") { value ->
            when (value) {
                "ENABLED" -> true
                "DISABLED" -> false
                else -> throw PendingException()
            }
        }

        When("code is executed") {
            checkNotNull(lastInstance, { "Cartridge Memory NOT loaded" })
            lastInstance!!.test()
        }

        Given("ROM memory {string}") { str: String ->
            val mem = str.split(' ').map { Integer.decode(it).toUByte() }.toUByteArray()

            lastInstance = memoryOf(*mem).let(::loadFromMemory)
        }

        And("{register} register is {binOrHex}") { register: Register, num: Int ->
            checkNotNull(lastInstance)
            when (register) {
                Accumulator -> lastInstance!!.accumulator = u8(num)
                X -> lastInstance!!.x = u8(num)
                Y -> lastInstance!!.y = u8(num)
            }
        }

        And("{binOrHex} is stored at address {binOrHex}") { data: Int, address: Int ->
            checkNotNull(lastInstance)
            lastInstance!!.write(u16(address), u8(data))
        }

        Then("{register} register should be {binOrHex}") { register: Register, num: Int ->
            checkNotNull(lastInstance)
            val expect = u8(num)
            when (register) {
                Accumulator -> assertMemoryEquals(expect, lastInstance!!.accumulator)
                X -> assertMemoryEquals(expect, lastInstance!!.x)
                Y -> assertMemoryEquals(expect, lastInstance!!.y)
            }
        }

        And("{flag} flag should be {enableable}") { flag: Flag, be: Boolean ->
            checkNotNull(lastInstance)
            when (be) {
                true -> assertTrue(
                    (lastInstance!!.status and flag.bitMask).compareTo(0u) > 0,
                    "Flag ${flag} should be ENABLED but seems to be DISABLED"
                )
                false -> assertTrue(
                    (lastInstance!!.status and flag.bitMask).compareTo(0u) == 0,
                    "Flag ${flag} should be DISABLED but seems to be ENABLED"
                )
            }
        }

        And("CPU should have performed {int} cycles") { cycles: Int ->
            checkNotNull(lastInstance)
            assertEquals(cycles, lastInstance!!.cycles)
        }
    }
}