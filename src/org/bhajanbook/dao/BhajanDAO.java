package org.bhajanbook.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bhajanbook.db.DBConnection;
import org.bhajanbook.service.BhajanTitleVO;

public class BhajanDAO {
	public static final String GANESHA = "GANESHA";
	
	public List<BhajanTitleVO> getBhajanTitleList(String deity) {
		Connection conn = null;
		ArrayList<BhajanTitleVO> bhajanTitleList = new ArrayList<BhajanTitleVO>();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer("select bhajan_id, bhajan_title  from bhajandb.bhajan ");
			
			StringBuffer whereClause = new StringBuffer(" where ");
			if (deity.equals(GANESHA)) {
				whereClause.append("ganesha_indic = 'Y'");
			}
			queryStr.append(whereClause);
			queryStr.append(" order by bhajan_title");
			
			ResultSet rs = stmt.executeQuery(queryStr.toString());
			
			while (rs.next()) {
				BhajanTitleVO btVO = new BhajanTitleVO();
				btVO.setId(rs.getInt("bhajan_id"));
				btVO.setBhajanTitle(rs.getString("bhajan_title"));
				bhajanTitleList.add(btVO);
			}
			return bhajanTitleList;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			return bhajanTitleList;
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
		BhajanDAO bDAO = new BhajanDAO();
		try {
			System.out.println(bDAO.getBhajanTitleList("GANESHA"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
