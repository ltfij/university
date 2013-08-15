package com.hjwylde.uni.comp103.assignment6.centipede;

/*
 * Code for Assignment 6, COMP 103
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 * 
 * Reference for what a Quadtree is:
 * http://www.cs.ubc.ca/~pcarbo/cs251/welcome.html. Example
 * Quadtree applet:
 * http://donar.umiacs.umd.edu/quadtree/regions/regionquad.html.
 * 
 * This code I wrote for a quadtree is very basic, and omits many basic features
 * you would normally
 * expect, such as remove(int index) or remove(Rectangle bounds). I only added
 * in the main
 * functionality that it was intended for: adding in FoodSources and searching
 * for them, but as a
 * FoodSource never moves or is never removed, I did not create these methods.
 * (These methods I will
 * add in (when I have more spare time) and when I want to improve it and use it
 * for more
 * functions.)
 * 
 * The rough idea of the structure of this Quadtree:
 * 
 * Each QuadtreeNode has 4 quadrants, each with a specific bounds that represent
 * it's area. These
 * quadrants can be split down into further quadrants a certain number of times
 * (maxLevels). A node
 * has 1 index, which is an integer representing an index for some object in
 * some array (in this
 * assignment, the index is the FoodSource in the FoodSource array). An index of
 * a node is within an
 * entire node's bounds, so if the index only covers a small bounding area then
 * it is best to make
 * the Quadtree divide up into many small quadrants (as far down as 1x1 is
 * accurate, 2x2 can work
 * for roughness but is not as accurate).
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

public class Quadtree {
    
    // Constants
    private static final int TOP_RIGHT = 0; // First quadrant
    
    private static final int TOP_LEFT = 1; // Second quadrant
    
    private static final int BOTTOM_LEFT = 2; // Third quadrant
    
    private static final int BOTTOM_RIGHT = 3; // Forth quadrant
    
    public static final int NODE_TRANSPARENT = -1; // A node is transparent iff it
                                                   // has subnodes
    
    // Structure
    private QuadtreeNode root; // Root node
    
    /*
     * Constructors
     */
    
    /**
     * Create a new <code>Quadtree</code> with the given root node.
     * 
     * @param root
     *            create a <code>Quadtree</code> with the given <code>root</code>
     */
    public Quadtree(QuadtreeNode root) {
        this.root = root;
    }
    
    /**
     * Create a new Quadtree in the given <code>bounds</code> with specified <code>maxLevels</code>.
     * 
     * @param bounds
     *            the bounds.
     * @param maxLevels
     *            max number of division levels.
     */
    public Quadtree(Rectangle bounds, int maxLevels) {
        root = new QuadtreeNode(null, bounds, maxLevels);
    }
    
    /*
     * Getters
     */
    
    /**
     * (For Testing)
     * 
     * Draws this <code>Quadtree</code> on a new JFrame.
     */
    public void dumpTree() {
        @SuppressWarnings("serial")
        JFrame frame = new JFrame() {
            
            @Override
            public void paint(Graphics g) {
                root.dumpTree(g);
            }
        };
        
        frame.setSize(root.getBounds().width, root.getBounds().height);
        frame.setVisible(true);
        frame.validate();
    }
    
    /**
     * (For Testing)
     * 
     * Draws this <code>Quadtree</code> on the given graphics object.
     */
    public void dumpTree(Graphics g) {
        root.dumpTree(g);
    }
    
    /*
     * Setters
     */
    
    /**
     * Find all the index's that are contained within the given <code>bounds</code>.
     * 
     * @param bounds
     *            the area to search.
     * @return a Set of unique indexes.
     */
    public Set<Integer> getAllWithin(Rectangle bounds) {
        return root.getAllWithin(bounds);
    }
    
    /*
     * Opperations
     */
    
    /**
     * Returns this <code>Quadtree</code>'s area / bounds.
     * 
     * @return the bounds.
     */
    public Rectangle getBounds() {
        return root.getBounds();
    }
    
    /**
     * Returns the max levels of this <code>Quadtree</code>.
     * 
     * @return the max levels
     */
    public int getMaxLevels() {
        return root.getMaxLevels();
    }
    
    /*
     * Testing
     */
    
    /**
     * Inserts the given <code>index</code> in the given <code>bounds</code>. If
     * an index is already
     * within the bounds given, then that index will be overwritten with the new
     * index.
     * 
     * @param index
     *            the index.
     * @param bounds
     *            the bounds of the index.
     */
    public void insert(int index, Rectangle bounds) {
        root.insert(index, bounds);
    }
    
    /**
     * Set this <code>Quadtree</code>'s root to the given node
     * 
     * @param root
     *            the new root node
     */
    public void setRoot(QuadtreeNode root) {
        this.root = root;
    }
    
    public class QuadtreeNode {
        
        // Structure
        private final QuadtreeNode parent;
        private QuadtreeNode[] subNodes; // The four quadrants
        
        // Fields
        private int index;
        private final Rectangle bounds;
        private int maxLevels;
        
        /*
         * Constructors
         */
        
        /**
         * Create a new <code>QuadtreeNode</code> with the specified parent node,
         * bounding area and max
         * division levels.
         * 
         * @param parent
         *            the nodes parent.
         * @param bounds
         *            the nodes bounds.
         * @param maxLevels
         *            the nodes max division levels.
         */
        public QuadtreeNode(QuadtreeNode parent, Rectangle bounds, int maxLevels) {
            this.parent = parent;
            
            index = Quadtree.NODE_TRANSPARENT;
            this.bounds = bounds;
            this.maxLevels = maxLevels;
        }
        
        /*
         * Getters
         */
        
        /**
         * (For Testing)
         * 
         * Draws this <code>QuadtreeNode</code> on the given graphics object.
         */
        public void dumpTree(Graphics g) {
            if (index != Quadtree.NODE_TRANSPARENT) {
                g.setColor(Color.RED);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            } else if (subNodes != null) {
                g.setColor(Color.BLACK);
                g.drawLine(bounds.x + (bounds.width / 2), bounds.y, bounds.x
                    + (bounds.width / 2), bounds.y + bounds.height);
                g.drawLine(bounds.x, bounds.y + (bounds.height / 2), bounds.x
                    + bounds.width, bounds.y + (bounds.height / 2));
                
                subNodes[Quadtree.TOP_RIGHT].dumpTree(g);
                subNodes[Quadtree.TOP_LEFT].dumpTree(g);
                subNodes[Quadtree.BOTTOM_LEFT].dumpTree(g);
                subNodes[Quadtree.BOTTOM_RIGHT].dumpTree(g);
            }
        }
        
        /**
         * Find all the index's that are contained within the given <code>bounds</code>.
         * 
         * @param bounds
         *            the area to search.
         * @return a Set of unique indexes.
         */
        public Set<Integer> getAllWithin(Rectangle bounds) {
            Set<Integer> indexes = new HashSet<>(); // Unique set.
            
            if (!isTransparent()) // If this nodes index covers all subnodes / is
                                  // set...
                indexes.add(index); // ...then add the index to the set.
            else if (subNodes != null) { // If this QuadtreeNode has further
                                         // subnodes...
                // ...then get all indexes within subnodes that are within the given
                // bounds.
                if (bounds.intersects(getQuadrantBounds(Quadtree.TOP_RIGHT)))
                    indexes
                        .addAll(subNodes[Quadtree.TOP_RIGHT].getAllWithin(bounds
                            .intersection(getQuadrantBounds(Quadtree.TOP_RIGHT))));
                if (bounds.intersects(getQuadrantBounds(Quadtree.TOP_LEFT)))
                    indexes
                        .addAll(subNodes[Quadtree.TOP_LEFT].getAllWithin(bounds
                            .intersection(getQuadrantBounds(Quadtree.TOP_LEFT))));
                if (bounds.intersects(getQuadrantBounds(Quadtree.BOTTOM_LEFT)))
                    indexes
                        .addAll(subNodes[Quadtree.BOTTOM_LEFT].getAllWithin(bounds
                            .intersection(getQuadrantBounds(Quadtree.BOTTOM_LEFT))));
                if (bounds.intersects(getQuadrantBounds(Quadtree.BOTTOM_RIGHT)))
                    indexes
                        .addAll(subNodes[Quadtree.BOTTOM_RIGHT].getAllWithin(bounds
                            .intersection(getQuadrantBounds(Quadtree.BOTTOM_RIGHT))));
            }
            
            return indexes;
        }
        
        /**
         * Gets this nodes bounding area.
         * 
         * @return the area.
         */
        public Rectangle getBounds() {
            return bounds;
        }
        
        /**
         * Gets this nodes <code>index</code>.
         * 
         * @return this nodes index.
         */
        public int getIndex() {
            return index;
        }
        
        /**
         * Gets the max levels this node can be divided into (depth of the Quadtree
         * from this node).
         * 
         * @return the max levels.
         */
        public int getMaxLevels() {
            return maxLevels;
        }
        
        /**
         * Gets this nodes <code>parent</code>.
         * 
         * @return the parent node.
         */
        public QuadtreeNode getParent() {
            return parent;
        }
        
        /*
         * Setters
         */
        
        /**
         * Returns a new <code>Quadtree</code> with this node as the root.
         * 
         * @return the sub Quadtree.
         */
        public Quadtree getSubQuadtree() {
            return new Quadtree(this);
        }
        
        /*
         * Operations
         */
        
        /**
         * Inserts the given <code>index</code> in the given <code>bounds</code>. If
         * an index is already
         * within the bounds given, then that index will be overwritten with the new
         * index.
         * 
         * @param index
         *            the index.
         * @param bounds
         *            the bounds of the index.
         */
        public void insert(int index, Rectangle bounds) {
            if (bounds.equals(this.bounds))
                this.index = index;
            else if (maxLevels > 0) {
                if ((getQuadrantBounds(Quadtree.TOP_RIGHT).width <= 0)
                    || (getQuadrantBounds(Quadtree.TOP_RIGHT).height <= 0)) {
                    // If further subnodes will be invalid...
                    maxLevels = 0; // ...then make this node the lowest level.
                    
                    this.index = index;
                    return; // Don't create any subnodes.
                }
                
                // Initiate subnodes.
                if (subNodes == null) {
                    subNodes = new QuadtreeNode[4];
                    subNodes[Quadtree.TOP_RIGHT] = new QuadtreeNode(this,
                        getQuadrantBounds(Quadtree.TOP_RIGHT), maxLevels - 1);
                    subNodes[Quadtree.TOP_LEFT] = new QuadtreeNode(this,
                        getQuadrantBounds(Quadtree.TOP_LEFT), maxLevels - 1);
                    subNodes[Quadtree.BOTTOM_LEFT] = new QuadtreeNode(this,
                        getQuadrantBounds(Quadtree.BOTTOM_LEFT), maxLevels - 1);
                    subNodes[Quadtree.BOTTOM_RIGHT] = new QuadtreeNode(this,
                        getQuadrantBounds(Quadtree.BOTTOM_RIGHT), maxLevels - 1);
                }
                
                // Insert the index into applicable quadrants.
                if (bounds.intersects(getQuadrantBounds(Quadtree.TOP_RIGHT)))
                    subNodes[Quadtree.TOP_RIGHT].insert(index, bounds
                        .intersection(getQuadrantBounds(Quadtree.TOP_RIGHT)));
                if (bounds.intersects(getQuadrantBounds(Quadtree.TOP_LEFT)))
                    subNodes[Quadtree.TOP_LEFT].insert(index, bounds
                        .intersection(getQuadrantBounds(Quadtree.TOP_LEFT)));
                if (bounds.intersects(getQuadrantBounds(Quadtree.BOTTOM_LEFT)))
                    subNodes[Quadtree.BOTTOM_LEFT].insert(index, bounds
                        .intersection(getQuadrantBounds(Quadtree.BOTTOM_LEFT)));
                if (bounds.intersects(getQuadrantBounds(Quadtree.BOTTOM_RIGHT)))
                    subNodes[Quadtree.BOTTOM_RIGHT]
                        .insert(
                            index,
                            bounds
                                .intersection(getQuadrantBounds(Quadtree.BOTTOM_RIGHT)));
            } else
                // We are at the bottom of the Quadtree, set the index.
                this.index = index;
        }
        
        /**
         * Returns true if this node is transparent. Note: At this moment I have not
         * added in
         * functionality for parent nodes transparency to be altered if all subnodes
         * have the same index
         * (to speed up searching).
         * 
         * @return true if transparent.
         */
        public boolean isTransparent() {
            return index == Quadtree.NODE_TRANSPARENT;
        }
        
        /**
         * Sets the index of this node to <code>index</code>.
         * 
         * @param index
         *            the index to set to.
         */
        public void setIndex(int index) {
            this.index = index;
        }
        
        /*
         * Testing
         */
        
        /**
         * Get's a bounds representation of the quadrant specified by <code>area</code>
         * 
         * @param area
         *            the quadrant area (TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT,
         *            BOTTOM_RIGHT).
         * @return the bounds representing this quadrant area.
         */
        private Rectangle getQuadrantBounds(int area) {
            Rectangle quadBounds;
            
            switch (area) {
            case (Quadtree.TOP_RIGHT):
                quadBounds = new Rectangle(bounds.x + (bounds.width / 2),
                    bounds.y, bounds.width / 2, bounds.height / 2);
                break;
            case (Quadtree.TOP_LEFT):
                quadBounds = new Rectangle(bounds.x, bounds.y,
                    bounds.width / 2, bounds.height / 2);
                break;
            case (Quadtree.BOTTOM_LEFT):
                quadBounds = new Rectangle(bounds.x, bounds.y
                    + (bounds.height / 2), bounds.width / 2, bounds.height / 2);
                break;
            case (Quadtree.BOTTOM_RIGHT):
                quadBounds = new Rectangle(bounds.x + (bounds.width / 2),
                    bounds.y + (bounds.height / 2), bounds.width / 2,
                    bounds.height / 2);
                break;
            default:
                throw new IllegalArgumentException(area
                    + " is not a valid quadrant area.");
            }
            
            return quadBounds;
        }
    }
}
