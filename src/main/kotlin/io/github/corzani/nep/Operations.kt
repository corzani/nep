package io.github.corzani.nep

fun opcodes() = listOf(
// ROW 0
    Op("BRK", brk(Immediate), 7),
    Op("ORA", ora(IndirectX), 6),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 3),
    Op("ORA", ora(ZeroPage), 3),
    Op("ASL", asl(ZeroPage), 5),
    Op("???", xxx(Implied), 5),
    Op("PHP", php(Implied), 3),
    Op("ORA", ora(Immediate), 2),
    Op("ASL", asl(Implied), 2),
    Op("???", xxx(Implied), 2),
    Op("???", nop(Implied), 4),
    Op("ORA", ora(Absolute), 4),
    Op("ASL", asl(Absolute), 6),
    Op("???", xxx(Implied), 6),
// ROW 1
    Op("BPL", bpl(Relative), 2),
    Op("ORA", ora(IndirectY), 5),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 4),
    Op("ORA", ora(ZeroPageX), 4),
    Op("ASL", asl(ZeroPageX), 6),
    Op("???", xxx(Implied), 6),
    Op("CLC", clc(Implied), 2),
    Op("ORA", ora(AbsoluteY), 4),
    Op("???", nop(Implied), 2),
    Op("???", xxx(Implied), 7),
    Op("???", nop(Implied), 4),
    Op("ORA", ora(AbsoluteX), 4),
    Op("ASL", asl(AbsoluteX), 7),
    Op("???", xxx(Implied), 7),
// ROW 2
    Op("JSR", jsr(Absolute), 6),
    Op("AND", and(IndirectX), 6),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("BIT", bit(ZeroPage), 3),
    Op("AND", and(ZeroPage), 3),
    Op("ROL", rol(ZeroPage), 5),
    Op("???", xxx(Implied), 5),
    Op("PLP", plp(Implied), 4),
    Op("AND", and(Immediate), 2),
    Op("ROL", rol(Implied), 2),
    Op("???", xxx(Implied), 2),
    Op("BIT", bit(Absolute), 4),
    Op("AND", and(Absolute), 4),
    Op("ROL", rol(Absolute), 6),
    Op("???", xxx(Implied), 6),
// ROW 3
    Op("BMI", bmi(Relative), 2),
    Op("AND", and(IndirectY), 5),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 4),
    Op("AND", and(ZeroPageX), 4),
    Op("ROL", rol(ZeroPageX), 6),
    Op("???", xxx(Implied), 6),
    Op("SEC", sec(Implied), 2),
    Op("AND", and(AbsoluteY), 4),
    Op("???", nop(Implied), 2),
    Op("???", xxx(Implied), 7),
    Op("???", nop(Implied), 4),
    Op("AND", and(AbsoluteX), 4),
    Op("ROL", rol(AbsoluteX), 7),
    Op("???", xxx(Implied), 7),
//ROW 4
    Op("RTI", rti(Implied), 6),
    Op("EOR", eor(IndirectX), 6),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 3),
    Op("EOR", eor(ZeroPage), 3),
    Op("LSR", lsr(ZeroPage), 5),
    Op("???", xxx(Implied), 5),
    Op("PHA", pha(Implied), 3),
    Op("EOR", eor(Immediate), 2),
    Op("LSR", lsr(Implied), 2),
    Op("???", xxx(Implied), 2),
    Op("JMP", jmp(Absolute), 3),
    Op("EOR", eor(Absolute), 4),
    Op("LSR", lsr(Absolute), 6),
    Op("???", xxx(Implied), 6),
// ROW 5
    Op("BVC", bvc(Relative), 2),
    Op("EOR", eor(IndirectY), 5),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 4),
    Op("EOR", eor(ZeroPageX), 4),
    Op("LSR", lsr(ZeroPageX), 6),
    Op("???", xxx(Implied), 6),
    Op("CLI", cli(Implied), 2),
    Op("EOR", eor(AbsoluteY), 4),
    Op("???", nop(Implied), 2),
    Op("???", xxx(Implied), 7),
    Op("???", nop(Implied), 4),
    Op("EOR", eor(AbsoluteX), 4),
    Op("LSR", lsr(AbsoluteX), 7),
    Op("???", xxx(Implied), 7),
