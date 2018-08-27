package org.bhajanbook.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bhajanbook.db.DBConnection;
import org.bhajanbook.service.BaseVO;
import org.bhajanbook.service.BhajanLyricsVO;
import org.bhajanbook.service.BhajanTitleVO;
import org.bhajanbook.util.BhajanBookConstants;

public class BhajanDAO {

	public final static int SEARCH_MECH1 = 1;
	public final static int SEARCH_MECH2 = 2;
	public final static int SEARCH_MECH3 = 3;
	public final static String RECENTLY_ADDED = "RECENT";
	public final static String TOP10 = "TOP10";
	public final static String FAVORITES = "_FAV_";

	private final static Logger logger = LogManager.getLogger(BhajanDAO.class);

	public static void main(String[] args) {
		BhajanDAO bDAO = new BhajanDAO();
		try {
			System.out.println(bDAO.getBhajanTitleList("GANESHA"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BaseVO addToFavorite(String userId, String id) {
		return this.addToPlaylist(userId, FAVORITES, id);
	}

	public BaseVO addToPlaylist(String userId, String playlistId, String id) {
		Connection conn = null;
		StringBuffer queryStr = null;
		BaseVO retVO = new BaseVO();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();

			if (playlistId.equals(FAVORITES)) {
				if (this.isBhajanInPlaylist(userId, FAVORITES, id)) {
					logger.info("Removing from favorite " + id);
					retVO = this.removeFromFavorite(userId, id);
					return retVO;
				}
				queryStr = new StringBuffer("insert into user_favorite ");
				queryStr.append("(user_id, bhajan_id, last_updated_by,last_update)");
				queryStr.append("values ('" + userId + "'," + id + ",'" + userId + "', now())");
				stmt.executeUpdate(queryStr.toString());
				conn.commit();
				retVO.setStatus(BhajanBookConstants.SUCCESS);
				return retVO;
			} else {
				if (!this.isAccessAllowed(userId, playlistId)) {
					throw new Exception("Access Denied to the playlist");
				}

				queryStr = new StringBuffer("update playlist_bhajan ");
				queryStr.append("set last_update = now(), last_updated_by ='" + userId + "' ");
				queryStr.append("where  bhajan_id = " + id + " ");
				queryStr.append("and playlist_key = " + playlistId);
				int rc = stmt.executeUpdate(queryStr.toString());
				if (rc == 0) {
					queryStr = new StringBuffer("insert into playlist_bhajan ");
					queryStr.append("(playlist_key, bhajan_id, last_updated_by, last_update) ");
					queryStr.append("values (" + playlistId + "," + id + ", '" + userId + "', now())");
					stmt.executeUpdate(queryStr.toString());
				}
				conn.commit();
				retVO.setStatus(BhajanBookConstants.SUCCESS);
				return retVO;
			}
		} catch (Exception e) {
			// TODO: report error to support.
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			;
			logger.error("Exception occured: " + e.toString() + " " + queryStr);
			retVO.setMesg("Internal Error occured. Please contact support.");
			retVO.setStatus(BhajanBookConstants.FAILURE);
			return retVO;
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

	public BaseVO createPlaylist(String userId, String playlistName, String id) {
		Connection conn = null;
		StringBuffer queryStr = null;
		BaseVO retVO = new BaseVO();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			conn.setAutoCommit(false);
		 	playlistName = playlistName.replaceAll("'", "''");

			Statement stmt = conn.createStatement();
			queryStr = new StringBuffer("insert into playlist \r\n" );
			queryStr.append("( playlist_name, owner_user_id, created_on, last_updated_by, last_update) ");
			queryStr.append("values ('" + playlistName + "','" + userId + "', now(), '" + userId + "', now())");
			
			int rc = stmt.executeUpdate(queryStr.toString(), Statement.RETURN_GENERATED_KEYS);
			if (rc == 0) {
				retVO.setMesg("Error creating " + playlistName + " playlist. Contact support.");
				retVO.setStatus(BhajanBookConstants.FAILURE);
				return retVO;
			}  			
			conn.commit();
			int playlistKey = -1;

		    ResultSet rs = stmt.getGeneratedKeys();

		    if (rs.next()) {
		        playlistKey = rs.getInt(1);
		    }  else {
		    	throw new Exception("Error retrieving playlist information");
		    }
		    String playlistKeyStr = Integer.toString(playlistKey);
			// Add bhajan to Playlist.
			retVO = this.addToPlaylist(userId, playlistKeyStr, id);

			return retVO;
		} catch (Exception e) {
			// TODO: report error to support.
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			;
			logger.error("Exception occured: " + e.toString() + " " + queryStr);
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("Internal Error occured. Please contact support.");

			return retVO;
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

	public BhajanLyricsVO getBhajan(String userId, String bhajanId) {
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
				// check if this is a favorite. If yes, set the favorite variable.
				boolean like = this.isBhajanInPlaylist(userId, FAVORITES, bhajanId);
				if (like) {
					bhajanVO.setFavorite("Y");
				} else {
					bhajanVO.setFavorite("N");
				}

			} else {
				bhajanVO.setBhajanTitle("Error Fetching Bhajan. Please contact support");
			}
			return bhajanVO;
		} catch (Exception e) {
			// TODO: report error to support.
			logger.error("Exception occured: " + e.toString());
			bhajanVO.setBhajanTitle("Error Fetching Bhajan. Please contact support ");
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

	public List<BhajanTitleVO> getBhajanTitleList(String deity) {
		Connection conn = null;
		ArrayList<BhajanTitleVO> bhajanTitleList = new ArrayList<BhajanTitleVO>();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			if ("ALL".equals(deity.toUpperCase())) { // If ALL.
				queryStr.append("select b.bhajan_id, bhajan_title  from bhajandb.bhajan b ");
			} else {
				queryStr.append("select b.bhajan_id, bhajan_title  from bhajandb.bhajan b, bhajandb.bhajan_deity bd ");
				queryStr.append(" where b.bhajan_id = bd.bhajan_id ");
				queryStr.append(" and bd.deity = '");
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

	public List<BhajanTitleVO> getPlaylistBhajans(String userId, String playlistId) {
		Connection conn = null;
		List<BhajanTitleVO> bhajanTitleList = new ArrayList<BhajanTitleVO>();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			if (RECENTLY_ADDED.equals(playlistId.toUpperCase())) { // If ALL.
				queryStr.append(
						"select b.bhajan_id, b.bhajan_title, b.shruthi from bhajan b  ORDER BY date_added desc limit 30 ");
			} else if (TOP10.equals(playlistId.toUpperCase())) {
				queryStr.append("select b.bhajan_id, b.bhajan_title, b.shruthi\r\n"
						+ "from log_bhajan_access l, bhajan b\r\n" + "where l.bhajan_id = b.bhajan_id\r\n"
						+ "group by b.bhajan_id, b.bhajan_title, b.shruthi\r\n" + "order by count(*) desc\r\n"
						+ "limit 10");
			} else if (FAVORITES.equals(playlistId.toUpperCase())) {
				queryStr.append("select b.bhajan_id, b.bhajan_title, b.shruthi\r\n"
						+ "from user_favorite l, bhajan b\r\n" + "where l.bhajan_id = b.bhajan_id\r\n"
						+ " and l.user_id = '" + userId + "'" + "order by  l.last_update asc \r\n");
			} else {
				if (!isAccessAllowed(userId, playlistId)) {
					throw new Exception("Access Denied to the playlist");
				}
				// user specific play list - to do.
				queryStr.append("select b.bhajan_id, b.bhajan_title, b.shruthi from playlist_bhajan u"
						+ " join bhajan b " + " on u.bhajan_id = b.bhajan_id\r\n" + " and playlist_key = " + playlistId
						+ " order by u.last_update asc");
			}
			System.out.println(queryStr);
			ResultSet rs = stmt.executeQuery(queryStr.toString());

			while (rs.next()) {
				BhajanTitleVO btVO = new BhajanTitleVO();
				btVO.setId(rs.getInt("bhajan_id"));
				btVO.setBhajanTitle(rs.getString("bhajan_title"));
				// To add shruthi
				bhajanTitleList.add(btVO);
			}
			return bhajanTitleList;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			logger.error(e.toString());
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

	public boolean isAccessAllowed(String userId, String playlistKey) {
		// query for the favorites and return true or false
		Connection conn = null;
		BhajanLyricsVO bhajanVO = new BhajanLyricsVO();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer("select playlist_key from playlist ");
			queryStr.append("where   owner_user_id = '" + userId + "' ");
			queryStr.append(" and playlist_key = " + playlistKey);
			queryStr.append(" union select playlist_key from playlist_shared  ");
			queryStr.append("where   user_id = '" + userId + "' ");
			queryStr.append(" and playlist_key = " + playlistKey);

			ResultSet rs = stmt.executeQuery(queryStr.toString());
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: report error to support.
			logger.error("Exception occured: " + e.toString());
			return false;
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

	public boolean isBhajanInPlaylist(String userId, String playlistKey, String bhajanId) {
		// query for the favorites and return true or false
		Connection conn = null;
		BhajanLyricsVO bhajanVO = new BhajanLyricsVO();
		try {
			int id = Integer.parseInt(bhajanId);
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr;
			if (playlistKey.equals(FAVORITES)) {
				queryStr = new StringBuffer("select * from user_favorite " + "where user_id = '" + userId + "' "
						+ "and bhajan_id = " + bhajanId);
			} else {
				queryStr = new StringBuffer("select * from playlist_bhajan " + "where   playlist_key = " + playlistKey
						+ " and bhajan_id = " + bhajanId);
			}
			ResultSet rs = stmt.executeQuery(queryStr.toString());
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: report error to support.
			logger.error("Exception occured: " + e.toString());
			return false;
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
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			;
			logger.error("Exception occured: " + e.toString() + " " + queryStr);
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

	public int logBhajanSearch(String searchStr, String userId, String userAgent, String ip, int searchMech, int hits) {
		Connection conn = null;
		StringBuffer queryStr = null;
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			queryStr = new StringBuffer("insert into log_bhajan_search ");
			queryStr.append("(search_str, date_searched, user_agent, remote_addr, user_id, search_mech, result_hits)");
			queryStr.append("values ('" + searchStr + "', now(), '" + userAgent + "', '" + ip + "',null, " + searchMech
					+ ", " + hits + ")");

			logger.debug(queryStr);

			int rc = stmt.executeUpdate(queryStr.toString());
			conn.commit();
			return rc;
		} catch (Exception e) {
			// TODO: report error to support.
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			;
			logger.error("Exception occured: " + e.toString() + " " + queryStr);
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

	public List<String> playlistWithBhajan(String userId, String bhajanId) {
		Connection conn = null;
		List<String> playlistBhajans = new ArrayList<String>();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			// user specific play list - to do.
			queryStr.append("select p.playlist_key, p.owner_user_id\r\n" + 
					"from playlist p join playlist_bhajan b on p.playlist_key = b.playlist_key\r\n" + 
					"and b.bhajan_id =" + bhajanId + "\r\n" + 
					"and owner_user_id = '" + userId + "'\r\n" + 
					"union \r\n" + 
					"select p.playlist_key, p.user_id\r\n" + 
					"from playlist_shared p join playlist_bhajan b on p.playlist_key = b.playlist_key\r\n" + 
					"and b.bhajan_id = " + bhajanId + "\r\n" + 
					"and p.user_id = '"  + userId + "'\r\n");
			System.out.println(queryStr);
			ResultSet rs = stmt.executeQuery(queryStr.toString());

			while (rs.next()) {
				playlistBhajans.add(Integer.toString(rs.getInt("playlist_key")));
			}
			return playlistBhajans;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			logger.error(e.toString());
			return playlistBhajans;
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
	
	public BaseVO removeFromFavorite(String userId, String id) {
		Connection conn = null;
		StringBuffer queryStr = null;
		BaseVO retVO = new BaseVO();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();

			queryStr = new StringBuffer("delete from user_favorite\r\n" + "where user_id = '" + userId + "'\r\n" + "and bhajan_id = " + id);
			int rc = stmt.executeUpdate(queryStr.toString());
			if (rc == 0) {
				retVO.setMesg("Unable to find bhajan to remove from Favorite");
				retVO.setStatus(BhajanBookConstants.FAILURE);
			} else {
				retVO.setStatus(BhajanBookConstants.SUCCESS);
			}
			conn.commit();

			return retVO;
		} catch (Exception e) {
			// TODO: report error to support.
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			;
			logger.error("Exception occured: " + e.toString() + " " + queryStr);
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("Internal Error occured. Please contact support.");

			return retVO;
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

	public BaseVO removeFromPlaylist(String userId, String playlistId, String id) {
		Connection conn = null;
		StringBuffer queryStr = null;
		BaseVO retVO = new BaseVO();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			
			if (!this.isAccessAllowed(userId, playlistId)) {
				throw new Exception("Access Denied to the playlist");
			}

			queryStr = new StringBuffer("delete from playlist_bhajan\r\n" + "where playlist_key = " + playlistId
					+ "\r\n" + "and bhajan_id = " + id);
			int rc = stmt.executeUpdate(queryStr.toString());
			if (rc == 0) {
				retVO.setMesg("Unable to find bhajan to remove from playlist");
				retVO.setStatus(BhajanBookConstants.FAILURE);
			} else {
				retVO.setStatus(BhajanBookConstants.SUCCESS);
			}
			conn.commit();

			return retVO;
		} catch (Exception e) {
			// TODO: report error to support.
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			;
			logger.error("Exception occured: " + e.toString() + " " + queryStr);
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("Internal Error occured. Please contact support.");

			return retVO;
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

	public List<BhajanTitleVO> searchBhajan(String searchStr, String userId, String userAgent, String ip,
			boolean firstTime) {
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
			logger.debug(queryStr);
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
			queryStr.append("and soundex_keyword = soundex(" + searchStr.toUpperCase() + ") ");
			queryStr.append("order by bhajan_title ");

			logger.debug(queryStr);
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
}
