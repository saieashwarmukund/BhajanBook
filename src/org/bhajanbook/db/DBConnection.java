package org.bhajanbook.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bhajanbook.util.BhajanBookProps;

public class DBConnection {
	private static final String DATABASE_HOST = BhajanBookProps.getProp("DATABASE_HOST");
	private static final String DATABASE = BhajanBookProps.getProp("DATABASE");
	private static final String CONNUSER = BhajanBookProps.getProp("DBUSER");
	private static final String DWP = BhajanBookProps.getProp("DBPASSWORD");
	
	public static Connection getDBConnection() throws Exception {

		try {
			// TODO: To pickup configurations from property file.
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + DATABASE_HOST + "/" + DATABASE + "?user=" + CONNUSER
					 + "&password=" + DWP + "&allowPublicKeyRetrieval=true&useSSL=false"); 
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