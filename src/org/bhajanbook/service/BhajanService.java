
package org.bhajanbook.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bhajanbook.dao.BhajanDAO;
import org.bhajanbook.dao.SecurityDAO;
import org.bhajanbook.util.BhajanBookConstants;

@Path("/Bhajan")
public class BhajanService  {
    /**
     * Default constructor. 
     */
	private final static Logger logger = LogManager.getLogger(BhajanService.class);

    public BhajanService() {
        // TODO Auto-generated constructor stub
    }
    
    @POST
    @Path("/AddToFavorite") 
    @Produces(MediaType.APPLICATION_JSON)
    public BaseVO addToFavorite(@Context HttpHeaders headers, @Context HttpServletRequest request, 
    		@FormParam("id") String id)
    throws Exception {
    	logger.info("addToFavorite called.");
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   BaseVO returnVO = new BaseVO();
 	   // System.out.println("Id to add is " + id);
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); 
 	   
 	   returnVO = bhajanDAO.addToFavorite(userId, id);
 	   return returnVO;
    }  
    
    @POST
    @Path("/AddToPlaylist") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlaylistVO> addToPlaylist(@Context HttpHeaders headers, @Context HttpServletRequest request, 
    		@FormParam("playlistId") String playlistId, @FormParam("id") String id)
    throws Exception {
    	logger.info("addToPlaylist called.");
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); 
 	   BaseVO retVO = bhajanDAO.addToPlaylist(userId, playlistId, id);
 	   if (retVO.getStatus() != BhajanBookConstants.SUCCESS) {
 		   throw new Exception (retVO.getMesg());
 	   }
 	   
 	   SecurityDAO secDAO = new SecurityDAO();
 	   List<PlaylistVO> newPlaylistList = secDAO.getUserPlaylist(userId);
 	   return newPlaylistList;
    }
    
    
    @POST
    @Path("/ClearPlaylist") 
    @Produces(MediaType.APPLICATION_JSON)
    public  BaseVO    clearPlaylist(@Context HttpHeaders headers, @Context HttpServletRequest request,
    		 @FormParam("playlistKey") String playlistKey) throws Exception {
    	logger.info("clearPlaylist called.");
  	   BhajanDAO bhajanDAO = new BhajanDAO();
  	   SessionManager sesMgr = new SessionManager();
  	   UserRoleVO userRoleVO = sesMgr.getUser(request);
  	   String userId = userRoleVO.getUserId(); 
  	   BaseVO retVO = bhajanDAO.clearPlaylist(userId, playlistKey);
  	   if (retVO.getStatus() != BhajanBookConstants.SUCCESS) {
  		   throw new Exception (retVO.getMesg());
  	   }
  	   return retVO;
    }
    
    
    @POST
    @Path("/CreatePlaylist") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlaylistVO> createPlaylist(@Context HttpHeaders headers, @Context HttpServletRequest request,  
    		@FormParam("playlistName") String playlistName, @FormParam("id") String id)
    throws Exception {
    	logger.info("createPlaylist called.");
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); 
 	   BaseVO retVO = bhajanDAO.createPlaylist(userId, playlistName, id);
 	   if (retVO.getStatus() != BhajanBookConstants.SUCCESS) {
 		   throw new Exception (retVO.getMesg());
 	   }
 	   SecurityDAO secDAO = new SecurityDAO();
 	   List<PlaylistVO> newPlaylistList = secDAO.getUserPlaylist(userId);
 	   return newPlaylistList;
    }
    
    
    @POST
    @Path("/DeletePlaylist") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlaylistVO>  deletePlaylist(@Context HttpHeaders headers, @Context HttpServletRequest request,
    		 @FormParam("playlistKey") String playlistKey) throws Exception {
    	logger.info("deletePlaylist called.");
  	   BhajanDAO bhajanDAO = new BhajanDAO();
  	   SessionManager sesMgr = new SessionManager();
  	   UserRoleVO userRoleVO = sesMgr.getUser(request);
  	   String userId = userRoleVO.getUserId(); 
  	   BaseVO retVO = bhajanDAO.deletePlaylist(userId, playlistKey);
  	   if (retVO.getStatus() != BhajanBookConstants.SUCCESS) {
  		   throw new Exception (retVO.getMesg());
  	   }
  	   
  	   SecurityDAO secDAO = new SecurityDAO();
  	   List<PlaylistVO> newPlaylistList = secDAO.getUserPlaylist(userId);
  	   return newPlaylistList;
    }
    
    @GET
    @Path("/Show") 
    @Produces(MediaType.APPLICATION_JSON)
    public BhajanLyricsVO getBhajan(@Context HttpHeaders headers, @Context HttpServletRequest request,  
    		@QueryParam("id") String id) throws Exception {
    	logger.info("getBhajan called.");

 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   List<String> userAgentList = headers.getRequestHeader("User-Agent");
 	   String userAgent = "";
 	   if (userAgentList != null) {
 		   userAgent = userAgentList.get(0);
 	   }
 	   String ip = request.getRemoteAddr();
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); // For later.
 	   BhajanLyricsVO bhajanVO = bhajanDAO.getBhajan(userId, id);
 	   bhajanDAO.logBhajanAccess(id, userId, userAgent, ip);
 	   return bhajanVO;
    }
    
    @GET
    @Path("/") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<BhajanTitleVO> getBhajanTitles(@QueryParam("deity") String deity) {
    	logger.info("getBhajanTitles called.");

 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   List<BhajanTitleVO> bhajanList = bhajanDAO.getBhajanTitleList(deity);
 	   return bhajanList;
    }  
    @GET
    @Path("/Playlist") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<BhajanTitleVO> getPlaylistBhajans(@QueryParam("playlistId") String playlistId, @Context HttpServletRequest request)
    throws Exception {
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); 
 	   List<BhajanTitleVO> bhajanList = bhajanDAO.getPlaylistBhajans(userId, playlistId);
 	   return bhajanList;
    }
    
    @GET
    @Path("/PlaylistWithBhajan") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> playlistWithBhajan(@Context HttpHeaders headers, @Context HttpServletRequest request, 
    		@QueryParam("id") String id)
    throws Exception {
    	logger.info("playlistWithBhajan called.");

 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); 
 	   List<String> playlistList = bhajanDAO.playlistWithBhajan(userId, id);
 	   
 	   return playlistList;
    }
    
    @POST
    @Path("/RemoveFromFavorite") 
    @Produces(MediaType.APPLICATION_JSON)
    public BaseVO removeFromFavorite(@Context HttpHeaders headers, @Context HttpServletRequest request, 
    		@FormParam("id") String id)
    throws Exception {
    	logger.info("removeFromFavorite called.");

 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   BaseVO returnVO = new BaseVO();
 	   
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); 
 	   
 	   returnVO = bhajanDAO.removeFromFavorite(userId, id);
 	   return returnVO;
    }
    
    
    @POST
    @Path("/RemoveFromPlaylist") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlaylistVO>  removeFromPlaylist(@Context HttpHeaders headers, @Context HttpServletRequest request,
    		@FormParam("playlistId") String playlistId, @FormParam("id") String id) throws Exception {
    	logger.info("removeFromPlaylist called.");
  	   BhajanDAO bhajanDAO = new BhajanDAO();
  	   SessionManager sesMgr = new SessionManager();
  	   UserRoleVO userRoleVO = sesMgr.getUser(request);
  	   String userId = userRoleVO.getUserId(); 
  	   BaseVO retVO = bhajanDAO.removeFromPlaylist(userId, playlistId, id);
  	   if (retVO.getStatus() != BhajanBookConstants.SUCCESS) {
  		   throw new Exception (retVO.getMesg());
  	   }
  	   
  	   SecurityDAO secDAO = new SecurityDAO();
  	   List<PlaylistVO> newPlaylistList = secDAO.getUserPlaylist(userId);
  	   return newPlaylistList;
    }
    
    @POST
    @Path("/RenamePlaylist") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlaylistVO> renamePlaylist(@Context HttpHeaders headers, @Context HttpServletRequest request,  
    		@FormParam("playlistId") String playlistId, @FormParam("newName") String newPlaylistName)
    throws Exception {
    	logger.info("renamePlaylist called.");
 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); 
 	   
 	   BaseVO retVO = bhajanDAO.renamePlaylist(userId, playlistId, newPlaylistName);
 	   if (retVO.getStatus() != BhajanBookConstants.SUCCESS) {
 		   throw new Exception (retVO.getMesg());
 	   }
 	   SecurityDAO secDAO = new SecurityDAO();
 	   List<PlaylistVO> newPlaylistList = secDAO.getUserPlaylist(userId);
 	   return newPlaylistList;
    }
    
    
    @GET
    @Path("/Search") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<BhajanTitleVO> searchBhajan(@Context HttpHeaders headers, @Context HttpServletRequest request,  @QueryParam("searchstr") String searchStr) 
    throws Exception {
    	logger.info("searchBhajan called.");

 	   BhajanDAO bhajanDAO = new BhajanDAO();
 	   SessionManager sesMgr = new SessionManager();
 	   UserRoleVO userRoleVO = sesMgr.getUser(request);
 	   String userId = userRoleVO.getUserId(); // For later.

 	   List<String> userAgentList = headers.getRequestHeader("User-Agent");
 	   String userAgent = "";
 	   if (userAgentList != null) {
 		   userAgent = userAgentList.get(0);
 	   }
 	   String ip = request.getRemoteAddr();
 	   List<BhajanTitleVO> bhajanVOlist = bhajanDAO.searchBhajan(searchStr, userId, userAgent, ip, true);
 	   return bhajanVOlist;
    }
}
