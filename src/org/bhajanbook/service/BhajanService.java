
package org.bhajanbook.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;

import org.bhajanbook.dao.BhajanDAO;

@Path("/Bhajan")
public class BhajanService  {

    /**
     * Default constructor. 
     */
    public BhajanService() {
        // TODO Auto-generated constructor stub
    }
    
    @GET
    @Path("/") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<BhajanTitleVO> getBhajanTitles(@QueryParam("deity") String deity) {
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   List<BhajanTitleVO> bhajanList = bhajanDAO.getBhajanTitleList(deity);
 	   return bhajanList;
    }  
    @GET
    @Path("/show") 
    @Produces(MediaType.APPLICATION_JSON)
    public BhajanLyricsVO getBhajan(@QueryParam("id") String id, @Context HttpHeaders headers) {
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   
 	   //String userAgent = headers.getRequestHeader("user_agent").get(0);
 	   //System.out.println("UserAgent is " +  userAgent);
 	   BhajanLyricsVO bhajanVO = bhajanDAO.getBhajan(id);
 	   return bhajanVO;
    }  

}
