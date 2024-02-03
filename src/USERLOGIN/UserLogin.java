package USERLOGIN;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class UserLogin {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pharmacy";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";
    Scanner din = new Scanner(System.in);

    public void LogType() {
        int Choice;
        System.out.println("Select Your Option");
        System.out.println("Press 1. For Login");
        System.out.println("Press 2. For Register");
        Choice = din.nextInt();

        switch (Choice) {
            case 1:
                Login();
                break;
            case 2:
                register();
                break;
        }
    }

    void Login() {
        String username, password;
        System.out.println("\t \t User Login Here");

        System.out.println("\t Enter UserName");
        username = din.next();
        System.out.println("\t  Enter Password");
        password = din.next();

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", "1234");
            String sql = "select * from customer where cuser = ? and cpass = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                System.out.println(" ***  Welcome  User *****");
                userhome(username);
            } else {
                System.out.println("Login failed\n please give correct username and password ");
                Login();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void register() {
        String cname, cuser, cpass, cemail, cmobile, caddress;

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", "1234");
            System.out.println("Enter customer Name");
            cname = din.next();
            System.out.println("Enter customer UserName");
            cuser = din.next();
            System.out.println("Enter customer Password");
            cpass = din.next();
            System.out.println("Enter customer Email");
            cemail = din.next();
            System.out.println("Enter customer Mobile");
            cmobile = din.next();
            System.out.println("Enter customer Address");
            caddress = din.next();
            String sql = "insert into customer(cname, cuser, cpass, cemail, cmobile,caddress) values(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cname);
            ps.setString(2, cuser);
            ps.setString(3, cpass);
            ps.setString(4, cemail);
            ps.setString(5, cmobile);
            ps.setString(6, caddress);
            int i = ps.executeUpdate();

            if (i == 1) {
                // Customer data created successfully
                sendRegistrationEmail(cemail);
                System.out.println("Customer data created successfully");
//                System.out.println("Please wait for a second");
                System.out.println("your registration successfully completed");
                System.out.println("Do you Want Login press y");
                String option = din.next();
                if(option.equalsIgnoreCase("y"))
                {
                	UserLogin ul = new UserLogin();
                    ul.Login();
                }else {
                	UserLogin ul = new UserLogin();
                    ul.LogType();
                }
            } else {
                System.out.println("Customer data creation failed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void sendRegistrationEmail(String cemail) {
        final String username = "99maheshchary@gmail.com";
        final String password = "xdct hhmg lyvz dvrj";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cemail));
            message.setSubject("Hello user welcome to pharmacy ...");
            message.setText("Your Registration successfully completed\n now you can order your medicines by login");

            Transport.send(message);

            System.out.println("Message sent successfully...");
        } catch (MessagingException e) {
            System.out.println("Failed to send email");
            e.printStackTrace();
        }
    }

    void userhome(String username) {
        while (true) {
            System.out.println("Hello " + username);

            int choice;
            System.out.println("\t Welcome To Pharmacy");
            System.out.println("Please Choose Your Options");
            System.out.println("\t  1. Search Medicine");
            System.out.println("\t  2. Order Medicine");
            System.out.println("\t  3. View Order History");
            System.out.println("\t  4. Exit");
            choice = din.nextInt();

            switch (choice) {
                case 1:
                    searchMedicine();
                    break;
                case 2:
                    orderMedicine(username);
                    break;
                case 3:
                    viewOrderHistory(username);
                    break;
                case 4:
                    System.out.println("Exiting . Visit Again");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    void searchMedicine() {
        System.out.println("Enter the medicine name to search: ");
        String medicineName = din.next();
        String url = "jdbc:mysql://localhost:3306/pharmacy";
        String username = "root";
        String password = "1234";
        String sql = "SELECT * FROM addmedicine WHERE medicine_name LIKE ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + medicineName + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No medicine found ");
                } else {
                    while (resultSet.next()) {
                        System.out.println("Medicine ID: " + resultSet.getInt("mid"));
                        System.out.println("Medicine Name: " + resultSet.getString("medicine_name"));
                        System.out.println("Type: " + resultSet.getString("type"));
                        System.out.println("Dosage: " + resultSet.getString("dosage"));
                        System.out.println("Cost: $" + resultSet.getDouble("cost"));
                        System.out.println("stock: " + resultSet.getInt("stock"));
                        System.out.println("manufacturing_year:" + resultSet.getDate("manufacturing_year"));//showing medicine date
                        System.out.println("expiry_date:" + resultSet.getDate("expiry_date"));
                        
                      //  System.out.println("Quantity: " + resultSet.getInt("quantity"));
                        System.out.println("----------------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void orderMedicine(String username) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the medicine name to order: ");
        String medicine_name = scanner.nextLine();
        System.out.println("Enter the address to order: ");
        String address = scanner.nextLine();
        System.out.println("Enter customer Email");
        String oemail = scanner.nextLine();
        System.out.println("Enter the quantity to order: ");
        int quantity = scanner.nextInt();
        
        System.out.println("Enter the cusername: ");
        String cusername = scanner.next();

       
        String selectMedicineSql = "SELECT * FROM addmedicine WHERE medicine_name = ?";
        String insertOrderSql = "INSERT INTO orders (cusername,medicine_name, quantity, address, oemail) VALUES (?, ?,?, ?, ?)";
        String updateStockSql = "UPDATE addmedicine SET stock = ? WHERE medicine_name = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
             PreparedStatement selectMedicineStatement = connection.prepareStatement(selectMedicineSql);
             PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderSql);
             PreparedStatement updateStockStatement = connection.prepareStatement(updateStockSql)) {

            selectMedicineStatement.setString(1, medicine_name);

            try (ResultSet resultSet = selectMedicineStatement.executeQuery()) {
                if (resultSet.next()) {
                    int availableStock = resultSet.getInt("stock");
                    Date manufacturingYear = resultSet.getDate("manufacturing_year");
                    Date expiryDate = resultSet.getDate("expiry_date");
                    double cost = resultSet.getDouble("cost");

                    if (availableStock >= quantity && !isExpired(manufacturingYear, expiryDate)) {
                        // Update stock in addmedicine table
                        updateStockStatement.setInt(1, availableStock - quantity);
                        updateStockStatement.setString(2, medicine_name);
                        updateStockStatement.executeUpdate();

                        //  Insert order into the orders table
                        insertOrderStatement.setString(1, cusername);
                        insertOrderStatement.setString(2,medicine_name);
                        insertOrderStatement.setInt(3,quantity);
                        insertOrderStatement.setString(4, address);
                        insertOrderStatement.setString(5, oemail);
                        int rowsAffected = insertOrderStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Order placed successfully!");
//                            String sql="select * from orders";
//                            Statement st=connection.createStatement();
//                            ResultSet rs=st.executeQuery(sql);
//                            if (rs.next()) {
//                            	String name=rs.getString(2);
//                          
//                            }
                            
                            // Insert into billing table
                            String addBillingSQL = "INSERT INTO billing (medicine_name, total_cost, total_quantity,cusername) VALUES (?,?, ?,?)";
                            		 try (PreparedStatement billingPS = connection.prepareStatement(addBillingSQL)) {
                                billingPS.setString(1, medicine_name);
                                billingPS.setDouble(2, cost * quantity);
                                billingPS.setInt(3, quantity);
                                billingPS.setString(4,cusername);
                                //billingPS.setString(5, address);

                                int billingRowsAffected = billingPS.executeUpdate();

                                if (billingRowsAffected == 1) {
                                    System.out.println("Billing record added Successfully");
                                    sendOrderConfirmationEmail(oemail, medicine_name, quantity);
                                } else {
                                    System.out.println("Unable to add billing record");
                                }
                            }
                        } else {
                            System.out.println("Order placement failed");
                        }
                    } else {
                        System.out.println("you are entered large amount of quantity than the available stock.\n so,please enter <= "+availableStock);
                    }
                } else {
                    System.out.println("Medicine not found in the inventory that you are entered.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isExpired(Date manufacturingYear, Date expiryDate) {
        
        return false;  
    }

    private void sendOrderConfirmationEmail1(String oemail, String medicineName, int quantity) {
       
        System.out.println("Order confirmation email sent to: " + oemail);
    }

    private boolean isExpired1(int manufacturingYear, Date expiryDate) {
       
        return false;  
    }
    
    private void sendOrderConfirmationEmail2(String oemail, String medicineName, int quantity) {
        
        System.out.println("Order confirmation email sent to: " + oemail);
    }

    void sendOrderConfirmationEmail(String oemail, String medicineName, int quantity) {
        final String username2 = "99maheshchary@gmail.com";
        final String password2 = "xdct hhmg lyvz dvrj";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username2, password2);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username2));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(oemail));
            message.setSubject("Order Confirmation - Pharmacy");
            message.setText("Dear Customer,\n\n"
                    + "Your order for -" + quantity + "- units of -" + medicineName + "- has been successfully processed. \n Thank you for choosing our pharmacy.\n\n"
                    + "Best Regards,\nPharmacy Team");

            Transport.send(message);

            System.out.println("Order confirmation email sent successfully to: " + oemail);

        } catch (MessagingException e) {
            System.out.println("Failed to send order confirmation email");
            e.printStackTrace();
        }
    }


    void viewOrderHistory(String cusername) {
        String orderQuery = "SELECT * FROM orders WHERE cusername=?";
        String billingQuery = "SELECT total_cost FROM billing WHERE cusername=?";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement orderStatement = con.prepareStatement(orderQuery);
             PreparedStatement billingStatement = con.prepareStatement(billingQuery)) {

			orderStatement.setString(1, cusername);
            ResultSet orderResultSet = orderStatement.executeQuery();

            billingStatement.setString(1, cusername);
            ResultSet billingResultSet = billingStatement.executeQuery();

            if (!orderResultSet.isBeforeFirst()) {
                System.out.println("No order history found for " + cusername);
            } else {
                while (orderResultSet.next()) {
                    String address1 = orderResultSet.getString("address");
                    String medicine_name = orderResultSet.getString("medicine_name");
                    int quantity1 = orderResultSet.getInt("quantity");

                    System.out.println("Medicine Name: " + medicine_name);
                    System.out.println("Quantity: " + quantity1);
                    System.out.println("Address: " + address1);
                    System.out.println("Username: " + cusername);

                    // Check if there is billing information available
                    if (billingResultSet.next()) {
                        Double total_cost = billingResultSet.getDouble("total_cost");
                        System.out.println("Total Cost: " + total_cost);
                    } else {
                        System.out.println("No billing information found for " + cusername);
                    }

                    System.out.println("---------------------------");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving order history: " + e.getMessage());
            e.printStackTrace();
        }
    }
}