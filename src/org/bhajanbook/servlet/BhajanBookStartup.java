package org.bhajanbook.servlet;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bhajanbook.service.SessionManager;
import org.bhajanbook.util.BhajanBookConstants;

public class BhajanBookStartup extends HttpServlet {

	public void init() throws ServletException {
		// Do required initialization
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Starting Up");
		SessionManager sesMgr = new SessionManager();
		try {
		sesMgr.checkSession(request, response);
		} catch (Exception e) {
			throw new ServletException("Error processing session. Contact support.");
		}

		// Forward to the target page - MAIN page
		// RequestDispatcher rd = request.getRequestDispatcher(..page...); 
		// Forward to the target page.
		// rd.forward(request,  response);
		return;
	}


	public void destroy() {
		// do nothing.
	}
}
