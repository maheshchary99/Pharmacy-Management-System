package ADMIN;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin_Login {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pharmacy";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";
    private static final String url = "jdbc:mysql://localhost:3306/pharmacy";
    private static final String user = "root";
    private static final String password = "1234";

    static Scanner sc = new Scanner(System.in);
    Scanner din = new Scanner(System.in);


    public static void main(String[] args) {
        Admin_Login adminLogin = new Admin_Login();
        adminLogin.Login1();
    }


    public void Login1() {
        String username, password;
        System.out.println("\t  Admin Login Here");

        Scanner din = new Scanner(System.in);

        System.out.println("\t Enter UserName");
        username = din.next();
        System.out.println("\t Enter Password");
        password = din.next();

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "select * from admin";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                if (username.equals(rs.getString(2)) && password.equals(rs.getString(3))) {
                    System.out.println(" ***  Welcome  Admin *****");
                    adminhome(); // Calling admin processing logic here
                } else {
                    System.out.println("Login failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adminhome() {
    	Scanner sc=new Scanner(System.in);
        System.out.println("Hai Admin");
        String option1="y";
        while(option1.equalsIgnoreCase("y")) {
        int choice;
        //Scanner din = new Scanner(System.in);

        System.out.println("\t Welcome  To Pharmacy");
        System.out.println("Please Choose Your Options");
        System.out.println("\t  1. Add Medicine");
        System.out.println("\t  2. View Medicine");
        System.out.println("\t  3. View Orders");
        System.out.println("\t  4. View Customers");
        System.out.println("\t  5. View Stocks");
        System.out.println("\t  6. Add Stock");
        System.out.println("\t  7. EXIT");

        choice = din.nextInt();

        switch (choice) {
            case 1:
                addMedicine();
                break;
            case 2:
                viewMedicine();
                break;
            case 3:
                viewOrders();
                break;
            case 4:
                viewCustomers();
                break;
            case 5:
                viewStocks();
                break;
            case 6:
                addStock();
                break;
            case 7:
                System.out.println("Exiting. Visit Again");
                return;
            default:
                System.out.println("Invalid choice");
        }
System.out.println("Do you want to continue press'y'");
option1=sc.next();
        }
    }

   

    private void addMedicine() {
       // try (Connection con = DriverManager.getConnection(url, user, password);
            //Scanner sc = new Scanner(System.in)) {

            String company, medicineName, type, dosage;
            Date expiry_date ,manufacturing_year ;
            double cost;
            int quantity, stock;

            System.out.println("Enter Company Name");
            company = sc.nextLine();
           
            System.out.println("Enter medicine name");
            medicineName = sc.nextLine();
            
            System.out.println("Enter Type (Pill, Injection, or Liquid)");
            type = sc.next();
            System.out.println("Enter dosage ");
            dosage = sc.next();
            System.out.println("Enter Cost");
            cost = sc.nextDouble();
            System.out.println("Enter strip Quantity");
            quantity = sc.nextInt();
            System.out.println("Enter stock");
            stock = sc.nextInt();
            System.out.println("Enter manufacturing Date(YYYY-MM-DD)");
            manufacturing_year = Date.valueOf(sc.next());
            System.out.println("Enter expiry Date(YYYY-MM-DD)"); 
            expiry_date = Date.valueOf(sc.next());
            
            // Calculate total cost
            double totalCost = cost * quantity;

            String addMedicineSQL = "INSERT INTO addmedicine(company, medicine_name, type, dosage, cost,quantity, stock, manufacturing_year, expiry_date)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?,?, ?)";

            try //(PreparedStatement ps = con.prepareStatement(addMedicineSQL))
            {
            	Connection con = DriverManager.getConnection(url, user, password);
            	PreparedStatement ps = con.prepareStatement(addMedicineSQL);

                ps.setString(1, company);
                ps.setString(2, medicineName);
                ps.setString(3, type);
                ps.setString(4, dosage);
                ps.setDouble(5, cost);
                ps.setInt(6, quantity);
                ps.setInt(7, stock);
                ps.setDate(8, manufacturing_year);
                ps.setDate(9, expiry_date);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 1) {
                    System.out.println("Medicine added Successfully");
                } else {
                    System.out.println("Unable to add medicine");
                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewMedicine() {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            String sql = "SELECT * FROM addmedicine";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("Company\tMedicine Name\tType\tDosage\tCost\tStock\tManufacturing Year\tExpiry Date");
            while (rs.next()) {
                System.out.println(
                        rs.getString("company") + "\t" +
                                rs.getString("medicine_name") + "\t" +
                                rs.getString("type") + "\t" +
                                rs.getString("dosage") + "\t" +
                                rs.getString("cost") + "\t" +
                               // rs.getString("quantity") + "\t" +
                                rs.getString("stock") + "\t" +
                                rs.getString("manufacturing_year") + "\t" +
                                rs.getString("expiry_date")
                );
            }
            System.out.println("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void viewOrders() {
        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            String sql1 = "select * from billing";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql1);

            if (rs.next()) {
                do {
                    System.out.println("Bid: " + rs.getInt(1));
                    System.out.println("cusername: " + rs.getString(5));
                    System.out.println("total_cost: " + rs.getDouble(3));
                    System.out.println("total_quantity: " + rs.getInt(4));
                    System.out.println("medicine_name: " + rs.getString(2));
                    System.out.println("*******************************");
                } while (rs.next());
            } else {
                System.out.println("Orders not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 

//    private void viewOrders() {
//    	try {
//    	  	Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//    		
//    	      String sql1="select * from billing";
//    	      Statement st=con.createStatement();
//    	  	ResultSet rs=st.executeQuery(sql1);
//   	  	//if(rs.next()) {
//    	  		
//    	  	while(rs.next()) {
//    	  	System.out.println("Bid :" +rs.getInt(1));
//    	  	System.out.println("cusername :" +rs.getString(5));
//    	  	System.out.println("total_cost :"+rs.getDouble(3));
//    	  	System.out.println("total_quantity :"+rs.getInt(4));
//    	  	System.out.println("medicine_name :"+rs.getString(2));
//    	  	System.out.println("*******************************");
//    	  	}
//    	  	
//    	  //	else {
//    	  	//	System.out.println("Orders not found");
//  	//}
//    		
//    		}catch(Exception e) {
//    	  	e.printStackTrace();
//    		}
//    	
//          }


    private void viewCustomers() {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            String sql = "select * from customer";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("customerName \t CustomerUserName  \t cusemail");
            while (rs.next()) {
                System.out.println(rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(5));
            }
            System.out.println("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewStocks() {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            String sql = "SELECT * FROM addmedicine";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("Company\t\tMedicineName\t\tType\t\tDosage\t\tCost\t\tStock");
            while (rs.next()) {
                System.out.println(
                        rs.getString("company") + "\t\t" +
                                rs.getString("medicine_name") + "\t\t" +
                                rs.getString("type") + "\t\t" +
                                rs.getString("dosage") + "\t\t" +
                                rs.getString("cost") + "\\t\t" +
                               // rs.getString("quantity") + "\t" +
                                rs.getString("stock")
                );
            }
            System.out.println("");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addStock() {
   	 String updateStockSQL = "UPDATE addmedicine SET stock = ? WHERE medicine_name = ?";

        try (
            
            Connection connection = DriverManager.getConnection(url, user, password);

            
            PreparedStatement preparedStatement = connection.prepareStatement(updateStockSQL)
        ) {
            
            System.out.print("Enter the medicine name: ");
            String medicineName = sc.next();

            
            System.out.print("Enter the new stock value: ");
            int stock = sc.nextInt();

            preparedStatement.setInt(1, stock);
            preparedStatement.setString(2, medicineName);

    
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Stock updated successfully.");
            } else {
                System.out.println(" Medicine not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
		
	

}
