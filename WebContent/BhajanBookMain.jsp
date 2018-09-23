<!DOCTYPE html>
<%@page import="org.bhajanbook.service.UserRoleVO"%>
<%@page import="org.bhajanbook.service.SessionManager"%>
<%@page import="org.bhajanbook.util.BhajanBookConstants"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="Bootstrap4/css/bootstrap.min.css">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link href='https://fonts.googleapis.com/css?family=Great Vibes|Acme' rel='stylesheet'>
<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="https://use.fontawesome.com/000aaaa000.js"></script>
<script src="Bootstrap4/js/bootstrap.min.js"></script>
<script src="jquery.twbsPagination.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="BhajanBook.css">
<link rel="icon" href="https://i.imgur.com/wEVYj2v.png" type="image" sizes="16x16">

<title>BhajanBook</title>

</head>
<%
if(null == session.getAttribute("BHAJAN_BOOK_USER")){
    Cookie cookie = null;
    Cookie[] cookies = null;
    
    // Get an array of Cookies associated with the this domain
    cookies = request.getCookies();
    
    boolean cookieFound = false;
    String secKey = "";
    if( cookies != null ) {
       for (int i = 0; i < cookies.length; i++) {
          cookie = cookies[i];
          if (cookie.getName().equals(BhajanBookConstants.COOKIE_USER)) {
             cookieFound = true;
             System.out.print("Value: " + cookie.getValue()+" <br/>");
             break;
          }
       }
    }
    
    if (cookieFound) {
        secKey = cookie.getValue();
		SessionManager sesMgr = new SessionManager();
		UserRoleVO userVO = sesMgr.getRememberedUser(secKey);
		if (userVO == null) { 
		    RequestDispatcher rd = request.getRequestDispatcher("LOGIN"); 
			rd.forward(request,  response);
			return;			
		}
		sesMgr.createSession(request, response, "N", userVO);
		RequestDispatcher rd = request.getRequestDispatcher("MAIN"); 
		rd.forward(request,  response);
		return;  			 
    }
    
    System.out.println("No cookie found.");
    RequestDispatcher rd = request.getRequestDispatcher("LOGIN"); 
	rd.forward(request,  response);
	return;
}


%>

<body class="Site">
<div class="Site-content">

<div class="container-fullwidth" id="nav">
<div style="background-color:white">
    <nav class="navbar navbar-expand-lg navbar-light bg-white w3-animate-opacity">
        <a class="logo navbar-brand" href="/BhajanBook/" ><img src="/BhajanBook/images/BhajanBook_logo.png" style="height:40px;"></a>
       
        <img onclick="showSearchDiv()" class="img-responsive" src="https://i.imgur.com/NgZcVuz.png" style="height:20px;"></img>
        
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent">
        		<span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/BhajanBook/">Home</a>
                </li>
                <!--<li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" data-toggle="dropdown">Login</a>
						<ul id="login-dp" class="dropdown-menu">
							<li>
								 <div class="row">
										<div class="col-md-12">
											 <form class="form" role="form" method="post" action="login" accept-charset="UTF-8" id="login-nav">
													<div class="form-group">
														 <label class="sr-only" for="exampleInputEmail2">Email address</label>
														 <input type="email" class="form-control" id="exampleInputEmail2" placeholder="Email address" required>
													</div>
													<div class="form-group">
														 <label class="sr-only" for="exampleInputPassword2">Password</label>
														 <input type="password" class="form-control" id="exampleInputPassword2" placeholder="Password" required>
			                                             <div class="help-block text-right"><a href="">Forget your password ?</a></div>
													</div>
													<div class="form-group">
														 <button type="submit" class="btn btn-dark btn-block">Sign in</button>
													</div>
													
													<div class="checkbox">
														 <label>
														 <input type="checkbox"> Remember Me
														 </label>
													</div>
											 </form>
										</div>
										<div class="bottom text-center">
											New here? <a href="#"><b>Register Here</b></a>
										</div>
								 </div>
							</li>
						</ul>
			        </li> -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" data-toggle="dropdown">Bhajans</a>
                	<div class="dropdown-menu text-center text-lg-left" style="border:0px;">
                		<a class="dropdown-item" onclick="showDeityBhajans('All')" data-toggle="collapse" data-target=".navbar-collapse.show">All</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Devi')" data-toggle="collapse" data-target=".navbar-collapse.show">Devi</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Eashwaramba')" data-toggle="collapse" data-target=".navbar-collapse.show">Eashwaramba</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('English')" data-toggle="collapse" data-target=".navbar-collapse.show">English</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Ganesha')" data-toggle="collapse" data-target=".navbar-collapse.show">Ganesha</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Guru')" data-toggle="collapse" data-target=".navbar-collapse.show">Guru</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Hanuman')" data-toggle="collapse" data-target=".navbar-collapse.show">Hanuman</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Krishna')" data-toggle="collapse" data-target=".navbar-collapse.show">Krishna</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Narayana')" data-toggle="collapse" data-target=".navbar-collapse.show">Narayana</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Rama')" data-toggle="collapse" data-target=".navbar-collapse.show">Rama</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Sai')" data-toggle="collapse" data-target=".navbar-collapse.show">Sai</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Sarva Dharma')" data-toggle="collapse" data-target=".navbar-collapse.show">Sarva Dharma</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Shirdi')" data-toggle="collapse" data-target=".navbar-collapse.show">Shirdi</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Shiva')" data-toggle="collapse" data-target=".navbar-collapse.show">Shiva</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Subramanya')" data-toggle="collapse" data-target=".navbar-collapse.show">Subramanya</a>
                		<a class="dropdown-item" onclick="showDeityBhajans('Vitthala')" data-toggle="collapse" data-target=".navbar-collapse.show">Vitthala</a>
                	</div> 
                </li>
                                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" data-toggle="dropdown">Account</a>
                	<div class="dropdown-menu text-center text-lg-left" style="border:0px;">
                		<a class="dropdown-item" onclick="showPlaylistMenu()" data-toggle="collapse" data-target=".navbar-collapse.show">Playlists</a>
                		<a class="dropdown-item" id="changePasswordBtn" onclick="changePasswordModal()" data-toggle="modal" data-target="#changePasswordDiv">Change Password</a>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="logout()">Log Out</a>
                </li>
            </ul>
        </div>
    </nav>
    </div>
