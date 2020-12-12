Feature: ASL Opcodes

  Scenario: ASL (0xA0) Immediate
    Given ROM memory "0x0A"
    And Accumulator register is 0xF5
    When code is executed
    Then Accumulator register should be 0xEA
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And C flag should be ENABLED
    And CPU should have performed 2 cycles

  Scenario: ASL (0x06) Zero Page
    Given ROM memory "0x06 0xAA"
    And 0x8B is stored at address 0x00AA
    When code is executed
    Then address 0x00AA should contain 0x16
    And N flag should be DISABLED
    And Z flag should be DISABLED
    And C flag should be ENABLED
    And CPU should have performed 5 cycles

  Scenario: ASL (0x16) Zero Page, X
    Given ROM memory "0x16 0xAA"
    And X register is 0x02
    And 0x8B is stored at address 0x00AC
    When code is executed
    Then address 0x00AC should contain 0x16
    And N flag should be DISABLED
    And Z flag should be DISABLED
    And C flag should be ENABLED
    And CPU should have performed 6 cycles

#  Scenario: ASL (0x0E) Absolute
#    Given ROM memory "0x2D 0xAA 0xBF"
#    And Accumulator register is 0x8D
#    And 0x8B is stored at address 0xBFAA
#    When code is executed
#    Then Accumulator register should be 0x89
#    And N flag should be ENABLED
#    And Z flag should be DISABLED
#    And CPU should have performed 4 cycles
#
#  Scenario: ASL (0x1E) Absolute, X
#    Given ROM memory "0x3D 0xAA 0xBF"
#    And Accumulator register is 0x8D
#    And X register is 0x02
#    And 0x8B is stored at address 0xBFAC
#    When code is executed
#    Then Accumulator register should be 0x89
#    And N flag should be ENABLED
#    And Z flag should be DISABLED
#    And CPU should have performed 4 cycles
