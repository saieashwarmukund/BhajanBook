package org.bhajanbook.service;


import java.io.IOException;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bhajanbook.dao.SecurityDAO;
import org.bhajanbook.util.BhajanBookConstants;


public class SessionManager {
	
	public static final int SUCCESS = 1;
	public static final int FAILURE = 0;
	
	private final static Logger logger = LogManager.getLogger(SessionManager.class);

	private UserRoleVO checkPersistentLogin(HttpServletRequest request) {
		UserRoleVO userRoleVO = null;
		
		String cookieStr = this.getSecKey(request);
		if ("".equals(cookieStr)) {
			logger.info("Nothing in the cookie");
			return userRoleVO;
		}
		SecurityDAO secDAO = new SecurityDAO();
		userRoleVO = secDAO.getUserForSecKey(cookieStr);
		return userRoleVO;
	}
	public void checkSession(HttpServletRequest request, HttpServletResponse response) 
			throws Exception, ServletException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			System.out.println("Session found");
		} else {
			System.out.println("Session NOT found");
		}
		if ((session == null) || (session.getAttribute(BhajanBookConstants.BHAJAN_BOOK_USER) == null)) {
			
			if (session != null) {
				session.invalidate();
			}
			UserRoleVO userRoleVO = checkPersistentLogin(request);
			if (userRoleVO != null) {
				logger.info("PersistentLogin returned " + userRoleVO.getUserId() );
	 			createSession(request, response, "N", userRoleVO);

				RequestDispatcher rd = request.getRequestDispatcher(BhajanBookConstants.PAGE_MAIN); 
				rd.forward(request,  response);
				return;
			} else {
				System.out.println("Forwarding to Login Screen");
				RequestDispatcher rd = request.getRequestDispatcher(BhajanBookConstants.PAGE_LOGIN); 
				rd.forward(request,  response);
				return;
			}
		} else {
			System.out.println("Session and user found ");
			RequestDispatcher rd = request.getRequestDispatcher(BhajanBookConstants.PAGE_MAIN); 
			rd.forward(request,  response);
			return;
		}
	}
	public void createSession(HttpServletRequest request, HttpServletResponse response, String rememberMe, UserRoleVO userRoleVO) 
			throws IOException, Exception {
		HttpSession session = request.getSession(true);
		if (session == null) {
			throw new Exception("Error creating session. Contact Support");
		}
		System.out.println("Session created");
		session.setAttribute(BhajanBookConstants.BHAJAN_BOOK_USER, userRoleVO);
		System.out.println("Attribute set for " + userRoleVO.getFirstName());
		System.out.println(session.getAttribute(BhajanBookConstants.BHAJAN_BOOK_USER));
		
		if ("Y".equals(rememberMe)) {
			String randomStr = this.randomString(20);
			SecurityDAO secDAO = new SecurityDAO();
			randomStr = userRoleVO.getUserId() + randomStr;
			secDAO.createSecKey(userRoleVO.getUserId(), randomStr);
			
			Cookie c = new Cookie(BhajanBookConstants.COOKIE_USER, randomStr);
			c.setMaxAge(60 * 24 * 3600);
			c.setPath("/");
			response.addCookie(c);
			System.out.println("Cookie set for user");
		}
		return;
	}
	
	public UserRoleVO getRememberedUser(String secKey) {
		SecurityDAO secDAO = new SecurityDAO();
		return secDAO.getUserForSecKey(secKey);
	}
	private String getSecKey(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return "";
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(BhajanBookConstants.COOKIE_USER)) {
				String cookieStr = cookie.getValue();
				return cookieStr;
			}
		}
		return "";
	}
	
	public UserRoleVO getUser(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new Exception("Error getting session. Contact Support");
		}
		return (UserRoleVO) session.getAttribute(BhajanBookConstants.BHAJAN_BOOK_USER);
	}
	private String randomString(final int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder(length);
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String output = sb.toString();
		System.out.println(output);
		return output;
	}
	
	public void removeSession(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, Exception {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		
		UserRoleVO userRoleVO = this.getUser(request);
		session.invalidate();
		String cookieStr = this.getSecKey(request);
		if ("".equals(cookieStr)) {
			return;
		}
		SecurityDAO secDAO = new SecurityDAO();
		secDAO.removeSecKey(userRoleVO.getUserId(), cookieStr);
		
		// Can remove if value present.
	    Cookie cookie = new Cookie(BhajanBookConstants.COOKIE_USER, "");
	    cookie.setMaxAge(0);
	    cookie.setPath("/");

	    response.addCookie(cookie);
		return;
	}
}