</div>


  <!-- Modal -->
  <div class="modal fade" id="changePasswordDiv" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Change Password</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body">
        
		  <form action="" id="chgPwdForm" class="form">
		    <div class="form-group">
		      <label class="form-label" for="oldPass">Enter your old password</label>
		      <input id="oldPass" class="form-input" type="password" />
		    </div>
		    <div class="form-group">
		      <label class="form-label" for="newPass">New password</label>
		      <input id="newPass" class="form-input" type="password" />
		    </div>
		    <div class="form-group">
		      <label class="form-label" for="confirmNewPassword">Confirm password</label>
		      <input id="confirmNewPass" class="form-input" type="password" />
		    </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-dark" id="submitPass" onclick="changePasswordSubmit()">Submit</button>
          <button type="button" class="btn btn-light" data-dismiss="modal">Close</button>
        </div>
        </div>
      </div>
      
    </div>
  </div>



<!-- The Modal -->
  <div class="modal fade" id="renamePlaylistDiv">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Rename Playlist</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          <input type="hidden" id="playlistId">
          <input type="text" id="newPlaylistName">
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
        	<button type="button" class="btn btn-dark" id="submitPass" onclick="renamePlaylist('playlistId', 'newPlaylistName')" data-dismiss="modal">Rename</button>
        </div>
        
      </div> 
    </div>
  </div>


<div id="search_bar" style="height:99px;background-color:white;">
		<div class="w3-animate-opacity search_field" style="padding-top:30px;">
			<img class="img-responsive" src="https://i.imgur.com/gN3jfB1.png" onclick="showSearchDiv(); showMainPage()" style="height:20px;">
			<input id="search_field" placeholder="Search for Bhajan" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Search for Bhajan'" type="text" style="text-align:center;height:40px; width:70%;font-size:23px;">
			<img id="search_icon" class="img-responsive" src="https://i.imgur.com/NgZcVuz.png" onclick="showSearchBhajans('bhajan','search_field')" style="height:23px;">
		</div>
	</div>

 <div id="TFTD_card">
	<div class="fade-in">
	  <div id="image-band">
	     <img class="responsive" src="images/Baba_bg.png"/>
	     <div class="top-right TFTD" style="font-size:3vw; color:maroon"></div>
	  </div>
	</div>
 </div>
 
 
 
 
