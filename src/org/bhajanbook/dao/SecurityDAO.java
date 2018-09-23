package org.bhajanbook.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bhajanbook.db.DBConnection;
import org.bhajanbook.service.AuthService;
import org.bhajanbook.service.BaseVO;
import org.bhajanbook.service.BhajanLyricsVO;
import org.bhajanbook.service.BhajanTitleVO;
import org.bhajanbook.service.PlaylistVO;
import org.bhajanbook.service.UserRoleVO;
import org.bhajanbook.util.BhajanBookConstants;

public class SecurityDAO {

	private static final int MAX_INCOR_LOGIN_ATTEMPTS = 5;
	private static final int LOCKOUT_INTERVAL = 15;
	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_ERR = -1;
	private static final String INCORRECT_LOGIN = "Incorrect Login or Password";
	private final static Logger logger = LogManager.getLogger(SecurityDAO.class);


	public UserRoleVO authenticate(String userId, String passwd) {
		Connection conn = null;
		UserRoleVO userRoleVO = new UserRoleVO();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();

			// Check in bhajan_book_user table, for a match based on given user id and SHA1
			// of the passwd.
			queryStr.append("select user_id, first_name, last_name, disabled_indic, disabled_until ");
			queryStr.append("from bhajan_book_user ");
			queryStr.append("where (user_id = '" + userId.toLowerCase() + "' or email_id = '" + userId.toLowerCase() +"' )");
			queryStr.append("and pass = sha1('" + passwd.trim() + "')");
			queryStr.append("and ( (disabled_indic = 'N') or (disabled_indic = 'Y' and disabled_until < now()))");

			System.out.println(queryStr);
			ResultSet rs = stmt.executeQuery(queryStr.toString());

			if (rs.next()) {
				userId = rs.getString("user_id");
				updateLoginAttempt(userId, true);
				userRoleVO.setStatus(LOGIN_SUCCESS);
				userRoleVO.setMesg("");
				userRoleVO.setUserId(userId);
				userRoleVO.setFirstName(rs.getString("first_name"));
				userRoleVO.setLastName(rs.getString("last_name"));
				// Todo: Retrieve user role information.
				
				System.out.println("success. getting playlist.");
				
				List<PlaylistVO> playlistList = this.getUserPlaylist(userId);
				userRoleVO.setUserPlaylistList(playlistList);
				return userRoleVO;
			}
			// No Match found.
			System.out.println("failure");
			updateLoginAttempt(userId, false);
			userRoleVO.setStatus(LOGIN_ERR);
			userRoleVO.setMesg(INCORRECT_LOGIN);
			return userRoleVO;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			e.printStackTrace();
			userRoleVO.setStatus(LOGIN_ERR);
			userRoleVO.setMesg(INCORRECT_LOGIN);
			return userRoleVO;
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
	
	public UserRoleVO getUserForSecKey(String secKey) {
		Connection conn = null;
		UserRoleVO userRoleVO = null;
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();

			queryStr.append("select b.user_id, b.first_name, b.last_name from bhajan_book_sec_key k, bhajan_book_user b");
			queryStr.append(" where k.sec_key = '" + secKey + "'");
			queryStr.append( " and k.user_id = b.user_id ");
			logger.info(queryStr);
			ResultSet rs = stmt.executeQuery(queryStr.toString());
			if (rs.next()) { 
				userRoleVO = new UserRoleVO();
				String userId = rs.getString("user_id");
				userRoleVO.setStatus(LOGIN_SUCCESS);
				userRoleVO.setMesg("");
				userRoleVO.setUserId(userId);
				userRoleVO.setFirstName(rs.getString("first_name"));
				userRoleVO.setLastName(rs.getString("last_name"));
				// Todo: Retrieve user role information.
				
				logger.info("success. getting playlist.");
				
				List<PlaylistVO> playlistList = this.getUserPlaylist(userId);
				userRoleVO.setUserPlaylistList(playlistList);
				return userRoleVO;
			}
			return userRoleVO;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			e.printStackTrace();
			return userRoleVO;
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
	
	public BaseVO changePassword(String userId, String oldPassword, String newPassword) {
		Connection conn = null;
		BaseVO retVO = new BaseVO();
	 	newPassword = newPassword.replaceAll("'", "''");
	 	oldPassword = oldPassword.replaceAll("'", "''");
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			
			if (oldPassword == null) {
				retVO.setStatus(BhajanBookConstants.FAILURE);
				retVO.setMesg("Empty Old Password");
				return retVO;
			}

			// find where userid and oldpass match
			// error if oldpass does not match
			// if match, change password 
			
			queryStr.append("select * from bhajan_book_user where user_id = '" + userId + "' ");
			queryStr.append("and pass = sha1('" + oldPassword.trim() + "') ");
			logger.info(queryStr);
			
			ResultSet rs = stmt.executeQuery(queryStr.toString());
			
			if (rs.next()) {
				// Ignore.
			} else {
				retVO.setStatus(BhajanBookConstants.FAILURE);
				retVO.setMesg("Current Password incorrect");
				return retVO;
			}
			queryStr = new StringBuffer();
			queryStr.append("update bhajan_book_user set pass = sha1('" + newPassword.trim() + "') ");
			queryStr.append(" where user_id = '" + userId + "' ");
			System.out.println(queryStr);
			int rc = stmt.executeUpdate(queryStr.toString());
			if (rc == 0) {
				throw new Exception("Error changing password. Contact support. ");
			}
			retVO.setStatus(BhajanBookConstants.SUCCESS);
			retVO.setMesg("Password changed");
			return retVO;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			e.printStackTrace();
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("Internal Error. Contact support");
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
	
	public void createSecKey(String userId, String secKey) {
		Connection conn = null;
		UserRoleVO userRoleVO = new UserRoleVO();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();

			queryStr.append("insert into bhajan_book_sec_key (sec_key, user_id, created_date) ");
			queryStr.append("values ('" + secKey + "', '" + userId + "', now())");
			
			logger.info(queryStr);
			int rc = stmt.executeUpdate(queryStr.toString());
			if (rc == 0) {
				throw new Exception("Error creating Security Key. Contact support. ");
			}
			return;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			e.printStackTrace();
			return;
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

	public void removeSecKey(String userId, String secKey) {
		Connection conn = null;
		UserRoleVO userRoleVO = new UserRoleVO();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();

			queryStr.append("delete from bhajan_book_sec_key   ");
			queryStr.append("where sec_key =  '" + secKey + "' and  user_id  = '" + userId + "'");
			
			logger.info(queryStr);
			stmt.executeUpdate(queryStr.toString());
			return;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			e.printStackTrace();
			return;
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
	
	public List<PlaylistVO> getUserPlaylist(String userId) {
		Connection conn = null;
		List<PlaylistVO> playlistList = new ArrayList<PlaylistVO>();
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			queryStr.append("select playlist_key, playlist_name, 'Y' own_yn, last_update from playlist\r\n");
			queryStr.append("where owner_user_id = '" + userId + "'\r\n" );
			queryStr.append(" union\r\n");
			queryStr.append("select s.playlist_key, p.playlist_name, 'N' own_yn, p.last_update ");
			queryStr.append(" from playlist_shared s, playlist p\r\n" );
			queryStr.append(" where s.playlist_key = p.playlist_key\r\n" );
			queryStr.append(" and s.user_id = '" + userId + "' \r\n");
			queryStr.append(" order by last_update desc ");
			

			ResultSet rs = stmt.executeQuery(queryStr.toString());
			
			while (rs.next()) {
				PlaylistVO btVO = new PlaylistVO();
				btVO.setPlaylistKey(rs.getString("playlist_key"));
				btVO.setPlaylistName(rs.getString("playlist_name"));
				String ownYN = rs.getString("own_yn");
				if ("Y".equals(ownYN)) {
					btVO.setOwned(true);
				} else {
					btVO.setOwned(false);
				}
				playlistList.add(btVO);
			}
			return playlistList;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			return playlistList;
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

	public int updateLoginAttempt(String userId, boolean loginOK) {
		Connection conn = null;
		StringBuffer queryStr = null;
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			queryStr = new StringBuffer("update bhajan_book_user ");
			if (loginOK) {
				queryStr.append("set incor_login_attempts = 0");
				queryStr.append(", disabled_indic = 'N'");
				queryStr.append(", disabled_until = null");
				queryStr.append(" where user_id = '" + userId.toLowerCase() + "'");
				stmt.executeUpdate(queryStr.toString());
				conn.commit();
				
				return 1;
			}
			queryStr.append(" set incor_login_attempts = incor_login_attempts + 1");
			queryStr.append(" where user_id = '" + userId.toLowerCase() + "'");
			int rc = stmt.executeUpdate(queryStr.toString());
			conn.commit();

			queryStr = new StringBuffer("update bhajan_book_user ");
			queryStr.append(" set  disabled_indic = 'Y'");
			queryStr.append(", disabled_until = now() + INTERVAL " + LOCKOUT_INTERVAL + " minute");
			queryStr.append(" where user_id = '" + userId.toLowerCase() + "'");
			queryStr.append(" and incor_login_attempts > " + MAX_INCOR_LOGIN_ATTEMPTS);

			stmt.executeUpdate(queryStr.toString());
			conn.commit();
			return rc;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: report error to support.
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			;
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
	
	/**
	 *  -- check email does not exist in bhajan_book_user 
	 *  -- check email does not exist in bhajan_book_new_user
	 *  -- add row to table
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param passwd
	 * @return
	 */

	public BaseVO registerAccount(String firstName, String lastName, String email, String passwd) {
		Connection conn = null;
		BaseVO retVO = new BaseVO();
		firstName = firstName.replaceAll("'", "''");
		lastName = lastName.replaceAll("'", "''");
		email = email.replaceAll("'", "''");
	 	passwd = passwd.replaceAll("'", "''");
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();

			queryStr.append("select * from bhajan_book_user where email_id = '" + email + "' ");
			logger.info(queryStr);
			
			ResultSet rs = stmt.executeQuery(queryStr.toString());
			
			if (rs.next()) {
				retVO.setStatus(BhajanBookConstants.FAILURE);
				retVO.setMesg("An account with this email address already exists");
				return retVO;
			}
		    queryStr = new StringBuffer();
			queryStr.append("select * from bhajan_book_new_user where email_id = '" + email + "' ");
			logger.info(queryStr);
			
			rs = stmt.executeQuery(queryStr.toString());
			
			if (rs.next()) {
				retVO.setStatus(BhajanBookConstants.FAILURE);
				retVO.setMesg("An account with this email address has already been registered. Please wait for the activation email.");
				return retVO;
			}
			
			
			queryStr = new StringBuffer();
			queryStr.append("insert into bhajan_book_new_user (first_name, last_name, email_id, pass, created_date)");
			queryStr.append("values ('" + firstName + "', '" + lastName + "', '" + email + "',");
			queryStr.append("sha1('" + passwd.trim() + "'), now())");
			logger.info(queryStr);
			int rc = stmt.executeUpdate(queryStr.toString());
			if (rc == 0) { 
				throw new Exception("Error registering account. Contact support. ");
			}
			retVO.setStatus(BhajanBookConstants.SUCCESS);
			retVO.setMesg("Account registered");
			return retVO;
		} catch (Exception e) {
			// TODO: report error to support.
			// Return empty list.
			e.printStackTrace();
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("Internal Error. Contact support");
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
}
