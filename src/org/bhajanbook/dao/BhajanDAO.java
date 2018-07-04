package org.bhajanbook.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bhajanbook.db.DBConnection;
import org.bhajanbook.service.BhajanLyricsVO;
import org.bhajanbook.service.BhajanTitleVO;

public class BhajanDAO {
	
	public List<BhajanTitleVO> getBhajanTitleList(String deity) {
		Connection conn = null;
		ArrayList<BhajanTitleVO> bhajanTitleList = new ArrayList<BhajanTitleVO>();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer("select b.bhajan_id, bhajan_title  from bhajandb.bhajan b, bhajandb.bhajan_deity bd ");
			
			queryStr.append(" where b.bhajan_id = bd.bhajan_id and bd.deity = '");
			queryStr.append(deity.toUpperCase() + "'");
			
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
	public BhajanLyricsVO getBhajan(String bhajanId) {
		Connection conn = null;
		BhajanLyricsVO bhajanVO = new BhajanLyricsVO();
		try {
			int id = Integer.parseInt(bhajanId);
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer("select b.bhajan_id, bhajan_title, lyrics ");
			queryStr.append(", lang, raaga, beat, audio_file_path from bhajandb.bhajan b ");
			queryStr.append("  where b.bhajan_id = ");
			queryStr.append(id);
			
			ResultSet rs = stmt.executeQuery(queryStr.toString());
			if (rs.next()) {
				// Set values to BhajanVO.
				bhajanVO.setId(rs.getInt("bhajan_id"));
				bhajanVO.setBhajanTitle(rs.getString("bhajan_title"));
				bhajanVO.setLyrics(rs.getString("lyrics"));
				bhajanVO.setLang(rs.getString("lang"));
				bhajanVO.setRaaga(rs.getString("raaga"));
				bhajanVO.setBeat(rs.getString("beat"));
				bhajanVO.setAudioFilePath(rs.getString("audio_file_path"));
				bhajanVO.setBeat(rs.getString("beat"));
			} else {
				bhajanVO.setBhajanTitle("Error Fetching Bhajan. Please contact support");
			}
			return bhajanVO;
		} catch (Exception e) {
			// TODO: report error to support.
			System.out.println("Exception occured: " + e.toString());
			bhajanVO.setBhajanTitle("Error Fetching Bhajan. Please contact support " );
			return bhajanVO;
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
