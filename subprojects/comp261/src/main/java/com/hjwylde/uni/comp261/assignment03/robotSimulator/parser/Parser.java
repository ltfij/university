package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes.*;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.*;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.BooleanConditionType.BooleanConditionOpType;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * Grammar:
 * 
 * <pre>
 * PROGRAM ::= INSTRUCTION*
 * 
 * INSTRUCTION ::= VARIABLE [+|-|*|/]= VALUE;
 *               | do {INSTRUCTION*} while (BOOLEAN);
 *               | drop();
 *               | if (BOOLEAN) {INSTRUCTION*}[ else {INSTRUCTION*}]
 *               | move(VALUE);
 *               | pickup();
 *               | turn(VALUE);
 *               | turnTowardsFirstINSTRUCTION_METHOD;
 *               | while (BOOLEAN) {INSTRUCTION*}
 * 
 * INSTRUCTION_METHOD ::= Box()
 *                      | Thing()
 * 
 * BOOLEAN ::= (BOOLEAN_CONDITION)
 *           | !BOOLEAN
 *           | false
 *           | touchingBOOLEAN_METHOD
 *           | true
 * 
 * BOOLEAN_CONDITION ::= BOOLEAN (|||&&) BOOLEAN
 *                     | VALUE (==|!=|>|<) VALUE
 * 
 * BOOLEAN_METHOD ::= Box()
 *                  | Robot()
 *                  | Thing()
 *                  | Wall()
 * 
 * VALUE ::= VARIABLE
 *         | [-][0-9]+
 *         | getVALUE_METHOD
 * 
 * VALUE_METHOD ::= BoxDistance()
 *                | NumberOfThingsNotInBoxes()
 *                | ThingDistance()
 * 
 * VARIABLE ::= $[a-z][A-Za-z0-9_]*
 * </pre>
 */

public class Parser {

    /**
     * Parse a new <i>PROGRAM</i> from the given <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new ProgramNode from the given ParserScanner.
     * @throws ParseException if the input ParserScanner does not match the <i>PROGRAM</i> grammar.
     */
    public static ProgramNode parseProgram(ParserScanner s) throws ParseException {
        ProgramNode program = new ProgramNode();

        while (s.hasNext()) {
            program.addInstr(Parser.parseInstruction(s)); // Add each instruction to this
            // program.
            s.clearWhitespace();
        }

        return program;
    }

    /**
     * Parse a <i>BOOLEAN</i> from the given <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new BooleanNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the <i>BOOLEAN</i>
     *         grammar.
     */
    private static BooleanNode parseBoolean(ParserScanner s) throws ParseException {
        BooleanNode bool;

        s.expectToken(); // Always call expectToken() before peek().
        switch (s.peek()) { // Look ahead 1.
            case 't': // BooleanType.TRUE | BooleanType.TOUCHING
                s.expect("t", true, false); // Don't clear whitespace after expecting.

                s.expectToken();
                switch (s.peek()) { // Look ahead 1 (or 2 from start as the reference),
                    case 'r': // BooleanType.TRUE
                        bool = new BooleanNode(BooleanType.TRUE);

                        s.expect("rue", false, true); // Don't clear whitespace before
                                                      // expecting.

                        break;
                    case 'o': // BooleanType.TOUCHING
                        bool = new BooleanNode(BooleanType.TOUCHING);

                        s.expect("ouching", false); // Don't clear whitespace before or after
                                                    // expecting.
                        bool.setBoolMethod(Parser.parseBooleanMethod(s)); // Get the next
                        // BOOLEAN_METHOD.

                        break;
                    default: // Unknown BooleanType
                        throw new ParseException("Unknown begin characters for a boolean \"t"
                                + s.peek() + "\".");
                }

                break;
            case 'f': // BooleanType.FALSE
                bool = new BooleanNode(BooleanType.FALSE);

                s.expect("false");

                break;
            case '(': // BooleanType.CONDITION
                bool = new BooleanNode(BooleanType.CONDITION);

                s.expect("(");
                bool.setBoolCondition(Parser.parseBooleanCondition(s)); // Get the next
                // BOOLEAN_CONDITION.
                s.expect(")");

                break;
            case '!': // BooleanType.NOT
                bool = new BooleanNode(BooleanType.NOT);

                s.expect("!");
                bool.setBool(Parser.parseBoolean(s)); // Get the next BOOLEAN.

                break;
            default: // Unknown BooleanType
                throw new ParseException("Unknown begin character for a boolean \"" + s.peek()
                        + "\".");
        }

        return bool;
    }

