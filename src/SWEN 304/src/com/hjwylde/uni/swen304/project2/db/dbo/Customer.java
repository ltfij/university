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
public final class Customer {
    
    private Integer id;
    
    private String firstName;
    private String lastName;
    
    private String city;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        
        Customer customer = (Customer) obj;
        
        return Objects.equal(id, customer.id);
    }
    
    /**
     * Gets the city.
     * 
     * @return the city.
     */
    public String getCity() {
        return city;
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    
    /**
     * Sets the city.
     * 
     * @param city the new city.
     */
    public void setCity(String city) {
        this.city = city;
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
            + " (id: " + id + ") (city: " + (city == null ? null : city.trim()) + ")";
    }
    
    public static Customer fromResultSet(ResultSet rs) {
        Customer customer = new Customer();
        
        try {
            customer.setId(rs.getInt(Tables.Customers.ID));
            customer.setFirstName(rs.getString(Tables.Customers.FIRST_NAME));
            customer.setLastName(rs.getString(Tables.Customers.LAST_NAME));
            customer.setCity(rs.getString(Tables.Customers.CITY));
        } catch (SQLException e) {
            // Should only happen if this method is called with an invalid result set
            e.printStackTrace();
            throw new InternalError(e.getMessage());
        }
        
        return customer;
    }
}