<div id="main_panel" class="center-block w3-animate-opacity">
<div class="row">
  <div id="deity_card" class="col-sm-3 w-50 fade-in">
    <div class="row">
      <div class="" style="padding-left:30px; padding-bottom:20px;">
            <div id="deity_image" class="">
            </div>
      </div>
    </div>
  </div>
 
  <div id="bhajans_card" class="col-sm-9 w3-animate-opacity">
    <div class="row" >
      <div class="card-body col-md-9 col-sm-9 col-lg-6 offset-lg-1 text-left text-md-left p-2" style="padding-left:10px">
        <div id="bhajans_list_title" class="card-title" style="font-weight:bold;font-size:150%; text-align:center"></div>
        <div class="subhead text-center text-md-center" id="subhead"></div>
        	<div class="container" style="padding:10px 20px;">
        			<div style="overflow:scroll; height:400px; overflow-x:hidden;overflow-y: scroll; /* has to be scroll, not auto */
  -webkit-overflow-scrolling: touch;">
						<table id="bhajan_table" class="table table table-hover flex-column" cellspacing="0" width="100%">
						<colgroup><col width="100%"></colgroup>
						<tbody id="bhajan_list_page"></tbody>
						</table>
					</div>
			</div>
	</div>

	<div class="col-sm-9 col-md-9 col-lg-3" id="playlist_options" style="display:none">
        <h5 class="mt-4" style="font-weight:bold;"> Options</h5>
        <ul class="nav flex-column nav-pills" style=";">
            <li class="nav-item" >
                <a class="nav-link" onclick="clearPlaylist()" href="#">Clear playlist</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" onclick="deletePlaylist()" href="#">Delete playlist</a>
            </li>
             <li class="nav-item" id="renamePlaylist">
                <a class="nav-link" onclick="renamePlaylistModal()" href="#" data-toggle="modal" data-target="#renamePlaylistDiv">Rename playlist</a>
            </li>
        </ul>
    </div>
    
    
</div>
</div>

  
  <div id="lyrics_card" class="col-sm-9 w3-animate-opacity">
      <div class="card-body col-md-12 text-center text-md-left p-2">
        <u><h5 class="card-title" id="bhajan_title" style="font-size:150%"></h5></u>
        <pre class="card-text" id="bhajan_lyrics" style="font-family:arial; font-size:100%"></pre>
        <div id="bhajan_audio"></div>
        <div class="center col-md-3">
        	<div class="playlist_class" id="favorite" style="position: relative; top:6px; left:-15px"></div>
        	<div class="playlist_class" id="add_to_playlist" style="position: relative; left:4vh"></div>
        	
			<!-- Playlist Modal -->
			  <div class="modal fade" id="playlistModal" role="dialog">
			    <div class="modal-dialog">
			    
			      <!-- Add to Playlist Modal content-->
			      <div class="modal-content">
			        <div class="modal-header">
			          <h4 class="modal-title">Add to Playlist</h4>
			          <button type="button" class="close" data-dismiss="modal">&times;</button>
			        </div>
			        <div class="modal-body text-left" id="playlist_add_checklist" style="overflow-y: scroll; max-height:150px;">
						<form onSubmit="return false;" >
						  <div id="playlist_checkbox">
						  </div>
						  <div id="create_playlist_section" style="display:none">
  			              <input id="new_playlist_name" type="text" minlength="3" maxlength="30"  placeholder="Enter playlist name" style="padding-left:10px;" required/>
  			              </div>						  
						  <input type="Button" value="Create" id="submit_playlist_new" onclick="createPlaylist('new_playlist_name')" data-dismiss="modal">
						</form>
			        </div>
			        <div class="modal-footer">
			          <div id="create_playlist_hdr">
			          <button id="create_playlist_btn" type="button" class="btn btn-default" data-toggle="modal" 
			             onclick="toggleDiv('playlist_checkbox'); showSection('submit_playlist_new'); toggleDiv('create_playlist_section'); hideSection('create_playlist_hdr')">
			             Create New Playlist</button>
			          </div>
			        </div>
			      </div> 
			    </div>
			  </div>
			  
			  
  
        </div>
        <br>
        <div id="bhajan_meaning" class="col-md-9"></div>
        <div id="bhajan_shruti" class="col-md-9"></div>
        <div id="bhajan_raaga" class="col-md-9"></div>
        <div id="bhajan_beat" class="col-md-9"></div>
        <div id="bhajan_id_div"></div>
        <a  onclick="goBackToBhajans()" class="btn btn-light">Back</a>
      </div>
  </div>
