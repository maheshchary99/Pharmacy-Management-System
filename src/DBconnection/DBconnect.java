package DBconnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {

	static boolean getConnection() throws SQLException
	{
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy","root","1234"); 
	} catch (ClassNotFoundException e) {
		
		e.printStackTrace();
	}
	
	return true;
	}
}