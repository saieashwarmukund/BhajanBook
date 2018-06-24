package org.bhajanbook.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bhajanbook.db.DBConnection;

public class ThoughtForTheDayDAO {

	public String getRandomThoughtForTheDay() {
		Connection conn = null;
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			// Get Random Id
			ResultSet rs = stmt.executeQuery("select floor(rand() * count(*)) thought_id  from bhajandb.thought ");

			int thoughtId = 0;
			if (rs.next()) {
				thoughtId = rs.getInt("thought_id");

				rs = stmt.executeQuery("select thought  from bhajandb.thought where thought_id = " + thoughtId);
				if (rs.next()) {
					String thought = rs.getString("thought");
					return thought;
				}
			}
			return "";
		} catch (Exception e) {
			// TODO: report error to support.
			return ("Error getting Thought For The Day.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					// do nothing.
				}
			}
		}
	}

	public static void main(String[] args) {
		ThoughtForTheDayDAO tftdDAO = new ThoughtForTheDayDAO();
		try {
			System.out.println(tftdDAO.getRandomThoughtForTheDay());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
