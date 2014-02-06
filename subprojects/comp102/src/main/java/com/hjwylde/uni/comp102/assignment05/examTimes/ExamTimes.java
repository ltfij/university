package com.hjwylde.uni.comp102.assignment05.examTimes;

/*
 * Code for Assignment 5, COMP 102
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Extracts and prints information from a file of exam timetable data. The
 * timetable file is called "examdata.txt" Each line of the file specifies an
 * exam room for a course. The format of the lines is the following: The first
 * token is the course code The second token is the exam room The third token is
 * the date (day of the month). The fourth token is the time of the exam: am or
 * pm (morning or afternoon). The fifth token is the number of students in the
 * room. The last two tokens give the range of names of students in that room.
 * Where a course is too big to fit all the students in one room, there will be
 * several lines for the course, one for each room. The date and time of the
 * exam will be the same on each line.
 */

public class ExamTimes {
    
    ArrayList<Course> courseList = new ArrayList<>();
    
    /**
     * Repeatedly asks the user for a command (one letter) and calls the
     * appropriate method
     */
    public void commandLoop() {
        try (Scanner sc = new Scanner(System.in)) {
            printHelp();
            
            while (true) {
                System.out.print("Command: ");
                String cmd = sc.next().substring(0, 1)
                    .toLowerCase(Locale.ENGLISH);
                
                if (cmd.equals("c"))
                    printCourse(sc.next());
                else if (cmd.equals("s"))
                    printSession(sc.nextInt());
                else if (cmd.equals("p"))
                    printPossibleExams(sc.nextInt(), sc.next());
                else if (cmd.equals("l"))
                    dailyLists();
                else if (cmd.equals("h"))
                    printHelp();
                else if (cmd.equals("q"))
                    return;
                else {
                    System.out.println("Command '" + cmd + "' not recognised");
                    printHelp();
                }
            }
        }
    }
    
    /**
     * (Extension - harder) dailyLists writes a file listing, for each time slot,
     * all the course codes with an exam in that slot, along with the number of
     * students in the course, and also the total number of students expected in
     * each time slot. The name of the file should be "exams-by-session.txt". It
     * may assume that the dates (which are all in the same month) go from 1 to
     * 22. It may also assume that the examdata.txt file is sorted by course code.
     */
    public void dailyLists() {
        // YOUR CODE HERE
    }
    
    /**
     * Asks the user for a course code, and then reads the timetable file,
     * printing out all the lines that involve that course (ie, where the first
     * token on the line matches the course)
     */
    public void printCourse(String code) {
        System.out.printf("Course %s:\n", code);
        
        // Cycles through each course, listing ones that are the same as 'code'.
        // Prints out all data for each course.
        for (Course course : courseList)
            if (course.getCourseCode().equalsIgnoreCase(code))
                System.out.println(course.getExamRoom() + " "
                    + course.getDate() + " " + course.getTime() + " "
                    + course.getNumStudents() + " " + course.getBeginRange()
                    + " " + course.getEndRange());
    }
    
    /** Prints a list of the possible commands */
    public void printHelp() {
        System.out.println("Commands are:");
        System.out.println("\tC for print course");
        System.out.println("\tS for print session");
        System.out.println("\tP for possible exams");
        System.out.println("\tL for daily lists");
        System.out.println("\tH for this help");
        System.out.println("\tQ for quit");
    }
    
    /**
     * Asks the user for a date and student's family name. Then reads the
     * timetable file and prints out (to System.out) all the exams (course and
     * room) that the student could possibly be in at that time. (taking into
     * account the range of names in the rooms).
     */
    public void printPossibleExams(int date, String surname) {
        System.out.printf("Possible courses for student %s on day %d:\n",
            surname, date);
        for (Course course : courseList)
            if ((course.getDate() == date)
                && (surname.compareToIgnoreCase(course.getBeginRange()) > 0)
                && (surname.compareToIgnoreCase(course.getEndRange()) < 0))
                System.out.println(course.getCourseCode() + " "
                    + course.getExamRoom());
        
        System.out.println();
    }
    
