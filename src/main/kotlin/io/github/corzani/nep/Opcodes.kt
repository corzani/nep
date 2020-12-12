package io.github.corzani.nep

fun adc(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        val sum = u16(fetched + accumulator + (status and 0x80u).rotateLeft(1))
        val sumLo8 = sum.lo8()

        setFlag(Flag.C, sum > 0xFFu)
        setFlag(
            Flag.V,
            ((u16(accumulator) xor fetched).inv() and (u16(accumulator) xor sum) and 0x0080u) > 0x00u
        )
        status = flagsOf(status, sumLo8, ::zeroFlag, ::negativeFlag)
        accumulator = sumLo8
    }
    1
}

fun and(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        read(ram, fetched).let { address ->
            accumulator = accumulator and address
            status = flagsOf(status, accumulator, ::zeroFlag, ::negativeFlag)
        }
    }
    0
}

fun asl(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        fun doShiftLeft(data: U8): U8 {
            setFlag(Flag.C, getBit(data, 7))
            return (data.rotateLeft(1) and 0xFEu).also {
                status = flagsOf(status, it, ::zeroFlag, ::negativeFlag)
            }
        }
        when (addressMode) {
            Implied -> doShiftLeft(accumulator).also { accumulator = it }
            else -> {
                val (fetched) = addressMode.address(this)
                read(ram, fetched).let(::doShiftLeft).also {
                    write(ram, fetched, it)
                }
            }
        }
        0
    }
}

fun bcc(addressMode: AddressMode) = { nesArch: NesArch ->
    branchOnFlag(nesArch, addressMode, Flag.C, false)
    0
}

fun bcs(addressMode: AddressMode) = { nesArch: NesArch ->
    branchOnFlag(nesArch, addressMode, Flag.C, true)
    0
}

fun beq(addressMode: AddressMode) = { nesArch: NesArch ->
    branchOnFlag(nesArch, addressMode, Flag.Z, true)
    0
}

fun bit(addressMode: AddressMode) = { nesArch: NesArch ->
    0
}

fun bmi(addressMode: AddressMode) = { nesArch: NesArch ->
    branchOnFlag(nesArch, addressMode, Flag.N, true)
    0
}

fun bne(addressMode: AddressMode) = { nesArch: NesArch ->
    branchOnFlag(nesArch, addressMode, Flag.C, false)
    0
}

fun bpl(addressMode: AddressMode) = { nesArch: NesArch ->
    branchOnFlag(nesArch, addressMode, Flag.N, false)
    0
}

fun brk(addressMode: AddressMode) = { nesArch: NesArch ->

    0
}

fun bvc(addressMode: AddressMode) = { nesArch: NesArch ->
    branchOnFlag(nesArch, addressMode, Flag.Z, false)
    0
}

fun bvs(addressMode: AddressMode) = { nesArch: NesArch ->
    branchOnFlag(nesArch, addressMode, Flag.V, true)
    0
}

fun clc(addressMode: AddressMode) = { nesArch: NesArch ->
    setFlag(nesArch, Flag.C, false)
    0
}

fun cld(addressMode: AddressMode) = { nesArch: NesArch ->
    setFlag(nesArch, Flag.D, false)
    0
}

fun cli(addressMode: AddressMode) = { nesArch: NesArch ->

    setFlag(nesArch, Flag.I, false)
    0
}

fun clv(addressMode: AddressMode) = { nesArch: NesArch ->
    setFlag(nesArch, Flag.V, false)
    0
}

fun cmp(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun cpx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun cpy(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun dec(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun dex(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun dey(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun eor(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun inc(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun inx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun iny(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun jmp(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun jsr(addressMode: AddressMode) = { nesArch: NesArch -> 0 }

fun lda(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        read(ram, fetched).let { data -> accumulator = data }
        status = flagsOf(status, accumulator, ::zeroFlag, ::negativeFlag)
    }
    0
}

fun ldx(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        x = read(ram, fetched)
        status = flagsOf(status, x, ::zeroFlag, ::negativeFlag)
    }
    1
}

fun ldy(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        y = read(ram, fetched)
        status = flagsOf(status, y, ::zeroFlag, ::negativeFlag)
    }
    1
}


// TODO Check carefully, very carefully
fun lsr(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        setFlag(Flag.C, (fetched and 0x0001u).lo8() > 0u)
        val temp = fetched.rotateLeft(1) and 0x7Fu
        setFlag(Flag.Z, temp.lo8() == u8(0x00u))
        setFlag(Flag.N, (temp.lo8() and 0x80u) > u8(0))

        when (addressMode) {
            Immediate -> {
                accumulator = temp.lo8()
            }
            else -> write(fetched, temp.lo8())
        }
    }
    0
}

fun nop(addressMode: AddressMode) = { _: NesArch ->
    when (addressMode) {
        Implied -> 1
        else -> 0
    }
}

fun ora(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        val temp = read(ram, fetched)
        accumulator = accumulator or temp
        status = flagsOf(status, accumulator, ::zeroFlag, ::negativeFlag)
    }
    1
}

fun pha(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        write(ram, u16(0x0100u + stackpointer), accumulator)
            .also {
                --stackpointer
            }
        0
    }
}

fun php(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        write(ram, u16(0x0100u + stackpointer), status or Flag.B.bitMask or Flag.U.bitMask)
            .also {
                setFlag(Flag.B, false)
                setFlag(Flag.U, false)
                --stackpointer
            }
        0
    }
}

fun pla(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        ++stackpointer
        accumulator = read(ram, u16(0x0100u + stackpointer))
        status = flagsOf(status, accumulator, ::zeroFlag, ::negativeFlag)
        0
    }
}

fun plp(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        ++stackpointer
        status = read(ram, u16(0x0100u + stackpointer))
        setFlag(Flag.U, true)
    }
    0
}

