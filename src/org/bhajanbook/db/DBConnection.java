package org.bhajanbook.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String HOST_PORT = "localhost:3306";
	private static final String DATABASE = "bhajandb";
	private static final String CONNUSER = "bhajanadm";
	private static final String DWP = "Pa55word";
	
	public static Connection getDBConnection() throws Exception {

		try {
			// TODO: To pickup configurations from property file.
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + HOST_PORT + "/" + DATABASE + "?user=" + CONNUSER
					 + "&password=" + DWP + "&useSSL=false"); 
			// localhost:3306/bhajandb?" +"user=bhajanadm&password=Pa55word");


			return conn;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			throw new Exception("Error getting Database connection.");
		}
	}

	
	public static void main (String[] args) {
		try {
		  DBConnection.getDBConnection();
		System.out.println("Got Connection");
		} catch (Exception e) {
			System.out.println("Exception occured");
		}
	}
	
}