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
	
	public final static int SEARCH_MECH1 = 1;
	public final static int SEARCH_MECH2 = 2;
	public final static int SEARCH_MECH3 = 3;
	

	public List<BhajanTitleVO> getBhajanTitleList(String deity) {
		Connection conn = null;
		ArrayList<BhajanTitleVO> bhajanTitleList = new ArrayList<BhajanTitleVO>();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			if ("ALL".equals(deity.toUpperCase())) { // If  ALL.
				queryStr.append("select b.bhajan_id, bhajan_title  from bhajandb.bhajan b ");
			} else {
				queryStr.append("select b.bhajan_id, bhajan_title  from bhajandb.bhajan b, bhajandb.bhajan_deity bd ");  			
				queryStr.append(" where b.bhajan_id = bd.bhajan_id ");
				queryStr.append( " and bd.deity = '");
				queryStr.append(deity.toUpperCase() + "'");
			}

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
			StringBuffer queryStr = new StringBuffer("select b.bhajan_id, bhajan_title, meaning, lyrics ");
			queryStr.append(", lang, raaga, beat, audio_file_path from bhajandb.bhajan b ");
			queryStr.append("  where b.bhajan_id = ");
			queryStr.append(id);

			ResultSet rs = stmt.executeQuery(queryStr.toString());
			if (rs.next()) {
				// Set values to BhajanVO.
				bhajanVO.setId(rs.getInt("bhajan_id"));
				bhajanVO.setBhajanTitle(rs.getString("bhajan_title"));
				bhajanVO.setLyrics(rs.getString("lyrics"));
				bhajanVO.setMeaning(rs.getString("meaning"));
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
	public int logBhajanAccess(String id, String userAgent, String ip) {
		Connection conn = null;
		StringBuffer queryStr = null;
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
		    queryStr = new StringBuffer("insert into log_bhajan_access ");
			queryStr.append("(bhajan_id, date_accessed, user_agent, remote_addr)");
			queryStr.append("values (" + id + ", now(), '" + userAgent + "', '" + ip + "')");

			int rc = stmt.executeUpdate(queryStr.toString());
			conn.commit();
			return rc;
		} catch (Exception e) {
			// TODO: report error to support.
			try {conn.rollback();} catch (Exception e2) {};
			System.out.println("Exception occured: " + e.toString() + " " + queryStr);
			return 0;
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
 
	public int logBhajanSearch(String searchStr, String userId, String userAgent, String ip, int searchMech, int hits ) {
		Connection conn = null;
		StringBuffer queryStr = null;
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
		    queryStr = new StringBuffer("insert into log_bhajan_search ");
			queryStr.append("(search_str, date_searched, user_agent, remote_addr, user_id, search_mech, result_hits)");
			queryStr.append("values ('" + searchStr  + "', now(), '" + userAgent + "', '" + ip + "',null, " + searchMech + ", " + hits + ")");
System.out.println(queryStr);
			int rc = stmt.executeUpdate(queryStr.toString());
			conn.commit();
			return rc;
		} catch (Exception e) {
			// TODO: report error to support.
			try {conn.rollback();} catch (Exception e2) {};
			System.out.println("Exception occured: " + e.toString() + " " + queryStr);
			return 0;
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
	public List<BhajanTitleVO> searchBhajan(String searchStr, String userId, String userAgent, String ip, boolean firstTime) {
		Connection conn = null;
		List<BhajanTitleVO> bhajanTitleList = new ArrayList<BhajanTitleVO>();
		try {
			searchStr = searchStr.replaceAll("'", "''");
			// Get DB Connection. 
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			
			// Search based on full search string
			queryStr.append("select b.bhajan_id, bhajan_title  from bhajandb.bhajan b ");  			
			queryStr.append("where title_key like '");
			queryStr.append(searchStr.toUpperCase().trim() + "%'");
System.out.println(queryStr);
			ResultSet rs = stmt.executeQuery(queryStr.toString());

			while (rs.next()) {
				BhajanTitleVO btVO = new BhajanTitleVO();
				btVO.setId(rs.getInt("bhajan_id"));
				btVO.setBhajanTitle(rs.getString("bhajan_title"));
				bhajanTitleList.add(btVO);
			}
			
			if (bhajanTitleList.size() > 0 || searchStr.length() <= 5) {
				if (firstTime) {
					logBhajanSearch(searchStr, userId, userAgent, ip, SEARCH_MECH1, bhajanTitleList.size());
				} else {
					logBhajanSearch(searchStr, userId, userAgent, ip, SEARCH_MECH3, bhajanTitleList.size());
				}
				return bhajanTitleList;				
			}
			
			// search based on sound of first word
			bhajanTitleList = searchBhajanSoundex(searchStr);
			if (bhajanTitleList.size() > 0) {
				logBhajanSearch(searchStr, userId, userAgent, ip, SEARCH_MECH2, bhajanTitleList.size());
				return bhajanTitleList;
			}
			
			
			searchStr = searchStr.substring(0, searchStr.length() - 1);
			// searchStr = searchStr.substring(0, 5);
			return searchBhajan(searchStr, userId, userAgent, ip, false);
			// return bhajanTitleList;
			
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
	
	public List<BhajanTitleVO> searchBhajanSoundex(String searchStr) {
		Connection conn = null;
		List<BhajanTitleVO> bhajanTitleList = new ArrayList<BhajanTitleVO>();
		try {
			searchStr = searchStr.replaceAll("'", "''");
			
			// find first word
			int i = searchStr.indexOf(' ');
			if (i != -1) {
				 searchStr = searchStr.substring(0, i);
			} 
			
			// Get DB Connection. 
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			queryStr.append("select b.bhajan_id, b.bhajan_title ");
			queryStr.append("from bhajan b, bhajan_key bk ");
			queryStr.append("where b.bhajan_id = bk.bhajan_id ");
			queryStr.append("and soundex_keyword = soundex("+ searchStr.toUpperCase() +") ");
			queryStr.append("order by bhajan_title ");
			
			
System.out.println(queryStr);
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