</div>
</div>

 <div id="contact_card" class="text-center pagination-centered col-sm-9">
      <div class="card-body col-md-12 text-center text-md-left p-2">
        <u><h5 class="card-title text-center"style="font-size:150%"></h5></u>
        
        
    <div class="row w3-animate-opacity">
        <div class="col-md-8">
            <div class="well well-sm">
                <form>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="name">
                                Name</label>
                            <input type="text" class="form-control" id="name" placeholder="Enter name" required="required" />
                        </div>
                        <div class="form-group">
                            <label for="email">
                                Email Address</label>
                            <div class="input-group">
                                <span class="input-group-addon"><span class="glyphicon glyphicon-envelope"></span>
                                </span>
                                <input type="email" class="form-control" id="email" placeholder="Enter email" required="required" /></div>
                        </div>
                        <div class="form-group">
                            <label for="subject">
                                Subject</label>
                            <select id="opts" name="subject" class="form-control" required="required" onchange = "showForm()">
                                <option value="feedback" selected >Feedback</option>
                                <option value="bhajan_sugg">Bhajan Suggestions</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                        
                            <div id = "feedback" style="display:block">
							<form name= "feedf">
		                          <div class="form-group">
		                            <label for="name">
		                                Feedback</label>
		                            <textarea name="message" id="message" class="form-control" rows="9" cols="25" required="required"
		                                placeholder="Feedback"></textarea>
		                        </div>
		                        <div class="col-md-12 center-block">
                       				<button onclick="sendFeedback()" type="submit" class="btn btn-light pull-right" id="btnContactUs">
                           				Submit</button>
                    			</div>
							</form>
							</div>



							<div id = "suggestions" style="display:none">
							<form name= "suggf">
							
							<br>
							  <text style="color:red">* Required</text><br><br>
							  <text>Bhajan Title <text style="color:red">*</text></text>
							  <input type="text" name="Title"  required>
							  <br><br>
							  <text>Lyrics <text style="color:red">*</text></text>
							  <input type="text" name="Lyrics"  required>
							  <br><br>
							  <text>Audio Link</text>
							  <input type="text" name="Lyrics">
							  <br><br>
							  <text>Meaning</text>
							  <input type="text" name="Lyrics">
							  <br><br>
							  <text>Shruti</text>
							  <input type="text" name="Lyrics">
							  <br><br>
							  <text>Raaga</text>
							  <input type="text" name="Lyrics">
							  <br><br>
							  <text>Beat</text>
							  <input type="text" name="Lyrics">
							  <br><br>
							  <div class="col-md-12 center-block">
                       			<button type="submit" class="btn btn-light pull-right" id="btnContactUs">
                            	Submit</button>
                    		</div>
							</form> 
							</div>

                    </div>
                </div>
                </form>
            </div>
        </div>
        <div class="col-md-4">
        </div>
    </div>
</div>
</div>


<div id="analytics_card">
<div class="card-body col-md-12 text-center text-md-left p-2">
        <u><h5 class="card-title" id="bhajan_title" style="font-size:150%"></h5></u>
        <pre class="card-text" id="bhajan_lyrics" style="font-family:arial; font-size:100%"></pre>
        <div id="bhajan_audio"></div>
        <div id="bhajan_meaning"></div>
        <div id="bhajan_raaga"></div>
        <div id="bhajan_beat"></div>
        <a  onclick="goBackToBhajans()" class="btn btn-light">Back</a>
      </div>
</div>


</div>

<div class="row w3-animate-opacity" style="padding-top:25px">
<div class="mx-auto">
<table><tr>
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Devi')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/devi_icon.png"><br>Devi</a></td>
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Ganesha')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/ganesha_icon.png"><br>Ganesha</a></td>
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Guru')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/guru_icon.png"><br>Guru</a></td>
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Hanuman')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/hanuman_icon.png"><br>Hanuman</a></td>
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Krishna')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/krishna_icon.png"><br>Krishna</a></td>
</tr></table>
</div>
</div>

<div class="row w3-animate-opacity">
<div class="mx-auto">
<table>
<tr >
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Narayana')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/narayana_icon.png"><br>Narayana</a></td>
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Rama')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/rama_icon.png"><br>Rama</a></td>
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Sai')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/sai_icon.png"><br>Sai</a></td>
<td class="small" valign="middle" align="center"><br><a href="#" onclick="showDeityBhajans('Sarva Dharma')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/sarva_dharma_icon.png"><br>Sarva<br>Dharma</a></td>
<td class="small" valign="middle" align="center"><a href="#" onclick="showDeityBhajans('Shiva')" class="deity_icons"><img class="icon" src="/BhajanBook/images/icons/shiva_icon.png"><br>Shiva</a></td></tr></table></div>
</div>
<br><br><br>
</div>

<!-- Footer -->
<footer class="page-footer font-small special-color-dark pt-4 bg-white">

    <!-- Footer Elements -->
    <div class="container">

      <!-- Social buttons -->
      <ul class="list-unstyled list-inline text-center">
        <a class="" href="#" onclick="showContact()">Contact</a><br>
      </ul>
      <!-- Social buttons -->

    </div>
    <!-- Footer Elements -->
    <!-- Copyright -->
    <div class="footer-copyright text-center py-3 copyright-bg no-hover">
      <a href="/BhajanBook">www.saibhajanbook.org</a><br>
      <a href="mailto:saibhajanbookapp@gmail.com">saibhajanbookapp@gmail.com</a>
    </div>
    <!-- Copyright -->
  </footer>
  <!-- Footer -->
</body>

<script src="BhajanBook.js"></script>
<script src="BhajanBookPaging.js"></script>
<script src="login.js"></script>

</html>
