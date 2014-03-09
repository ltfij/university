package com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.World;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A non-terminal node representing a PROGRAM.
 * 
 * @author Henry J. Wylde
 */
public class ProgramNode implements Node {

    private final List<InstructionNode> instrs;

    /**
     * Constructs a <code>ProgramNode</code> of the specified type.
     */
    public ProgramNode() {
        instrs = new LinkedList<>();
    }

    /**
     * Add an instruction to this <code>ProgramNode</code>.
     * 
     * @param instrNode the InstructionNode to add.
     * @see InstructionNode
     */
    public void addInstr(InstructionNode instrNode) {
        instrs.add(instrNode);
    }

    /**
     * Runs through this <code>ProgramNode</code>'s instructions.
     * 
     * @param robotID the robot ID to run for.
     * @param world the world to run for.
     * @param g the graphics object to animate any robot movements.
     */
    public void run(int robotID, World world, Graphics g) {
        // Variables are having values stored by value not by reference. This is to
        // prevent recursion problems with using the "[+|-|*|/]=" operators or
        // setting a variable equal to itself.
        Map<VariableNode, Integer> variables = new HashMap<>();

        for (InstructionNode instr : instrs)
            instr.execute(variables, robotID, world, g);
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = new String();
        for (InstructionNode instrNode : instrs)
            str += "\n" + instrNode;

        str = str.replaceAll("\n", "\n  "); // Indent the code.

        return "run {" + str + "\n}";
    }
}
