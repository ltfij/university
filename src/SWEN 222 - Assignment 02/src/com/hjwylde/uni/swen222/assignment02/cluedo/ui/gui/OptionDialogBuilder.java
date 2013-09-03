package com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.awt.Component;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.common.base.Optional;

/**
 * A option dialog builder for building dialogs that have images as radio buttons. The radio buttons
 * are done in an unconventional style - the image is selectable and will automatically close the
 * dialog rather than the user having to click a button saying, for example, "ok".
 * 
 * TODO: The builder should be an inner class in the OptionDialog so that <code>getOption()</code>
 * cannot be called until after the dialog is shown.
 * 
 * @author Henry J. Wylde
 * @param <T> the type of option for the dialog.
 * 
 * @since 2/09/2013
 */
public final class OptionDialogBuilder<T> implements ActionListener {
    
    private final Component parent;
    
    private String title = null;
    private String query = null;
    
    private List<T> options;
    private JButton[] buttons;
    
    private int res = JOptionPane.CLOSED_OPTION;
    
    /**
     * Creates a new <code>OptionDialogBuilder</code> with the given component parent for the
     * dialog. This will help for ensuring the parent cannot be interacted with while the dialog is
     * active.
     * 
     * @param parent the dialog parent.
     */
    public OptionDialogBuilder(Component parent) {
        this.parent = checkNotNull(parent, "Parent cannot be null");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int ac;
        try {
            ac = Integer.parseInt(e.getActionCommand());
        } catch (NumberFormatException nfe) {
            System.err.println(nfe);
            return;
        }
        
        res = ac;
        
        Window window = SwingUtilities.getWindowAncestor((Component) e
            .getSource());
        window.dispose();
    }
    
    /**
     * Gets the option selected by the user from the dialog, or absent if it they cancelled.
     * 
     * @return the selected option.
     */
    public Optional<T> getOption() {
        if ((res < 0) || (res >= options.size()))
            return Optional.absent();
        
        return Optional.of(options.get(res));
    }
    
    /**
     * Sets the options in this dialog builder.
     * 
     * @param options the options for the dialog.
     * @return this.
     */
    public OptionDialogBuilder<T> options(List<T> options) {
        this.options = options;
        
        buttons = new JButton[options.size()];
        for (int i = 0; i < options.size(); i++) {
            buttons[i] = new JButton(options.get(i).toString());
            buttons[i].setActionCommand(String.valueOf(i));
            buttons[i].addActionListener(this);
        }
        
        return this;
    }
    
    /**
     * Sets the options and images for the options in this dialog builder.
     * 
     * @param options the options for the dialog.
     * @param images the images for use for the options.
     * @return this.
     */
    public OptionDialogBuilder<T> options(List<T> options, List<Image> images) {
        this.options = options;
        
        buttons = new JButton[options.size()];
        for (int i = 0; i < options.size(); i++) {
            buttons[i] = new JButton(new ImageIcon(images.get(i)));
            buttons[i].setActionCommand(String.valueOf(i));
            buttons[i].addActionListener(this);
        }
        
        return this;
    }
    
    /**
     * Sets the query or message string in the dialog.
     * 
     * @param query the query string.
     * @return this.
     */
    public OptionDialogBuilder<T> query(String query) {
        this.query = query;
        return this;
    }
    
    /**
     * Shows this dialog. None of the required parameters may be null or an exception will be
     * thrown. This method is blocking until the user selects an option.
     */
    public void show() {
        checkState(title != null, "Title cannot be null!");
        checkState(query != null, "Query cannot be null!");
        checkState(!Arrays.asList(options).contains(null),
            "Options cannot contain null!");
        
        JOptionPane.showOptionDialog(parent, query, title,
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
            buttons, buttons[0]);
    }
    
    /**
     * Sets the title for this dialog.
     * 
     * @param title the title string.
     * @return this.
     */
    public OptionDialogBuilder<T> title(String title) {
        this.title = title;
        return this;
    }
}