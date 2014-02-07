package com.hjwylde.uni.comp102.util;

import javax.swing.JFileChooser;

/**
 * Provides static methods to get a file name using the standard swing file dialog box. Does not
 * throw errors, but returns the null string if user cancels.
 */
public class FileChooser {

    private static JFileChooser chooser = new JFileChooser(".");

    /**
     * Constructor is private to prevent user from constructing an instance of this class
     */
    private FileChooser() {}

    /**
     * Opens a file chooser dialog box to allow the user to select an existing file to open. Returns
     * a string that is the name of the file, or null if the user cancelled
     */
    public static String open() {
        final int returnVal = FileChooser.chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            return (FileChooser.chooser.getSelectedFile().getPath());

        return null;
    }

    /**
     * Opens a file chooser dialog box with a specified title. Allows the user to select an existing
     * file to open. Returns a string that is the name of the file, or null if the user cancelled
     */
    public static String open(String title) {
        FileChooser.chooser.setDialogTitle(title);
        final String ans = FileChooser.open();
        FileChooser.chooser.setDialogTitle("");
        return ans;
    }

    /**
     * Opens a file chooser dialog box to allow the user to select a file (possibly new) to save to.
     * Returns a string that is the name of the file, or null if the user cancelled
     */
    public static String save() {
        final int returnVal = FileChooser.chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            return (FileChooser.chooser.getSelectedFile().getPath());

        return null;
    }

    /**
     * Opens a file chooser dialog box with a specified title. Allows the user to select a file
     * (possibly new) to save to. Returns a string that is the name of the file, or null if the user
     * cancelled
     */
    public static String save(String title) {
        FileChooser.chooser.setDialogTitle(title);
        final String ans = FileChooser.save();
        FileChooser.chooser.setDialogTitle("");
        return ans;
    }

}
