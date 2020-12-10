package io.github.corzani.nep.bdd

import io.cucumber.java8.En
import io.cucumber.java8.PendingException
import io.github.corzani.nep.*
import kotlin.test.assertEquals

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


//        Given("this data table:") { peopleTable: DataTable ->
//            val people: List<Person> = peopleTable.asList(Person::class.java)
//            assertEquals("Aslak", people[0].first)
//            assertEquals("Hellesy", people[0].last)
//        }
//
//        val alreadyHadThisManyCukes = 1
//        Given("I have {long} cukes in my belly") { n: Long ->
//            assertEquals(1, alreadyHadThisManyCukes)
//            assertEquals(42L, n)
//        }

        When("code is executed") {
            checkNotNull(lastInstance, { "Cartridge Memory NOT loaded" })
            lastInstance!!.test()
        }

        Given("ROM memory {string}") { str: String ->
            val mem = str.split(' ').map { Integer.decode(it).toUByte() }.toUByteArray()

            lastInstance = memoryOf(*mem).let(::loadFromMemory)
        }


        Then("the {} register is {int}") {

        }
        And("{register} register is {binOrHex}") { register: Register, num: Int ->
            checkNotNull(lastInstance)
            when (register) {
                Accumulator -> lastInstance!!.accumulator = u8(num)
                X -> lastInstance!!.x = u8(num)
                Y -> lastInstance!!.y = u8(num)
            }
        }

        Then("{register} register should be {binOrHex}") { register: Register, num: Int ->
            checkNotNull(lastInstance)
            val expect = u8(num)
            when (register) {
                Accumulator -> assertEquals(expect, lastInstance!!.accumulator)
                X -> assertEquals(expect, lastInstance!!.x)
                Y -> assertEquals(expect, lastInstance!!.y)
            }
        }

        And("{flag} flag should be {boolean}") { flag: Flag, be: Boolean ->

        }
    }
}

data class Person(val first: String?, val last: String?)