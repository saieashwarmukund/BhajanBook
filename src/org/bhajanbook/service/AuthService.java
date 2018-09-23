
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
import org.bhajanbook.dao.SecurityDAO;
import org.bhajanbook.util.BhajanBookConstants;
import org.bhajanbook.util.MailUtil;

@Path("/Authenticate")
public class AuthService {
	private final static Logger logger = LogManager.getLogger(AuthService.class);

	/**
	 * Default constructor.
	 */
	public AuthService() {
		// TODO Auto-generated constructor stub
	}

	@POST
	@Path("/ChangePassword")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseVO changePassword(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword)
			throws Exception {
		logger.info("password change service");
		int minLength = 5;
		BaseVO retVO = new BaseVO();
		SessionManager sesMgr = new SessionManager();
		UserRoleVO userRoleVO = sesMgr.getUser(request);
		String userId = userRoleVO.getUserId();
		// Throw exception if new password is blank

		if ((newPassword == null) || newPassword.trim().equals("")) {
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("New password is empty");
			return retVO;
		}
		// Check the length of the new password and throw exception if not enough.
		if (newPassword.trim().length() < minLength) {
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("New password must have minimum of " + minLength + " characters");
			return retVO;
		}
		// instantiate the DAO class
		// Call the method to reset the password.
		SecurityDAO secDAO = new SecurityDAO();
		retVO = secDAO.changePassword(userId, oldPassword, newPassword);

		return retVO;
	}

	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public UserRoleVO login(@FormParam("userId") String userId, @FormParam("passwd") String passwd,
			@FormParam("rememberMe") String rememberMe, @Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		SecurityDAO secDAO = new SecurityDAO();
		UserRoleVO retVO = secDAO.authenticate(userId, passwd);
		System.out.println(retVO.getMesg());
		if (retVO.getStatus() == SecurityDAO.LOGIN_SUCCESS) {
			System.out.println("Login Sucess. Creating session");
			SessionManager sesMgr = new SessionManager();
			sesMgr.createSession(request, response, rememberMe, retVO);
		}
		// ToDo: If rememberMe is set, set Cookie.
		return retVO;
	}

	@POST
	@Path("/Logout")
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
		logger.info("logout service");
		SessionManager sesMgr = new SessionManager();
		sesMgr.removeSession(request, response);
		return;
	}
	
	
	@POST
	@Path("/Register")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseVO registerAccount(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@FormParam("firstName") String firstName, @FormParam("lastName") String lastName, 
			@FormParam("email") String email, @FormParam("passwd") String passwd )
			throws Exception {
		logger.info("register account service");
		int minLength = 5;
		BaseVO retVO = new BaseVO();
		
		if ((passwd == null) || passwd.trim().equals("")) {
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("Password is empty");
			return retVO;
		}
		// Check the length of the new password and throw exception if not enough.
		if (passwd.trim().length() < minLength) {
			retVO.setStatus(BhajanBookConstants.FAILURE);
			retVO.setMesg("Password must have minimum of " + minLength + " characters");
			return retVO;
		}
		// instantiate the DAO class
		SecurityDAO secDAO = new SecurityDAO();
		retVO = secDAO.registerAccount(firstName, lastName, email, passwd);
		if (retVO.getStatus() == BhajanBookConstants.SUCCESS) {
			MailUtil.notifyAdmin("New User " + firstName + " " + lastName + " Registered",
					firstName + " " + lastName + " has registered for access.");
		}
		return retVO;
	}
}


