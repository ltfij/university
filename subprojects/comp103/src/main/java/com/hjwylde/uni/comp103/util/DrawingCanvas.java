package com.hjwylde.uni.comp103.util;

import java.awt.*;
import java.io.File;

/**
 * A DrawingCanvas is an area for drawing shapes, text, and images. It uses double buffering for
 * smoother graphics.
 * <P>
 * It provides methods for
 * <UL>
 * <LI>drawing (outline or filled) shapes
 * <LI>inverting lines and rectangles
 * <LI>drawing text
 * <LI>redisplaying, and clearing the canvas
 * <LI>changing the foreground color and the fontsize
 * </UL>
 */

// It is not clear what class DrawingCanvas should extend
// For java 1.2 we had to have a heavyweight than lightweight component
// or we get NullPointerExceptions from
// modal dialog boxes invoked from mouse handlers on the component.
// Component works on netbsd with java 1.4.2, but not windows with java 1.4.2
// public class DrawingCanvas extends Component {
// JPanel works on netbsd with java 1.4.2, but not windows with java 1.4.2
// public class DrawingCanvas extends JPanel {
// Canvas seems to work on both netbsd with java 1.4.2, and windows with java
// 1.4.2
public class DrawingCanvas extends Canvas {

    private static final long serialVersionUID = 1L;

    private Image imgBuf;
    private Graphics imgGraphic, visibleGraphic;

    /** Maximum width of the canvas */
    public static final int MaxX = 1024; /* 832; /* 1280 */

    /** Maximum height of the canvas */
    public static final int MaxY = 768; /* 624; /* 1024 */

    @Override
    public void addNotify() {
        super.addNotify();
        setBackground(Color.white);
        imgBuf = createImage(DrawingCanvas.MaxX, DrawingCanvas.MaxY); // Can only be done by peer
        imgGraphic = imgBuf.getGraphics();
        imgGraphic.setPaintMode();
        imgGraphic.setColor(Color.black);
        visibleGraphic = getGraphics();
        clear(false);
    }

    /** Clear the canvas area. */
    public void clear() {
        clear(true);
    }

    /**
     * Clear the canvas area. With an argument of false, do not redisplay the canvas yet.
     */
    public void clear(boolean redraw) {
        final Color save = imgGraphic.getColor();
        imgGraphic.setColor(Color.white);
        imgGraphic.fillRect(0, 0, DrawingCanvas.MaxX, DrawingCanvas.MaxY);
        imgGraphic.setColor(save);
        if (redraw)
            display();
    }

    /**
     * Clear the rectangular region with left edge at <I>x</I>, top edge at <I>y</ I> of
     * <I>width</I> and <I>height</I>.<BR>
     * With a fifth argument of <I>false</I>, do not redisplay the canvas yet.
     */
    public void clearRect(int x, int y, int width, int height) {
        clearRect(x, y, width, height, true);
    }

    public void clearRect(int x, int y, int width, int height, boolean redraw) {
        final Color save = imgGraphic.getColor();
        imgGraphic.setColor(Color.white);
        imgGraphic.fillRect(x, y, width, height);
        imgGraphic.setColor(save);
        if (redraw)
            display(x, y, width, height);
    }

    /**
     * Redisplay the canvas area, including all the shapes that have not yet been redisplayed .
     */
    public void display() {
        final Dimension d = getSize();
        display(0, 0, d.width, d.height);
    }

    public void display(int x, int y, int width, int height) {
        final java.awt.Shape clip = visibleGraphic.getClip();
        visibleGraphic.setClip(x, y, width + 1, height + 1);
        visibleGraphic.drawImage(imgBuf, 0, 0, null);
        visibleGraphic.setClip(clip);
        repaint();
    }

