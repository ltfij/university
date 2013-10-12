package com.hjwylde.uni.swen304.project2.db.dbo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.base.Objects;
import com.hjwylde.uni.swen304.project2.db.util.Tables;

/**
 * TODO: Documentation
 * 
 * @author Henry J. Wylde
 * @version 1.0.0
 * 
 * @since 1.0.0, 12/10/2013
 */
public final class Book {
    
    private Integer isbn;
    
    private String title;
    private Integer editionNumber;
    
    private Integer numberOfCopies;
    private Integer numberLeft;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        
        Book book = (Book) obj;
        
        return Objects.equal(isbn, book.isbn);
    }
    
    /**
     * Gets the editionNumber.
     * 
     * @return the editionNumber.
     */
    public Integer getEditionNumber() {
        return editionNumber;
    }
    
    /**
     * Gets the isbn.
     * 
     * @return the isbn.
     */
    public Integer getIsbn() {
        return isbn;
    }
    
    /**
     * Gets the numberLeft.
     * 
     * @return the numberLeft.
     */
    public Integer getNumberLeft() {
        return numberLeft;
    }
    
    /**
     * Gets the numberOfCopies.
     * 
     * @return the numberOfCopies.
     */
    public Integer getNumberOfCopies() {
        return numberOfCopies;
    }
    
    /**
     * Gets the title.
     * 
     * @return the title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }
    
    /**
     * Sets the editionNumber.
     * 
     * @param editionNumber the new editionNumber.
     */
    public void setEditionNumber(Integer editionNumber) {
        this.editionNumber = editionNumber;
    }
    
    /**
     * Sets the isbn.
     * 
     * @param isbn the new isbn.
     */
    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }
    
    /**
     * Sets the numberLeft.
     * 
     * @param numberLeft the new numberLeft.
     */
    public void setNumberLeft(Integer numberLeft) {
        this.numberLeft = numberLeft;
    }
    
    /**
     * Sets the numberOfCopies.
     * 
     * @param numberOfCopies the new numberOfCopies.
     */
    public void setNumberOfCopies(Integer numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }
    
    /**
     * Sets the title.
     * 
     * @param title the new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // return new Gson().toJson(this);
        
        return title == null ? null : title.trim() + ", ed " + editionNumber + " (ISBN: " + isbn + ") (copies: "
            + numberOfCopies + ", left: " + numberLeft + ")";
    }
    
    public static Book fromResultSet(ResultSet rs) {
        Book book = new Book();
        
        try {
            book.setIsbn(rs.getInt(Tables.Books.ISBN));
            book.setTitle(rs.getString(Tables.Books.TITLE));
            book.setEditionNumber(rs.getInt(Tables.Books.EDITION_NUMBER));
            book.setNumberOfCopies(rs.getInt(Tables.Books.NUMBER_OF_COPIES));
            book.setNumberLeft(rs.getInt(Tables.Books.NUMBER_LEFT));
        } catch (SQLException e) {
            // Should only happen if this method is called with an invalid result set
            e.printStackTrace();
            throw new InternalError(e.getMessage());
        }
        
        return book;
    }
}