    /**
     * Parse a <i>BOOLEAN_CONDITION</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new BooleanConditionNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the
     *         <i>BOOLEAN_CONDITION</i> grammar.
     */
    private static BooleanConditionNode parseBooleanCondition(ParserScanner s)
            throws ParseException {
        BooleanConditionNode boolCondition;

        s.expectToken();
        switch (s.peek()) { // Look ahead 1 to check the first sets to see whether
                            // we are looking at "(BOOLEAN..." or "(VALUE..."
            case 't': // BooleanType.TRUE | BooleanType.TOUCHING
            case 'f': // BooleanType.FALSE
            case '(': // BooleanType.CONDITION
            case '!': // BooleanType.NOT
                // BooleanConditionType.BOOLEAN
                boolCondition = new BooleanConditionNode(BooleanConditionType.BOOLEAN);

                boolCondition.setFirstBool(Parser.parseBoolean(s)); // Get the next BOOLEAN.

                s.expectToken();
                switch (s.peek()) { // Look ahead 1 to check the operation type.
                    case '|': // BooleanConditionOpType.OR
                        s.expect("||");

                        boolCondition.setOpType(BooleanConditionOpType.OR);

                        break;
                    case '&': // BooleanConditionOpType.AND
                        s.expect("&&");

                        boolCondition.setOpType(BooleanConditionOpType.AND);

                        break;
                    default: // Unknown BooleanConditionOpType
                        throw new ParseException("Unknown boolean operator \"" + s.peek() + "\".");
                }

                s.clearWhitespace();

                boolCondition.setSecBool(Parser.parseBoolean(s)); // Get the next BOOLEAN.

                break;
            default: // BooleanConditionType.VALUE
                boolCondition = new BooleanConditionNode(BooleanConditionType.VALUE);

                boolCondition.setFirstVal(Parser.parseValue(s)); // Get the next VALUE.

                s.clearWhitespace();
                s.expectToken();
                switch (s.peek()) {
                    case '=': // BooleanConditionOpType.EQUALS
                        s.expect("==");

                        boolCondition.setOpType(BooleanConditionOpType.EQUALS);

                        break;
                    case '!': // BooleanConditionOpType.NOT_EQUALS
                        s.expect("!=");

                        boolCondition.setOpType(BooleanConditionOpType.NOT_EQUALS);

                        break;
                    case '>': // BooleanConditionOpType.GREATER_THAN
                        s.expect(">");

                        boolCondition.setOpType(BooleanConditionOpType.GREATER_THAN);

                        break;
                    case '<': // BooleanConditionOpType.LESS_THAN
                        s.expect("<");

                        boolCondition.setOpType(BooleanConditionOpType.LESS_THAN);

                        break;
                    default: // Unknown BooleanConditionOpType
                        throw new ParseException("Unknown boolean operator \"" + s.peek() + "\".");
                }

                s.clearWhitespace();

                boolCondition.setSecVal(Parser.parseValue(s)); // Get the next VALUE.
        }

        return boolCondition;
    }

