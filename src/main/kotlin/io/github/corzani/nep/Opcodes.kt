package io.github.corzani.nep

fun opImpl(possibleAdditionalCycle: Boolean, block: NesArch.() -> Unit): (NesArch) -> Boolean =
    fun(nesArch: NesArch): Boolean {
        block(nesArch)
        return possibleAdditionalCycle
    }

fun adc(addressMode: AddressMode) = opImpl(true) {
    val (fetched) = addressMode.address(this)
    val sum = u16(fetched + accumulator + (status and 0x80u).rotateLeft(1))
    val sumLo8 = sum.lo8()

    setCarryFlag(sum > 0xFFu)
    setOverflowFlag(
        ((u16(accumulator) xor fetched).inv() and (u16(accumulator) xor sum) and 0x0080u) > 0x00u
    )
    setFlagsFrom(sumLo8, ::zeroFlag, ::negativeFlag)
    accumulator = sumLo8
}

fun and(addressMode: AddressMode) = opImpl(false) {
    val (fetched) = addressMode.address(this)
    read(fetched).let { address ->
        accumulator = accumulator and address
        setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
    }
}

fun asl(addressMode: AddressMode) = opImpl(false) {

    fun doShiftLeft(data: U8): U8 {
        setCarryFlag(data.isBitSet(7))
        return (data.rotateLeft(1) and 0xFEu).also {
            setFlagsFrom(it, ::zeroFlag, ::negativeFlag)
        }
    }

    when (addressMode) {
        Implied -> doShiftLeft(accumulator).also { accumulator = it }
        else -> {
            val (fetched) = addressMode.address(this)
            read(fetched).let(::doShiftLeft).also {
                write(fetched, it)
            }
        }
    }
}

fun bcc(addressMode: AddressMode) = opImpl(false) {
    branchOnFlag(addressMode, Flag.C, false)

}

fun bcs(addressMode: AddressMode) = opImpl(false) {
    branchOnFlag(addressMode, Flag.C, true)

}

fun beq(addressMode: AddressMode) = opImpl(false) {
    branchOnFlag(addressMode, Flag.Z, true)

}

fun bit(addressMode: AddressMode) = opImpl(false) {
    TODO()
}

fun bmi(addressMode: AddressMode) = opImpl(false) {
    branchOnFlag(addressMode, Flag.N, true)

}

fun bne(addressMode: AddressMode) = opImpl(false) {
    branchOnFlag(addressMode, Flag.C, false)

}

fun bpl(addressMode: AddressMode) = opImpl(false) {
    branchOnFlag(addressMode, Flag.N, false)

}

fun brk(addressMode: AddressMode) = opImpl(false) {
}

fun bvc(addressMode: AddressMode) = opImpl(false) {
    branchOnFlag(addressMode, Flag.Z, false)

}

fun bvs(addressMode: AddressMode) = opImpl(false) {
    branchOnFlag(addressMode, Flag.V, true)

}

fun clc(addressMode: AddressMode) = opImpl(false) {
    setCarryFlag(false)

}

fun cld(addressMode: AddressMode) = opImpl(false) {
    setDecimalFlag(false)
}

fun cli(addressMode: AddressMode) = opImpl(false) {
    setInterruptFlag(false)
}

fun clv(addressMode: AddressMode) = opImpl(false) {
    setOverflowFlag(false)

}

fun cmp(addressMode: AddressMode) = opImpl(false) { }
fun cpx(addressMode: AddressMode) = opImpl(false) { }
fun cpy(addressMode: AddressMode) = opImpl(false) { }
fun dec(addressMode: AddressMode) = opImpl(false) { }
fun dex(addressMode: AddressMode) = opImpl(false) { }
fun dey(addressMode: AddressMode) = opImpl(false) { }
fun eor(addressMode: AddressMode) = opImpl(false) { }
fun inc(addressMode: AddressMode) = opImpl(false) { }
fun inx(addressMode: AddressMode) = opImpl(false) { }
fun iny(addressMode: AddressMode) = opImpl(false) { }
fun jmp(addressMode: AddressMode) = opImpl(false) { }
fun jsr(addressMode: AddressMode) = opImpl(false) { }

fun lda(addressMode: AddressMode) = opImpl(false) {
    val (fetched) = addressMode.address(this)
    read(fetched).let { data -> accumulator = data }
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun ldx(addressMode: AddressMode) = opImpl(true) {
    val (fetched) = addressMode.address(this)
    x = read(fetched)
    setFlagsFrom(x, ::zeroFlag, ::negativeFlag)
}

fun ldy(addressMode: AddressMode) = opImpl(true) {
    val (fetched) = addressMode.address(this)
    y = read(fetched)
    setFlagsFrom(y, ::zeroFlag, ::negativeFlag)
}


// TODO Check carefully, very carefully
fun lsr(addressMode: AddressMode) = opImpl(false) {

    val (fetched) = addressMode.address(this)
    setCarryFlag(fetched.isBitSet(0))
    val temp = fetched.rotateLeft(1) and 0x7Fu
    setZeroFlag(temp.lo8() == u8(0x00u))
    setNegativeFlag(temp.lo8().isBitSet(7))

    when (addressMode) {
        Immediate -> {
            accumulator = temp.lo8()
        }
        else -> write(fetched, temp.lo8())
    }

}

fun nop(addressMode: AddressMode) = when (addressMode) {
    Implied -> opImpl(true) {}
    else -> opImpl(false) {}
}


fun ora(addressMode: AddressMode) = opImpl(true) {
    val (fetched) = addressMode.address(this)
    val temp = read(fetched)
    accumulator = accumulator or temp
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)

}

