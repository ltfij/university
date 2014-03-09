package com.hjwylde.uni.comp261.assignment03.robotSimulator;

/*
 * Code for Assignment 3, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

public class JavaRobotSimulator {

    public static void main(String[] args) {
        final World world = new World();

        final JFrame frame = new JFrame("Java Robot Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the drawing area (double bufferred).
        @SuppressWarnings("serial")
        final JPanel panel = new JPanel(true) {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(world.getWidth(), world.getHeight());
            }

            @Override
            public void paint(Graphics g) {
                world.drawWorld(g);
            }
        };

        // Create robot control panel for playing with the simulator.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 0));

        final JButton bRobot = new JButton("Robot ID: NONE");
        bRobot.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot control robots, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<Integer> robotIDs = world.getRobotIDs();
                Object[] robots = new String[robotIDs.size()];

                for (int i = 0; i < robots.length; i++)
                    robots[i] = "Robot ID: " + robotIDs.get(i);

                String selectedRobot =
                        (String) JOptionPane.showInputDialog(null,
                                "Select which robot you want to control:", "Pick Robot ID",
                                JOptionPane.INFORMATION_MESSAGE, null, robots, robots[0]);

                if (selectedRobot != null) {
                    int id = Integer.valueOf(selectedRobot.substring(10));
                    bRobot.setText("Robot ID: " + id);
                }
            }
        });
        buttonPanel.add(bRobot);

        JButton bRobotMove = new JButton("MOVE");
        bRobotMove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot control robots, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bRobot.getText().equals("Robot ID: NONE")) {
                    JOptionPane.showMessageDialog(null,
                            "Please select ROBOT using ROBOT button first!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.valueOf(bRobot.getText().substring(10));
                int move = 10;
                String input = JOptionPane.showInputDialog("How many pixels to move?", "" + move);

                try {
                    move = Integer.valueOf(input);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Cannot understand " + input
                            + " as integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                world.moveRobot(id, move, panel.getGraphics());
            }
        });
        buttonPanel.add(bRobotMove);

        JButton bRobotTurn = new JButton("TURN");
        bRobotTurn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot control robots, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bRobot.getText().equals("Robot ID: NONE")) {
                    JOptionPane.showMessageDialog(null,
                            "Please select ROBOT using ROBOT button first!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.valueOf(bRobot.getText().substring(10));
                int turn = 45;
                String input =
                        JOptionPane.showInputDialog(
                                "How many degrees to turn anticlockwise (left)?", "" + turn);

                try {
                    turn = Integer.valueOf(input);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Cannot understand " + input
                            + " as integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                world.turnRobot(id, turn, panel.getGraphics());
            }
        });
        buttonPanel.add(bRobotTurn);

        JButton bRobotPickUp = new JButton("PICKUP");
        bRobotPickUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot control robots, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bRobot.getText().equals("Robot ID: NONE")) {
                    JOptionPane.showMessageDialog(null,
                            "Please select ROBOT using ROBOT button first!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.valueOf(bRobot.getText().substring(10));
                world.pickUp(id, panel.getGraphics());
            }
        });
        buttonPanel.add(bRobotPickUp);

        JButton bRobotDrop = new JButton("DROP");
        bRobotDrop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot control robots, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bRobot.getText().equals("Robot ID: NONE")) {
                    JOptionPane.showMessageDialog(null,
                            "Please select ROBOT using ROBOT button first!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.valueOf(bRobot.getText().substring(10));
                world.drop(id, panel.getGraphics());
            }
        });
        buttonPanel.add(bRobotDrop);

        // Create advanced robot control panel for playing with the simulator.
        JPanel buttonPanelAdvanced = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 0));

        JButton bRobotTurnTowardsFirstThing = new JButton("TURN TOWARDS FIRST THING");
        bRobotTurnTowardsFirstThing.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot control robots, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bRobot.getText().equals("Robot ID: NONE")) {
                    JOptionPane.showMessageDialog(null,
                            "Please select ROBOT using ROBOT button first!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.valueOf(bRobot.getText().substring(10));
                world.turnTowardsFirstThing(id, panel.getGraphics());
            }
        });
        buttonPanelAdvanced.add(bRobotTurnTowardsFirstThing);

        JButton bRobotTurnTowardsFirstBox = new JButton("TURN TOWARDS FIRST BOX");
        bRobotTurnTowardsFirstBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot control robots, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bRobot.getText().equals("Robot ID: NONE")) {
                    JOptionPane.showMessageDialog(null,
                            "Please select ROBOT using ROBOT button first!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.valueOf(bRobot.getText().substring(10));
                world.turnTowardsFirstBox(id, panel.getGraphics());
            }
        });
        buttonPanelAdvanced.add(bRobotTurnTowardsFirstBox);

        // Create a standard kind of menu.
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program");
        menuBar.add(menu);

        JMenuItem miLoadWorld = new JMenuItem("Load World", KeyEvent.VK_W);
        miLoadWorld.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
                // chooser.setFileFilter(new
                // FileNameExtensionFilter("Text Files with Worlds", "world"));
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    if (world.loadWorld(chooser.getSelectedFile().getPath())) {
                        bRobot.setText("Robot ID: NONE");
                        frame.pack();
                        panel.repaint();
                    }
            }
        });
        menu.add(miLoadWorld);

        JMenuItem miLoadProgram = new JMenuItem("Load Program", KeyEvent.VK_P);
        miLoadProgram.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot load program, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<Integer> robotIDs = world.getRobotIDs();
                Object[] robots = new String[robotIDs.size()];
                for (int i = 0; i < robots.length; i++)
                    robots[i] = "Robot ID: " + robotIDs.get(i);

                String selectedRobot =
                        (String) JOptionPane.showInputDialog(null,
                                "Select which robot to load a program for:", "Pick Robot ID",
                                JOptionPane.INFORMATION_MESSAGE, null, robots, robots[0]);

                if (selectedRobot != null) {
                    JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
                    // chooser.setFileFilter(new
                    // FileNameExtensionFilter("Text Files with Programs",
                    // "program"));
                    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                        world.loadProgram(chooser.getSelectedFile().getPath(),
                                Integer.valueOf(selectedRobot.substring(10)), panel.getGraphics());
                }
            }
        });
        menu.add(miLoadProgram);

        JMenuItem miExecuteProgram = new JMenuItem("Execute Program", KeyEvent.VK_E);
        miExecuteProgram.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!world.validWorldLoaded()) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot execute program, since no world is loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<Integer> validRIDs = world.getRobotIDsWithValidProgramsLoaded();
                if (validRIDs.size() == 0) {
                    JOptionPane.showMessageDialog(null, "No robots have programs loaded!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Object[] robots = new String[validRIDs.size()];
                for (int i = 0; i < robots.length; i++)
                    robots[i] = "Robot ID: " + validRIDs.get(i);

                String selectedRobot =
                        (String) JOptionPane.showInputDialog(null,
                                "Select which robot to execute the program for:", "Pick Robot ID",
                                JOptionPane.INFORMATION_MESSAGE, null, robots, robots[0]);

                if (selectedRobot != null)
                    world.executeProgram(Integer.valueOf(selectedRobot.substring(10)),
                            panel.getGraphics());
            }
        });
        menu.add(miExecuteProgram);

        JMenuItem miExit = new JMenuItem("Exit", KeyEvent.VK_X);
        miExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        menu.add(miExit);

        // Put all the parts onto the form and pack it together.
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        frame.getContentPane().add(buttonPanelAdvanced, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}