    /**
     * Parse a <i>BOOLEAN_METHOD</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new BooleanMethodNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the
     *         <i>BOOLEAN_METHOD</i> grammar.
     */
    private static BooleanMethodNode parseBooleanMethod(ParserScanner s) throws ParseException {
        s.expectToken();
        switch (s.peek()) { // Look ahead 1.
            case 'B': // BooleanMethodType.BOX
                s.expect("Box()", false, true); // Don't clear whitespace before
                                                // expecting.

                return new BooleanMethodNode(BooleanMethodType.BOX);
            case 'R': // BooleanMethodType.ROBOT
                s.expect("Robot()", false, true); // Don't clear whitespace before
                                                  // expecting.

                return new BooleanMethodNode(BooleanMethodType.ROBOT);
            case 'T': // BooleanMethodType.THING
                s.expect("Thing()", false, true); // Don't clear whitespace before
                                                  // expecting.

                return new BooleanMethodNode(BooleanMethodType.THING);
            case 'W': // BooleanMethodType.WALL
                s.expect("Wall()", false, true); // Don't clear whitespace before
                                                 // expecting.

                return new BooleanMethodNode(BooleanMethodType.WALL);
            default: // Unknown BooleanMethodType
                throw new ParseException("Unknown begin character for a boolean method \""
                        + s.peek() + "\".");
        }
    }

