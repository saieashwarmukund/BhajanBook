
package org.bhajanbook.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
    @Path("/Show") 
    @Produces(MediaType.APPLICATION_JSON)
    public BhajanLyricsVO getBhajan(@Context HttpHeaders headers, @Context HttpServletRequest request,  @QueryParam("id") String id) {
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   List<String> userAgentList = headers.getRequestHeader("User-Agent");
 	   String userAgent = "";
 	   if (userAgentList != null) {
 		   userAgent = userAgentList.get(0);
 	   }
 	   String ip = request.getRemoteAddr();
 	   BhajanLyricsVO bhajanVO = bhajanDAO.getBhajan(id);
 	   bhajanDAO.logBhajanAccess(id, userAgent, ip);
 	   return bhajanVO;
    }
    
    
    @GET
    @Path("/Search") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<BhajanTitleVO> searchBhajan(@Context HttpHeaders headers, @Context HttpServletRequest request,  @QueryParam("searchstr") String searchStr) {
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   String userId = ""; // For later.

 	   List<String> userAgentList = headers.getRequestHeader("User-Agent");
 	   String userAgent = "";
 	   if (userAgentList != null) {
 		   userAgent = userAgentList.get(0);
 	   }
 	   String ip = request.getRemoteAddr();
 	   List<BhajanTitleVO> bhajanVOlist = bhajanDAO.searchBhajan(searchStr);
 	   bhajanDAO.logBhajanSearch(searchStr, userId, userAgent, ip, bhajanVOlist.size());
 	   return bhajanVOlist;
    }
    

}
