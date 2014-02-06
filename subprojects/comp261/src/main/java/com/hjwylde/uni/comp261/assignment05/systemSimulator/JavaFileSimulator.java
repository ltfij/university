package com.hjwylde.uni.comp261.assignment05.systemSimulator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.hjwylde.uni.comp261.assignment05.system.Disk;
import com.hjwylde.uni.comp261.assignment05.system.files.BasicFile;
import com.hjwylde.uni.comp261.assignment05.system.files.HeapFile;
import com.hjwylde.uni.comp261.assignment05.system.files.SequentialFile;

/*
 * Code for Assignment 5, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class JavaFileSimulator {
    
    public static void main(String[] args) {
        // Create the disk.
        final Disk disk = new Disk();
        
        // Create the files.
        final BasicFile basicFile = new BasicFile(disk);
        final SequentialFile sequentialFile = new SequentialFile(disk);
        final HeapFile heapFile = new HeapFile(disk);
        
        // Create main window.
        final JFrame frame = new JFrame("Java File  Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create text area to display (not edit!) the file's contents.
        final JTextArea textArea = new JTextArea(20, 80);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 5));
        
        final JButton bBasicOpen = new JButton("Basic Open");
        final JButton bBasicWrite = new JButton("Basic Write");
        final JButton bBasicRead = new JButton("Basic Read");
        final JButton bBasicSeek = new JButton("Basic Seek");
        final JButton bBasicClose = new JButton("Basic Close");
        
        final JButton bSequentialOpen = new JButton("Sequential Open");
        final JButton bSequentialWrite = new JButton("Sequential Write");
        final JButton bSequentialFind = new JButton("Sequential Find");
        final JButton bSequentialDelete = new JButton("Sequential Delete");
        final JButton bSequentialClose = new JButton("Sequential Close");
        
        final JButton bHeapOpen = new JButton("Heap Open");
        final JButton bHeapWrite = new JButton("Heap Write");
        final JButton bHeapFind = new JButton("Heap Find");
        final JButton bHeapSort = new JButton("Heap Sort");
        final JButton bHeapClose = new JButton("Heap Close");
        
        // Create basic file control buttons.
        bBasicOpen.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane
                    .showInputDialog("Please input a file name:");
                boolean createIfDoesNotExist = JOptionPane.showConfirmDialog(
                    null, "Create file if it does not exist?", "Create File?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                if (basicFile.open(fileName, createIfDoesNotExist)) {
                    // File opened successfully.
                    bBasicWrite.setEnabled(true);
                    bBasicRead.setEnabled(true);
                    bBasicSeek.setEnabled(true);
                    bBasicClose.setEnabled(true);
                    
                    bBasicOpen.setEnabled(false);
                    bBasicOpen.setText("Opened: " + fileName);
                    
                    bSequentialOpen.setEnabled(false);
                    bHeapOpen.setEnabled(false);
                } else
                    JOptionPane.showMessageDialog(null,
                        "Cannot open the basic file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                
                disk.printDisk(textArea);
            }
        });
        bBasicOpen.setEnabled(false);
        buttonPanel.add(bBasicOpen);
        
        bBasicWrite.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = JOptionPane
                    .showInputDialog("Enter characters to write:");
                if (data.indexOf('~') != -1) {
                    JOptionPane.showMessageDialog(null,
                        "Input CANNOT contain end of file character: " + +'~',
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!basicFile.write(data.toCharArray()))
                    JOptionPane.showMessageDialog(null,
                        "Error writing to the basic file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                disk.printDisk(textArea);
            }
        });
        bBasicWrite.setEnabled(false);
        buttonPanel.add(bBasicWrite);
        
        bBasicRead.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                int numChars = -1;
                boolean isNumber = false;
                do {
                    String readNumChars = JOptionPane
                        .showInputDialog("Enter number of characters to read:");
                    
                    try {
                        numChars = Integer.parseInt(readNumChars);
                        if (numChars >= 0)
                            isNumber = true;
                    } catch (NumberFormatException ex) {
                        isNumber = false;
                    }
                } while (!isNumber);
                
                char[] data = new char[numChars];
                if (basicFile.read(data))
                    // File read from successfully.
                    JOptionPane.showMessageDialog(null, "Read: "
                        + new String(data), "Read As Follows",
                        JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null,
                        "Error reading from the basic file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bBasicRead.setEnabled(false);
        buttonPanel.add(bBasicRead);
        
        bBasicSeek.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                int seekForwardFromCurrent = 0;
                boolean isNumber = false;
                do {
                    String seek = JOptionPane
                        .showInputDialog("Enter how far (plus or minus) from current "
                            + "location to seek to (note that cannot go before "
                            + "start of file, but can go past the end of file):");
                    
                    try {
                        seekForwardFromCurrent = Integer.parseInt(seek);
                        isNumber = true;
                    } catch (NumberFormatException ex) {
                        isNumber = false;
                    }
                } while (!isNumber);
                
                if (basicFile.seek(seekForwardFromCurrent)) {
                    // File seeked successfully.
                } else
                    JOptionPane.showMessageDialog(null,
                        "Error seeking in the basic file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bBasicSeek.setEnabled(false);
        buttonPanel.add(bBasicSeek);
        
        bBasicClose.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (basicFile.close()) {
                    // File closed successfully.
                    bBasicWrite.setEnabled(false);
                    bBasicRead.setEnabled(false);
                    bBasicSeek.setEnabled(false);
                    bBasicClose.setEnabled(false);
                    
                    bBasicOpen.setEnabled(true);
                    bBasicOpen.setText("Basic Open");
                    
                    bSequentialOpen.setEnabled(true);
                    bHeapOpen.setEnabled(true);
                } else
                    JOptionPane.showMessageDialog(null,
                        "Error closing the file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bBasicClose.setEnabled(false);
        buttonPanel.add(bBasicClose);
        
        // Create sequential file control buttons.
        bSequentialOpen.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane
                    .showInputDialog("Please input a file name:");
                boolean createIfDoesNotExist = JOptionPane.showConfirmDialog(
                    null, "Create file if it does not exist?", "Create File?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                if (sequentialFile.open(fileName, createIfDoesNotExist)) {
                    // File opened successfully.
                    bSequentialWrite.setEnabled(true);
                    bSequentialFind.setEnabled(true);
                    bSequentialDelete.setEnabled(true);
                    bSequentialClose.setEnabled(true);
                    
                    bSequentialOpen.setEnabled(false);
                    bSequentialOpen.setText("Opened: " + fileName);
                    
                    bBasicOpen.setEnabled(false);
                    bHeapOpen.setEnabled(false);
                } else
                    JOptionPane.showMessageDialog(null,
                        "Cannot open the sequential file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                
                disk.printDisk(textArea);
            }
        });
        bSequentialOpen.setEnabled(false);
        buttonPanel.add(bSequentialOpen);
        
        bSequentialWrite.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane
                    .showInputDialog("Please input a name (up to 15 letters):");
                if (name == null)
                    return;
                
                if (name.length() > 15) {
                    JOptionPane.showMessageDialog(null,
                        "Name should've less than or equal to 15 letters!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String grade = JOptionPane
                    .showInputDialog("Please input a grade (one letter):");
                if (grade == null)
                    return;
                if (grade.length() != 1) {
                    JOptionPane.showMessageDialog(null,
                        "Grade should've been 1 letter!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String id = JOptionPane
                    .showInputDialog("Please enter ID between 0 and 999:");
                if (id == null)
                    return;
                int idN;
                try {
                    idN = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been a number!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((idN < 0) || (idN > 999)) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been in 0 - 999 range!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                SequentialFile.Record r = new SequentialFile.Record();
                r.id = idN;
                for (int i = 0; i < name.length(); i++)
                    r.name[i] = name.charAt(i);
                r.grade = grade.toCharArray();
                if (sequentialFile.write(r))
                    // File written to successfully.
                    disk.printDisk(textArea);
                else
                    JOptionPane.showMessageDialog(null,
                        "Error writing to the sequential file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bSequentialWrite.setEnabled(false);
        buttonPanel.add(bSequentialWrite);
        
        bSequentialFind.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane
                    .showInputDialog("Please enter ID between 0 and 999:");
                int idN;
                try {
                    idN = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been a number!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((idN < 0) || (idN > 999)) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been in 0 - 999 range!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                SequentialFile.Record r = sequentialFile.find(idN);
                if (r != null)
                    // File read from successfully.
                    JOptionPane.showMessageDialog(null, "ID: " + r.id
                        + " Name: " + String.valueOf(r.name) + " Grade: "
                        + String.valueOf(r.grade), "Read As Follows",
                        JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null,
                        "Error finding in the sequential file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bSequentialFind.setEnabled(false);
        buttonPanel.add(bSequentialFind);
        
        bSequentialDelete.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane
                    .showInputDialog("Please enter ID between 0 and 999:");
                int idN;
                try {
                    idN = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been a number!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((idN < 0) || (idN > 999)) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been in 0 - 999 range!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (sequentialFile.delete(idN))
                    // File seeked successfully.
                    disk.printDisk(textArea);
                else
                    JOptionPane.showMessageDialog(null,
                        "Error deleting from the sequential file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bSequentialDelete.setEnabled(false);
        buttonPanel.add(bSequentialDelete);
        
        bSequentialClose.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sequentialFile.close()) {
                    // File closed successfully.
                    bSequentialWrite.setEnabled(false);
                    bSequentialFind.setEnabled(false);
                    bSequentialDelete.setEnabled(false);
                    bSequentialClose.setEnabled(false);
                    
                    bSequentialOpen.setEnabled(true);
                    bSequentialOpen.setText("Sequential Open");
                    
                    bBasicOpen.setEnabled(true);
                    bHeapOpen.setEnabled(true);
                } else
                    JOptionPane.showMessageDialog(null,
                        "Error closing the file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bSequentialClose.setEnabled(false);
        buttonPanel.add(bSequentialClose);
        
        // Create heap file control buttons.
        bHeapOpen.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane
                    .showInputDialog("Please input a file name:");
                boolean createIfDoesNotExist = JOptionPane.showConfirmDialog(
                    null, "Create file if it does not exist?", "Create File?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                if (heapFile.open(fileName, createIfDoesNotExist)) {
                    // File opened successfully.
                    bHeapWrite.setEnabled(true);
                    bHeapFind.setEnabled(true);
                    bHeapSort.setEnabled(true);
                    bHeapClose.setEnabled(true);
                    
                    bHeapOpen.setEnabled(false);
                    bHeapOpen.setText("Opened: " + fileName);
                    
                    bBasicOpen.setEnabled(false);
                    bSequentialOpen.setEnabled(false);
                } else
                    JOptionPane.showMessageDialog(null,
                        "Cannot open the heap file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                
                disk.printDisk(textArea);
            }
        });
        bHeapOpen.setEnabled(false);
        buttonPanel.add(bHeapOpen);
        
        bHeapWrite.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane
                    .showInputDialog("Please input a name (up to 15 letters):");
                if (name.length() > 15) {
                    JOptionPane.showMessageDialog(null,
                        "Name should've less than or equal to 15 letters!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String grade = JOptionPane
                    .showInputDialog("Please input a grade (one letter):");
                if (grade.length() != 1) {
                    JOptionPane.showMessageDialog(null,
                        "Grade should've been 1 letter!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String id = JOptionPane
                    .showInputDialog("Please enter ID between 0 and 999:");
                int idN;
                try {
                    idN = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been a number!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((idN < 0) || (idN > 999)) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been in 0 - 999 range!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                HeapFile.Record r = new HeapFile.Record();
                r.id = idN;
                for (int i = 0; i < name.length(); i++)
                    r.name[i] = name.charAt(i);
                r.grade = grade.toCharArray();
                heapFile.write(r);
                disk.printDisk(textArea);
            }
        });
        bHeapWrite.setEnabled(false);
        buttonPanel.add(bHeapWrite);
        
        bHeapFind.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane
                    .showInputDialog("Please enter ID between 0 and 999:");
                int idN;
                try {
                    idN = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been a number!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((idN < 0) || (idN > 999)) {
                    JOptionPane.showMessageDialog(null,
                        "ID should've been in 0 - 999 range!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                HeapFile.Record r = heapFile.find(idN);
                if (r != null)
                    // File read from successfully.
                    JOptionPane.showMessageDialog(null, "ID: " + r.id
                        + " Name: " + String.valueOf(r.name) + " Grade: "
                        + String.valueOf(r.grade), "Read As Follows",
                        JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null,
                        "Error finding in the heap file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bHeapFind.setEnabled(false);
        buttonPanel.add(bHeapFind);
        
        bHeapSort.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                heapFile.sort();
                disk.printDisk(textArea);
            }
        });
        bHeapSort.setEnabled(false);
        buttonPanel.add(bHeapSort);
        
        bHeapClose.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (heapFile.close()) {
                    // File closed successfully.
                    bHeapWrite.setEnabled(false);
                    bHeapFind.setEnabled(false);
                    bHeapSort.setEnabled(false);
                    bHeapClose.setEnabled(false);
                    
                    bHeapOpen.setEnabled(true);
                    bHeapOpen.setText("Heap Open");
                    
                    bBasicOpen.setEnabled(true);
                    bSequentialOpen.setEnabled(true);
                } else
                    JOptionPane.showMessageDialog(null,
                        "Error closing the file!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bHeapClose.setEnabled(false);
        buttonPanel.add(bHeapClose);
        
        // Create a standard kind of menu.
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.getAccessibleContext().setAccessibleDescription(
            "The only menu in this program");
        menuBar.add(menu);
        
        JMenuItem miNewDisk = new JMenuItem("New Disk", KeyEvent.VK_N);
        miNewDisk.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                disk.newDisk();
                
                bBasicOpen.setEnabled(true);
                bSequentialOpen.setEnabled(true);
                bHeapOpen.setEnabled(true);
                
                disk.printDisk(textArea);
                
                basicFile.setDisk(disk);
                sequentialFile.setDisk(disk);
                heapFile.setDisk(disk);
            }
        });
        menu.add(miNewDisk);
        
        JMenuItem miLoadDisk = new JMenuItem("Load Disk", KeyEvent.VK_L);
        miLoadDisk.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(System
                    .getProperty("user.dir"));
                // chooser.setFileFilter(new FileNameExtensionFilter("Files with Disks", "disk"));
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    if (disk.loadDisk(chooser.getSelectedFile().getPath())) {
                        // Disk loaded successfully.
                        bBasicOpen.setEnabled(true);
                        bSequentialOpen.setEnabled(true);
                        bHeapOpen.setEnabled(true);
                        
                        disk.printDisk(textArea);
                        
                        basicFile.setDisk(disk);
                        sequentialFile.setDisk(disk);
                        heapFile.setDisk(disk);
                    } else
                        JOptionPane.showMessageDialog(null,
                            "Cannot open the disk!", "Error",
                            JOptionPane.ERROR_MESSAGE);
            }
        });
        menu.add(miLoadDisk);
        
        JMenuItem miSaveDisk = new JMenuItem("Save Disk", KeyEvent.VK_L);
        miSaveDisk.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(System
                    .getProperty("user.dir"));
                // chooser.setFileFilter(new FileNameExtensionFilter("Files with Disks", "disk"));
                if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
                    if (disk.saveDisk(chooser.getSelectedFile().getPath())) {
                        // Disk saved successfully.
                    } else
                        JOptionPane.showMessageDialog(null,
                            "Cannot open the disk!", "Error",
                            JOptionPane.ERROR_MESSAGE);
            }
        });
        menu.add(miSaveDisk);
        
        JMenuItem miExit = new JMenuItem("Exit", KeyEvent.VK_X);
        miExit.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        menu.add(miExit);
        
        // Put all the parts onto the form and pack it together.
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
