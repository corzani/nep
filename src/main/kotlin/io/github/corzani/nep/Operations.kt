package io.github.corzani.nep

val opcodes = listOf(
// ROW 0
    Op("BRK", brk(::immediate), 7),
    Op("ORA", ora(::indirectX), 6),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 3),
    Op("ORA", ora(::zeroPage), 3),
    Op("ASL", asl(::zeroPage), 5),
    Op("???", xxx(::implied), 5),
    Op("PHP", php(::implied), 3),
    Op("ORA", ora(::immediate), 2),
    Op("ASL", asl(::implied), 2),
    Op("???", xxx(::implied), 2),
    Op("???", nop(::implied), 4),
    Op("ORA", ora(::absolute), 4),
    Op("ASL", asl(::absolute), 6),
    Op("???", xxx(::implied), 6),
// ROW 1
    Op("BPL", bpl(::relative), 2),
    Op("ORA", ora(::indirectY), 5),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 4),
    Op("ORA", ora(::zeroPageX), 4),
    Op("ASL", asl(::zeroPageX), 6),
    Op("???", xxx(::implied), 6),
    Op("CLC", clc(::implied), 2),
    Op("ORA", ora(::absoluteY), 4),
    Op("???", nop(::implied), 2),
    Op("???", xxx(::implied), 7),
    Op("???", nop(::implied), 4),
    Op("ORA", ora(::absoluteX), 4),
    Op("ASL", asl(::absoluteX), 7),
    Op("???", xxx(::implied), 7),
// ROW 2
    Op("JSR", jsr(::absolute), 6),
    Op("AND", and(::indirectX), 6),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("BIT", bit(::zeroPage), 3),
    Op("AND", and(::zeroPage), 3),
    Op("ROL", rol(::zeroPage), 5),
    Op("???", xxx(::implied), 5),
    Op("PLP", plp(::implied), 4),
    Op("AND", and(::immediate), 2),
    Op("ROL", rol(::implied), 2),
    Op("???", xxx(::implied), 2),
    Op("BIT", bit(::absolute), 4),
    Op("AND", and(::absolute), 4),
    Op("ROL", rol(::absolute), 6),
    Op("???", xxx(::implied), 6),
// ROW 3
    Op("BMI", bmi(::relative), 2),
    Op("AND", and(::indirectY), 5),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 4),
    Op("AND", and(::zeroPageX), 4),
    Op("ROL", rol(::zeroPageX), 6),
    Op("???", xxx(::implied), 6),
    Op("SEC", sec(::implied), 2),
    Op("AND", and(::absoluteY), 4),
    Op("???", nop(::implied), 2),
    Op("???", xxx(::implied), 7),
    Op("???", nop(::implied), 4),
    Op("AND", and(::absoluteX), 4),
    Op("ROL", rol(::absoluteX), 7),
    Op("???", xxx(::implied), 7),
//ROW 4
    Op("RTI", rti(::implied), 6),
    Op("EOR", eor(::indirectX), 6),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 3),
    Op("EOR", eor(::zeroPage), 3),
    Op("LSR", lsr(::zeroPage), 5),
    Op("???", xxx(::implied), 5),
    Op("PHA", pha(::implied), 3),
    Op("EOR", eor(::immediate), 2),
    Op("LSR", lsr(::implied), 2),
    Op("???", xxx(::implied), 2),
    Op("JMP", jmp(::absolute), 3),
    Op("EOR", eor(::absolute), 4),
    Op("LSR", lsr(::absolute), 6),
    Op("???", xxx(::implied), 6),
// ROW 5
    Op("BVC", bvc(::relative), 2),
    Op("EOR", eor(::indirectY), 5),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 4),
    Op("EOR", eor(::zeroPageX), 4),
    Op("LSR", lsr(::zeroPageX), 6),
    Op("???", xxx(::implied), 6),
    Op("CLI", cli(::implied), 2),
    Op("EOR", eor(::absoluteY), 4),
    Op("???", nop(::implied), 2),
    Op("???", xxx(::implied), 7),
    Op("???", nop(::implied), 4),
    Op("EOR", eor(::absoluteX), 4),
    Op("LSR", lsr(::absoluteX), 7),
    Op("???", xxx(::implied), 7),
