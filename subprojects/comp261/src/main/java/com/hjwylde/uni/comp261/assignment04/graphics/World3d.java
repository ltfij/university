package com.hjwylde.uni.comp261.assignment04.graphics;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import com.hjwylde.uni.comp261.assignment04.graphics.lights.PointLight3d;
import com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope;
import com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope2d;
import com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope3d;
import com.hjwylde.uni.comp261.assignment04.graphics.polytopes.PolytopeGroup;
import com.hjwylde.uni.comp261.assignment04.parser.ParseException;
import com.hjwylde.uni.comp261.assignment04.parser.Parser;
import com.hjwylde.uni.comp261.assignment04.parser.nodes.Polytope3dNode;

/*
 * Code for Assignment 4, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * Coordinate system used: Right-handed coordinate system. X-axis and y-axis
 * are right and up from screen, with z coming out of screen. Camera is looking
 * down z-axis (into negatives) at the view plane (the screen).
 * 
 * @author Henry J. Wylde
 */
public class World3d implements KeyListener {
    
    public static double ambientLight = 0.8;
    
    public static boolean wireframeMode = false;
    public static boolean hiddenRemovalMode = true;
    public static boolean renderLightsMode = true;
    
    private ViewPlane view;
    private Matrix4d camera;
    
    private PolytopeGroup polyGroups;
    private List<PointLight3d> lights;
    
    /*
     * For registering key events with rotations / zooms.
     */
    private boolean zoomIn;
    private boolean zoomOut;
    private boolean turnLeft;
    private boolean turnRight;
    
    /**
     * Creates a new <code>World3d</code> and initializes the camera to be 500 pixels away from
     * <code>(0, 0, 0)</code>.
     */
    public World3d() {
        view = new ViewPlane();
        camera = new Matrix4d();
        camera.setIdentity();
        camera.setTranslation(new Vector3d(0.0, 0.0, 500.0));
        
        polyGroups = new PolytopeGroup();
        lights = new ArrayList<>();
    }
    
    /*
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            zoomIn = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            zoomOut = true;
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            turnLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            turnRight = true;
        
        e.consume();
    }
    
    /*
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            zoomIn = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            zoomOut = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            turnLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            turnRight = false;
        
        e.consume();
    }
    
    /*
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }
    
    /**
     * Loads a <code>Polytope</code> from the given <code>File</code> and adds it to
     * <code>polyGroups</code>. Also adds the light loaded by the object file to the list of current
     * lights.
     * 
     * @param f the File.
     * @throws ParseException if the grammar is wrong in the File.
     * @throws IOException if there is an I/O error.
     */
    public void loadPolytope(File f) throws ParseException, IOException {
        Polytope3dNode polyNode = Parser.parse(f); // Try to parse the given file to a polytope.
        Polytope3d poly = polyNode.createInstance(); // Create a new Polytope3d from the parsed
                                                     // file.
        
        poly.sub(findCenter(poly)); // Center the polytope for smoother rotations.
        
        polyGroups.addPolytope(poly); // Add the polytope to the list of polytopes.
        lights.add(polyNode.getLight().createInstance()); // Add a new light source to the list of
                                                          // lights.
    }
    
    /**
     * Renders this <code>World3d</code> to the graphics object passed. This method will create
     * scratch clones of all the <code>Polytope</code>s before transforming them, clipping, backface
     * removal and projection onto the <code>ViewPlane</code> so that originals are not modified.
     * 
     * @param g the Graphics2D to draw to.
     */
    public void render(Graphics2D g) {
        if ((view.getWidth() == 0) || (view.getHeight() == 0))
            return;
        
        ZBuffer z = new ZBuffer((int) view.getWidth(), (int) view.getHeight());
        
        PolytopeGroup scratch;
        scratch = (PolytopeGroup) polyGroups.clone(); // Create a clone of all polytopes so that
                                                      // transforming operations won't be applied
                                                      // permanently.
        List<PointLight3d> scratchLights = new ArrayList<>();
        for (PointLight3d light : lights) {
            scratchLights.add((PointLight3d) light.clone()); // Scratch lights are needed too!
            polyGroups.getTransform().transform(
                scratchLights.get(scratchLights.size() - 1)); // Transform the lights too.
        }
        
        scratch.transform(); // Apply polytope self transformations.
        scratch.sub(camera); // Subtract the camera transform / position.
        if (!scratch.clip(-1.0)) // Clip the polytopes to the z = -1.0 plane (so we don't display
                                 // any
                                 // that are behind the camera).
            return; // If clipping it resulted in no polytopes being shown... return.
            
        scratch.project(view); // Perspective projection to the ViewPlane.
        
        scratch.render(z, new ScanConverter(view, scratchLights));
        g.drawImage(z.getImage(), 0, 0, null);
    }
    
    /**
     * Set the height of the <code>ViewPlane</code>.
     * 
     * @param height the height of the ViewPlane.
     */
    public void setHeight(int height) {
        view.setHeight(height);
    }
    
    /**
     * Set the viewing angle of the <code>ViewPlane</code>
     * 
     * @param angle the angle in radians.
     */
    public void setViewAngle(double angle) {
        view.setAngle(angle);
    }
    
    /**
     * Set the width of the <code>ViewPlane</code>
     * 
     * @param width the width.
     */
    public void setWidth(int width) {
        view.setWidth(width);
    }
    
    /**
     * Update this world by applying any rotations / zooms based on key events.
     * 
     * @param elapsedTime the time since update was last called.
     */
    public void update(long elapsedTime) {
        Matrix4d translation = new Matrix4d();
        translation.setIdentity();
        
        if (zoomIn && !zoomOut)
            translation.m23 -= 1.0 * elapsedTime; // Zoom in!
        if (zoomOut && !zoomIn)
            translation.m23 += 1.0 * elapsedTime; // Zoom out!
            
        translation.mul(camera); // Must apply in correct order.
        camera = new Matrix4d(translation);
        
        Matrix4d rotation = new Matrix4d();
        rotation.setIdentity();
        
        if (turnLeft && !turnRight) {
            rotation.rotY(elapsedTime * 0.002); // Turn left!
            
            polyGroups.getTransform().mul(rotation);
        } else if (turnRight && !turnLeft) {
            rotation.rotY(elapsedTime * -0.002); // Turn right!
            
            polyGroups.getTransform().mul(rotation);
        }
    }
    
    /**
     * Finds the center of the given <code>Polytope</code> and returns it as a vector. This can then
     * be used with <code>Polytope.sub(center);</code> to center a <code>Polytope</code>
     * 
     * @param poly the Polytope to center.
     * @return the center vector.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.graphics.polytopes.Polytope
     */
    private Vector3d findCenter(Polytope poly) {
        int vertexCount = 0;
        Vector3d center = new Vector3d();
        
        if (poly instanceof Polytope2d)
            for (Vector3d v : ((Polytope2d) poly).getVertices()) {
                center.add(v);
                vertexCount++;
            }
        else if (poly instanceof Polytope3d)
            for (Polytope2d f : (Polytope3d) poly) {
                center.add(findCenter(f));
                vertexCount++;
            }
        else
            for (Polytope p : (PolytopeGroup) poly) {
                center.add(findCenter(p));
                vertexCount++;
            }
        
        center.scale(1.0 / vertexCount);
        return center;
    }
}