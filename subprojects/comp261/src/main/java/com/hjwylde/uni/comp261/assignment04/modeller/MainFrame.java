package com.hjwylde.uni.comp261.assignment04.modeller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.swing.*;

import com.hjwylde.uni.comp261.assignment04.graphics.World2d;
import com.hjwylde.uni.comp261.assignment04.graphics.World3d;
import com.hjwylde.uni.comp261.assignment04.parser.ParseException;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * File filter for accepting ".poly" files. Able to be parsed with a parser.
     */
    public static final javax.swing.filechooser.FileFilter POLY_FILTER =
            new javax.swing.filechooser.FileFilter() {

                @Override
                public boolean accept(File f) {
                    if (f.getName().toLowerCase(Locale.ENGLISH).endsWith(".poly"))
                        return true;

                    return f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return ".poly";
                }
            };

    /*
     * A canvas that draws a 2d picture of the 3d world.
     */
    private World2d world;

    private JMenuBar mbMain;
    private JMenu mFile, mWorld;
    private JMenuItem miExit;
    private JMenuItem miLoad, miCameraAngle, miAmbientLight;
    private JCheckBoxMenuItem miWireframeMode, miHiddenRemovalMode, miRenderLightsMode;

    public MainFrame() {
        setTitle("3d Graphic Modeller");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setVisible(true);

        world = new World2d();
        world.initListeners(this); // Register key / mouse listeners to this component so they
                                   // correctly
                                   // fire events.
        add(world);

        initMenuBar();
        setJMenuBar(mbMain);

        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);

        new Thread(world).start(); // Constantly re-draw.
    }

    /**
     * Initiate / set up the menu bar for this <code>JFrame</code>.
     */
    private void initMenuBar() {
        mbMain = new JMenuBar();

        mFile = new JMenu("File");
        mFile.setMnemonic(KeyEvent.VK_F);
        mbMain.add(mFile);

        miExit = new JMenuItem("Exit", KeyEvent.VK_X);
        miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        miExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mFile.add(miExit);

        mWorld = new JMenu("World");
        mWorld.setMnemonic(KeyEvent.VK_W);
        mbMain.add(mWorld);

        miLoad = new JMenuItem("Load Polytope", KeyEvent.VK_L);
        miLoad.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File path = MainFrame.polySearchDialog();

                if (path != null)
                    try {
                        world.loadPolytope(path);
                    } catch (ParseException e1) {
                        Main.displayError(e1.getMessage()); // Grammar error.
                    } catch (IOException e1) {
                        Main.displayError(e1.getMessage()); // File reading error.
                    }
            }
        });
        mWorld.add(miLoad);

        mWorld.addSeparator();

        miCameraAngle = new JMenuItem("Set Camera View Angle");
        miCameraAngle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String input =
                        JOptionPane
                                .showInputDialog("Please enter a new angle for the camera in degrees between 0 and 180.");

                if (input == null) // User clicked cancel.
                    return;

                try {
                    world.setViewAngle((Main.constrain(Double.parseDouble(input), 0.0, 180.0) * Math.PI) / 180.0); // Constrain
                                                                                                                   // angle
                                                                                                                   // and
                                                                                                                   // convert
                                                                                                                   // to
                                                                                                                   // radians.
                } catch (NumberFormatException e2) {
                    return; // User entered something that isn't a number!
                }
            }
        });
        mWorld.add(miCameraAngle);

        miAmbientLight = new JMenuItem("Set Ambient Light");
        miAmbientLight.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String input =
                        JOptionPane
                                .showInputDialog("Please enter the ambient light level with a value between 0 and 1.");

                if (input == null) // User clicked cancel.
                    return;

                try {
                    World3d.ambientLight = Main.constrain(Double.parseDouble(input), 0.0, 1.0); // Constrain
                                                                                                // ambient
                                                                                                // light.
                } catch (NumberFormatException e2) {
                    return; // User entered something that isn't a number!
                }
            }
        });
        mWorld.add(miAmbientLight);

        mWorld.addSeparator();

        /*
         * Wireframe mode only draws the edges of each polygon.
         */
        miWireframeMode = new JCheckBoxMenuItem("Wireframe Mode");
        miWireframeMode.setState(World3d.wireframeMode);
        miWireframeMode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                World3d.wireframeMode = miWireframeMode.getState();
            }
        });
        mWorld.add(miWireframeMode);

        /*
         * Hidden removal mode doesn't draw any polygons behind the currently drawn ones. Hard to
         * see if wireframe mode is not selected as z-buffering will remove hidden surfaces for most
         * of the time.
         */
        miHiddenRemovalMode = new JCheckBoxMenuItem("Hidden Removal Mode");
        miHiddenRemovalMode.setState(World3d.hiddenRemovalMode);
        miHiddenRemovalMode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                World3d.hiddenRemovalMode = miHiddenRemovalMode.getState();
            }
        });
        mWorld.add(miHiddenRemovalMode);

        /*
         * Render lights mode toggles whether to render the lights specified in object files.
         * Ambient light is still rendered.
         */
        miRenderLightsMode = new JCheckBoxMenuItem("Render Lights Mode");
        miRenderLightsMode.setState(World3d.renderLightsMode);
        miRenderLightsMode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                World3d.renderLightsMode = miRenderLightsMode.getState();
            }
        });
        mWorld.add(miRenderLightsMode);
    }

    /**
     * Shows a dialog for the user to select a .poly file. Returns the selected file or null if user
     * clicked cancel.
     * 
     * @return the chosen .poly file.
     */
    private static File polySearchDialog() {
        JFileChooser fc = new JFileChooser();

        fc.setCurrentDirectory(new File(Main.ROOT_PATH));

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(MainFrame.POLY_FILTER);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();

        return null;
    }
}
