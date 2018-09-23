
package org.bhajanbook.service;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bhajanbook.dao.MiscDAO;
import org.bhajanbook.util.BhajanBookConstants;
import org.bhajanbook.util.MailUtil;

@Path("/Feedback")
public class FeedbackService {
	private final static Logger logger = LogManager.getLogger(FeedbackService.class);

	/**
	 * Default constructor.
	 */
	public FeedbackService() {
		// TODO Auto-generated constructor stub
	}

	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseVO captureFeedback(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@FormParam("message") String message)
			throws Exception {
		logger.info("feedback service");
		BaseVO retVO = new BaseVO();
		SessionManager sesMgr = new SessionManager();
		UserRoleVO userRoleVO = sesMgr.getUser(request);
		String userId = userRoleVO.getUserId();
		if (message.trim().length() <= 0) {
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("Feedback message is blank");
			return retVO;
		}
		// instantiate the DAO class
		// Call the method to reset the password.
		MiscDAO miscDAO = new MiscDAO();
		retVO = miscDAO.captureFeedback(userId, message);
		if (retVO.getStatus() == BhajanBookConstants.SUCCESS) {
			MailUtil.notifyAdmin("Feedback from " + userId + " recieved",
					userId + "'s message:\n" + message);
		}
		return retVO;
	}

	
}