    /**
     * Parse an <i>INSTRUCTION</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new InstructionNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the
     *         <i>INSTRUCTION</i> grammar.
     */
    private static InstructionNode parseInstruction(ParserScanner s) throws ParseException {
        InstructionNode instruction;

        s.expectToken();
        switch (s.peek()) { // Look ahead 1
            case 'd': // InstructionType.DROP | InstructionType.DO
                s.expect("d", true, false); // Don't clear whitespace after expecting.

                s.expectToken();
                switch (s.peek()) { // Look ahead 1 (2).
                    case 'r': // InstructionType.DROP
                        instruction = new InstructionNode(InstructionType.DROP);

                        s.expect("rop()", false, true); // Don't clear whitespace before
                                                        // expecting.
                        s.expect(";");

                        break;
                    case 'o': // InstructionType.DO
                        instruction = new InstructionNode(InstructionType.DO);

                        s.expect("o", false, true); // Don't clear whitespace before expecting.
                        s.expect("{");
                        while (s.peek() != '}') {
                            instruction.addInstr(Parser.parseInstruction(s)); // Get the next
                            // INSTRUCTION.
                            s.clearWhitespace();
                        }
                        s.expect("}", "while", "("); // Expect these tokens clearing whitespace
                                                     // in between them.
                        instruction.setBool(Parser.parseBoolean(s)); // Get the next BOOLEAN.
                        s.expect(")", ";"); // Expect these tokens clearing whitespace in
                                            // between them.

                        break;
                    default: // Unknown InstructionType
                        throw new ParseException("Unknown begin characters for an instruction \"d"
                                + s.peek() + "\".");
                }

                break;
            case 'p': // InstructionType.PICKUP
                instruction = new InstructionNode(InstructionType.PICKUP);

                s.expect("pickup()", ";"); // Expect these tokens clearing whitespace
                                           // in between them.

                break;
            case '$': // InstructionType.SET
                instruction = new InstructionNode(InstructionType.VARIABLE);

                instruction.setVariable(Parser.parseVariable(s)); // Get the next VARIABLE.

                s.clearWhitespace();
                s.expectToken();
                switch (s.peek()) { // Look ahead 1 (2).
                    case '=': // OperationType.SET
                        instruction.setOpType(OperationType.SET);

                        break;
                    case '+': // OperationType.ADD
                        instruction.setOpType(OperationType.ADD);

                        s.expect("+", true, false); // Don't clear whitespace after expecting.

                        break;
                    case '-': // OperationType.SUB
                        instruction.setOpType(OperationType.SUB);

                        s.expect("-", true, false); // Don't clear whitespace after expecting.

                        break;
                    case '*': // OperationType.MUL
                        instruction.setOpType(OperationType.MUL);

                        s.expect("*", true, false); // Don't clear whitespace after expecting.

                        break;
                    case '/': // OperationType.DIV
                        instruction.setOpType(OperationType.DIV);

                        s.expect("/", true, false); // Don't clear whitespace after expecting.

                        break;
                    default:
                        throw new ParseException("Unknown begin character for an operation \""
                                + s.peek() + "\".");
                }
                s.expect("=", false, true); // Don't clear whitespace before expecting.
                instruction.setVal(Parser.parseValue(s)); // Get the next VALUE.
                s.expect(";");

                break;
            case 'i': // InstructionType.IF
                instruction = new InstructionNode(InstructionType.IF);

                s.expect("if", "("); // Expect these tokens, clearing whitespace in
                                     // between them.
                instruction.setBool(Parser.parseBoolean(s)); // Get the next BOOLEAN.
                s.expect(")", "{"); // Expect these tokens, clearing whitespace in between
                                    // them.
                while (s.peek() != '}') {
                    instruction.addInstr(Parser.parseInstruction(s)); // Get the next INSTRUCTION.
                    s.clearWhitespace();
                }
                s.expect("}");

                if (s.peek() == 'e') { // If an else token has been specified.
                    s.expect("else", "{"); // Expect these tokens, clearing whitespace in
                                           // between them.
                    while (s.peek() != '}') {
                        instruction.addElseInstr(Parser.parseInstruction(s)); // Get the next
                        // INSTRUCTION.
                        s.clearWhitespace();
                    }
                    s.expect("}");
                }

                break;
            case 'm': // InstructionType.MOVE
                instruction = new InstructionNode(InstructionType.MOVE);

                s.expect("move", "("); // Expect these tokens, clearing whitespace in
                                       // between them.
                instruction.setVal(Parser.parseValue(s)); // Get the next VALUE.
                s.expect(")", ";"); // Expect these tokens, clearing whitespace in between
                                    // them.

                break;
            case 't': // InstructionType.TURN | InstructionType.TURN_TOWARDS_FIRST
                s.expect("turn", true, false); // Don't clear whitespace after expecting.

                s.expectToken();
                switch (s.peek()) {
                    case '(': // InstructionType.TURN
                        instruction = new InstructionNode(InstructionType.TURN);

                        s.expect("(");
                        instruction.setVal(Parser.parseValue(s)); // Get the next VALUE.
                        s.expect(")", ";"); // Expect these tokens, clearing whitespace in
                                            // between them.

                        break;
                    case 'T': // InstructionType.TURN_TOWARDS_FIRST
                        instruction = new InstructionNode(InstructionType.TURN_TOWARDS_FIRST);

                        s.expect("TowardsFirst", false); // Don't clear whitespace before or
                                                         // after expecting.
                        instruction.setInstrMethod(Parser.parseInstructionMethod(s)); // Get the
                                                                                      // next
                        // INSTRUCTION_METHOD.
                        s.expect(";");

                        break;
                    default: // Unknown InstructionType
                        throw new ParseException(
                                "Unknown begin characters for an instruction method \"turn"
                                        + s.peek() + "\".");
                }

                break;
            case 'w': // InstructionType.WHILE
                instruction = new InstructionNode(InstructionType.WHILE);

                s.expect("while", "("); // Expect these tokens, clearing whitespace in
                                        // between them.
                instruction.setBool(Parser.parseBoolean(s)); // Get the next BOOLEAN.
                s.expect(")", "{"); // Expect these tokens, clearing whitespace in between
                                    // them.
                while (s.peek() != '}') {
                    instruction.addInstr(Parser.parseInstruction(s)); // Get the next INSTRUCTION.
                    s.clearWhitespace();
                }
                s.expect("}");

                break;
            default: // Unknown InstructionType
                throw new ParseException("Unknown begin character for an instruction \"" + s.peek()
                        + "\".");
        }

        return instruction;
    }

