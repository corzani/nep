Feature: Branch Instructions

#  MNEMONIC                       HEX
#  BPL (Branch on PLus)           $10
#  BMI (Branch on MInus)          $30
#  BVC (Branch on oVerflow Clear) $50
#  BVS (Branch on oVerflow Set)   $70
#  BCC (Branch on Carry Clear)    $90
#  BCS (Branch on Carry Set)      $B0
#  BNE (Branch on Not Equal)      $D0
#  BEQ (Branch on EQual)          $F0

  Scenario: BPL (0x10) Branch on PLus - Jump when N flag is DISABLED
    Given ROM memory "0x10 0x2F"
    And N CPU flag is DISABLED
    When code is executed
    And PC register should be 0x8032
    And CPU should have performed 2 cycles

  Scenario: BPL (0x10) Branch on PLus - do NOT Jump when N flag is ENABLED
    Given ROM memory "0x10 0x2F"
    And N CPU flag is ENABLED
    When code is executed
    And PC register should be 0x8002
    And CPU should have performed 2 cycles

  Scenario: BMI (0x30) Branch on MInus - Jump when N flag is ENABLED
    Given ROM memory "0x30 0x2F"
    And N CPU flag is ENABLED
    When code is executed
    And PC register should be 0x8032
    And CPU should have performed 2 cycles

  Scenario: BMI (0x30) Branch on MInus - do NOT Jump when N flag is DISABLED
    Given ROM memory "0x30 0x2F"
    And N CPU flag is DISABLED
    When code is executed
    And PC register should be 0x8002
    And CPU should have performed 2 cycles

  Scenario: BVC (0x50) Branch on oVerflow Clear - Jump when V flag is DISABLED
    Given ROM memory "0x50 0x2F"
    And V CPU flag is DISABLED
    When code is executed
    And PC register should be 0x8032
    And CPU should have performed 2 cycles

  Scenario: BVC (0x50) Branch on oVerflow Clear - do NOT Jump when V flag is ENABLED
    Given ROM memory "0x50 0x2F"
    And V CPU flag is ENABLED
    When code is executed
    And PC register should be 0x8002
    And CPU should have performed 2 cycles

  Scenario: BVS (0x70) Branch on oVerflow Set - Jump when V flag is ENABLED
    Given ROM memory "0x70 0x2F"
    And V CPU flag is ENABLED
    When code is executed
    And PC register should be 0x8032
    And CPU should have performed 2 cycles

  Scenario: BVS (0x70) Branch on oVerflow Set - do NOT Jump when V flag is DISABLED
    Given ROM memory "0x70 0x2F"
    And V CPU flag is DISABLED
    When code is executed
    And PC register should be 0x8002
    And CPU should have performed 2 cycles

  Scenario: BCC (0x90) Branch on Carry Clear - do NOT Jump when C flag is ENABLED
    Given ROM memory "0x90 0x2F"
    And C CPU flag is ENABLED
    When code is executed
    And PC register should be 0x8002
    And CPU should have performed 2 cycles

  Scenario: BCC (0x90) Branch on Carry Clear - Jump when C flag is DISABLED
    Given ROM memory "0x90 0x2F"
    And C CPU flag is DISABLED
    When code is executed
    And PC register should be 0x8032
    And CPU should have performed 2 cycles

  Scenario: BCS (0xB0) Branch on Carry Set - Jump when C flag is ENABLED
    Given ROM memory "0xB0 0x2F"
    And C CPU flag is ENABLED
    When code is executed
    And PC register should be 0x8032
    And CPU should have performed 2 cycles

  Scenario: BCS (0xB0) Branch on Carry Set - do NOT Jump when C flag is DISABLED
    Given ROM memory "0xB0 0x2F"
    And C CPU flag is DISABLED
    When code is executed
    And PC register should be 0x8002
    And CPU should have performed 2 cycles

  Scenario: BNE (0xD0) Branch on Not Equal - Jump when Z flag is DISABLED
    # CPY #$03  -> Compare Y register, Set Z <- Y == 3 <= true
    # BNE $2F
    Given ROM memory "0xC0 0x03 0xD0 0x2F"
    When code is executed
    And PC register should be 0x8034
    And CPU should have performed 4 cycles

  Scenario: BNE (0xD0) Branch on Not Equal - do NOT Jump when Z flag is ENABLED
    Given ROM memory "0xC0 0x03 0xD0 0x2F"
    And Y register is 0x03
    When code is executed
    And PC register should be 0x8004
    And CPU should have performed 4 cycles

  Scenario: BEQ (0xF0) Branch on EQual - do NOT Jump when Z flag is ENABLED
    # CPY #$03  -> Compare Y register, Set Z <- Y == 3 <= true
    # BNE $2F
    Given ROM memory "0xC0 0x03 0xF0 0x2F"
    When code is executed
    And Z flag should be DISABLED
    And PC register should be 0x8004
    And CPU should have performed 4 cycles

  Scenario: BEQ (0xF0) Branch on EQual - Jump when Z flag is DISABLED
    Given ROM memory "0xC0 0x03 0xF0 0x2F"
    And Y register is 0x03
    When code is executed
    And Z flag should be ENABLED
    And PC register should be 0x8034
    And CPU should have performed 4 cycles
