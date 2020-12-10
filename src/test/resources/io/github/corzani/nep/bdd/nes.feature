Feature: Nes Opcodes

  Scenario: AND Opcode
    Given ROM memory "0x29 0xAA"
    And Accumulator register is 0x8D
    When code is executed
    Then Accumulator register should be 0x88
#    And N flag should be 0x0101

#    Then I really have 42 cukes in my belly

#  Scenario: another scenario which should have isolated state
#    Given I have 42 cukes in my belly

#  Scenario: Parameterless lambdas
#    Given A statement with a simple match
#    Given A statement with a scoped argument
#
#  Scenario: I can use body expressions
#    Given A statement with a body expression
#
#  Scenario: Multi-param lambdas
#    Given I will give you 1 and 2.2 and three and 4
#
#  Scenario: use a table
#    Given this data table:
#      | first  | last     |
#      | Aslak  | Helley  |
#      | Donald | Duck     |