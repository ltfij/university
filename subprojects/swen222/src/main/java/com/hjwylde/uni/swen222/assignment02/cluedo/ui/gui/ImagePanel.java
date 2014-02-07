package com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.Collection;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * Panel that will display a sequential list of images horizontally. Each image has a hover string
 * that is used as a tool tip. TODO: The panel is hardcoded with values to work with the cards and
 * current GUI - this should be changed for a fluid layout.
 * 
 * @author Henry J. Wylde
 * 
 * @since 2/09/2013
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new empty <code>ImagePanel</code>.
     */
    public ImagePanel() {
        this(new Image[0], new String[0]);
    }

    /**
     * Creates a new <code>ImagePanel</code> with the given images and hover strings.
     * 
     * @param images the images.
     * @param hovers the hover strings for the tool tips.
     */
    public ImagePanel(Collection<Image> images, Collection<String> hovers) {
        this(images.toArray(new Image[0]), hovers.toArray(new String[0]));
    }

    /**
     * Creates a new <code>ImagePanel</code> with the given images and hover strings.
     * 
     * @param images the images.
     * @param hovers the hover strings for the tool tips.
     */
    public ImagePanel(Image[] images, String[] hovers) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBackground(Color.WHITE);

        setPreferredSize(new Dimension(365, 75));
        setMinimumSize(new Dimension(365, 75));

        setImages(images, hovers);
    }

    /**
     * Clears the images in this image panel.
     */
    public void clearImages() {
        setImages(new Image[0], new String[0]);
    }

    /**
     * Sets the images and hover strings in this image panel. This method will clear any existing
     * images.
     * 
     * @param images the new images.
     * @param hovers the hover strings.
     */
    public void setImages(Image[] images, String[] hovers) {
        checkArgument(images.length == hovers.length, "Can't give two arrays of different sizes!");

        removeAll();
        for (int i = 0; i < images.length; i++) {
            JLabel label =
                    new JLabel(new ImageIcon(images[i].getScaledInstance(47, 70, Image.SCALE_FAST)));
            label.setToolTipText(hovers[i]);
            label.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

            add(label);
            // add(Box.createHorizontalGlue());
            add(Box.createRigidArea(new Dimension(10, 0)));
        }

        validate();
    }

    /**
     * Sets the images and hover strings in this image panel. This method will clear any existing
     * images.
     * 
     * @param images the new images.
     * @param hovers the hover strings.
     */
    public void setImages(List<Image> images, List<String> hovers) {
        setImages(images.toArray(new Image[0]), hovers.toArray(new String[0]));
    }
}
