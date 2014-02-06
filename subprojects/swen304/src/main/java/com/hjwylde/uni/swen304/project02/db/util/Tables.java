package com.hjwylde.uni.swen304.project02.db.util;

/**
 * Information about the database tables and columns.
 * 
 * @author Henry J. Wylde
 * @version 1.0.0
 * 
 * @since 1.0.0, 12/10/2013
 */
@SuppressWarnings("javadoc")
public final class Tables {
    
    /**
     * This class cannot be instantiated.
     */
    private Tables() {}
    
    /**
     * Information about the columns in the author books table.
     * 
     * @author Henry J. Wylde
     * @version 1.0.0
     * 
     * @since 1.0.0, 12/10/2013
     */
    public static final class AuthorBooks {
        
        public static final String ISBN = "isbn";
        
        public static final String AUTHOR_ID = "authorid";
        
        public static final String AUTHOR_SEQUENCE_NUMBER = "authorseqno";
        
        /**
         * This class cannot be instantiated.
         */
        private AuthorBooks() {}
        
        /**
         * Gets the table name.
         * 
         * @return the table name.
         */
        public static String tableName() {
            return "book_author";
        }
    }
    
    /**
     * Information about the columns in the authors table.
     * 
     * @author Henry J. Wylde
     * @version 1.0.0
     * 
     * @since 1.0.0, 12/10/2013
     */
    public static final class Authors {
        
        public static final String ID = "authorid";
        
        public static final String FIRST_NAME = "name";
        public static final String LAST_NAME = "surname";
        
        /**
         * This class cannot be instantiated.
         */
        private Authors() {}
        
        /**
         * Gets the table name.
         * 
         * @return the table name.
         */
        public static String tableName() {
            return "author";
        }
    }
    
    /**
     * Information about the columns in the books table.
     * 
     * @author Henry J. Wylde
     * @version 1.0.0
     * 
     * @since 1.0.0, 12/10/2013
     */
    public static final class Books {
        
        public static final String ISBN = "isbn";
        
        public static final String TITLE = "title";
        public static final String EDITION_NUMBER = "edition_no";
        public static final String NUMBER_OF_COPIES = "numofcop";
        public static final String NUMBER_LEFT = "numleft";
        
        /**
         * This class cannot be instantiated.
         */
        private Books() {}
        
        /**
         * Gets the table name.
         * 
         * @return the table name.
         */
        public static String tableName() {
            return "book";
        }
    }
    
    /**
     * Information about the columns in the customer books table.
     * 
     * @author Henry J. Wylde
     * @version 1.0.0
     * 
     * @since 1.0.0, 12/10/2013
     */
    public static final class CustomerBooks {
        
        public static final String ISBN = "isbn";
        
        public static final String CUSTOMER_ID = "customerid";
        public static final String DUE_DATE = "duedate";
        
        /**
         * This class cannot be instantiated.
         */
        private CustomerBooks() {}
        
        /**
         * Gets the table name.
         * 
         * @return the table name.
         */
        public static String tableName() {
            return "cust_book";
        }
    }
    
    /**
     * Information about the columns in the customer table.
     * 
     * @author Henry J. Wylde
     * @version 1.0.0
     * 
     * @since 1.0.0, 12/10/2013
     */
    public static final class Customers {
        
        public static final String ID = "customerid";
        
        public static final String FIRST_NAME = "f_name";
        public static final String LAST_NAME = "l_name";
        
        public static final String CITY = "city";
        
        /**
         * This class cannot be instantiated.
         */
        private Customers() {}
        
        /**
         * Gets the table name.
         * 
         * @return the table name.
         */
        public static String tableName() {
            return "customer";
        }
    }
}