package com.hjwylde.uni.swen304.project2.ui;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.postgresql.util.PSQLException;

import com.google.common.base.Joiner;
import com.hjwylde.uni.swen304.project2.db.dbo.Author;
import com.hjwylde.uni.swen304.project2.db.dbo.Book;
import com.hjwylde.uni.swen304.project2.db.dbo.Customer;
import com.hjwylde.uni.swen304.project2.db.util.*;
import com.hjwylde.uni.swen304.project2.db.util.Tables.AuthorBooks;
import com.hjwylde.uni.swen304.project2.db.util.Tables.Authors;
import com.hjwylde.uni.swen304.project2.db.util.Tables.Books;
import com.hjwylde.uni.swen304.project2.db.util.Tables.CustomerBooks;
import com.hjwylde.uni.swen304.project2.db.util.Tables.Customers;

/**
 * Model for the library database.
 * 
 * @author Henry J. Wylde
 * @version 1.0.0
 * 
 * @since 1.0.0, 12/10/2013
 */
public final class LibraryModel {
    
    // private static final String URI = "jdbc:postgresql://192.168.1.57:5432/";
    private static final String URI = "jdbc:postgresql://db.ecs.vuw.ac.nz/";
    
    // For use in creating dialogs and making them modal
    private final JFrame dialogParent;
    
    private final Connection con;
    
    public LibraryModel(JFrame parent, String userid, String password) {
        dialogParent = parent;
        
        con = getConnection(userid, password);
    }
    
    public synchronized String bookLookup(int isbn) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(Books.tableName());
        sb.append(" NATURAL JOIN ");
        sb.append(AuthorBooks.tableName());
        sb.append(" NATURAL JOIN ");
        sb.append(Authors.tableName());
        sb.append(" WHERE ");
        sb.append(Books.ISBN).append("=?");
        sb.append(" ORDER BY ");
        sb.append(AuthorBooks.AUTHOR_SEQUENCE_NUMBER);
        sb.append(";");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
            
            stmt.setInt(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return "No book found with ISBN: " + isbn;
                
                // Generate a list of book -> authors
                // This list will already be ordered by sequence number due to the query
                Map<Book, List<Author>> books = new HashMap<>();
                
                do {
                    Book book = Book.fromResultSet(rs);
                    Author author = Author.fromResultSet(rs);
                    
                    if (!books.containsKey(book))
                        books.put(book, new LinkedList<Author>());
                    
                    books.get(book).add(author);
                } while (rs.next());
                
                // Print it out nicely!
                return PrettyPrinter.print(books);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public synchronized String borrowBook(int isbn, int customerID, int day, int month, int year) {
        try {
            con.setAutoCommit(false);
            
            // Check if the customer exists
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(Customers.tableName());
            sb.append(" WHERE ");
            sb.append(Customers.ID).append("=?");
            sb.append(" FOR SHARE;");
            
            try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
                
                stmt.setInt(1, customerID);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return "No customer found with id: " + customerID;
                    }
                }
            }
            
            // Check if the book exists
            sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(Books.tableName());
            sb.append(" WHERE ");
            sb.append(Books.ISBN).append("=?");
            sb.append(" FOR UPDATE;");
            
            Book book = null;
            try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
                
