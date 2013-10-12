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
public final class Author {
    
    private Integer id;
    
    private String firstName;
    private String lastName;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        
        Author author = (Author) obj;
        
        return Objects.equal(id, author.id);
    }
    
    /**
     * Gets the firstName.
     * 
     * @return the firstName.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Gets the id.
     * 
     * @return the id.
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Gets the lastName.
     * 
     * @return the lastName.
     */
    public String getLastName() {
        return lastName;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    
    /**
     * Sets the firstName.
     * 
     * @param firstName the new firstName.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Sets the id.
     * 
     * @param id the new id.
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Sets the lastName.
     * 
     * @param lastName the new lastName.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // return new Gson().toJson(this);
        
        return (lastName == null ? null : lastName.trim()) + ", " + (firstName == null ? null : firstName.trim())
            + " (id: " + id + ")";
    }
    
    public static Author fromResultSet(ResultSet rs) {
        Author author = new Author();
        
        try {
            author.setId(rs.getInt(Tables.Authors.ID));
            author.setFirstName(rs.getString(Tables.Authors.FIRST_NAME));
            author.setLastName(rs.getString(Tables.Authors.LAST_NAME));
        } catch (SQLException e) {
            // Should only happen if this method is called with an invalid result set
            e.printStackTrace();
            throw new InternalError(e.getMessage());
        }
        
        return author;
    }
}