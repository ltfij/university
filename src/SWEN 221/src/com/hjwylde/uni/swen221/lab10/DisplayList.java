package com.hjwylde.uni.swen221.lab10;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/*
 * Code for Laboratory 10, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The threaded list display provides a UI which shows the current position of a worker thread
 * during the sorting process.
 * 
 * @author djp
 */
public class DisplayList<T> extends ArrayList<T> {
    
    private static final long serialVersionUID = 1L;
    
    private final JFrame frame;
    private final Canvas canvas;
    private final JScrollPane scroller;
    
    /**
     * The lastTouched set maintains a list of locations which have been recently touched by a
     * thread.
     * The idea is that everytime a thread sets an item in the list, we update it's last touched
     * information. In particular, the touchedMap maps a thread to its last touched index, whilst
     * the
     * reverse touched map maps an index to the thread that last touched it.
     */
    private final HashSet<Integer> lastTouched = new HashSet<>();
    private final HashMap<Thread, Integer> touchedMap = new HashMap<>();
    private final HashMap<Integer, Thread> reverseTouchedMap = new HashMap<>();
    
    private static final HashMap<Thread, Integer> colorMap = new HashMap<>();
    private static int coloridx = 0;
    private static Color[] colors = {
        Color.RED, Color.BLUE, Color.GREEN, Color.WHITE, Color.GRAY,
        Color.CYAN,
    };
    
    public DisplayList(Collection<T> items) {
        super(items);
        frame = new JFrame("Sorting Algorithm Animation") {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public void repaint() {
                canvas.repaint();
            }
        };
        frame.setLayout(new BorderLayout());
        canvas = new DisplayCanvas<>(this);
        scroller = new JScrollPane(canvas,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(scroller, BorderLayout.CENTER);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        
        // Display window
        frame.setVisible(true);
        
        new ClockThread(50, frame).start();
    }
    
    /**
     * Get an item from the items list.
     * 
     * @param index index of item to read
     * @return the item.
     */
    @Override
    public synchronized T get(int index) {
        return super.get(index);
    }
    
    /**
     * Set an item in the items list.
     * 
     * @param idx index of item to set
     * @param value new value for item
     */
    @Override
    public synchronized T set(int idx, T value) {
        Thread thread = Thread.currentThread();
        DisplayList.register(Thread.currentThread());
        
        Integer lt = touchedMap.get(thread);
        if (lt != null) {
            lastTouched.remove(lt);
            reverseTouchedMap.remove(lt);
        }
        
        touchedMap.put(thread, idx);
        reverseTouchedMap.put(idx, thread);
        lastTouched.add(idx);
        
        return super.set(idx, value);
    }
    
    private synchronized T internalGet(int index) {
        return super.get(index);
    }
    
    public static void register(Thread t) {
        if (!DisplayList.colorMap.containsKey(t))
            DisplayList.colorMap.put(t, DisplayList.coloridx++);
    }
    
    /**
     * The display canvas is what actually draws the list being sorted.
     * 
     * @author djp
     */
    public static class DisplayCanvas<T> extends Canvas {
        
        // Note, I could have used a non-static inner class here. However, it's
        // helpful to make the parent pointer explicit, in case you need to
        // synchronize on it.
        
        private static final long serialVersionUID = 1L;
        
        private DisplayList<T> parent;
        private Font font;
        private int boxWidth = 30;
        private int boxHeight = 30;
        
        // Following is helper for Display Canvas
        private static final String[] preferredFonts = {
            "Arial", "Times New Roman"
        };
        
        public DisplayCanvas(DisplayList<T> parent) {
            this.parent = parent;
            // Choose the font to display with
            GraphicsEnvironment env = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
            HashSet<String> availableNames = new HashSet<>();
            
            for (String name : env.getAvailableFontFamilyNames())
                availableNames.add(name);
            
            for (String pf : DisplayCanvas.preferredFonts)
                if (availableNames.contains(pf)) {
                    font = new Font(pf, Font.PLAIN, 12);
                    break;
                }
            
            // Calculate size of the canvas
            int pwidth = 1024;
            int padding = 15;
            int spacing = 15;
            int ncols = (pwidth - (2 * padding)) / boxWidth;
            int nrows = parent.size() / ncols;
            if ((nrows * ncols) < parent.size())
                nrows++;
            int height = (nrows * (spacing + boxHeight)) + (padding * 2);
            setPreferredSize(new Dimension(pwidth, height));
            setBackground(Color.BLACK);
        }
        
        @Override
        public void paint(Graphics g) {
            Image img = createImage();
            
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(img, 0, 0, null);
        }
        
        // following implements primitive double buffering
        @Override
        public void update(Graphics g) {
            Graphics offgc;
            Image offscreen = null;
            Dimension d = this.getSize();
            
            // create the offscreen buffer and associated Graphics
            offscreen = createImage(d.width, d.height);
            offgc = offscreen.getGraphics();
            paint(offgc);
            // transfer offscreen to window
            g.drawImage(offscreen, 0, 0, this);
        }
        
        private Image createImage() {
            Image image;
            
            Graphics g;
            int padding = 15;
            int spacing = 15;
            int ncols = (getWidth() - (2 * padding)) / boxWidth;
            int nrows = parent.size() / ncols;
            if ((nrows * ncols) < parent.size())
                nrows++;
            int width = (ncols * boxWidth) + (padding * 2);
            int height = (nrows * (spacing + boxHeight)) + (padding * 2);
            
            // create the offscreen buffer and associated Graphics
            image = createImage(width, height);
            g = image.getGraphics();
            
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
            
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics();
            int ascent = metrics.getAscent();
            int cheight = metrics.getHeight();
            
            int idx = 0;
            outer:
            for (int y = 0; y != nrows; ++y) {
                int ry = (y * (boxHeight + spacing)) + padding + spacing;
                for (int x = 0; x != ncols; ++x) {
                    int rx = (x * boxWidth) + padding;
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(rx, ry, boxWidth, boxHeight);
                    g.setColor(Color.YELLOW);
                    g.drawRect(rx, ry, boxWidth, boxHeight);
                    g.drawRect(rx + 1, ry + 1, boxWidth - 2, boxHeight - 2);
                    
                    // now draw the actual item
                    char[] chars = parent.internalGet(idx).toString()
                        .toCharArray();
                    int cwidth = metrics.charsWidth(chars, 0, chars.length);
                    int cx = (boxWidth - cwidth) / 2;
                    int cy = (boxHeight - cheight) / 2;
                    g.drawChars(chars, 0, chars.length, rx + cx, ry + cy
                        + ascent);
                    
                    // finally, draw a thread marked if applicable
                    if (parent.lastTouched.contains(idx)) {
                        Thread t = parent.reverseTouchedMap.get(idx);
                        int cidx = DisplayList.colorMap.get(t);
                        g.setColor(DisplayList.colors[cidx]);
                        g.fillRect(rx, (8 + ry) - spacing, boxWidth, 7);
                    }
                    
                    if (++idx >= parent.size())
                        break outer;
                }
            }
            
            g.dispose();
            
            return image;
        }
        
    }
}