// ROW 6
    Op("RTS", rts(::implied), 6),
    Op("ADC", adc(::indirectX), 6),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 3),
    Op("ADC", adc(::zeroPage), 3),
    Op("ROR", ror(::zeroPage), 5),
    Op("???", xxx(::implied), 5),
    Op("PLA", pla(::implied), 4),
    Op("ADC", adc(::immediate), 2),
    Op("ROR", ror(::implied), 2),
    Op("???", xxx(::implied), 2),
    Op("JMP", jmp(::indirect), 5),
    Op("ADC", adc(::absolute), 4),
    Op("ROR", ror(::absolute), 6),
    Op("???", xxx(::implied), 6),
// ROW 7
    Op("BVS", bvs(::relative), 2),
    Op("ADC", adc(::indirectY), 5),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 4),
    Op("ADC", adc(::zeroPageX), 4),
    Op("ROR", ror(::zeroPageX), 6),
    Op("???", xxx(::implied), 6),
    Op("SEI", sei(::implied), 2),
    Op("ADC", adc(::absoluteY), 4),
    Op("???", nop(::implied), 2),
    Op("???", xxx(::implied), 7),
    Op("???", nop(::implied), 4),
    Op("ADC", adc(::absoluteX), 4),
    Op("ROR", ror(::absoluteX), 7),
    Op("???", xxx(::implied), 7),
// ROW 8
    Op("???", nop(::implied), 2),
    Op("STA", sta(::indirectX), 6),
    Op("???", nop(::implied), 2),
    Op("???", xxx(::implied), 6),
    Op("STY", sty(::zeroPage), 3),
    Op("STA", sta(::zeroPage), 3),
    Op("STX", stx(::zeroPage), 3),
    Op("???", xxx(::implied), 3),
    Op("DEY", dey(::implied), 2),
    Op("???", nop(::implied), 2),
    Op("TXA", txa(::implied), 2),
    Op("???", xxx(::implied), 2),
    Op("STY", sty(::absolute), 4),
    Op("STA", sta(::absolute), 4),
    Op("STX", stx(::absolute), 4),
    Op("???", xxx(::implied), 4),
// ROW 9
    Op("BCC", bcc(::relative), 2),
    Op("STA", sta(::indirectY), 6),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 6),
    Op("STY", sty(::zeroPageX), 4),
    Op("STA", sta(::zeroPageX), 4),
    Op("STX", stx(::zeroPageY), 4),
    Op("???", xxx(::implied), 4),
    Op("TYA", tya(::implied), 2),
    Op("STA", sta(::absoluteY), 5),
    Op("TXS", txs(::implied), 2),
    Op("???", xxx(::implied), 5),
    Op("???", nop(::implied), 5),
    Op("STA", sta(::absoluteX), 5),
    Op("???", xxx(::implied), 5),
    Op("???", xxx(::implied), 5),
// ROW 10
    Op("LDY", ldy(::immediate), 2),
    Op("LDA", lda(::indirectX), 6),
    Op("LDX", ldx(::immediate), 2),
    Op("???", xxx(::implied), 6),
    Op("LDY", ldy(::zeroPage), 3),
    Op("LDA", lda(::zeroPage), 3),
    Op("LDX", ldx(::zeroPage), 3),
    Op("???", xxx(::implied), 3),
    Op("TAY", tay(::implied), 2),
    Op("LDA", lda(::immediate), 2),
    Op("TAX", tax(::implied), 2),
    Op("???", xxx(::implied), 2),
    Op("LDY", ldy(::absolute), 4),
    Op("LDA", lda(::absolute), 4),
    Op("LDX", ldx(::absolute), 4),
    Op("???", xxx(::implied), 4),
