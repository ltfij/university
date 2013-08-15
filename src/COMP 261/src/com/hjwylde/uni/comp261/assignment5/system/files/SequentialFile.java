package com.hjwylde.uni.comp261.assignment5.system.files;

import java.util.Arrays;

import com.hjwylde.uni.comp261.assignment5.system.Disk;
import com.hjwylde.uni.comp261.assignment5.systemSimulator.Main;

/*
 * Code for Assignment 5, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class SequentialFile {
    
    private static final int BLOCK_SIZE = Disk.BLOCK_SIZE;
    private static final int RECORD_SIZE = Record.length();
    
    private static final int BLOCKING_FACTOR = SequentialFile.BLOCK_SIZE
        / SequentialFile.RECORD_SIZE;
    
    private Disk disk;
    
    private int fileHandle; // File handle.
    private int filePointer; // File pointer - points to the current character.
    private int numBlocks; // Number of blocks in this file.
    
    public SequentialFile(Disk disk) {
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
     * Delete the given record id if it exists.
     * 
     * @param id the record id.
     * @return true if deleted.
     */
    public boolean delete(int id) {
        if (!fileOpen())
            return false;
        
        int index = indexOf(id);
        if (index < 0) // If the record doesn't exist...
            return false;
        
        deleteRecord(index); // Delete the record.
        
        return true;
    }
    
    /**
     * Find a record with the given id.
     * 
     * @param id the record id.
     * @return the record.
     */
    public Record find(int id) {
        if (!fileOpen())
            return null;
        
        int index = indexOf(id);
        if (index < 0) // If the record doesn't exist...
            return null;
        
        setFilePointer(index); // Set the file pointer.
        
        return SequentialFile.readRecord(getBlock(index).get(),
            relativeFilePointer()); // Return the record.
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
            || (fileName.length() > (SequentialFile.BLOCK_SIZE - 20))
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
                    while (((j + 5) < SequentialFile.BLOCK_SIZE)
                        && (buffer[j + 5] != '>'))
                        // Advance j until we reach the number of blocks position.
                        j++;
                    
                    if ((j + 6) >= SequentialFile.BLOCK_SIZE) // Broken file format, no end bracket
                                                              // on file
                                                              // name.
                        return false;
                    
                    i += Main.nextInt(buffer, j + 6); // Skip the number of blocks this file
                                                      // occupies.
                    
                    continue blockFor; // Carry on searching blocks.
                }
        } // No file found!
        
        if (createIfDoesNotExist) { // Create new file?
            char[] buffer = Arrays.copyOf(
                ("FILE<" + fileName + ">0").toCharArray(),
                SequentialFile.BLOCK_SIZE); // Create a new character array with correct header!
            
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
     * Sets the disk for this BasicFile and clears any file specific fields.
     * 
     * @param disk the disk to set to.
     */
    public void setDisk(Disk disk) {
        this.disk = disk;
        
        clear();
    }
    
    /**
     * Write the given record to the file in its correct position.
     * 
     * @param r the record to write.
     * @return true if the record was written.
     */
    public boolean write(Record r) {
        if (!fileOpen())
            return false;
        
        int index = indexOf(r.id);
        if (index >= 0) // If the record already exists...
            return false;
        
        index = -index - 1; // Convert to correct index.
        
        insertRecord(index); // Insert a record at the index.
        
        setFilePointer(index); // Set the file pointer to index.
        
        SequentialFile.writeRecord(r, getBlock(index).get(),
            relativeFilePointer()); // Write the
                                    // record.
        
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
     * Delete the block at the given block index.
     * 
     * @param index the block index.
     */
    private void deleteBlock(int index) {
        for (int i = index; i < (disk.getNumberOfBlocks() - 1); i++)
            disk.getBlock(i).set(disk.getBlock(i + 1).get()); // Move all blocks down one.
        
        disk.deleteLastBlock(); // Delete the last block.
        
        numBlocks--; // Decrement.
        
        updateNumBlocks(); // Update the number of blocks in file header.
    }
    
    /**
     * Delete the record at the given index.
     * 
     * @param index the record index to delete.
     */
    private void deleteRecord(int index) {
        char[] record = new char[SequentialFile.RECORD_SIZE]; // Storage for when moving a record
                                                              // down a
                                                              // block.
        
        int eof = eof(); // Find the end of file record index.
        
        char[] buffer = getBlock(eof).get(); // Get the block at the end of file index.
        
        for (int i = eof; i >= index; i--) { // For every record from eof to index...
            if (((i + 1) % SequentialFile.BLOCKING_FACTOR) == 0) // Check if we need to update the
                                                                 // current
                                                                 // data block...
                buffer = getBlock(i).get();
            
            setFilePointer(i); // Set the file pointer to the current index.
            
            char[] holder = Arrays.copyOfRange(buffer, relativeFilePointer(),
                relativeFilePointer() + SequentialFile.RECORD_SIZE); // Temp.
            System.arraycopy(record, 0, buffer, relativeFilePointer(),
                SequentialFile.RECORD_SIZE); // Move the previous
            // record down.
            record = holder; // Transfer.
        }
        
        buffer = getBlock((SequentialFile.BLOCKING_FACTOR * numBlocks) - 1)
            .get(); // Get the last
                    // block.
        
        if (buffer[0] == Character.UNASSIGNED) // If the last block has no records in it...
            deleteBlock(fileHandle + numBlocks); // ... delete the block.
    }
    
    /**
     * Find the end of file record index.
     * 
     * @return the eof record index.
     */
    private int eof() {
        if (numBlocks == 0) // No records...
            return -1;
        
        int eof = (SequentialFile.BLOCKING_FACTOR * numBlocks) - 1; // Set to the max possible
                                                                    // record
                                                                    // index.
        
        char[] buffer = getBlock(eof).get(); // Get the block the eof index is at.
        
        while (eof >= (SequentialFile.BLOCKING_FACTOR * (numBlocks - 1))) { // For each record in
                                                                            // the
                                                                            // eof block...
            setFilePointer(eof); // Set the file pointer.
            
            if (buffer[relativeFilePointer()] != Character.UNASSIGNED) // If we've found the last
                                                                       // record...
                break; // ... break and return that index.
                
            eof--; // Decrement.
        }
        
        return eof;
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
     * Get the block the pointer is pointing to. The pointer is specified as the record number from
     * the start of the file.
     * 
     * @param filePointer the index pointer.
     * @return the block of the pointer.
     */
    private Disk.Block getBlock(int filePointer) {
        return disk.getBlock(1 + fileHandle
            + (filePointer / SequentialFile.BLOCKING_FACTOR)); // Convert into block
        // index then return.
    }
    
    /**
     * Finds the record index of the given record id between record indices low and high.
     * 
     * @param id the record id.
     * @param low the low bounds.
     * @param high the high bounds.
     * @return the index of the record.
     */
    private int indexOf(char[] id, int low, int high) {
        if (low >= high) // If we've found it...
            return low; // ... return the index.
            
        int mid = (low + high) / 2; // Calculate the mid index.
        
        setFilePointer(mid); // Set the file pointer to the mid index.
        
        char[] buffer = getBlock(mid).get(); // Get the mid block.
        
        for (int i = 0; i < Record.ID_LENGTH; i++)
            // For each digit in the record id...
            if (id[i] > buffer[relativeFilePointer() + i]) // If the id is greater than the middle
                                                           // index...
                return indexOf(id, mid + 1, high); // ... search in the bounds of (mid + 1, high).
            else if (id[i] < buffer[relativeFilePointer() + i]) // If the id is less than the middle
                                                                // index...
                return indexOf(id, low, mid); // ... search in the bounds of (low, mid).
                
        return mid; // The id is equal to the mid id.
    }
    
    /**
     * Uses a binary search algorithm to find the given record id. If the record is not present it
     * will return -index - 1 where index is the correct position of the record.
     * 
     * @param id the record id to search for.
     * @return the index of the record.
     */
    private int indexOf(int id) {
        if (numBlocks == 0)
            return -1;
        
        int index = indexOf(Main.toCharArray(id, Record.ID_LENGTH), 0,
            eof() + 1); // Binary search!
        
        if (index >= (SequentialFile.BLOCKING_FACTOR * numBlocks)) // Simple boundary checks so the
                                                                   // next
                                                                   // steps won't
            // get
            // indexOutOfBoundsException().
            return -index - 1;
        
        setFilePointer(index); // Set the file pointer to the current index.
        
        char[] buffer = getBlock(index).get(); // Get the block the index is in.
        
        /*
         * Check whether the record actually exists... if it doesn't return -index - 1.
         */
        if (Arrays.equals(Arrays.copyOfRange(buffer, relativeFilePointer(),
            relativeFilePointer() + Record.ID_LENGTH), Main.toCharArray(id,
            Record.ID_LENGTH)))
            return index;
        
        return -index - 1;
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
        
        disk.getBlock(index).set(new char[SequentialFile.BLOCK_SIZE]); // Clear the new block.
        
        numBlocks++; // Increment number of blocks.
        
        updateNumBlocks(); // Update the number of blocks in the file header.
    }
    
    /**
     * Insert a record at the given index.
     * 
     * @param index the index to insert at.
     */
    private void insertRecord(int index) {
        if (((eof() + 1) % SequentialFile.BLOCKING_FACTOR) == 0) // Will inserting a record push
                                                                 // them
                                                                 // over a block?
            insertBlock(1 + fileHandle + numBlocks); // ... then insert a block.
            
        char[] record = new char[SequentialFile.RECORD_SIZE]; // Holder for transfering records over
                                                              // blocks.
        
        char[] buffer = getBlock(index).get(); // Get the block of the index.
        
        for (int i = index; i < (SequentialFile.BLOCKING_FACTOR * numBlocks); i++) { // For each
                                                                                     // record
                                                                                     // from index
                                                                                     // to
            // the
            // max records...
            if ((i % SequentialFile.BLOCKING_FACTOR) == 0) // Check if we need to update the current
                                                           // block...
                buffer = getBlock(i).get();
            
            setFilePointer(i); // Set the file pointer to the current index.
            
            /*
             * Shuffle the records up one.
             */
            char[] holder = Arrays.copyOfRange(buffer, relativeFilePointer(),
                relativeFilePointer() + SequentialFile.RECORD_SIZE);
            System.arraycopy(record, 0, buffer, relativeFilePointer(),
                SequentialFile.RECORD_SIZE);
            record = holder;
        }
    }
    
    /**
     * Returns the relative file pointer which may be used for array indices. Equivalent to
     * <code>filePointer % BLOCK_SIZE</code>.
     * 
     * @return the relative file pointer.
     */
    private int relativeFilePointer() {
        return filePointer % SequentialFile.BLOCK_SIZE;
    }
    
    /**
     * Sets the file pointer to the correct character pointer for the given record index.
     * 
     * @param index the record index.
     */
    private void setFilePointer(int index) {
        filePointer = (SequentialFile.RECORD_SIZE * (index % SequentialFile.BLOCKING_FACTOR))
            + (SequentialFile.BLOCK_SIZE * (index / SequentialFile.BLOCKING_FACTOR));
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
            SequentialFile.BLOCK_SIZE, (char) Character.UNASSIGNED); // Pad any space after the
                                                                     // number
                                                                     // of blocks incase we didn't
                                                                     // overwrite all digits, eg.
                                                                     // going
                                                                     // from 10 blocks to 9
                                                                     // wouldn't overwrite the 0.
    }
    
    /**
     * Read a record from the buffer at the given position.
     * 
     * @param buffer the buffer to read from.
     * @param from the position to read at.
     * @return the record.
     */
    private static Record readRecord(char[] buffer, int from) {
        Record r = new Record();
        r.id = Main.nextInt(buffer, from, Record.ID_LENGTH);
        r.name = Arrays.copyOfRange(buffer, from + Record.ID_LENGTH, from
            + Record.ID_LENGTH + Record.NAME_LENGTH);
        r.grade = Arrays.copyOfRange(buffer, from + Record.ID_LENGTH
            + Record.NAME_LENGTH, from + Record.ID_LENGTH + Record.NAME_LENGTH
            + Record.GRADE_LENGTH);
        
        return r;
    }
    
    /**
     * Write the given record to the buffer at the specified position.
     * 
     * @param r the record to write.
     * @param buffer the buffer to write to.
     * @param to the position to write to.
     */
    private static void writeRecord(Record r, char[] buffer, int to) {
        System.arraycopy(r.toCharArray(), 0, buffer, to,
            SequentialFile.RECORD_SIZE);
    }
    
    public static class Record {
        
        public static final int ID_LENGTH = 3;
        public static final int NAME_LENGTH = 15;
        public static final int GRADE_LENGTH = 1;
        
        public int id; // Has to be between 0 and 999
        public char[] name = new char[Record.NAME_LENGTH];
        public char[] grade = new char[Record.GRADE_LENGTH];
        
        /**
         * Conver the record to a character array.
         * 
         * @return the record as a char array.
         */
        public char[] toCharArray() {
            char[] record = new char[Record.length()];
            
            System.arraycopy(Main.toCharArray(id, Record.ID_LENGTH), 0, record,
                0, Record.ID_LENGTH);
            System.arraycopy(name, 0, record, Record.ID_LENGTH,
                Record.NAME_LENGTH);
            System.arraycopy(grade, 0, record, Record.ID_LENGTH
                + Record.NAME_LENGTH, Record.GRADE_LENGTH);
            
            return record;
        }
        
        /*
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "{(" + id + "), (" + Arrays.toString(name) + "), ("
                + Arrays.toString(grade) + ")}";
        }
        
        public static int length() {
            return Record.ID_LENGTH + Record.NAME_LENGTH + Record.GRADE_LENGTH;
        }
    }
}