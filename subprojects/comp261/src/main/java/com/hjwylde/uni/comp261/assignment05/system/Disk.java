package com.hjwylde.uni.comp261.assignment05.system;

import java.io.*;
import java.util.ArrayList;

import javax.swing.JTextArea;

/*
 * Code for Assignment 5, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class Disk {

    public static final int BLOCK_SIZE = 80;

    private ArrayList<Block> blocks;

    public Disk() {
        newDisk();
    }

    public void addBlock(Block b) {
        blocks.add(b);
    }

    public boolean deleteLastBlock() {
        if (blocks.size() > 1) {
            blocks.remove(blocks.size() - 1);

            return true;
        }

        return false;
    }

    public Block getBlock(int i) {
        return blocks.get(i);
    }

    public int getNumberOfBlocks() {
        return blocks.size();
    }

    public boolean loadDisk(String diskFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(diskFileName))) {

            newDisk(); // reset disk

            while (br.ready()) {
                char[] data = new char[Disk.BLOCK_SIZE];
                int result = br.read(data, 0, data.length);
                if (result == -1) {
                    System.err
                            .println("ERROR: When loading disk from a file the blocks didn't align!");

                    return false;
                }
                String eol = br.readLine();
                if ((eol != null) && !eol.equals("")) {
                    System.err.println("ERROR: Expected the block to end with a new line!");
                    return false;
                }
                Block b = new Block();
                if (!b.set(data))
                    System.err.println("ERROR: Internal error - block size should've matched!");
                blocks.add(b);
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());

            return false;
        }

        return true;
    }

    public void newDisk() {
        blocks = new ArrayList<>();
    }

    public void printDisk(JTextArea ta) {
        ta.setText("");

        for (Block b : blocks) {
            char[] data = b.get();
            for (int i = 0; i < data.length; i++)
                ta.append(data[i] + "");
            ta.append("\n");
        }
    }

    public boolean saveDisk(String diskFileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(diskFileName))) {
            for (Block b : blocks) {
                char[] data = b.get();
                try {
                    bw.write(data, 0, data.length);
                    bw.write("\n");
                } catch (IOException e) {
                    System.err.println("ERROR: When trying to save to disk.");
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            return false;
        }

        return true;
    }

    public boolean setBlock(int i, Block b) {
        try {
            blocks.set(i, b);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public static class Block {

        private char[] data;

        public Block() {
            data = new char[Disk.BLOCK_SIZE];
        }

        public char[] get() {
            return data;
        }

        public boolean set(char[] data) {
            if ((data == null) || (data.length != Disk.BLOCK_SIZE))
                return false;

            this.data = data;
            return true;
        }
    }
}
