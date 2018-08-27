
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

@Path("/Authenticate")
public class AuthService  {
	private final static Logger logger = LogManager.getLogger(AuthService.class);

    /**
     * Default constructor. 
     */
    public AuthService() {
        // TODO Auto-generated constructor stub
    }
    
    @POST
    @Path("/") 
    @Produces(MediaType.APPLICATION_JSON)
    public UserRoleVO login(@FormParam("userId") String userId, 
    		@FormParam("passwd") String passwd, @FormParam("rememberMe") String rememberMe,
    		@Context HttpServletRequest request,
    		@Context HttpServletResponse response
    		) throws Exception {
  	   SecurityDAO secDAO = new SecurityDAO();
 	   UserRoleVO retVO  = secDAO.authenticate(userId, passwd);
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
    public void logout(
    		@Context HttpServletRequest request,
    		@Context HttpServletResponse response
    		) throws Exception {
    	logger.info("logout service");
    	SessionManager sesMgr = new SessionManager();
 		sesMgr.removeSession(request, response);
 	   return;
    } 
}


