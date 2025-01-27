Feature: AND - bitwise AND with accumulator

  Scenario: AND (0x29) Immediate
    Given ROM memory "0x29 0xAA"
    And Accumulator register is 0x55
    When code is executed
    Then Accumulator register should be 0x00
    And N flag should be DISABLED
    And Z flag should be ENABLED
    And CPU should have performed 2 cycles

  Scenario: AND (0x25) Zero Page
    Given ROM memory "0x25 0xAA"
    And Accumulator register is 0x8D
    And 0x8B is stored at address 0x00AA
    When code is executed
    Then Accumulator register should be 0x89
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 3 cycles

  Scenario: AND (0x35) Zero Page, X
    Given ROM memory "0x35 0xAA"
    And Accumulator register is 0x8D
    And X register is 0x02
    And 0x8B is stored at address 0x00AC
    When code is executed
    Then Accumulator register should be 0x89
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 4 cycles

  Scenario: AND (0x2D) Absolute
    Given ROM memory "0x2D 0x10 0x02"
    And Accumulator register is 0x8D
    And 0x8B is stored at address 0x0210
    When code is executed
    Then Accumulator register should be 0x89
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 4 cycles

  Scenario: AND (0x3D) Absolute, X
    Given ROM memory "0x3D 0x10 0x02"
    And Accumulator register is 0x8D
    And X register is 0x02
    And 0x8B is stored at address 0x0212
    When code is executed
    Then Accumulator register should be 0x89
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 4 cycles

  Scenario: AND (0x39) Absolute, Y
    Given ROM memory "0x39 0x10 0x02"
    And Accumulator register is 0x8D
    And Y register is 0x02
    And 0x8B is stored at address 0x0212
    When code is executed
    Then Accumulator register should be 0x89
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 4 cycles

  Scenario: AND (0x21) Indirect, X
    Given ROM memory "0x21 0xC0"
    And Accumulator register is 0x8D
    And 0x8B is stored at address 0x01C0
    When code is executed
    Then Accumulator register should be 0x89
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 6 cycles

  Scenario: AND (0x31) Indirect,Y
    Given ROM memory "0x31 0x44"
    And Accumulator register is 0x8A
    And Y register is 0x05
    And 0x06 is stored at address 0x0044
    And 0xDD is stored at address 0x000B
    When code is executed
    Then Accumulator register should be 0x88
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 5 cycles
