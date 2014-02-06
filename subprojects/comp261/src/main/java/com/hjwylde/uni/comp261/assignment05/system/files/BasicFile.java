package com.hjwylde.uni.comp261.assignment05.system.files;

import java.util.Arrays;

import com.hjwylde.uni.comp261.assignment05.system.Disk;
import com.hjwylde.uni.comp261.assignment05.systemSimulator.Main;

/*
 * Code for Assignment 5, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class BasicFile {
    
    private static final int BLOCK_SIZE = Disk.BLOCK_SIZE;
    
    private Disk disk;
    
    private int fileHandle; // File handle.
    private int filePointer; // File pointer - points to the current character.
    private int numBlocks; // Number of blocks in this file.
    
    public BasicFile(Disk disk) {
        setDisk(disk);
    }
    
    /**
     * Close the currently opened file on the disk if one is open. Return false if none are open.
     * 
     * @return true if a file was closed.
     */
    public boolean close() {
        if (!fileOpen())
            return false;
        
        clear();
        
        return true;
    }
    
    /**
     * Open the file labelled as <code>fileName</code>. Create a new one if it doesn't exist only if
     * <code>createIfDoesNotExist</code> is set to true.
     * 
     * @param fileName the file name to open.
     * @param createIfDoesNotExist whether to create a new file if it doesn't exist
     * @return true if a file was opened successfully.
     */
    public boolean open(String fileName, boolean createIfDoesNotExist) {
        if (fileOpen())
            return false;
        /*
         * Reserve 20 characters of the block for the number of blocks the file occupies and the
         * FILE<>
         * header.
         */
        if (fileName.equals("")
            || (fileName.length() > (BasicFile.BLOCK_SIZE - 20))
            || fileName.contains("<") || fileName.contains(">")
            || fileName.contains("~"))
            return false;
        
        blockFor:
        for (int i = 0; i < disk.getNumberOfBlocks(); i++) { // For every block on the disk...
            char[] buffer = disk.getBlock(i).get(); // Read the block data.
            
            for (int j = 0; j <= fileName.length(); j++)
                // For every char in the file name...
                if ((buffer[j + 5] == '>') && (j == fileName.length())) { // If we have reached the
                                                                          // end of
                                                                          // header correctly...
                    fileHandle = i; // Set the file handle to the current block.
                    filePointer = 0; // Set the file pointer to the beginning.
                    numBlocks = Main.nextInt(buffer, j + 6); // Record the number of blocks this
                                                             // file
                                                             // occupies.
                    
                    return true;
                } else if ((j == fileName.length())
                    || (buffer[j + 5] != fileName.charAt(j))) {// ... else if characters don't
                                                               // match...
                    while (((j + 5) < BasicFile.BLOCK_SIZE)
                        && (buffer[j + 5] != '>'))
                        // Advance j until we reach the number of blocks position.
                        j++;
                    
                    if ((j + 6) >= BasicFile.BLOCK_SIZE) // Broken file format, no end bracket on
                                                         // file name.
                        return false;
                    
                    i += Main.nextInt(buffer, j + 6); // Skip the number of blocks this file
                                                      // occupies.
                    
                    continue blockFor; // Carry on searching blocks.
                }
        } // No file found!
        
        if (createIfDoesNotExist) { // Create new file?
            char[] buffer = Arrays
                .copyOf(("FILE<" + fileName + ">0").toCharArray(),
                    BasicFile.BLOCK_SIZE); // Create a new character array with correct header!
            
            Disk.Block block = new Disk.Block(); // Create a new block.
            block.set(buffer); // Set the block characters to the newly created header.
            disk.addBlock(block); // Add the block.
            
            fileHandle = disk.getNumberOfBlocks() - 1; // Set file handle.
            filePointer = 0; // Set file pointer to start.
            numBlocks = 0; // Set number of blocks to 0.
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Reads some data from the current file pointer into the given character array. If the end of
     * file character is encountered before the array is filled this method abruptly stops and
     * returns
     * false.
     * 
     * @param data the array to read data into.
     * @return whether data is read successfully.
     */
    public boolean read(char[] data) {
        if (!fileOpen())
            return false;
        
        if (eof() < (filePointer + data.length)) // Will we go over the end of file?
            return false;
        
        char[] buffer = getBlock(filePointer).get(); // Get the current block.
        
        for (int i = 0; i < data.length; i++) { // For every character in data...
            if (relativeFilePointer() == 0) // Check if we have crossed over onto a new block on the
                                            // disk...
                buffer = getBlock(filePointer).get(); // ... update our current block.
                
            data[i] = buffer[relativeFilePointer()]; // Set the character in data to the read
                                                     // character.
            
            seek(1); // Advance the file pointer.
        }
        
        return true;
    }
    
    /**
     * Advance the file pointer some length. Returns false if the length results in the file pointer
     * being set before the beginning of the file.
     * 
     * @param l the length to advance the pointer.
     * @return true if the file pointer was altered.
     */
    public boolean seek(int l) {
        if (!fileOpen())
            return false;
        if ((filePointer + l) < 0)
            return false;
        
        filePointer += l;
        
        return true;
    }
    
    /**
     * Sets the disk for this BasicFile and clears any file specific fields.
     * 
     * @param disk the disk to set to.
     */
    public void setDisk(Disk disk) {
        this.disk = disk;
        
        clear();
    }
    
    /**
     * Write the given character array to the currently open file. Pads the file if the current file
     * pointer is greater than the end of file.
     * 
     * @param data the data to write.
     * @return true if data was written.
     */
    public boolean write(char[] data) {
        if (!fileOpen() || (data.length == 0))
            return false;
        
        int eof = eof(); // Find the eof.
        if (eof == -1) { // In other words: If the file has numBlocks == 0...
            insertBlock(1 + fileHandle + numBlocks); // Add one block.
            eof = 0; // Set eof to 0.
        }
        
        // Calculate the number of blocks that may need to be padded if the file pointer is larger
        // than
        // the eof.
        int insert = ((filePointer + data.length) / BasicFile.BLOCK_SIZE)
            - (eof / BasicFile.BLOCK_SIZE);
        while (insert > 0) { // Loop until padded.
            insertBlock(1 + fileHandle + numBlocks); // Insert a block.
            insert--;
        }
        
        if (eof < filePointer) // Do we need to overwrite the current eof marker?
            getBlock(eof).get()[eof % BasicFile.BLOCK_SIZE] = ' '; // Clear the eof marker.
            
        char[] buffer = null;
        
        int begin = filePointer;
        while (filePointer < (data.length + begin)) { // For the length of the data to write...
            if (((filePointer % BasicFile.BLOCK_SIZE) == 0) || (buffer == null)) // If we need to
                                                                                 // update
                                                                                 // our current
                // block.
                buffer = getBlock(filePointer).get(); // Get the new block.
                
            buffer[relativeFilePointer()] = data[filePointer - begin]; // Write the character from
                                                                       // the
                                                                       // data.
            
            seek(1); // Advance the file pointer.
        }
        
        if (filePointer >= eof) { // If we wrote over the previous eof...
            buffer = getBlock(filePointer).get(); // Get the new eof block.
            buffer[relativeFilePointer()] = '~'; // Add in the eof marker.
        }
        
        return true;
    }
    
    /**
     * Clears all file fields.
     */
    private void clear() {
        fileHandle = -1;
        filePointer = 0;
        numBlocks = 0;
    }
    
    /**
     * Searches for the end of file marker and returns its index position as the number of
     * characters
     * from the beginning of the file.
     * 
     * @return the eof index.
     */
    private int eof() {
        if (numBlocks == 0) // If no eof...
            return -1;
        
        int eof = (BasicFile.BLOCK_SIZE * numBlocks) - 1; // The block the eof will be in.
        
        char[] buffer = getBlock(eof).get(); // Get the eof block.
        
        while (eof >= (BasicFile.BLOCK_SIZE * (numBlocks - 1))) { // While the eof is still pointing
                                                                  // to
                                                                  // the eof
            // block...
            if (buffer[eof % BasicFile.BLOCK_SIZE] == '~') // If we have found the eof marker...
                break; // Break and return the current eof.
                
            eof--; // Decrement.
        }
        
        return eof; // Return the index of the eof marker.
    }
    
    /**
     * Returns whether a file is currently open.
     * 
     * @return true if a file is open.
     */
    private boolean fileOpen() {
        return (fileHandle != -1);
    }
    
    /**
     * Get the block the pointer is pointing to. The pointer is specified as a number from the start
     * of the file.
     * 
     * @param filePointer the pointer.
     * @return the block of the pointer.
     */
    private Disk.Block getBlock(int filePointer) {
        return disk.getBlock(1 + fileHandle
            + (filePointer / BasicFile.BLOCK_SIZE)); // Convert into
                                                     // block index
        // then return.
    }
    
    /**
     * Insert a block at the given block index.
     * 
     * @param index the block index to insert at.
     */
    private void insertBlock(int index) {
        disk.addBlock(new Disk.Block()); // Add a new block at end.
        
        /*
         * Move every block up one.
         */
        for (int i = disk.getNumberOfBlocks() - 1; i > index; i--)
            disk.getBlock(i).set(disk.getBlock(i - 1).get());
        
        disk.getBlock(index).set(new char[BasicFile.BLOCK_SIZE]); // Clear the new block.
        
        numBlocks++; // Increment number of blocks.
        
        updateNumBlocks(); // Update the number of blocks in the file header.
    }
    
    /**
     * Returns the relative file pointer which may be used for array indices. Equivalent to
     * <code>filePointer % BLOCK_SIZE</code>.
     * 
     * @return the relative file pointer.
     */
    private int relativeFilePointer() {
        return filePointer % BasicFile.BLOCK_SIZE;
    }
    
    /**
     * Updates the number of blocks in the file header.
     */
    private void updateNumBlocks() {
        char[] buffer = disk.getBlock(fileHandle).get(); // Get the file header block.
        
        /*
         * Find the number of blocks index.
         */
        int fileSizePointer = 5;
        do
            fileSizePointer++;
        while (buffer[fileSizePointer - 1] != '>');
        
        char[] numBlocks = Main.toCharArray(this.numBlocks); // Convert the number of blocks into a
                                                             // char
                                                             // array.
        
        System.arraycopy(numBlocks, 0, buffer, fileSizePointer,
            numBlocks.length); // Write the number
                               // of blocks to the
                               // header.
        Arrays.fill(buffer, fileSizePointer + numBlocks.length,
            BasicFile.BLOCK_SIZE, (char) Character.UNASSIGNED); // Pad any space after the number of
                                                                // blocks incase we didn't
                                                                // overwrite all digits, eg. going
                                                                // from
                                                                // 10 blocks to 9
                                                                // wouldn't overwrite the 0.
    }
}