package com.hjwylde.uni.comp261.assignment5.system.files;

import java.util.*;

import com.hjwylde.uni.comp261.assignment5.system.Disk;
import com.hjwylde.uni.comp261.assignment5.systemSimulator.Main;

/*
 * Code for Assignment 5, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class HeapFile {
    
    private static final int BLOCK_SIZE = Disk.BLOCK_SIZE;
    private static final int RECORD_SIZE = Record.length();
    
    private static final int BLOCKING_FACTOR = HeapFile.BLOCK_SIZE
        / HeapFile.RECORD_SIZE;
    
    private Disk disk;
    
    private int fileHandle; // File handle.
    private int filePointer; // File pointer - points to the current character.
    private int numBlocks; // Number of blocks in this file.
    
    public HeapFile(Disk disk) {
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
        
        return HeapFile
            .readRecord(getBlock(index).get(), relativeFilePointer()); // Return the record.
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
            || (fileName.length() > (HeapFile.BLOCK_SIZE - 20))
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
                    while (((j + 5) < HeapFile.BLOCK_SIZE)
                        && (buffer[j + 5] != '>'))
                        // Advance j until we reach the number of blocks position.
                        j++;
                    
                    if ((j + 6) >= HeapFile.BLOCK_SIZE) // Broken file format, no end bracket on
                                                        // file name.
                        return false;
                    
                    i += Main.nextInt(buffer, j + 6); // Skip the number of blocks this file
                                                      // occupies.
                    
                    continue blockFor; // Carry on searching blocks.
                }
        } // No file found!
        
        if (createIfDoesNotExist) { // Create new file?
            char[] buffer = Arrays.copyOf(
                ("FILE<" + fileName + ">0").toCharArray(), HeapFile.BLOCK_SIZE); // Create a new
                                                                                 // character array
                                                                                 // with correct
                                                                                 // header!
            
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
     * Sorts this heap file using a sort-merge algorithm and the default number of buffers specified
     * in <code>Core</code>.
     */
    public void sort() {
        if (!fileOpen() || (numBlocks == 0))
            return;
        
        /*
         * NOT FINISHED!
         * 
         * I did not manage to do the merge phase of this sorting algorithm, wasn't sure how / ran
         * out
         * of time. The code I started to try is still there but it doesn't really work well.
         * 
         * The sort runs part will just sort each of the runs where each run has n - 1 blocks.
         */
        sortRuns(Main.NUMBER_BUFFERS);
        // mergeRuns(Core.NUMBER_BUFFERS);
    }
    
    /**
     * Write the given record to the end of the file or overwrite an existing record if one exists.
     * 
     * @param r the record to write.
     */
    public void write(Record r) {
        if (!fileOpen())
            return;
        
        int index = indexOf(r.id); // Find record position.
        if (index < 0) // If the record doesn't exist...
            index = -index - 1; // Calculate positive index.
            
        if ((index / HeapFile.BLOCKING_FACTOR) >= numBlocks) // Insert another block if need.
            insertBlock(1 + fileHandle + numBlocks);
        
        setFilePointer(index); // Set the file pointer to the given index.
        
        HeapFile.writeRecord(r, getBlock(index).get(), relativeFilePointer());
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
     * Find the end of file record index.
     * 
     * @return the eof record index.
     */
    private int eof() {
        if (numBlocks == 0) // No records...
            return -1;
        
        int eof = (HeapFile.BLOCKING_FACTOR * numBlocks) - 1; // Set to the max possible record
                                                              // index.
        
        char[] buffer = getBlock(eof).get(); // Get the block the eof index is at.
        
        while (eof >= (HeapFile.BLOCKING_FACTOR * (numBlocks - 1))) { // For each record in the eof
                                                                      // block...
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
            + (filePointer / HeapFile.BLOCKING_FACTOR)); // Convert into block
        // index then return.
    }
    
    /**
     * Search function returning the index of the record. The index is the record number from the
     * beginning of the file. If the record doesn't exist, <code>-eof() - 1</code> will be returned.
     * Essentially the negative says it doesn't exist and converting it back to positive returns the
     * end of file index.
     * 
     * @param id
     * @return
     */
    private int indexOf(int id) {
        char[] buffer = null;
        
        for (int i = 0; i < (numBlocks * HeapFile.BLOCKING_FACTOR); i++) { // Go through every block
                                                                           // in
                                                                           // this
            // file.
            if ((i % HeapFile.BLOCKING_FACTOR) == 0) // Check if we need to update the current block
                                                     // in
                                                     // buffer.
                buffer = getBlock(i).get();
            
            if (buffer[HeapFile.RECORD_SIZE * (i % HeapFile.BLOCKING_FACTOR)] == Character.UNASSIGNED) // If
                                                                                                       // we've
                                                                                                       // reached
                // the end...
                return -i - 1; // ... return the end index as a negative.
                
            setFilePointer(i); // Set the file pointer to the current index.
            
            /*
             * Check if the current index matches the id.
             */
            if (Arrays.equals(Arrays.copyOfRange(buffer, relativeFilePointer(),
                relativeFilePointer() + Record.ID_LENGTH), Main.toCharArray(id,
                Record.ID_LENGTH)))
                return i; // ... match! Return i.
        }
        
        return (-numBlocks * HeapFile.BLOCKING_FACTOR) - 1; // All blocks searched, no index found.
                                                            // Return -eof -
        // 1.
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
        
        disk.getBlock(index).set(new char[HeapFile.BLOCK_SIZE]); // Clear the new block.
        
        numBlocks++; // Increment number of blocks.
        
        updateNumBlocks(); // Update the number of blocks in the file header.
    }
    
    /*
     * DOES NOT WORK!
     */
    @SuppressWarnings("unused")
    private void mergeRuns(int n) {
        int blockEof = disk.getNumberOfBlocks() - numBlocks;
        int writeIndex = 0;
        
        for (int i = 1 + (numBlocks / (n - 1)); i > 1; i /= (n - 1)) {
            /*
             * I tried:
             * 
             * To iterate through each subfile (note: at the moment writing the subfiles to the end
             * of the
             * disk is disabled in the sortRuns() method) and create a sort of stack like buffer
             * using an
             * iterator that has a peek() method for each run.
             */
            List<PeekIterator> runIts = new ArrayList<PeekIterator>();
            for (int run = 0; run < i; run++)
                // Create all the PeekIterators.
                runIts.add(new PeekIterator(blockEof + (run * (n - 1)),
                    1 + (numBlocks / i)));
            
            /*
             * Go through all of the iterators in total until none are left, adding the lowest
             * poll() of
             * them to a subfile written at the bottom of the disk.
             */
            while (!runIts.isEmpty()) {
                int low = -1;
                for (int j = 0; j < runIts.size(); j++) {
                    PeekIterator it = runIts.get(j);
                    
                    if (!it.hasNext()) {
                        runIts.remove(it);
                        continue;
                    }
                    
                    if (low == -1)
                        low = j;
                    else {
                        Record r = it.peek();
                        if (r.id < runIts.get(low).peek().id)
                            low = j;
                    }
                }
                
                if (low == -1)
                    continue;
                
                if ((writeIndex % HeapFile.BLOCKING_FACTOR) == 0)
                    disk.addBlock(new Disk.Block());
                
                HeapFile.writeRecord(runIts.get(low).next(),
                    disk.getBlock(disk.getNumberOfBlocks() - 1).get(),
                    HeapFile.RECORD_SIZE
                        * (writeIndex % HeapFile.BLOCKING_FACTOR));
            }
        }
    }
    
    /**
     * Returns the relative file pointer which may be used for array indices. Equivalent to
     * <code>filePointer % BLOCK_SIZE</code>.
     * 
     * @return the relative file pointer.
     */
    private int relativeFilePointer() {
        return filePointer % HeapFile.BLOCK_SIZE;
    }
    
    /**
     * Sets the file pointer to the correct character pointer for the given record index.
     * 
     * @param index the record index.
     */
    private void setFilePointer(int index) {
        filePointer = (HeapFile.RECORD_SIZE * (index % HeapFile.BLOCKING_FACTOR))
            + (HeapFile.BLOCK_SIZE * (index / HeapFile.BLOCKING_FACTOR));
    }
    
    /**
     * Sorts the heap file runs with the specified number of buffers. The result is the entire file
     * written as sorted subfiles which each contain n - 1 blocks. Writes the subfiles to the end of
     * the disk.
     * 
     * @param n the number of buffers available.
     */
    private void sortRuns(int n) {
        PriorityQueue<Record> queue = new PriorityQueue<>(
            HeapFile.BLOCKING_FACTOR * (n - 1), new RecordSorter()); // Priority queue for sorting.
        /*
         * Note: I didn't use any other buffer really apart from the priority queue as a combination
         * of
         * them all... just felt this would be easier for sorting them.
         */
        
        char[] blockBuffer = new char[HeapFile.BLOCK_SIZE];
        
        int eof = eof();
        
        for (int run = 0; run < numBlocks; run += (n - 1)) { // For each run...
            queue.clear(); // Clear the queue (Should not be needed).
            
            bufferLoop:
            for (int buffer = 0; buffer < (n - 1); buffer++) { // For each buffer in the run...
                int block = run + buffer; // Calculate the block id.
                
                blockBuffer = getBlock(block * HeapFile.BLOCKING_FACTOR).get(); // Get the block.
                
                for (int index = block * HeapFile.BLOCKING_FACTOR; index < ((1 + block) * HeapFile.BLOCKING_FACTOR); index++) { // For
                    // each
                    // record
                    // in
                    // the
                    // block...
                    if (index > eof) // If we've reached the eof... break.
                        break bufferLoop;
                    
                    setFilePointer(index);
                    
                    queue.offer(HeapFile.readRecord(blockBuffer,
                        relativeFilePointer())); // Offer the record
                                                 // to the
                    // runs queue.
                }
            }
            
            /*
             * Records are now sorted for the run, write them out.
             * 
             * I have commented out the writing the runs as subfiles at the end of the disk as the
             * merge
             * phase doesn't work. Instead for now they just write the sorted runs back to the file.
             */
            
            bufferLoop:
            for (int buffer = 0; buffer < (n - 1); buffer++) {
                int block = run + buffer;
                
                // blockBuffer = new char[BLOCK_SIZE];
                
                for (int index = block * HeapFile.BLOCKING_FACTOR; index < ((1 + block) * HeapFile.BLOCKING_FACTOR); index++) {
                    if (index > eof)
                        break bufferLoop;
                    
                    setFilePointer(index);
                    
                    HeapFile.writeRecord(queue.poll(), getBlock(index).get(),
                        relativeFilePointer()); // Write back to the correct run / block positions.
                    
                    // writeRecord(queue.poll(), blockBuffer, relativeFilePointer());
                }
                
                /*
                 * Disk.Block diskBlock = new Disk.Block();
                 * diskBlock.set(blockBuffer);
                 * 
                 * disk.addBlock(diskBlock);
                 * 
                 * blockBuffer = null;
                 */
            }
            
            /*
             * if (blockBuffer != null) {
             * Disk.Block diskBlock = new Disk.Block();
             * diskBlock.set(blockBuffer);
             * 
             * disk.addBlock(diskBlock);
             * }
             */
        }
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
            HeapFile.BLOCK_SIZE, (char) Character.UNASSIGNED); // Pad any space after the number of
                                                               // blocks incase we didn't
                                                               // overwrite all digits, eg. going
                                                               // from
                                                               // 10 blocks to 9
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
        System.arraycopy(r.toCharArray(), 0, buffer, to, HeapFile.RECORD_SIZE);
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
    
    /**
     * An iterator with a peek() method.
     * 
     * @author Henry J. Wylde
     */
    private class PeekIterator implements Iterator<HeapFile.Record> {
        
        private char[] buffer;
        
        private int index;
        
        private int blockIndex;
        private int runSize;
        
        public PeekIterator(final int blockIndex, final int runSize) {
            buffer = disk.getBlock(blockIndex).get();
            
            this.blockIndex = blockIndex;
            this.runSize = runSize;
        }
        
        @Override
        public boolean hasNext() {
            if (index >= (runSize * HeapFile.BLOCKING_FACTOR))
                return false;
            
            return (buffer[HeapFile.RECORD_SIZE
                * (index % HeapFile.BLOCKING_FACTOR)] != Character.UNASSIGNED);
        }
        
        @Override
        public HeapFile.Record next() {
            if (!hasNext())
                throw new NoSuchElementException();
            
            HeapFile.Record next = HeapFile.readRecord(buffer,
                HeapFile.RECORD_SIZE * (index++ % HeapFile.BLOCKING_FACTOR));
            
            // Only have one block stored in this iterator at a time.
            if (((index % HeapFile.BLOCKING_FACTOR) == 0)
                && (index < (runSize * HeapFile.BLOCKING_FACTOR)))
                buffer = disk.getBlock(
                    blockIndex + (index / HeapFile.BLOCKING_FACTOR)).get();
            
            return next;
        }
        
        public HeapFile.Record peek() {
            if (!hasNext())
                throw new NoSuchElementException();
            
            return HeapFile.readRecord(buffer, HeapFile.RECORD_SIZE
                * (index % HeapFile.BLOCKING_FACTOR));
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    /**
     * Record sorted by id for the priority queue in the sort phase of the sort-merge algorithm.
     * 
     * @author Henry J. Wylde
     */
    private static final class RecordSorter implements Comparator<Record> {
        
        @Override
        public int compare(Record r1, Record r2) {
            return r1.id - r2.id;
        }
    }
}