// ROW 11
    Op("BCS", bcs(::relative), 2),
    Op("LDA", lda(::indirectY), 5),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 5),
    Op("LDY", ldy(::zeroPageX), 4),
    Op("LDA", lda(::zeroPageX), 4),
    Op("LDX", ldx(::zeroPageY), 4),
    Op("???", xxx(::implied), 4),
    Op("CLV", clv(::implied), 2),
    Op("LDA", lda(::absoluteY), 4),
    Op("TSX", tsx(::implied), 2),
    Op("???", xxx(::implied), 4),
    Op("LDY", ldy(::absoluteX), 4),
    Op("LDA", lda(::absoluteX), 4),
    Op("LDX", ldx(::absoluteY), 4),
    Op("???", xxx(::implied), 4),
// ROW 12
    Op("CPY", cpy(::immediate), 2),
    Op("CMP", cmp(::indirectX), 6),
    Op("???", nop(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("CPY", cpy(::zeroPage), 3),
    Op("CMP", cmp(::zeroPage), 3),
    Op("DEC", dec(::zeroPage), 5),
    Op("???", xxx(::implied), 5),
    Op("INY", iny(::implied), 2),
    Op("CMP", cmp(::immediate), 2),
    Op("DEX", dex(::implied), 2),
    Op("???", xxx(::implied), 2),
    Op("CPY", cpy(::absolute), 4),
    Op("CMP", cmp(::absolute), 4),
    Op("DEC", dec(::absolute), 6),
    Op("???", xxx(::implied), 6),
// ROW 13
    Op("BNE", bne(::relative), 2),
    Op("CMP", cmp(::indirectY), 5),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 4),
    Op("CMP", cmp(::zeroPageX), 4),
    Op("DEC", dec(::zeroPageX), 6),
    Op("???", xxx(::implied), 6),
    Op("CLD", cld(::implied), 2),
    Op("CMP", cmp(::absoluteY), 4),
    Op("NOP", nop(::implied), 2),
    Op("???", xxx(::implied), 7),
    Op("???", nop(::implied), 4),
    Op("CMP", cmp(::absoluteX), 4),
    Op("DEC", dec(::absoluteX), 7),
    Op("???", xxx(::implied), 7),
// ROW 14
    Op("CPX", cpx(::immediate), 2),
    Op("SBC", sbc(::indirectX), 6),
    Op("???", nop(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("CPX", cpx(::zeroPage), 3),
    Op("SBC", sbc(::zeroPage), 3),
    Op("INC", inc(::zeroPage), 5),
    Op("???", xxx(::implied), 5),
    Op("INX", inx(::implied), 2),
    Op("SBC", sbc(::immediate), 2),
    Op("NOP", nop(::implied), 2),
    Op("???", sbc(::implied), 2),
    Op("CPX", cpx(::absolute), 4),
    Op("SBC", sbc(::absolute), 4),
    Op("INC", inc(::absolute), 6),
    Op("???", xxx(::implied), 6),
// ROW 15
    Op("BEQ", beq(::relative), 2),
    Op("SBC", sbc(::indirectY), 5),
    Op("???", xxx(::implied), 2),
    Op("???", xxx(::implied), 8),
    Op("???", nop(::implied), 4),
    Op("SBC", sbc(::zeroPageX), 4),
    Op("INC", inc(::zeroPageX), 6),
    Op("???", xxx(::implied), 6),
    Op("SED", sed(::implied), 2),
    Op("SBC", sbc(::absoluteY), 4),
    Op("NOP", nop(::implied), 2),
    Op("???", xxx(::implied), 7),
    Op("???", nop(::implied), 4),
    Op("SBC", sbc(::absoluteX), 4),
    Op("INC", inc(::absoluteX), 7),
    Op("???", xxx(::implied), 7)
)

fun getOperatorsMap() = opcodes.mapIndexed { idx, op -> op.name to idx }.toMap()