fun rol(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        val temp = (u16(read(ram, fetched)).rotateLeft(1) or 0xFEu) or u16(Flag.C.bitMask)
        setFlag(Flag.C, temp.hi8() > 0u)
        setFlag(Flag.Z, temp.lo8() == u8(0u))
        setFlag(Flag.N, (temp and 0x0080u) > 0u)

        when (addressMode) {
            Implied -> {
                accumulator = temp.lo8()
            }
            else -> write(ram, fetched, temp.lo8())
        }
    }
    0
}

// TODO Finish him!
fun ror(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        val temp = (u16(read(ram, fetched)).rotateLeft(1) or 0xFEu) or u16(Flag.C.bitMask)
        temp.lo8().let {
            setFlag(Flag.C, fetched and u16(Flag.C.bitMask) > 0u)
            setFlag(Flag.Z, it == u8(0u))
            setFlag(Flag.N, (temp and 0x80u) > 0u)

            when (addressMode) {
                Implied -> {
                    accumulator = temp.lo8()
                }
                else -> write(ram, fetched, temp.lo8())
            }

        }
    }
    0
}

fun rti(addressMode: AddressMode) = { nesArch: NesArch -> 0 }
fun rts(addressMode: AddressMode) = { nesArch: NesArch -> 0 }

fun sbc(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        val value: U16 = fetched xor 0x00FFu
        val temp: U16 = u16(value + accumulator + (status and 0x80u).rotateLeft(1))
        val tempLo8 = temp.lo8()

        setFlag(Flag.C, temp > 0xFFu)
        setFlag(
            Flag.V,
            ((u16(accumulator) xor temp) and (temp xor value) and 0x0080u) > 0x00u
        )
        status = flagsOf(status, tempLo8, ::zeroFlag, ::negativeFlag)
        accumulator = tempLo8
    }
    1
}

fun sec(addressMode: AddressMode) = { nesArch: NesArch ->
    setFlag(nesArch, Flag.C, true)
    0
}

fun sed(addressMode: AddressMode) = { nesArch: NesArch ->
    setFlag(nesArch, Flag.D, true)
    0
}

fun sei(addressMode: AddressMode) = { nesArch: NesArch ->
    setFlag(nesArch, Flag.I, true)
    0
}

fun sta(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        write(ram, fetched, accumulator)
    }
    0
}

fun stx(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        write(ram, fetched, x)
    }
    0
}

fun sty(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        val (fetched) = addressMode.address(this)
        write(ram, fetched, y)
    }
    0
}

fun tsx(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        x = stackpointer
        status = flagsOf(status, x, ::zeroFlag, ::negativeFlag)
        0
    }
}

//fun opcodeImp(block: NesArch.() -> Int): (NesArch) -> Int = fun(nesArch: NesArch): Int = block(nesArch)
//
//fun opimpl(addressMode:AddressMode) = opcodeImp {
//
//    0
//}

fun tay(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        y = accumulator
        status = flagsOf(status, y, ::zeroFlag, ::negativeFlag)
        0
    }
}

fun tax(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        x = accumulator
        status = flagsOf(status, x, ::zeroFlag, ::negativeFlag)
    }
    0
}

fun txa(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        accumulator = x
        status = flagsOf(status, accumulator, ::zeroFlag, ::negativeFlag)
    }
    0
}

// TODO
fun txs(addressMode: AddressMode) = { nesArch: NesArch ->
    0
}

fun tya(addressMode: AddressMode) = { nesArch: NesArch ->
    nesArch.run {
        accumulator = y
        status = flagsOf(status, accumulator, ::zeroFlag, ::negativeFlag)
    }
    0
}

fun xxx(addressMode: AddressMode) = { nesArch: NesArch -> 0 }