// ROW 6
    Op("RTS", rts(Implied), 6),
    Op("ADC", adc(IndirectX), 6),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 3),
    Op("ADC", adc(ZeroPage), 3),
    Op("ROR", ror(ZeroPage), 5),
    Op("???", xxx(Implied), 5),
    Op("PLA", pla(Implied), 4),
    Op("ADC", adc(Immediate), 2),
    Op("ROR", ror(Implied), 2),
    Op("???", xxx(Implied), 2),
    Op("JMP", jmp(Indirect), 5),
    Op("ADC", adc(Absolute), 4),
    Op("ROR", ror(Absolute), 6),
    Op("???", xxx(Implied), 6),
// ROW 7
    Op("BVS", bvs(Relative), 2),
    Op("ADC", adc(IndirectY), 5),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 4),
    Op("ADC", adc(ZeroPageX), 4),
    Op("ROR", ror(ZeroPageX), 6),
    Op("???", xxx(Implied), 6),
    Op("SEI", sei(Implied), 2),
    Op("ADC", adc(AbsoluteY), 4),
    Op("???", nop(Implied), 2),
    Op("???", xxx(Implied), 7),
    Op("???", nop(Implied), 4),
    Op("ADC", adc(AbsoluteX), 4),
    Op("ROR", ror(AbsoluteX), 7),
    Op("???", xxx(Implied), 7),
// ROW 8
    Op("???", nop(Implied), 2),
    Op("STA", sta(IndirectX), 6),
    Op("???", nop(Implied), 2),
    Op("???", xxx(Implied), 6),
    Op("STY", sty(ZeroPage), 3),
    Op("STA", sta(ZeroPage), 3),
    Op("STX", stx(ZeroPage), 3),
    Op("???", xxx(Implied), 3),
    Op("DEY", dey(Implied), 2),
    Op("???", nop(Implied), 2),
    Op("TXA", txa(Implied), 2),
    Op("???", xxx(Implied), 2),
    Op("STY", sty(Absolute), 4),
    Op("STA", sta(Absolute), 4),
    Op("STX", stx(Absolute), 4),
    Op("???", xxx(Implied), 4),
// ROW 9
    Op("BCC", bcc(Relative), 2),
    Op("STA", sta(IndirectY), 6),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 6),
    Op("STY", sty(ZeroPageX), 4),
    Op("STA", sta(ZeroPageX), 4),
    Op("STX", stx(ZeroPageY), 4),
    Op("???", xxx(Implied), 4),
    Op("TYA", tya(Implied), 2),
    Op("STA", sta(AbsoluteY), 5),
    Op("TXS", txs(Implied), 2),
    Op("???", xxx(Implied), 5),
    Op("???", nop(Implied), 5),
    Op("STA", sta(AbsoluteX), 5),
    Op("???", xxx(Implied), 5),
    Op("???", xxx(Implied), 5),
// ROW 10
    Op("LDY", ldy(Immediate), 2),
    Op("LDA", lda(IndirectX), 6),
    Op("LDX", ldx(Immediate), 2),
    Op("???", xxx(Implied), 6),
    Op("LDY", ldy(ZeroPage), 3),
    Op("LDA", lda(ZeroPage), 3),
    Op("LDX", ldx(ZeroPage), 3),
    Op("???", xxx(Implied), 3),
    Op("TAY", tay(Implied), 2),
    Op("LDA", lda(Immediate), 2),
    Op("TAX", tax(Implied), 2),
    Op("???", xxx(Implied), 2),
    Op("LDY", ldy(Absolute), 4),
    Op("LDA", lda(Absolute), 4),
    Op("LDX", ldx(Absolute), 4),
    Op("???", xxx(Implied), 4),
