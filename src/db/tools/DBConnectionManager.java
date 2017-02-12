package db.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

	/*MySQL*/
	public static String mysql_host = "jdbc:mysql://localhost"; //SQL local server address
	public static String mysql_db = "mysqldb"; //SQL Database's name
	public static String mysql_username = "root"; //SQL Database's username
	public static String mysql_password = "root"; //SQL Database's password


	/**
	 * MySQL server connection
	 * @return
	 * @throws SQLException
	 */
	public static Connection getMySQLServerConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(DBConnectionManager.mysql_host,
					DBConnectionManager.mysql_username, DBConnectionManager.mysql_password);
		} catch (ClassNotFoundException e) {e.printStackTrace();return null;}
	}

	/**
	 * MySQL Database connection
	 * @return
	 * @throws SQLException
	 */
	public static Connection getMySQLDBConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(DBConnectionManager.mysql_host + "/" + DBConnectionManager.mysql_db,
					DBConnectionManager.mysql_username, DBConnectionManager.mysql_password);
		} catch (ClassNotFoundException e) {e.printStackTrace();return null;}
	}
}