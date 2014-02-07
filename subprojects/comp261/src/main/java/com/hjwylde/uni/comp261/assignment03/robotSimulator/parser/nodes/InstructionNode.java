package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.World;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.InstructionType;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.types.OperationType;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A non-terminal node representing a INSTRUCTION.
 * 
 * @author Henry J. Wylde
 */
public class InstructionNode implements Node {

    private final InstructionType type;

    // For type = InstructionType.SET | InstructionType.MOVE |
    // InstructionType.TURN
    private VariableNode variable;
    private ValueNode val;
    private OperationType opType = OperationType.SET;

    // For type = InstructionType.IF | InstructionType.WHILE | InstructionType.DO
    private BooleanNode bool;

    private final List<InstructionNode> instrs;
    private final List<InstructionNode> elseInstrs;
    // For type = InstructionType.TOUCHING
    private InstructionMethodNode instrMethod;

    /**
     * Constructs a <code>InstructionNode</code> of the specified type.
     * 
     * @param type the InstructionType.
     */
    public InstructionNode(InstructionType type) {
        this.type = type;

        instrs = new LinkedList<>();
        elseInstrs = new LinkedList<>();
    }

    /**
     * Add an instruction to execute when the else condition is reached in an if statement. Only
     * useful if <code>type = InstructionType.IF</code>.
     * 
     * @param instr the instruction to add.
     */
    public void addElseInstr(InstructionNode instr) {
        elseInstrs.add(instr);
    }

    /**
     * Add an instruction to execute during a loop or if condition. Only useful if
     * <code>type = InstructionType.IF || type = InstructionType.DO || type = InstructionType.WHILE</code>
     * .
     * 
     * @param instr the instruction.
     */
    public void addInstr(InstructionNode instr) {
        instrs.add(instr);
    }

    /**
     * Executes this <code>InstructionNode</code>.
     * 
     * @param variables a list with variable values.
     * @param robotID the robot ID to execute for.
     * @param world the world to execute for.
     */
    public void execute(Map<VariableNode, Integer> variables, int robotID, World world, Graphics g) {
        switch (type) {
            case DROP:
                world.drop(robotID, g);

                break;
            case PICKUP:
                world.pickUp(robotID, g);

                break;
            case VARIABLE:
                int varVal = val.evaluate(variables, robotID, world);

                switch (opType) {
                    case ADD:
                        if (!variables.containsKey(variable)) {
                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "Attempted to add a value to an unset variable.\nIgnoring code line...",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        varVal += variables.get(variable);

                        break;
                    case SUB:
                        if (!variables.containsKey(variable)) {
                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "Attempted to subtract a value to an unset variable.\nIgnoring code line...",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        varVal = variables.get(variable) - varVal;

                        break;
                    case MUL:
                        if (!variables.containsKey(variable)) {
                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "Attempted to multiply a value to an unset variable.\nIgnoring code line...",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        varVal *= variables.get(variable);

                        break;
                    case DIV:
                        if (!variables.containsKey(variable)) {
                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "Attempted to divide a value to an unset variable.\nIgnoring code line...",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        if (varVal == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "Attempted to divide a value by 0.\nIgnoring code line...",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        varVal = variables.get(variable) / varVal;
                        break;
                    case SET:
                }

                variables.put(variable, varVal);

                break;
            case DO:
                do
                    for (InstructionNode instr : instrs)
                        instr.execute(variables, robotID, world, g);
                while (bool.evaluate(variables, robotID, world));

                break;
            case IF:
                if (bool.evaluate(variables, robotID, world))
                    for (InstructionNode instr : instrs)
                        instr.execute(variables, robotID, world, g);
                else
                    for (InstructionNode instr : elseInstrs)
                        instr.execute(variables, robotID, world, g);

                break;
            case MOVE:
                world.moveRobot(robotID, val.evaluate(variables, robotID, world), g);

                break;
            case TURN:
                world.turnRobot(robotID, val.evaluate(variables, robotID, world), g);

                break;
            case TURN_TOWARDS_FIRST:
                instrMethod.execute(robotID, world, g);

                break;
            case WHILE:
                while (bool.evaluate(variables, robotID, world))
                    for (InstructionNode instr : instrs)
                        instr.execute(variables, robotID, world, g);
        }
    }

    /**
     * @param bool the <code>bool</code> to set.
     */
    public void setBool(BooleanNode bool) {
        this.bool = bool;
    }

    /**
     * @param instrMethod the <code>instrMethod</code> to set.
     */
    public void setInstrMethod(InstructionMethodNode instrMethod) {
        this.instrMethod = instrMethod;
    }

    /**
     * @param opType the <code>opType</code> to set.
     */
    public void setOpType(OperationType opType) {
        this.opType = opType;
    }

    /**
     * @param val the <code>val</code> to set.
     */
    public void setVal(ValueNode val) {
        this.val = val;
    }

    /**
     * @param variable the <code>variable</code> to set.
     */
    public void setVariable(VariableNode variable) {
        this.variable = variable;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        switch (type) {
            case VARIABLE:
                return variable + " " + opType.getName() + " " + val + ";";
            case MOVE:
            case TURN:
                return type.getName() + "(" + val + ");";
            case IF:
            case WHILE:
                String str = "\n";
                for (int i = 0; i < instrs.size(); i++) {
                    if (i != 0)
                        str += "\n";

                    str += instrs.get(i);
                }

                str = str.replaceAll("\n", "\n  "); // Indent the code.

                return type.getName() + " (" + bool + ") {" + str + "\n}";
            case DO:
                str = "\n";
                for (int i = 0; i < instrs.size(); i++) {
                    if (i != 0)
                        str += "\n";

                    str += instrs.get(i);
                }

                str = str.replaceAll("\n", "\n  "); // Indent the code.

                return type.getName() + " {" + str + "\n} while (" + bool + ");";
            case TURN_TOWARDS_FIRST:
                return type.getName() + instrMethod + ";";
            default:
                return type.getName() + ";";
        }
    }
}
