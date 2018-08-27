
package org.bhajanbook.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bhajanbook.dao.SecurityDAO;
import org.bhajanbook.dao.ThoughtForTheDayDAO;

import javax.ws.rs.Path;


@Path("/ThoughtForTheDay")
public class ThoughtForTheDayService  {

    /**
     * Default constructor. 
     */
    public ThoughtForTheDayService() {
        // TODO Auto-generated constructor stub
    }
    
    @GET
    @Path("/") 
    @Produces(MediaType.APPLICATION_JSON)
    public ThoughtForTheDayVO getTFTD(@Context HttpServletRequest request) throws Exception {
  	   SessionManager sesMgr = new SessionManager();
  	   UserRoleVO userRoleVO = sesMgr.getUser(request);
  	   String userId = userRoleVO.getUserId(); 

 	   ThoughtForTheDayVO thought = new ThoughtForTheDayVO();
 	   ThoughtForTheDayDAO tftdDAO = new ThoughtForTheDayDAO();
 	   String msg = tftdDAO.getRandomThoughtForTheDay();
	   thought.setThought(msg);
	   SecurityDAO secDAO = new SecurityDAO();
	   List<PlaylistVO> userPlaylistList = secDAO.getUserPlaylist(userId);
	   thought.setUserPlaylistList(userPlaylistList);
 	   return thought;
    }  
}
