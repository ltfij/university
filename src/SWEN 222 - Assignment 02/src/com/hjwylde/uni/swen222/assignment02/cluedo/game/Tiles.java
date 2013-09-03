package com.hjwylde.uni.swen222.assignment02.cluedo.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.hjwylde.uni.swen222.assignment02.cluedo.error.ParseException;

/**
 * Helper class to take care of loading a board of tiles from a file.
 * 
 * @author Henry J. Wylde
 * @author Moss Cantwell
 * 
 * @since 5/08/2013
 */
public final class Tiles {
    
    /**
     * This class cannot be instantiated.
     */
    private Tiles() {}
    
    /**
     * Attempts to read a board of tiles from the given input stream. This method expects the file
     * to be written in the UTF-8 character set.
     * 
     * @param in the input stream.
     * @return the board of tiles.
     * 
     * @throws IOException if the file cannot be read.
     */
    public static Tile[][] parseTiles(InputStream in) throws IOException {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            in))) {
            String line = null;
            while ((line = reader.readLine()) != null)
                lines.add(line);
        }
        
        return parseTiles(lines);
    }
    
    /**
     * Attempts to read a board of tiles from the given file path. This method expects the file to
     * be written in the UTF-8 character set.
     * 
     * @param path the file path.
     * @return the board of tiles.
     * 
     * @throws IOException if the file cannot be read.
     */
    public static Tile[][] parseTiles(Path path) throws IOException {
        return parseTiles(Files.readAllLines(path, StandardCharsets.UTF_8));
    }
    
    /**
     * Attempts to parse a character as a tile.
     * 
     * @param c the character.
     * @return the tile.
     * 
     * @throws ParseException if the character is not a valid tile character.
     */
    private static Tile parseTile(char c) throws ParseException {
        for (Tile tile : Tile.values())
            if (tile.getTile() == c)
                return tile;
        
        throw new ParseException("Unrecognised character: " + c);
    }
    
    /**
     * Attempts to parse a list of lines as a board of tiles.
     * 
     * @param lines the lines to parse.
     * @return the board of tiles.
     * 
     * @throws ParseException if the lines cannot be parsed.
     */
    private static Tile[][] parseTiles(List<String> lines)
        throws ParseException {
        if (lines.size() == 0)
            return new Tile[0][0];
        
        Tile[][] board = new Tile[lines.get(0).length()][lines.size()];
        
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            
            if (line.length() != board.length)
                throw new ParseException("Board is not a perfect rectangle");
            
            for (int x = 0; x < line.length(); x++)
                try {
                    board[x][y] = parseTile(line.charAt(x));
                } catch (ParseException e) {
                    throw new ParseException(e.getMessage() + " at line: "
                        + (y + 1) + " and column: " + (x + 1), e);
                }
        }
        
        return board;
    }
}