package com.hjwylde.uni.comp102.assignment02.converter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/*
 * Code for Assignment 2, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Program for converting between kilometers and miles
 */
public class Converter {
    
    public static final String KILO_TO_MILE = "Kilometers to Miles";
    public static final String MILE_TO_KILO = "Miles to Kilometers";
    
    // 1 Kilometer = 0.621371192 Miles.
    public static final double CONVERSION_RATE = 0.621371192;
    
    private static JTextField inputBox = new JTextField(20);
    private static JLabel convertedUnit = new JLabel("Distance: ");
    private static JButton convertUnit = new JButton("Convert");
    private static JRadioButton kiloToMile = new JRadioButton(
        Converter.KILO_TO_MILE);
    private static JRadioButton mileToKilo = new JRadioButton(
        Converter.MILE_TO_KILO);
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager
                .getCrossPlatformLookAndFeelClassName());
        } catch (final UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (final IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (final InstantiationException ex) {
            ex.printStackTrace();
        } catch (final ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                Converter.setUpGUI();
            }
        });
    }
    
    public static double Round(double value, int Rpl) {
        final double p = Math.pow(10, Rpl);
        value *= p;
        return Math.round(value) / p;
    }
    
    private static void setUpGUI() {
        final JFrame frame = new JFrame("Distance Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        final JPanel panelTop = new JPanel(new FlowLayout());
        final JPanel panelBottom = new JPanel(new FlowLayout());
        
        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(Converter.kiloToMile);
        Converter.kiloToMile.setActionCommand(Converter.KILO_TO_MILE);
        Converter.kiloToMile.setSelected(true);
        buttonGroup.add(Converter.mileToKilo);
        Converter.mileToKilo.setActionCommand(Converter.MILE_TO_KILO);
        
        Converter.convertUnit.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ae) {
                final String command = buttonGroup.getSelection()
                    .getActionCommand();
                double distanceValue = 0;
                
                if (command.equals(Converter.KILO_TO_MILE))
                    distanceValue = Double.parseDouble(Converter.inputBox
                        .getText()) * Converter.CONVERSION_RATE;
                else
                    distanceValue = Double.parseDouble(Converter.inputBox
                        .getText()) / Converter.CONVERSION_RATE;
                
                Converter.convertedUnit.setText(String.valueOf(Converter.Round(
                    distanceValue, 3)));
                Converter.inputBox.requestFocus();
                Converter.inputBox.selectAll();
            }
        });
        
        panelTop.add(Converter.inputBox);
        panelTop.add(Converter.convertedUnit);
        panelBottom.add(Converter.kiloToMile);
        panelBottom.add(Converter.mileToKilo);
        panelBottom.add(Converter.convertUnit);
        frame.getContentPane().add(panelTop, BorderLayout.WEST);
        frame.getContentPane().add(panelBottom, BorderLayout.SOUTH);
        
        frame.pack();
        frame.setVisible(true);
    }
}
