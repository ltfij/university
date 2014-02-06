package com.hjwylde.uni.comp261.assignment01.geographicalMap;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/*
 * Code for Assignment 1, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class MainFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private MapPanel geographicalMap;
    
    private JMenuBar mbMain;
    private JMenu mFile, mMap, mSearching;
    private JCheckBoxMenuItem miVerbose;
    private JMenuItem miExit;
    private JMenuItem miLoadNodes, miLoadRoadRestrictions, miLoadRoads,
        miLoadRoadSegments;
    private JMenuItem miRegenerateMap;
    private JCheckBoxMenuItem miDrawNodes, miDrawRoads, miDrawPath;
    private JMenuItem miResetPath;
    private JRadioButtonMenuItem miAstar, miDijkstras;
    private JRadioButtonMenuItem miTravelAny, miTravelCar, miTravelPedestrian,
        miTravelBicycle;
    
    public MainFrame() {
        setTitle("Auckland Map");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        setMinimumSize(new Dimension(300, 330));
        setLocationRelativeTo(null);
        setVisible(true);
        
        geographicalMap = new MapPanel();
        add(geographicalMap);
        
        initMenuBar();
        setJMenuBar(mbMain);
        
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
        
        geographicalMap.loadNodes();
        geographicalMap.loadRoads();
        geographicalMap.loadRoadSegments();
        geographicalMap.loadRoadRestrictions();
    }
    
    private void initMenuBar() {
        mbMain = new JMenuBar();
        
        mFile = new JMenu("File");
        mFile.setMnemonic(KeyEvent.VK_F);
        mbMain.add(mFile);
        
        miVerbose = new JCheckBoxMenuItem("Verbose");
        miVerbose.setState(geographicalMap.doesVerbose());
        miVerbose.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setVerbose(miVerbose.getState());
            }
        });
        mFile.add(miVerbose);
        
        mFile.addSeparator();
        
        miExit = new JMenuItem("Exit", KeyEvent.VK_X);
        miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
            ActionEvent.ALT_MASK));
        miExit.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mFile.add(miExit);
        
        mMap = new JMenu("Map");
        mMap.setMnemonic(KeyEvent.VK_M);
        mbMain.add(mMap);
        
        miLoadNodes = new JMenuItem("Load Nodes");
        miLoadNodes.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.loadNodes();
            }
        });
        mMap.add(miLoadNodes);
        
        miLoadRoadRestrictions = new JMenuItem("Load Road Restrictions");
        miLoadRoadRestrictions.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.loadRoadRestrictions();
            }
        });
        mMap.add(miLoadRoadRestrictions);
        
        miLoadRoads = new JMenuItem("Load Roads");
        miLoadRoads.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.loadRoads();
            }
        });
        mMap.add(miLoadRoads);
        
        miLoadRoadSegments = new JMenuItem("Load Road Segments");
        miLoadRoadSegments.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.loadRoadSegments();
            }
        });
        mMap.add(miLoadRoadSegments);
        
        mMap.addSeparator();
        
        miRegenerateMap = new JMenuItem("Regenerate Map");
        miRegenerateMap.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.generateMapImage();
                geographicalMap.repaint();
            }
        });
        mMap.add(miRegenerateMap);
        
        mMap.addSeparator();
        
        miDrawNodes = new JCheckBoxMenuItem("Draw Nodes");
        miDrawNodes.setState(geographicalMap.doesDrawNodes());
        miDrawNodes.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setDrawNodes(miDrawNodes.getState());
            }
        });
        mMap.add(miDrawNodes);
        
        miDrawRoads = new JCheckBoxMenuItem("Draw Roads");
        miDrawRoads.setState(geographicalMap.doesDrawRoads());
        miDrawRoads.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setDrawRoads(miDrawRoads.getState());
            }
        });
        mMap.add(miDrawRoads);
        
        miDrawPath = new JCheckBoxMenuItem("Draw Shortest Path");
        miDrawPath.setState(geographicalMap.doesDrawPath());
        miDrawPath.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setDrawPath(miDrawPath.getState());
            }
        });
        mMap.add(miDrawPath);
        
        mSearching = new JMenu("Searching");
        mSearching.setMnemonic(KeyEvent.VK_S);
        mbMain.add(mSearching);
        
        miResetPath = new JMenuItem("Reset Path Data");
        miResetPath.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.resetPathData();
            }
        });
        mSearching.add(miResetPath);
        
        mSearching.addSeparator();
        
        ButtonGroup searchAlgorithms = new ButtonGroup();
        miAstar = new JRadioButtonMenuItem("Use A*");
        miAstar.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setSearchMethod(GeographyMap.SEARCH_ASTAR);
            }
        });
        miAstar
            .setSelected(geographicalMap.getSearchMethod() == GeographyMap.SEARCH_ASTAR);
        mSearching.add(miAstar);
        searchAlgorithms.add(miAstar);
        
        miDijkstras = new JRadioButtonMenuItem("Use Dijkstras");
        miDijkstras.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setSearchMethod(GeographyMap.SEARCH_DIJKSTRA);
            }
        });
        miDijkstras
            .setSelected(geographicalMap.getSearchMethod() == GeographyMap.SEARCH_DIJKSTRA);
        mSearching.add(miDijkstras);
        searchAlgorithms.add(miDijkstras);
        
        mSearching.addSeparator();
        
        ButtonGroup travelMethod = new ButtonGroup();
        miTravelAny = new JRadioButtonMenuItem("Travel by Any Method");
        miTravelAny.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setTravelMethod(GeographyMap.TRAVEL_ANY);
            }
        });
        miTravelAny
            .setSelected(geographicalMap.getTravelMethod() == GeographyMap.TRAVEL_ANY);
        mSearching.add(miTravelAny);
        travelMethod.add(miTravelAny);
        
        miTravelCar = new JRadioButtonMenuItem("Travel by Car");
        miTravelCar.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setTravelMethod(GeographyMap.TRAVEL_CAR);
            }
        });
        miTravelCar
            .setSelected(geographicalMap.getTravelMethod() == GeographyMap.TRAVEL_CAR);
        mSearching.add(miTravelCar);
        travelMethod.add(miTravelCar);
        
        miTravelPedestrian = new JRadioButtonMenuItem("Travel as Pedestrian");
        miTravelPedestrian.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setTravelMethod(GeographyMap.TRAVEL_PEDESTRIAN);
            }
        });
        miTravelPedestrian
            .setSelected(geographicalMap.getTravelMethod() == GeographyMap.TRAVEL_PEDESTRIAN);
        mSearching.add(miTravelPedestrian);
        travelMethod.add(miTravelPedestrian);
        
        miTravelBicycle = new JRadioButtonMenuItem("Travel by Bicycle");
        miTravelBicycle.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                geographicalMap.setTravelMethod(GeographyMap.TRAVEL_BICYCLE);
            }
        });
        miTravelBicycle
            .setSelected(geographicalMap.getTravelMethod() == GeographyMap.TRAVEL_BICYCLE);
        mSearching.add(miTravelBicycle);
        travelMethod.add(miTravelBicycle);
    }
}