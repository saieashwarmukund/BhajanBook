
package org.bhajanbook.service;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public ThoughtForTheDayVO getTFTD() {
 	   ThoughtForTheDayVO thought = new ThoughtForTheDayVO();
 	   ThoughtForTheDayDAO tftdDAO = new ThoughtForTheDayDAO();
 	   String msg = tftdDAO.getRandomThoughtForTheDay();
	   thought.setThought(msg);
 	   return thought;
    }  
}