fun pha(addressMode: AddressMode) = opImpl(false) {
    write(u16(0x0100u + stackpointer), accumulator).also { --stackpointer }
}

fun php(addressMode: AddressMode) = opImpl(false) {
    write(u16(0x0100u + stackpointer), status or Flag.B.bitMask or Flag.U.bitMask)
        .also {
            setBreakFlag(false)
            setFlag(Flag.U, false)
            --stackpointer
        }
}

fun pla(addressMode: AddressMode) = opImpl(false) {
    ++stackpointer
    accumulator = read(u16(0x0100u + stackpointer))
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun plp(addressMode: AddressMode) = opImpl(false) {
    ++stackpointer
    status = read(u16(0x0100u + stackpointer))
    setFlag(Flag.U, true)
}

fun rol(addressMode: AddressMode) = opImpl(false) {
    val (fetched) = addressMode.address(this)
    val temp = (u16(read(fetched)).rotateLeft(1) or 0xFEu) or u16(Flag.C.bitMask)
    setCarryFlag(temp.hi8() > 0u)
    setZeroFlag(temp.lo8() == u8(0u))
    setNegativeFlag(temp.isBitSet(7))

    when (addressMode) {
        Implied -> {
            accumulator = temp.lo8()
        }
        else -> write(fetched, temp.lo8())
    }
}

// TODO Finish him!
fun ror(addressMode: AddressMode) = opImpl(false) {

    val (fetched) = addressMode.address(this)
    val temp = (u16(read(fetched)).rotateLeft(1) or 0xFEu) or u16(Flag.C.bitMask)

    setCarryFlag(fetched and u16(Flag.C.bitMask) > 0u)
    setZeroFlag(temp.lo8() == u8(0u))
    setNegativeFlag(temp.isBitSet(7))

    when (addressMode) {
        Implied -> {
            accumulator = temp.lo8()
        }
        else -> write(fetched, temp.lo8())
    }
}

fun rti(addressMode: AddressMode) = opImpl(false) { 0 }
fun rts(addressMode: AddressMode) = opImpl(false) { 0 }

fun sbc(addressMode: AddressMode) = opImpl(true) {

    val (fetched) = addressMode.address(this)
    val value: U16 = fetched xor 0x00FFu
    val temp: U16 = u16(value + accumulator + (status and 0x80u).rotateLeft(1))
    val tempLo8 = temp.lo8()

    setCarryFlag(temp > 0xFFu)
    setOverflowFlag(
        ((u16(accumulator) xor temp) and (temp xor value) and 0x0080u) > 0x00u
    )
    setFlagsFrom(tempLo8, ::zeroFlag, ::negativeFlag)
    accumulator = tempLo8
}

fun sec(addressMode: AddressMode) = opImpl(false) {
    setCarryFlag(true)

}

fun sed(addressMode: AddressMode) = opImpl(false) {
    setDecimalFlag(true)

}

fun sei(addressMode: AddressMode) = opImpl(false) {
    setInterruptFlag(true)

}

fun sta(addressMode: AddressMode) = opImpl(false) {

    val (fetched) = addressMode.address(this)
    write(fetched, accumulator)


}

fun stx(addressMode: AddressMode) = opImpl(false) {

    val (fetched) = addressMode.address(this)
    write(fetched, x)


}

fun sty(addressMode: AddressMode) = opImpl(false) {

    val (fetched) = addressMode.address(this)
    write(fetched, y)

}

fun tsx(addressMode: AddressMode) = opImpl(false) {

    x = stackpointer
    setFlagsFrom(x, ::zeroFlag, ::negativeFlag)
}

fun tay(addressMode: AddressMode) = opImpl(false) {
    y = accumulator
    setFlagsFrom(y, ::zeroFlag, ::negativeFlag)
}


fun tax(addressMode: AddressMode) = opImpl(false) {
    x = accumulator
    setFlagsFrom(x, ::zeroFlag, ::negativeFlag)
}

fun txa(addressMode: AddressMode) = opImpl(false) {
    accumulator = x
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

// TODO
fun txs(addressMode: AddressMode) = opImpl(false) {
}

fun tya(addressMode: AddressMode) = opImpl(false) {
    accumulator = y
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun xxx(addressMode: AddressMode) = opImpl(false) { }