    /**
     * Parse an <i>INSTRUCTION_METHOD</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new InstructionMethodNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the
     *         <i>INSTRUCTION_METHOD</i> grammar.
     */
    private static InstructionMethodNode parseInstructionMethod(ParserScanner s)
            throws ParseException {
        s.expectToken();
        switch (s.peek()) { // Look ahead 1.
            case 'B': // InstructionMethodType.BOX
                s.expect("Box()", false, true); // Don't clear whitespace before
                                                // expecting.

                return new InstructionMethodNode(InstructionMethodType.BOX);
            case 'T': // InstructionMethodType.THING
                s.expect("Thing()", false, true); // Don't clear whitespace before
                                                  // expecting.

                return new InstructionMethodNode(InstructionMethodType.THING);
            default: // Unknown InstructionMethodType
                throw new ParseException("Unknown begin character for an instruction method \""
                        + s.peek() + "\".");
        }
    }

    /**
     * Parse a <i>VALUE</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new ValueNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the <i>VALUE</i>
     *         grammar.
     */
    private static ValueNode parseValue(ParserScanner s) throws ParseException {
        ValueNode value;

        s.expectToken();
        if (s.hasNextInt()) { // ValueType.INTEGER
            value = new ValueNode(ValueType.INTEGER);

            value.setVal(s.nextInt());
        } else
            switch (s.peek()) { // Look ahead 1.
                case '$': // ValueType.VARIABLE
                    value = new ValueNode(ValueType.VARIABLE);

                    value.setVariable(Parser.parseVariable(s)); // Get the next VARIABLE.

                    break;
                case 'g': // ValueType.GET
                    value = new ValueNode(ValueType.GET);

                    s.expect("get", true, false); // Don't clear whitespace after expecting.
                    value.setValMethod(Parser.parseValueMethod(s)); // Get the next VALUE_METHOD.

                    break;
                default:
                    throw new ParseException("Unknown begin character for a value \"" + s.peek()
                            + "\".");
            }

        return value;
    }

    /**
     * Parse a <i>VALUE_METHOD</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new ValueMethodNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the
     *         <i>VALUE_METHOD</i> grammar.
     */
    private static ValueMethodNode parseValueMethod(ParserScanner s) throws ParseException {
        s.expectToken();
        switch (s.peek()) { // Look ahead 1.
            case 'B': // ValueMethodType.BOX_DISTANCE
                s.expect("BoxDistance()", false, true); // Don't clear whitespace before
                                                        // expecting.

                return new ValueMethodNode(ValueMethodType.BOX_DISTANCE);
            case 'N': // ValueMethodType.NUMBER_OF_THINGS_NOT_IN_BOXES
                s.expect("NumberOfThingsNotInBoxes()", false, true); // Don't clear
                                                                     // whitespace before
                                                                     // expecting.

                return new ValueMethodNode(ValueMethodType.NUMBER_OF_THINGS_NOT_IN_BOXES);
            case 'T': // ValueMethodType.THING_DISTANCE
                s.expect("ThingDistance()", false, true); // Don't clear whitespace before
                                                          // expecting.

                return new ValueMethodNode(ValueMethodType.THING_DISTANCE);
            default:
                throw new ParseException("Unknown begin character for a value method \"" + s.peek()
                        + "\".");
        }
    }

    /**
     * Parse a <i>VARIABLE</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new VariableNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the <i>VARIABLE</i>
     *         grammar.
     */
    private static VariableNode parseVariable(ParserScanner s) throws ParseException {
        s.expect("$", true, false); // Don't clear whitespace after expecting.
        String varName = "$";

        s.expectToken(); // Variable name must begin with [a-z].
        while (Character.isDigit(s.peek()) || Character.isLetter(s.peek()) || (s.peek() == '_')) {
            s.expectToken();
            varName += s.next();
        }

        // Note: with the above code the variable name may begin with a number or _
        // or capital, when creating the variable though is when an error will be
        // thrown if the varName does not match VariableNode.NAME_REGEX.
        return new VariableNode(varName);
    }
}
