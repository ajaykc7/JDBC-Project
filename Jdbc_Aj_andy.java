package jdbc_aj_andy;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Mimi Opkins with some tweaking from Dave Brown Revised By Andy Sien
 * and Ajay KC
 */
public class Jdbc_Aj_andy {

    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
    //This is the specification for the printout that I'm doing:
    //each % denotes the start of a new field.
    //The - denotes left justification.
    //The number indicates how wide to make the field.
    //The "s" denotes that it's a string.  All of our output in this test are
    //strings, but that won't always be the case.
    static final String displayFormat = "%-5s%-15s%-15s%-15s\n";
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
//            + "testdb;user=";

    public static void main(String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
        Scanner in = new Scanner(System.in);
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        //        System.out.print("Database user name: ");
        //        USER = in.nextLine();
        //        System.out.print("Database password: ");
        //        PASS = in.nextLine();
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            //create main menu
            String menu = ("Enter a # choice: \n1) List all writing groups\n2) List specific group data\n3) List publishers"
                    + "\n4) List specific publisher data\n5) list all book titles\n6) list specific book data\n"
                    + "7) Insert new book \n8) insert new publisher and update\n10) Delete a specific book\n9) Exit");
            System.out.println();
            Scanner input = new Scanner(System.in);
            boolean cont = true;
            while (cont == true) {
                conn = DriverManager.getConnection(DB_URL);
                System.out.println();
                System.out.println(menu);
                try {
                    int choice = input.nextInt();

                    switch (choice) {
                        case 1: {//list all writing groups
                            System.out.println("Creating statement...");
                            stmt = conn.createStatement();

                            ResultSet rs = stmt.executeQuery("Select groupName from Writinggroup");
                            System.out.println("----All Writing Groups----");

                            //STEP 5: Extract data from result set
                            while (rs.next()) {
                                String groupName = rs.getString("groupName");
                                System.out.println(groupName);
                            }
                            System.out.println("-------------------------");
                        }//end case1
                        break;
                        case 2: {//list all data for a specific group
                            System.out.println("Enter the writing Group: ");
                            String Group = in.nextLine().toUpperCase();
                            System.out.println("Creating statement...");
                            String sql = "Select * from Writinggroup where groupName = ?";
                            PreparedStatement pstmt = null;
                            pstmt = conn.prepareStatement(sql);
                            pstmt.clearParameters();
                            pstmt.setString(1, Group);//map value to user input
                            ResultSet rs = pstmt.executeQuery();

                            int count = 0; // variable to count the number of rows in the assigned query.
                            while (rs.next()) {
                                count++;
                                String gName = rs.getString("groupName");
                                String hWriter = rs.getString("headWriter");
                                String yForm = rs.getString("yearFormed");
                                String subject = rs.getString("subject");
                                System.out.println("Group Name: " + gName + "\n" + "Head Writer: " + hWriter + "\n" + "Year Formed: " + yForm + "\n" + "Subject: " + subject + "\n");
                            }
                            if (count == 0) {
                                System.out.println("\nThere is no group with that name.");
                            }
                        }
                        break;
                        case 3: {//List all publishers
                            System.out.println("Creating statement...");
                            stmt = conn.createStatement();

                            ResultSet rs = stmt.executeQuery("Select publisherName from Publisher");
                            System.out.println("-------All Publishers -------");

                            //STEP 5: Extract data from result set
                            while (rs.next()) {
                                String pName = rs.getString("publisherName");
                                System.out.println(pName);
                            }
                            System.out.println("-------------------------");

                        }
                        break;
                        case 4: {//List all the data for a pubisher specified by the user
                            System.out.println("Enter the Publisher name: ");
                            String Pub = in.nextLine().toUpperCase();

                            System.out.println("Creating statement...");
                            //stmt = conn.createStatement();
                            String sql = "Select * from Publisher where publisherName = ?";

                            PreparedStatement pstmt = null;

                            pstmt = conn.prepareStatement(sql);
                            pstmt.clearParameters();
                            pstmt.setString(1, Pub);//map value to user input
                            ResultSet rs = pstmt.executeQuery();

                            int count = 0; // variable to count the number of rows in the assigned query.
                            while (rs.next()) {
                                count++;
                                String pName = rs.getString("publisherName");
                                String pAdd = rs.getString("publisherAddress");
                                String phone = rs.getString("publisherPhone");
                                String email = rs.getString("publisherEmail");
                                System.out.println("Publisher Name: " + pName + "\n" + "Address: " + pAdd + "\n" + "Phone: " + phone + "\n" + "Email: " + email + "\n");
                            }
                            if (count == 0) {
                                System.out.println("\nThere is no group with that name.");
                            }
                        }

                        break;
                        case 5: {//List all book titles
                            stmt = conn.createStatement();

                            ResultSet rs = stmt.executeQuery("Select bookTitle from Book");
                            System.out.println("----All Book Titles----");

                            //STEP 5: Extract data from result set
                            while (rs.next()) {
                                String title = rs.getString("bookTitle");
                                System.out.println(title);
                            }
                            System.out.println("-------------------------");

                        }
                        break;
                        case 6: {//List all the data for a book specified by the user
                            System.out.println("Enter the Book Title: ");
                            String bookN = in.nextLine().toUpperCase();
                            System.out.println("Creating statement...");
                            String sql = "SELECT * FROM Book NATURAL JOIN WritingGroup NATURAL JOIN Publisher WHERE bookTitle = ?";

                            PreparedStatement pstmt = null;

                            pstmt = conn.prepareStatement(sql);
                            pstmt.clearParameters();
                            pstmt.setString(1, bookN);//map value to user input (booktitle=2)
                            ResultSet rs = pstmt.executeQuery();

                            int count = 0; // variable to count the number of rows in the assigned query.
                            while (rs.next()) {
                                count++;
                                String gName = rs.getString("groupName");
                                String btitle = rs.getString("bookTitle");
                                String pName = rs.getString("publisherName");
                                String year = rs.getString("yearPublished");
                                String page = rs.getString("numberPages");
                                System.out.println("----Book Info----");
                                System.out.println("\nBook Title: " + btitle + "\n" + "Year: " + year + "\n" + "Pages: " + page + "\n");
                                //publisher info
                                System.out.println("----Publisher Info----");
                                String pAdd = rs.getString("publisherAddress");
                                String phone = rs.getString("publisherPhone");
                                String email = rs.getString("publisherEmail");
                                System.out.println("Publisher Name: " + pName + "\n" + "Address: " + pAdd + "\n" + "Phone: " + phone + "\n" + "Email: " + email + "\n");

                                //wriring group info
                                System.out.println("----Writing Group Info----");
                                String hWriter = rs.getString("headWriter");
                                String yForm = rs.getString("yearFormed");
                                String subject = rs.getString("subject");
                                System.out.println("Group Name: " + gName + "\n" + "Head Writer: " + hWriter + "\n" + "Year Formed: " + yForm + "\n" + "Subject: " + subject + "\n");
                            }
                            if (count == 0) {
                                System.out.println("\nThere is no group with that name.");
                            }
                        }

                        break;
                        case 7: {//Insert a  new book
                            boolean isBookValid = false;
                            while (!isBookValid) {
                                try {
                                    try {
                                        System.out.println("\nEnter NEW Book Title: ");
                                        String bookN = in.nextLine().toUpperCase();
                                        System.out.println("Enter Writing Group Name: ");
                                        String gName = in.nextLine().toUpperCase();
                                        System.out.println("Enter Publisher Name: ");
                                        String pName = in.nextLine().toUpperCase();
                                        System.out.println("Enter Year Published: ");
                                        String yPub = in.nextLine();
                                        System.out.println("Enter Number of Pages: ");
                                        int pNum = Integer.parseInt(in.nextLine());

                                        //check if the publisher exist or not.
                                        String publisher = "";
                                        boolean publisherPresent = false;
                                        stmt = conn.createStatement();
                                        ResultSet rs = stmt.executeQuery("Select publisherName from Publisher");
                                        while (rs.next()) {
                                            publisher = rs.getString("publisherName");
                                            if (publisher.equals(pName)) {
                                                publisherPresent = true;
                                            }
                                        }

                                        //check if the group exist or not
                                        String group = "";
                                        boolean groupPresent = false;
                                        stmt = conn.createStatement();
                                        rs = stmt.executeQuery("Select groupName from WritingGroup");
                                        while (rs.next()) {
                                            group = rs.getString("groupName");
                                            if (group.equals(gName)) {
                                                groupPresent = true;
                                            }
                                        }

                                        if ((publisherPresent) && (groupPresent)) {
                                            isBookValid = true;
                                            PreparedStatement pstmt = null;
                                            String sql = "INSERT INTO Book values (?,?,?,?,?)";
                                            pstmt = conn.prepareStatement(sql);
                                            pstmt.clearParameters();
                                            pstmt.setString(1, bookN);//map value to group name
                                            pstmt.setString(2, yPub);//map value to pub year
                                            pstmt.setInt(3, pNum);//map value to page num
                                            pstmt.setString(4, gName);//map value to group name
                                            pstmt.setString(5, pName);//map value to pub name
                                            pstmt.executeUpdate();
                                            System.out.println("Thank you!");
                                        } else if ((!publisherPresent) && (!groupPresent)) {
                                            System.out.println("Both the group and publisher does not exist");
                                        } else if (!groupPresent) {
                                            System.out.println("The group does not exist.");
                                        } else {
                                            System.out.println("The publisher does not exist");
                                        }
                                    } catch (NumberFormatException nfe) {
                                        System.out.println("Please enter the correct page number");
                                    }
                                } catch (java.sql.SQLIntegrityConstraintViolationException sqle) {
                                    System.out.println("The book with the group already exist. Please put the correct value");
                                }

                            }
                        }
                        break;
                        case 8: {//Insert a new publisher and update all book published by one publisher to be published by the new pubisher.
                            boolean isPublicationValid = false;
                            while (!isPublicationValid) {
                                try {
                                    System.out.println("\nEnter NEW Publisher Name: ");
                                    String pName = in.nextLine().toUpperCase();
                                    System.out.println("Enter Publisher address ");
                                    String pAdd = in.nextLine().toUpperCase();
                                    System.out.println("Enter Publisher Phone: ");
                                    String phone = in.nextLine();
                                    System.out.println("Enter Publisher Email: ");
                                    String email = in.nextLine().toUpperCase();

                                    PreparedStatement pstmt = null;
                                    String sql = "INSERT INTO Publisher values (?,?,?,?)";
                                    pstmt = conn.prepareStatement(sql);

                                    pstmt.clearParameters();
                                    pstmt.setString(1, pName);//map value to publisher name
                                    pstmt.setString(2, pAdd);//map value to address
                                    pstmt.setString(3, phone);//map value to phone
                                    pstmt.setString(4, email);//map value to pub email
                                    pstmt.executeUpdate();//execute prepared statements

                                   // System.out.println("Enter Publisher to replace: ");
                                   // String oPub = in.nextLine();
                                    
                                   boolean isBookValid = false;
 
                                    while (!isBookValid) {
                                        System.out.println("Enter name of publisher to update: ");
                                        String oPub = in.nextLine().toUpperCase();
                                        String sql6 = "Select * from Publisher where publisherName = ?";
                                        PreparedStatement pstmt6 = null;
                                        pstmt6 = conn.prepareStatement(sql6);
                                        pstmt6.clearParameters();
                                        pstmt6.setString(1, oPub);//map value to user input
                                        ResultSet rs = pstmt6.executeQuery();

                                        int count = 0; // variable to count the number of rows in the assigned query.
                                        while (rs.next()) {
                                            count++;
                                        }
                                        if (count == 0) {
                                            System.out.println("\nThere is no publisher with that name.");
                                        } else {
                                            System.out.println("Here");
                                            String sql2 = "Update Book Set publisherName = ? where publisherName = ?";
                                            PreparedStatement pstmt2 = null;
                                            pstmt2 = conn.prepareStatement(sql2);
                                            pstmt2.setString(1, pName);//map value to publisher name
                                            pstmt2.setString(2, oPub);//map value to publisher name


                                            pstmt2.executeUpdate();//execute prepared statements
                                             isBookValid = true;

                                        }
                                     }
                                            isPublicationValid = true;


                                } catch (java.sql.SQLIntegrityConstraintViolationException sqle) {
                                    System.out.println("The publisher already exist. Please try again");
                                }
                            }
                        }
                        break;

                        case 9: {//Remove a book specified by the user
                            boolean isBookValid = false;
                            while (!isBookValid) {
                                System.out.println("Enter name of book to delete: ");
                                String bName = in.nextLine().toUpperCase();
                                String sql = "Select * from Book where bookTitle = ?";
                                PreparedStatement pstmt = null;
                                pstmt = conn.prepareStatement(sql);
                                pstmt.clearParameters();
                                pstmt.setString(1, bName);//map value to user input
                                ResultSet rs = pstmt.executeQuery();

                                int count = 0; // variable to count the number of rows in the assigned query.
                                while (rs.next()) {
                                    count++;
                                }
                                if (count == 0) {
                                    System.out.println("\nThere is no book with that name.");
                                } else {
                                    System.out.println("The book has been deleted.");
                                    sql = "Delete from Book where bookTitle= ?";
                                    pstmt = conn.prepareStatement(sql);
                                    pstmt.setString(1, bName);//map value to publisher name
                                    pstmt.executeUpdate();
                                    isBookValid = true;
                                }
                            }
                        }
                        break;
                        case 10:
                            System.out.println("Thank you for using the program.");
                            cont = false;
                            break;
                        default:
                            System.out.println("We can only perform the above queries. Please make an appropriate choice");
                    }
                } catch (InputMismatchException ime) {
                    System.out.println("Please enter the correct choice");
                    input.next();
                }
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}
