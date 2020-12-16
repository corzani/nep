Feature: ORA - bitwise OR with Accumulator

  Scenario: ORA (0x09) Immediate
    Given ROM memory "0x09 0xAA"
    And Accumulator register is 0x55
    When code is executed
    Then Accumulator register should be 0xFF
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 2 cycles

  Scenario: ORA (0x05) Zero Page
    Given ROM memory "0x05 0xAA"
    And Accumulator register is 0x8D
    And 0x8B is stored at address 0x00AA
    When code is executed
    Then Accumulator register should be 0x8F
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 3 cycles

  Scenario: ORA (0x15) Zero Page, X
    Given ROM memory "0x15 0xAA"
    And Accumulator register is 0x8D
    And X register is 0x02
    And 0x8B is stored at address 0x00AC
    When code is executed
    Then Accumulator register should be 0x8F
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 4 cycles

  Scenario: ORA (0x0D) Absolute
    Given ROM memory "0x0D 0x10 0x02"
    And Accumulator register is 0x8D
    And 0x8B is stored at address 0x0210
    When code is executed
    Then Accumulator register should be 0x8F
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 4 cycles

  Scenario: ORA (0x1D) Absolute, X
    Given ROM memory "0x1D 0x10 0x02"
    And Accumulator register is 0x8D
    And X register is 0x02
    And 0x8B is stored at address 0x0212
    When code is executed
    Then Accumulator register should be 0x8F
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 4 cycles

  Scenario: ORA (0x19) Absolute, Y
    Given ROM memory "0x19 0x10 0x02"
    And Accumulator register is 0x8D
    And Y register is 0x02
    And 0x8B is stored at address 0x0212
    When code is executed
    Then Accumulator register should be 0x8F
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 4 cycles

  Scenario: ORA (0x01) Indirect, X
    Given ROM memory "0x01 0xC0"
    And Accumulator register is 0x8D
    And 0x8B is stored at address 0x01C0
    When code is executed
    Then Accumulator register should be 0x8F
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 6 cycles

  Scenario: ORA (0x11) Indirect,Y
    Given ROM memory "0x11 0x44"
    And Accumulator register is 0x8D
    And Y register is 0x05
    And 0x06 is stored at address 0x0044
    And 0x8B is stored at address 0x000B
    When code is executed
    Then Accumulator register should be 0x8F
    And N flag should be ENABLED
    And Z flag should be DISABLED
    And CPU should have performed 5 cycles
