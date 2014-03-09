package com.hjwylde.uni.comp261.assignment01.geographicalMap;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * Code for Assignment 1, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class MapPanel extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;

    public static final javax.swing.filechooser.FileFilter TAB_FILTER =
            new javax.swing.filechooser.FileFilter() {

                @Override
                public boolean accept(File f) {
                    if (f.getName().toLowerCase(Locale.ENGLISH).endsWith(".tab"))
                        return true;

                    return f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return ".tab";
                }
            };

    private GeographyMap aucklandMap;
    private List<Integer> shortestPath;

    private int searchMethod = GeographyMap.SEARCH_ASTAR;
    private int travelMethod = GeographyMap.TRAVEL_ANY;

    private int mapSize = 0;
    private Image mapImage;

    private Point2D mouseClickStart;
    private Point2D mouseClickEnd;

    private boolean verbose = true;
    private boolean drawNodes = true;
    private boolean drawRoads = true;
    private boolean drawPath = true;

    public MapPanel() {
        addMouseListener(this);

        aucklandMap = new GeographyMap();

        mapSize = 800;

        setVerbose(false);
        setTravelMethod(GeographyMap.TRAVEL_ANY);
    }

    /**
     * @return the <code>drawNodes</code>.
     */
    public boolean doesDrawNodes() {
        return drawNodes;
    }

    /**
     * @return the <code>drawPath</code>.
     */
    public boolean doesDrawPath() {
        return drawPath;
    }

    /**
     * @return the <code>drawRoads</code>.
     */
    public boolean doesDrawRoads() {
        return drawRoads;
    }

    /**
     * @return the <code>verbose</code>.
     */
    public boolean doesVerbose() {
        return verbose;
    }

    public void generateMapImage() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                mapImage =
                        aucklandMap.generateMapImage(mapSize, drawNodes, drawRoads,
                                (drawPath ? shortestPath : null));

                repaint();
            }

        }).start();
    }

    /**
     * @return the <code>searchMethod</code>.
     */
    public int getSearchMethod() {
        return searchMethod;
    }

    /**
     * @return the <code>travelMethod</code>.
     */
    public int getTravelMethod() {
        return travelMethod;
    }

    public void loadNodes() {
        resetPathData();

        File tabFile = new File(Main.ROOT_PATH, "roadInfo/nodeID-lat-lon.tab");

        if (!tabFile.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Could not load nodes from: \"" + tabFile.toString()
                            + "\"\nPlease pick the correct path.", "Error loading nodes",
                    JOptionPane.ERROR_MESSAGE);

            tabFile = tabSearchDialog();
        }

        if (tabFile == null)
            return;

        if (!aucklandMap.loadNodes(tabFile))
            JOptionPane.showMessageDialog(this,
                    "Could not load nodes for some reason.\nPlease try again.",
                    "Error loading nodes", JOptionPane.ERROR_MESSAGE);
        else
            generateMapImage();
    }

    public void loadRoadRestrictions() {
        resetPathData();

        File tabFile = new File(Main.ROOT_PATH, "roadInfo/restrictions.tab");

        if (!tabFile.exists()) {
            JOptionPane.showMessageDialog(this, "Could not load road restrictions from: \""
                    + tabFile.toString() + "\"\nPlease pick the correct path.",
                    "Error loading road restrictions", JOptionPane.ERROR_MESSAGE);

            tabFile = tabSearchDialog();
        }

        if (tabFile == null)
            return;

        if (!aucklandMap.loadRoadRestrictions(tabFile))
            JOptionPane
                    .showMessageDialog(
                            this,
                            "Could not load road restrictions for some reason.\nPlease try again.\nMake sure you have loaded the nodes and roads first",
                            "Error loading road restrictions", JOptionPane.ERROR_MESSAGE);
    }

    public void loadRoads() {
        resetPathData();

        File tabFile = new File(Main.ROOT_PATH, "roadInfo/roadID-roadInfo.tab");

        if (!tabFile.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Could not load roads from: \"" + tabFile.toString()
                            + "\"\nPlease pick the correct path.", "Error loading roads",
                    JOptionPane.ERROR_MESSAGE);

            tabFile = tabSearchDialog();
        }

        if (tabFile == null)
            return;

        if (!aucklandMap.loadRoads(tabFile))
            JOptionPane.showMessageDialog(this,
                    "Could not load roads for some reason.\nPlease try again.",
                    "Error loading roads", JOptionPane.ERROR_MESSAGE);
    }

    public void loadRoadSegments() {
        resetPathData();

        File tabFile = new File(Main.ROOT_PATH, "roadInfo/roadSeg-roadID-length-nodeID-nodeID.tab");

        if (!tabFile.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Could not load road segments from: \"" + tabFile.toString()
                            + "\"\nPlease pick the correct path.", "Error loading road segments",
                    JOptionPane.ERROR_MESSAGE);

            tabFile = tabSearchDialog();
        }

        if (tabFile == null)
            return;

        if (!aucklandMap.loadRoadSegments(tabFile))
            JOptionPane
                    .showMessageDialog(
                            this,
                            "Could not load road segments for some reason.\nPlease try again.\nMake sure you have loaded the nodes and roads first",
                            "Error loading road segments", JOptionPane.ERROR_MESSAGE);
        else
            generateMapImage();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if ((e.getX() < 0) || (e.getX() > getWidth()) || (e.getY() < 0) || (e.getY() > getHeight()))
            return;

        int xOffset = ((getWidth() > getHeight()) ? ((getWidth() - getHeight()) / 2) : 0);

        Point2D click = new Point2D.Double(e.getX() - xOffset, e.getY());
        click = aucklandMap.scaleVectorFrom(click, getMinEdge());

        if (aucklandMap.getIndexAt(click) == -1)
            return;

        if ((mouseClickStart == null) || (mouseClickEnd != null)) {
            resetPathData();

            mouseClickStart = click;
        } else {
            mouseClickEnd = click;

            int n1 = aucklandMap.getIndexAt(mouseClickStart);
            int n2 = aucklandMap.getIndexAt(mouseClickEnd);

            shortestPath = aucklandMap.searchGraph(n1, n2, searchMethod);
            generateMapImage();

            if (shortestPath == null)
                JOptionPane.showMessageDialog(this, "No path found between (" + n1 + ") and (" + n2
                        + ").", "Map Searching", JOptionPane.ERROR_MESSAGE);
            else
                verbose(aucklandMap.pathToString(shortestPath));
        }

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        int xOffset = ((getWidth() > getHeight()) ? ((getWidth() - getHeight()) / 2) : 0);

        g.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 14));
        FontMetrics fm = g.getFontMetrics();

        if (mapImage != null) {
            Image img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics iG = img.getGraphics();

            iG.setColor(Color.WHITE);
            iG.fillRect(0, 0, getWidth(), getHeight());
            iG.drawImage(mapImage, xOffset, 0, (int) getMinEdge(), (int) getMinEdge(), this);

            iG.setColor(Color.RED);
            Point2D click;
            if (mouseClickStart != null) {
                click = aucklandMap.scaleVectorTo(mouseClickStart, getMinEdge());
                click.setLocation(click.getX() + xOffset, click.getY());

                iG.fillOval((int) click.getX() - 3, (int) click.getY() - 3, 6, 6);
            }
            if (mouseClickEnd != null) {
                click = aucklandMap.scaleVectorTo(mouseClickEnd, getMinEdge());
                click.setLocation(click.getX() + xOffset, click.getY());

                iG.fillOval((int) click.getX() - 3, (int) click.getY() - 3, 6, 6);
            }

            iG.dispose();

            g.drawImage(img, 0, 0, this);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.drawRect((getWidth() - (fm.stringWidth("No map image to draw.") + 30)) / 2,
                    getHeight() / 2, fm.stringWidth("No map image to draw.") + 30,
                    fm.getHeight() + 20);

            g.drawString("No map image to draw.",
                    ((getWidth() - (fm.stringWidth("No map image to draw.") + 30)) / 2) + 20,
                    (getHeight() / 2) + fm.getHeight() + 5);
        }

        g.dispose();
    }

    public void resetPathData() {
        shortestPath = null;

        mouseClickStart = null;
        mouseClickEnd = null;

        generateMapImage();
    }

    /**
     * @param drawNodes the <code>drawNodes</code> to set.
     */
    public void setDrawNodes(boolean drawNodes) {
        this.drawNodes = drawNodes;

        generateMapImage();
    }

    /**
     * @param drawPath the <code>drawPath</code> to set.
     */
    public void setDrawPath(boolean drawPath) {
        this.drawPath = drawPath;

        generateMapImage();
    }

    /**
     * @param drawRoads the <code>drawRoads</code> to set.
     */
    public void setDrawRoads(boolean drawRoads) {
        this.drawRoads = drawRoads;

        generateMapImage();
    }

    /**
     * @param searchMethod the <code>searchMethod</code> to set.
     */
    public void setSearchMethod(int searchMethod) {
        this.searchMethod = searchMethod;
    }

    /**
     * @param travelMethod the <code>travelMethod</code> to set.
     */
    public void setTravelMethod(int travelMethod) {
        this.travelMethod = travelMethod;

        aucklandMap.setTravelMethod(travelMethod);

        generateMapImage();
    }

    /**
     * @param verbose the <code>verbose</code> to set.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;

        aucklandMap.setVerbose(verbose);
    }

    private double getMinEdge() {
        return Math.min(getWidth(), getHeight());
    }

    private File tabSearchDialog() {
        JFileChooser fc = new JFileChooser();

        fc.setCurrentDirectory(new File(Main.ROOT_PATH));

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(MapPanel.TAB_FILTER);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();

        return null;
    }

    private void verbose(String str) {
        if (verbose)
            System.out.println(str);
    }
}