    /**
     * Asks the user for a date and then reads the timetable file, printing out
     * (to the terminal window) out all the course codes, rooms, and name ranges
     * for exams on that date, morning first, then afternoon. It will be best to
     * read the seven tokens on each line individually.
     */
    public void printSession(int date) {
        System.out.printf("Courses on day %d:\n", date);
        
        StringBuilder amCourses = new StringBuilder("A.M Courses: \n");
        StringBuilder pmCourses = new StringBuilder("P.M. Courses: \n");
        
        // Cycles through each course, sorting through the courses on 'date' in to
        // A.M. courses and P.M courses.
        // Prints out the course code, exam room, begin range and end range for each
        // course.
        for (Course course : courseList)
            if ((course.getDate() == date)
                && course.getTime().equalsIgnoreCase("am"))
                amCourses.append(course.getCourseCode() + " "
                    + course.getExamRoom() + " " + course.getBeginRange() + " "
                    + course.getEndRange() + "\n");
            else if (course.getDate() == date)
                pmCourses.append(course.getCourseCode() + " "
                    + course.getExamRoom() + " " + course.getBeginRange() + " "
                    + course.getEndRange() + "\n");
        
        System.out.println(amCourses.toString() + pmCourses.toString());
    }
    
    /**
     * Reads the <code>file</code> and loads each line as a <code>Course</code>.
     * It then adds each <Code>Course</code> into an array list.
     * 
     * @param file
     *            the file to read.
     * @see Course
     */
    private void readFile(File file) {
        if (!file.exists()) {
            System.out.println("Error: File \"" + file.getPath()
                + "\" does not exist.");
            return;
        }
        
        // Read the file...
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringTokenizer tokens;
            
            // Creates a Course object for each course, and adds to an array of
            // courses.
            while ((line = reader.readLine()) != null) {
                tokens = new StringTokenizer(line);
                courseList.add(new Course(tokens.nextToken(), tokens
                    .nextToken(), Integer.parseInt(tokens.nextToken()), tokens
                    .nextToken(), Integer.parseInt(tokens.nextToken()), tokens
                    .nextToken(), tokens.nextToken()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a <code>File</code> from <code>filePath</code> then calls <code>readFile</code> with
     * the new file as a parameter.
     * 
     * @param filePath
     *            the file path.
     * @see #readFile(File)
     */
    private void readFile(String filePath) {
        readFile(new File(filePath));
    }
    
    // Main
    public static void main(String[] args) {
        ExamTimes et = new ExamTimes();
        et.readFile("G:\\Files\\Programming\\Java\\Comp 102 Files\\BlueJ\\Assignment05\\ExamTimes\\ExamData.txt");
        et.commandLoop();
    }
    
    /**
     * The <code>Course</code> class is a representation of an exam course for a
     * university. It holds the various variables that are applicable to any one
     * course.
     * 
     * @author Henry J. Wylde
     */
    private class Course {
        
        private final String courseCode;
        private final String examRoom;
        private final int date;
        private final String time;
        private final int numStudents;
        private final String beginRange;
        private final String endRange;
        
        /**
         * Creates a new <code>Course</code> with the specified parameters.
         * 
         * @param courseCode
         *            the course code.
         * @param examRoom
         *            the exam room for this course.
         * @param date
         *            the day of the month this course is on.
         * @param time
         *            the time that this course is on, am or pm.
         * @param numStudents
         *            the number of students for this course.
         * @param beginRange
         *            the beginning range of letters for a students surname.
         * @param endRange
         *            the end range of letters for a students surname.
         */
        public Course(String courseCode, String examRoom, int date,
            String time, int numStudents, String beginRange, String endRange) {
            this.courseCode = courseCode;
            this.examRoom = examRoom;
            this.date = date;
            this.time = time;
            this.numStudents = numStudents;
            this.beginRange = beginRange;
            this.endRange = endRange;
        }
        
        /**
         * Gets this Course's beginning range of letters for students surnames.
         * 
         * @return the beginning range of letters.
         */
        public String getBeginRange() {
            return beginRange;
        }
        
        /**
         * Gets this Course's code.
         * 
         * @return the course code.
         */
        public String getCourseCode() {
            return courseCode;
        }
        
        /**
         * Gets this Course's date.
         * 
         * @return the date in terms of day of month.
         */
        public int getDate() {
            return date;
        }
        
        /**
         * Gets this Course's end range of letters for students surnames.
         * 
         * @return the end range of letters.
         */
        public String getEndRange() {
            return endRange;
        }
        
        /**
         * Gets this Course's exam room.
         * 
         * @return the exam room.
         */
        public String getExamRoom() {
            return examRoom;
        }
        
        /**
         * Gets this Course's number of students.
         * 
         * @return the number of students.
         */
        public int getNumStudents() {
            return numStudents;
        }
        
        /**
         * Gets this Course's time.
         * 
         * @return the time: either am or pm.
         */
        public String getTime() {
            return time;
        }
    }
    
}