// ROW 11
    Op("BCS", bcs(Relative), 2),
    Op("LDA", lda(IndirectY), 5),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 5),
    Op("LDY", ldy(ZeroPageX), 4),
    Op("LDA", lda(ZeroPageX), 4),
    Op("LDX", ldx(ZeroPageY), 4),
    Op("???", xxx(Implied), 4),
    Op("CLV", clv(Implied), 2),
    Op("LDA", lda(AbsoluteY), 4),
    Op("TSX", tsx(Implied), 2),
    Op("???", xxx(Implied), 4),
    Op("LDY", ldy(AbsoluteX), 4),
    Op("LDA", lda(AbsoluteX), 4),
    Op("LDX", ldx(AbsoluteY), 4),
    Op("???", xxx(Implied), 4),
// ROW 12
    Op("CPY", cpy(Immediate), 2),
    Op("CMP", cmp(IndirectX), 6),
    Op("???", nop(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("CPY", cpy(ZeroPage), 3),
    Op("CMP", cmp(ZeroPage), 3),
    Op("DEC", dec(ZeroPage), 5),
    Op("???", xxx(Implied), 5),
    Op("INY", iny(Implied), 2),
    Op("CMP", cmp(Immediate), 2),
    Op("DEX", dex(Implied), 2),
    Op("???", xxx(Implied), 2),
    Op("CPY", cpy(Absolute), 4),
    Op("CMP", cmp(Absolute), 4),
    Op("DEC", dec(Absolute), 6),
    Op("???", xxx(Implied), 6),
// ROW 13
    Op("BNE", bne(Relative), 2),
    Op("CMP", cmp(IndirectY), 5),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 4),
    Op("CMP", cmp(ZeroPageX), 4),
    Op("DEC", dec(ZeroPageX), 6),
    Op("???", xxx(Implied), 6),
    Op("CLD", cld(Implied), 2),
    Op("CMP", cmp(AbsoluteY), 4),
    Op("NOP", nop(Implied), 2),
    Op("???", xxx(Implied), 7),
    Op("???", nop(Implied), 4),
    Op("CMP", cmp(AbsoluteX), 4),
    Op("DEC", dec(AbsoluteX), 7),
    Op("???", xxx(Implied), 7),
// ROW 14
    Op("CPX", cpx(Immediate), 2),
    Op("SBC", sbc(IndirectX), 6),
    Op("???", nop(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("CPX", cpx(ZeroPage), 3),
    Op("SBC", sbc(ZeroPage), 3),
    Op("INC", inc(ZeroPage), 5),
    Op("???", xxx(Implied), 5),
    Op("INX", inx(Implied), 2),
    Op("SBC", sbc(Immediate), 2),
    Op("NOP", nop(Implied), 2),
    Op("???", sbc(Implied), 2),
    Op("CPX", cpx(Absolute), 4),
    Op("SBC", sbc(Absolute), 4),
    Op("INC", inc(Absolute), 6),
    Op("???", xxx(Implied), 6),
// ROW 15
    Op("BEQ", beq(Relative), 2),
    Op("SBC", sbc(IndirectY), 5),
    Op("???", xxx(Implied), 2),
    Op("???", xxx(Implied), 8),
    Op("???", nop(Implied), 4),
    Op("SBC", sbc(ZeroPageX), 4),
    Op("INC", inc(ZeroPageX), 6),
    Op("???", xxx(Implied), 6),
    Op("SED", sed(Implied), 2),
    Op("SBC", sbc(AbsoluteY), 4),
    Op("NOP", nop(Implied), 2),
    Op("???", xxx(Implied), 7),
    Op("???", nop(Implied), 4),
    Op("SBC", sbc(AbsoluteX), 4),
    Op("INC", inc(AbsoluteX), 7),
    Op("???", xxx(Implied), 7)
)
