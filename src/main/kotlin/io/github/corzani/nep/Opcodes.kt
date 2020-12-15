package io.github.corzani.nep


fun opImpl(possibleAdditionalCycle: Boolean, block: NesArch.() -> Unit): (NesArch) -> Boolean =
    fun(nesArch: NesArch): Boolean {
        block(nesArch)
        return possibleAdditionalCycle
    }

// TODO Wrong! It's not the address but the fetched
fun adc(addressMode: Address) = opImpl(true) {
    val (fetched, address) = fetchFrom(addressMode)

    val sum = u16(fetched + accumulator + (status and 0x80u).rotateLeft(1))
    val sumLo8 = sum.lo8()

    setCarryFlag(sum > 0xFFu)
    setOverflowFlag(
        (u16((accumulator xor fetched).inv()) and (u16(accumulator) xor sum) and 0x0080u) > 0x00u
    )
    setFlagsFrom(sumLo8, ::zeroFlag, ::negativeFlag)
    accumulator = sumLo8
}

fun and(addressMode: Address) = opImpl(false) {
    val (fetched, _) = fetchFrom(addressMode)
    accumulator = accumulator and fetched
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun asl(addressMode: Address) = opImpl(false) {

    fun doShiftLeft(data: U8): U8 {
        setCarryFlag(data[7])
        return (data.rotateLeft(1) and 0xFEu).also {
            setFlagsFrom(it, ::zeroFlag, ::negativeFlag)
        }
    }

    when (addressMode.type) {
        AddressType.Implied -> doShiftLeft(accumulator).also { accumulator = it }
        else -> {
            val (fetched, address) = fetchFrom(addressMode)
            fetched
                .let(::doShiftLeft)
                .let { write(address, it) }
        }
    }
}

fun bcc(addressMode: Address) = opImpl(false) {
    branchOnFlag(addressMode, Flag.C, false)
}

fun bcs(addressMode: Address) = opImpl(false) {
    branchOnFlag(addressMode, Flag.C, true)
}

fun beq(addressMode: Address) = opImpl(false) {
    branchOnFlag(addressMode, Flag.Z, true)
}

fun bit(addressMode: Address) = opImpl(false) {
    val (fetched, _) = fetchFrom(addressMode)
    val temp = accumulator and fetched
    setZeroFlag(temp == u8(0u))
    setFlagsFrom(fetched, ::negativeFlag, ::overflowFlag)
}

fun bmi(addressMode: Address) = opImpl(false) {
    branchOnFlag(addressMode, Flag.N, true)

}

fun bne(addressMode: Address) = opImpl(false) {
    branchOnFlag(addressMode, Flag.C, false)
}

fun bpl(addressMode: Address) = opImpl(false) {
    branchOnFlag(addressMode, Flag.N, false)
}

fun brk(addressMode: Address) = opImpl(false) {
}

fun bvc(addressMode: Address) = opImpl(false) {
    branchOnFlag(addressMode, Flag.Z, false)
}

fun bvs(addressMode: Address) = opImpl(false) {
    branchOnFlag(addressMode, Flag.V, true)
}

fun clc(addressMode: Address) = opImpl(false) {
    setCarryFlag(false)
}

fun cld(addressMode: Address) = opImpl(false) {
    setDecimalFlag(false)
}

fun cli(addressMode: Address) = opImpl(false) {
    setInterruptFlag(false)
}

fun clv(addressMode: Address) = opImpl(false) {
    setOverflowFlag(false)
}

// TODO
fun cmp(addressMode: Address) = opImpl(true) {
    val (fetched) = fetchFrom(addressMode)

    setCarryFlag(accumulator >= fetched)
    setFlagsFrom(u16(accumulator - fetched).lo8(), ::zeroFlag, ::negativeFlag)
}

fun cpx(addressMode: Address) = opImpl(false) {
    val (fetched, _) = fetchFrom(addressMode)

    setFlag(Flag.C, x >= fetched)
    setFlagsFrom(u16(x - fetched).lo8(), ::zeroFlag, ::negativeFlag)
}

fun cpy(addressMode: Address) = opImpl(false) {
    val (fetched, _) = fetchFrom(addressMode)

    setFlag(Flag.C, y >= fetched)
    setFlagsFrom(u16(y - fetched).lo8(), ::zeroFlag, ::negativeFlag)
}

fun dec(addressMode: Address) = opImpl(false) {
    val (fetched, address) = fetchFrom(addressMode)
    val temp = u16(fetched - 1u).lo8() // This would be interesting to see what happen if u16 isn't used

    write(address, temp)
    setFlagsFrom(temp, ::zeroFlag, ::negativeFlag)
}

fun dex(addressMode: Address) = opImpl(false) {
    setFlagsFrom(--x, ::zeroFlag, ::negativeFlag)
}

fun dey(addressMode: Address) = opImpl(false) {
    setFlagsFrom(--y, ::zeroFlag, ::negativeFlag)
}

fun eor(addressMode: Address) = opImpl(true) {
    val (fetched) = fetchFrom(addressMode)
    accumulator = accumulator xor fetched
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun inc(addressMode: Address) = opImpl(false) {
    val (fetched, address) = fetchFrom(addressMode)
    val temp = u16(fetched + 1u).lo8()
    write(address, temp)
    setFlagsFrom(temp, ::zeroFlag, ::negativeFlag)
}

fun inx(addressMode: Address) = opImpl(false) {
    setFlagsFrom(++x, ::zeroFlag, ::negativeFlag)
}

fun iny(addressMode: Address) = opImpl(false) {
    setFlagsFrom(--y, ::zeroFlag, ::negativeFlag)
}

fun jmp(addressMode: Address) = opImpl(false) {
    pc = fetchFrom(addressMode).address
}

// TODO Test at all costs!!!!
fun jsr(addressMode: Address) = opImpl(false) {
    stackPush(u16(pc - 1u))
    pc = fetchFrom(addressMode).address
}

fun lda(addressMode: Address) = opImpl(false) {
    accumulator = fetchFrom(addressMode).fetched
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun ldx(addressMode: Address) = opImpl(true) {
    x = fetchFrom(addressMode).fetched
    setFlagsFrom(x, ::zeroFlag, ::negativeFlag)
}

fun ldy(addressMode: Address) = opImpl(true) {
    y = fetchFrom(addressMode).fetched
    setFlagsFrom(y, ::zeroFlag, ::negativeFlag)
}

fun lsr(addressMode: Address) = opImpl(false) {

    val (fetched, address) = fetchFrom(addressMode)

    setCarryFlag(fetched[0])
    val temp = fetched.rotateRight(1) and 0b01111111u

    setFlagsFrom(temp, ::zeroFlag, ::negativeFlag)

    when (addressMode.type) {
        AddressType.Immediate -> {
            accumulator = temp
        }
        else -> write(address, temp)
    }
}

fun nop(addressMode: Address) = when (addressMode.type) {
    AddressType.Implied -> opImpl(true) {}
    else -> opImpl(false) {}
}


fun ora(addressMode: Address) = opImpl(true) {
    val (fetched) = fetchFrom(addressMode)
    accumulator = accumulator or fetched
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun pha(addressMode: Address) = opImpl(false) {
    write(u16(0x0100u + stackPointer), accumulator).also { --stackPointer }
}

fun php(addressMode: Address) = opImpl(false) {
    write(u16(0x0100u + stackPointer), status or Flag.B.bitMask or Flag.U.bitMask)
        .also {
            setBreakFlag(false)
            setFlag(Flag.U, false)
            --stackPointer
        }
}

fun pla(addressMode: Address) = opImpl(false) {
    ++stackPointer
    accumulator = read(u16(0x0100u + stackPointer))
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun plp(addressMode: Address) = opImpl(false) {
    ++stackPointer
    status = read(u16(0x0100u + stackPointer))
    setFlag(Flag.U, true)
}

fun rol(addressMode: Address) = opImpl(false) {

    val (fetched, address) = fetchFrom(addressMode)

    val temp = (u16(fetched).rotateLeft(1) or 0xFEu) or u16(Flag.C.bitMask)
    setCarryFlag(temp.hi8() > 0u)
    setZeroFlag(temp.lo8() == u8(0u))
    setNegativeFlag(temp[7])

    when (addressMode.type) {
        AddressType.Implied -> {
            accumulator = temp.lo8()
        }
        else -> write(address, temp.lo8())
    }
}

// TODO Finish him!
fun ror(addressMode: Address) = opImpl(false) {
    fun carryFlagMask(): U8 = if (getFlag(Flag.C)) 0b10000000u else 0u
    //

    val (fetched, address) = fetchFrom(addressMode)
    val temp = carryFlagMask() or fetched.rotateRight(1)

    setCarryFlag((fetched and Flag.C.bitMask) > 0u)
    setFlagsFrom(temp, ::zeroFlag, ::negativeFlag)

    when (addressMode.type) {
        AddressType.Implied -> {
            accumulator = temp
        }
        else -> write(address, temp)
    }
}

// TODO Check this infamous opcode
fun rti(addressMode: Address) = opImpl(false) {
    status = stackPop8()
    setFlag(Flag.B, false)
    setFlag(Flag.U, false)

    pc = stackPop16()
}

fun rts(addressMode: Address) = opImpl(false) {
    pc = u16(stackPop16() + 1u)
}

fun sbc(addressMode: Address) = opImpl(true) {

    val (fetched, _) = fetchFrom(addressMode)

    val value = fetched xor u8(0xFFu)
    val temp: U16 = u16(value + accumulator + (status and 0x80u).rotateLeft(1))
    val tempLo8 = temp.lo8()

    setCarryFlag(temp > 0xFFu)
    setOverflowFlag(
        ((u16(accumulator) xor temp) and (temp xor u16(value)) and 0x0080u) > 0x00u
    )
    setFlagsFrom(tempLo8, ::zeroFlag, ::negativeFlag)
    accumulator = tempLo8
}

fun sec(addressMode: Address) = opImpl(false) {
    setCarryFlag(true)
}

fun sed(addressMode: Address) = opImpl(false) {
    setDecimalFlag(true)
}

fun sei(addressMode: Address) = opImpl(false) {
    setInterruptFlag(true)
}

fun sta(addressMode: Address) = opImpl(false) {
    val (_, address) = fetchFrom(addressMode)
    write(address, accumulator)
}

fun stx(addressMode: Address) = opImpl(false) {
    val (_, address) = fetchFrom(addressMode)
    write(address, x)
}

fun sty(addressMode: Address) = opImpl(false) {
    val (_, address) = fetchFrom(addressMode)
    write(address, y)
}

fun tsx(addressMode: Address) = opImpl(false) {
    x = stackPointer
    setFlagsFrom(x, ::zeroFlag, ::negativeFlag)
}

fun tay(addressMode: Address) = opImpl(false) {
    y = accumulator
    setFlagsFrom(y, ::zeroFlag, ::negativeFlag)
}


fun tax(addressMode: Address) = opImpl(false) {
    x = accumulator
    setFlagsFrom(x, ::zeroFlag, ::negativeFlag)
}

fun txa(addressMode: Address) = opImpl(false) {
    accumulator = x
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun txs(addressMode: Address) = opImpl(false) {
    stackPointer = x
}

fun tya(addressMode: Address) = opImpl(false) {
    accumulator = y
    setFlagsFrom(accumulator, ::zeroFlag, ::negativeFlag)
}

fun xxx(addressMode: Address) = opImpl(false) { }