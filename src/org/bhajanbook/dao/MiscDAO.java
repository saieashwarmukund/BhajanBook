package org.bhajanbook.dao;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bhajanbook.db.DBConnection;
import org.bhajanbook.service.BaseVO;
import org.bhajanbook.util.BhajanBookConstants;

public class MiscDAO {
	private final static Logger logger = LogManager.getLogger(MiscDAO.class);

	public BaseVO captureFeedback(String userId, String message) {
		Connection conn = null;
		BaseVO retVO = new BaseVO();
		message = message.replaceAll("'", "''");
		try {
			// Get DB Connection.
			conn = DBConnection.getDBConnection();
			Statement stmt = conn.createStatement();
			StringBuffer queryStr = new StringBuffer();
			queryStr.append("insert into feedback (user_id, message, date_recieved)");
			queryStr.append("values ('" + userId + "', '" + message + "', now())");
			logger.info(queryStr);
			int rc = stmt.executeUpdate(queryStr.toString());
			if (rc == 0) { 
				throw new Exception("Error sending feedback. Contact support. ");
			}
			retVO.setStatus(BhajanBookConstants.SUCCESS);
			retVO.setMesg("Message recieved. Thank you for your feedback.");
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
