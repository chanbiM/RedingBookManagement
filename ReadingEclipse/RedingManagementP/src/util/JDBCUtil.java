package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String connectionString = "jdbc:mysql://localhost/reading_m";
		String userId = "root";
		String password = "";
		
//		String connectionString = "jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6441284";
//		String userId = "sql6441284";
//		String password = "4lwCP6aFFg";
		
		Connection con = null;
		try {
			con = DriverManager.getConnection(connectionString, userId, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return con;
		
	}
}
