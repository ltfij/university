package com.hjwylde.uni.comp261.assignment03.robotSimulator;

/*
 * Code for Assignment 3, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.awt.Graphics;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.ParseException;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.Parser;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.ParserScanner;
import com.hjwylde.uni.comp261.assignment03.robotSimulator.parser.nodes.ProgramNode;

public class RobotProgram {
    
    private ProgramNode program = null;
    
    public boolean execute(int robotID, World world, Graphics g) {
        if (!validProgramLoaded()) {
            RobotProgram.displayError("No valid program is currently loaded.");
            
            return false;
        }
        
        program.run(robotID, world, g);
        
        return true;
    }
    
    public boolean parse(Scanner s) {
        ProgramNode program = null;
        
        try {
            program = Parser.parseProgram(new ParserScanner(s));
            this.program = program;
        } catch (ParseException e) {
            RobotProgram.displayError(e.getMessage());
            
            for (StackTraceElement str : e.getStackTrace())
                System.err.println(str);
            
            return false;
        }
        
        // Output the grammar that the parsing has created.
        System.out.println(program);
        
        return true;
    }
    
    public boolean validProgramLoaded() {
        return (program != null);
    }
    
    private static void displayError(String err) {
        JOptionPane.showMessageDialog(null, err, "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}