                stmt.setInt(1, isbn);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return "No book found with ISBN: " + isbn;
                    }
                    
                    // Check that at least one copy exists to get out on loan
                    book = Book.fromResultSet(rs);
                    if (book.getNumberLeft() == 0) {
                        con.rollback();
                        return "No copies are left of the book with ISBN: " + isbn;
                    }
                }
            }
            
            // Insert a loan record into customer books
            sb = new StringBuilder();
            sb.append("INSERT INTO ");
            sb.append(CustomerBooks.tableName());
            sb.append("(").append(CustomerBooks.ISBN);
            sb.append(",").append(CustomerBooks.CUSTOMER_ID);
            sb.append(",").append(CustomerBooks.DUE_DATE);
            sb.append(") VALUES (?,?,?);");
            
            try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
                
                stmt.setInt(1, isbn);
                stmt.setInt(2, customerID);
                stmt.setDate(3, new Date(year, month, day));
                
                stmt.executeUpdate();
            } catch (PSQLException e) {
                con.rollback();
                return "Customer with id: " + customerID + " is already borrowing book with ISBN: " + isbn;
            }
            
            // Show the dialog to pause execution
            int choice = JOptionPane.showConfirmDialog(dialogParent,
                "Borrow transaction paused... Would you like to continue?", "Borrowing Paused",
                JOptionPane.YES_NO_OPTION);
            switch (choice) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
            default:
                con.rollback();
                return "Borrowing cancelled by user";
            }
            
            // Update the book table
            sb = new StringBuilder();
            sb.append("UPDATE ");
            sb.append(Books.tableName());
            sb.append(" SET ");
            sb.append(Books.NUMBER_LEFT).append("=?");
            sb.append(" WHERE ");
            sb.append(Books.ISBN).append("=?;");
            
            try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
                
                stmt.setInt(1, book.getNumberLeft() - 1);
                stmt.setInt(2, isbn);
                
                if (stmt.executeUpdate() == 0) {
                    con.rollback();
                    return "Failed to udpate book in database";
                }
            }
            
            con.commit();
            
            return "Book with ISBN: " + isbn + " successfully taken out on loan by customer with id: " + customerID;
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized void closeDBConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized String deleteAuthor(int authorID) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(Authors.tableName());
        sb.append(" WHERE ");
        sb.append(Authors.ID).append("=?;");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
            
            stmt.setInt(1, authorID);
            
            try {
                stmt.executeUpdate();
                
                return stmt.getUpdateCount() + " authors deleted with id: " + authorID;
            } catch (PSQLException e) {
                return "Unable to delete (due to constraints) author with id: " + authorID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public synchronized String deleteBook(int isbn) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(Books.tableName());
        sb.append(" WHERE ");
        sb.append(Books.ISBN).append("=?;");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
            
            stmt.setInt(1, isbn);
            
            try {
                stmt.execute();
                
                return stmt.getUpdateCount() + " books deleted with isbn: " + isbn;
            } catch (PSQLException e) {
                return "Unable to delete (due to constraints) book with ISBN: " + isbn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public synchronized String deleteCus(int customerID) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(Customers.tableName());
        sb.append(" WHERE ");
        sb.append(Customers.ID).append("=?;");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
            
            stmt.setInt(1, customerID);
            
            try {
                stmt.execute();
                
                return stmt.getUpdateCount() + " customers deleted with id: " + customerID;
            } catch (PSQLException e) {
                return "Unable to delete (due to constraints) customer with id: " + customerID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public Connection getConnection(String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            
            Connection con = DriverManager.getConnection(URI + username + "_jdbc", username, password);
            
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new InternalError(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }
    
    public synchronized String returnBook(int isbn, int customerid) {
        try {
            con.setAutoCommit(false);
            
            // Check if the customer exists
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(Customers.tableName());
            sb.append(" WHERE ");
            sb.append(Customers.ID).append("=?");
            sb.append(" FOR SHARE;");
            
            try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
                
                stmt.setInt(1, customerid);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return "No customer found with id: " + customerid;
                    }
                }
            }
            
            // Check if the book exists
            sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(Books.tableName());
            sb.append(" WHERE ");
            sb.append(Books.ISBN).append("=?");
            sb.append(" FOR UPDATE;");
            
            Book book = null;
            try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
                
                stmt.setInt(1, isbn);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return "No book found with ISBN: " + isbn;
                    }
                    
                    // Check that at least one copy is out on loan
                    book = Book.fromResultSet(rs);
                    if (book.getNumberLeft() == book.getNumberOfCopies()) {
                        con.rollback();
                        return "Book with ISBN: " + isbn + " is not currently out on loan";
                    }
                }
            }
            
            // Delete the loan record from customer books
            sb = new StringBuilder();
            sb.append("DELETE FROM ");
            sb.append(CustomerBooks.tableName());
            sb.append(" WHERE ");
            sb.append(CustomerBooks.ISBN).append("=? AND ");
            sb.append(CustomerBooks.CUSTOMER_ID).append("=?;");
            
            try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
                
                stmt.setInt(1, isbn);
                stmt.setInt(2, customerid);
                
                if (stmt.executeUpdate() == 0) {
                    con.rollback();
                    return "Customer with id: " + customerid + " was not borrowing book with ISBN: " + isbn;
                }
            }
            
            // Show the dialog to pause execution
            int choice = JOptionPane.showConfirmDialog(dialogParent,
                "Return transaction paused... Would you like to continue?", "Returning Paused",
                JOptionPane.YES_NO_OPTION);
            switch (choice) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
            default:
                con.rollback();
                return "Returning cancelled by user";
            }
            
            // Update the book table
            sb = new StringBuilder();
            sb.append("UPDATE ");
            sb.append(Books.tableName());
            sb.append(" SET ");
            sb.append(Books.NUMBER_LEFT).append("=?");
            sb.append(" WHERE ");
            sb.append(Books.ISBN).append("=?;");
            
            try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
                
                stmt.setInt(1, book.getNumberLeft() + 1);
                stmt.setInt(2, isbn);
                
                if (stmt.executeUpdate() == 0) {
                    con.rollback();
                    return "Failed to udpate book in database";
                }
            }
            
            con.commit();
            
            return "Book with ISBN: " + isbn + " successfully returned by customer with id: " + customerid;
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized String showAllAuthors() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(Authors.tableName());
        sb.append(" ORDER BY ");
        sb.append(Authors.ID);
        sb.append(";");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString()); ResultSet rs = stmt.executeQuery()) {
            
            if (!rs.next())
                return "No authors found";
            
            List<Author> authors = new ArrayList<>();
            do
                authors.add(Author.fromResultSet(rs));
            while (rs.next());
            
            return Joiner.on('\n').join(authors);
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public String showAllCustomers() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(Customers.tableName());
        sb.append(" ORDER BY ");
        sb.append(Customers.ID);
        sb.append(";");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString()); ResultSet rs = stmt.executeQuery()) {
            
            if (!rs.next())
                return "No customers found";
            
            List<Customer> customers = new ArrayList<>();
            do
                customers.add(Customer.fromResultSet(rs));
            while (rs.next());
            
            return Joiner.on('\n').join(customers);
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public synchronized String showAuthor(int authorID) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(Authors.tableName());
        sb.append(" WHERE ");
        sb.append(Authors.ID).append("=?;");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
            
            stmt.setInt(1, authorID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return "No author found with id: " + authorID;
                
                return Author.fromResultSet(rs).toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public synchronized String showCatalogue() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(Books.tableName());
        sb.append(" ORDER BY ");
        sb.append(Books.ISBN);
        sb.append(";");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString()); ResultSet rs = stmt.executeQuery()) {
            
            if (!rs.next())
                return "No books found";
            
            List<Book> books = new ArrayList<>();
            do
                books.add(Book.fromResultSet(rs));
            while (rs.next());
            
            return Joiner.on('\n').join(books);
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public synchronized String showCustomer(int customerID) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(Customers.tableName());
        sb.append(" WHERE ");
        sb.append(Customers.ID).append("=?;");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString())) {
            
            stmt.setInt(1, customerID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return "No customer found with id: " + customerID;
                
                return Customer.fromResultSet(rs).toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
    
    public synchronized String showLoanedBooks() {
        // VERIFY: What is show loaned books meant to show?
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(Books.tableName());
        sb.append(" NATURAL JOIN ");
        sb.append(CustomerBooks.tableName());
        sb.append(" NATURAL JOIN ");
        sb.append(Customers.tableName());
        sb.append(" WHERE ");
        sb.append(Books.NUMBER_LEFT).append("<");
        sb.append(Books.NUMBER_OF_COPIES);
        sb.append(" ORDER BY ");
        sb.append(Books.ISBN).append(",");
        sb.append(CustomerBooks.DUE_DATE);
        sb.append(";");
        
        try (PreparedStatement stmt = con.prepareStatement(sb.toString()); ResultSet rs = stmt.executeQuery()) {
            
            if (!rs.next())
                return "No books currently on loan";
            
            // Generate a list of book -> customers
            // This list will be ordered by due date
            Map<Book, List<Customer>> books = new LinkedHashMap<>();
            
            do {
                Book book = Book.fromResultSet(rs);
                Customer customer = Customer.fromResultSet(rs);
                
                if (!books.containsKey(book))
                    books.put(book, new LinkedList<Customer>());
                
                books.get(book).add(customer);
            } while (rs.next());
            
            // Print it out nicely!
            return PrettyPrinter.print(books);
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(dialogParent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "Error: " + e.getMessage();
        }
    }
}