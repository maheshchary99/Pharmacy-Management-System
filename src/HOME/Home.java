package HOME;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import ADMIN.Admin_Login;
import USERLOGIN.UserLogin;

public class Home {
    public static void main(String[] args) {
        int choice;

   
        System.out.println("\t Welcome  To Pharmacy");
       
        System.out.println(" Please Selecct Your Login Option");

        Scanner din = new Scanner(System.in);
        System.out.println("\t  1.Admin Login");
        System.out.println("\t  2.User Login");
       

        choice = din.nextInt();
        switch (choice) {
            case 1:
                   Admin_Login al = new Admin_Login();
                   al.Login1();
                break;
            case 2:
                UserLogin ul = new UserLogin();
                ul.LogType();
                break;
           
            default:
                System.out.println("Invalid choice");
        }
    }
}

  
        
