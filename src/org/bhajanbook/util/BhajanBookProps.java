package org.bhajanbook.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class BhajanBookProps {
	private static HashMap<String, String> propMap = new HashMap<String, String>();

	static {
		Properties prop = new Properties();
		try
		{
			// load a properties file from class path, inside static method
			prop.load(BhajanBookProps.class.getClassLoader().getResourceAsStream("BhajanBook.props"));

			// get the property value and print it out
			propMap.put("DATABASE_HOST", prop.getProperty("DATABASE_HOST"));
			propMap.put("DATABASE", prop.getProperty("DATABASE"));
			propMap.put("DBUSER", prop.getProperty("DBUSER"));
			propMap.put("DBPASSWORD", prop.getProperty("DBPASSWORD"));			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static String getProp(String prop) {
		return propMap.get(prop);
	}
	
	public static void main(String[] args) {
		BhajanBookProps.getProp("DATABASE_HOST");
	}
}