    /**
     * Draw an arc of an oval (outline or filled) The left edge of the oval would be at <I>x</I>,
     * top edge at <I>y</I> of <I>width</I> and <I>height</I>.The arc begins at <I>startAngle</I>
     * and extends for <I>arcAngle</I> degrees.<BR>
     * Angles are interpreted such that 0 degrees is at the 3 o'clock position. A positive value
     * indicates a counter-clockwise rotation while a negative value indicates a clockwise rotation. <BR>
     * With a fifth argument of <I>false</I>, do not redisplay the canvas yet.
     */
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        drawArc(x, y, width, height, startAngle, arcAngle, true);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle,
            boolean redraw) {
        imgGraphic.drawArc(x, y, width, height, startAngle, arcAngle);
        if (redraw)
            display(x, y, width, height);
    }

    /**
     * Load the image from <I>name</I> and draw it with left edge at <I>x</I>, top edge at <I>y</I>
     * scaled to <I>width</I> and <I>height</I>.
     */

    public void drawImage(String name, int x, int y, int width, int height) {
        drawImage(name, x, y, width, height, true);
    }

    public void drawImage(String name, int x, int y, int width, int height, boolean redraw) {
        final File fh = new File(name);
        if (fh.canRead()) {
            Image img;
            final MediaTracker media = new MediaTracker(this);
            img = Toolkit.getDefaultToolkit().getImage(name);
            media.addImage(img, 0);
            try {
                media.waitForID(0);
            } catch (InterruptedException e) {
            }
            imgGraphic.drawImage(img, x, y, width, height, getBackground(), this);
        } else {
            // The file either doesn't exist or we don't have read access
            imgGraphic.drawRect(x, y, width, height);
            imgGraphic.drawLine(x, y, width, height);
            imgGraphic.drawLine(width, y, x, height);
        }
        if (redraw)
            display();
    }

    /**
     * Draw a line between (x1, y1) and (x2, y2). <BR>
     * With a fifth argument of <I>false</I>, do not redisplay the canvas yet.
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        drawLine(x1, y1, x2, y2, true);
    }

    public void drawLine(int x1, int y1, int x2, int y2, boolean redraw) {
        imgGraphic.drawLine(x1, y1, x2, y2);
        if (redraw)
            display(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1) + 1,
                    Math.abs(y2 - y1) + 1);
    }

    /**
     * Draw an oval (outline or filled) with left edge at <I>x</I>, top edge at <I >y</I> of
     * <I>width</I> and <I>height</I>.<BR>
     * With a fifth argument of <I>false</I>, do not redisplay the canvas yet.
     */
    public void drawOval(int x, int y, int width, int height) {
        drawOval(x, y, width, height, true);
    }

    public void drawOval(int x, int y, int width, int height, boolean redraw) {
        imgGraphic.drawOval(x, y, width, height);
        if (redraw)
            display(x, y, width, height);
    }

    /**
     * Draw a rectangle (outline or filled) with left edge at <I>x</I>, top edge at <I>y</I>of
     * <I>width</I> and <I>height</I>. <BR>
     * With a fifth argument of <I>false</I>, do not redisplay the canvas yet.
     */
    public void drawRect(int x, int y, int width, int height) {
        drawRect(x, y, width, height, true);
    }

    public void drawRect(int x, int y, int width, int height, boolean redraw) {
        imgGraphic.drawRect(x, y, width, height);
        if (redraw)
            display(x, y, width, height);
    }

    /**
     * Draw a string with its bottom left corner at (x1, y1). <BR>
     * With a fifth argument of <I>false</I>, do not redisplay the canvas yet.
     */
    public void drawString(String s, int x, int y) {
        drawString(s, x, y, true);
    }

    public void drawString(String s, int x, int y, boolean redraw) {
        imgGraphic.drawString(s, x, y);
        if (redraw) {
            final FontMetrics fm = imgGraphic.getFontMetrics();
            display(x, y - fm.getMaxAscent(), fm.stringWidth(s) + fm.getMaxAdvance(),
                    fm.getMaxAscent() + fm.getMaxDescent());
        }
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        fillArc(x, y, width, height, startAngle, arcAngle, true);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle,
            boolean redraw) {
        imgGraphic.fillArc(x, y, width, height, startAngle, arcAngle);
        imgGraphic.drawArc(x, y, width, height, startAngle, arcAngle);
        if (redraw)
            display(x, y, width, height);
    }

    public void fillOval(int x, int y, int width, int height) {
        fillOval(x, y, width, height, true);
    }

    public void fillOval(int x, int y, int width, int height, boolean redraw) {
        imgGraphic.fillOval(x, y, width, height);
        imgGraphic.drawOval(x, y, width, height);
        if (redraw)
            display(x, y, width, height);
    }

    public void fillRect(int x, int y, int width, int height) {
        fillRect(x, y, width, height, true);
    }

    public void fillRect(int x, int y, int width, int height, boolean redraw) {
        imgGraphic.fillRect(x, y, width + 1, height + 1);
        if (redraw)
            display(x, y, width, height);
    }

    /**
     * Get the Graphics object that is the backing store of the image, so that programs can do more
     * complicated operations on the image than are provided by this class. <BR>
     * Standard usage would be to get the graphics object, call methods on it, and then call the
     * display() method on the DrawingCanvas to update the visible imagewith the modifications.
     */
    public Graphics getBackingGraphics() {
        return imgGraphic;
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(DrawingCanvas.MaxX, DrawingCanvas.MaxY);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Math.min(640, DrawingCanvas.MaxX), Math.min(480, DrawingCanvas.MaxY));
    }

    /**
     * Invert the line between (x1, y1) and (x2, y2). <BR>
     * With a fifth argument of <I>false</I>, do not redisplay the canvas yet.
     */
    public void invertLine(int x1, int y1, int x2, int y2) {
        invertLine(x1, y1, x2, y2, true);
    }

    public void invertLine(int x1, int y1, int x2, int y2, boolean redraw) {
        imgGraphic.setXORMode(Color.white);
        imgGraphic.drawLine(x1, y1, x2, y2);
        imgGraphic.setPaintMode();
        if (redraw)
            display(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1) + 1,
                    Math.abs(y2 - y1) + 1);
    }

    /**
     * Invert the rectangular region with left edge at <I>x</I>, top edge at <I>y< /I>of
     * <I>width</I> and <I>height</I>. <BR>
     * With a fifth argument of <I>false</I>, do not redisplay the canvas yet.
     */
    public void invertRect(int x, int y, int width, int height) {
        invertRect(x, y, width, height, true);
    }

    public void invertRect(int x, int y, int width, int height, boolean redraw) {
        imgGraphic.setXORMode(Color.white);
        imgGraphic.drawRect(x, y, width, height);
        imgGraphic.setPaintMode();
        if (redraw)
            display(x, y, width, height);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(imgBuf, 0, 0, null);
    }

    @Override
    public void print(Graphics g) {
        super.print(g);
    }

    /**
     * Set the current foreground color - the color that all subsequent shapes or text
     */
    public void setColor(Color c) {
        if (imgGraphic != null)
            imgGraphic.setColor(c);
        // super.setForeground(c);
    }

    /** Set the current font size */
    public void setFontSize(int size) {
        imgGraphic.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, size));
    }

    /**
     * Set the current foreground color - the color that all subsequent shapes or text
     */
    @Override
    public void setForeground(Color c) {
        if (imgGraphic != null)
            imgGraphic.setColor(c);
        // super.setForeground(c);
    }

    @Override
    public void update(Graphics g) { // Stops component being cleared
        paint(g);
    }
}
