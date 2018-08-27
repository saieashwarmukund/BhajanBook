
var ALL_IMAGE = "/BhajanBook/images/deities/all.jpg"
	var DEVI_IMAGE = "/BhajanBook/images/deities/devi.jpg";
var EASHWARAMBA_IMAGE = "/BhajanBook/images/deities/eashwaramba.jpg";
var ENGLISH_IMAGE = "/BhajanBook/images/deities/english.jpg";
var GANESHA_IMAGE = "/BhajanBook/images/deities/ganesha.jpg";
var GURU_IMAGE = "/BhajanBook/images/deities/guru.jpg";
var HANUMAN_IMAGE = "/BhajanBook/images/deities/hanuman.jpg";
var KRISHNA_IMAGE = "/BhajanBook/images/deities/krishna.jpg";
var NARAYANA_IMAGE = "/BhajanBook/images/deities/narayana.jpg";
var RAMA_IMAGE = "/BhajanBook/images/deities/rama.jpg";
var SAI_IMAGE = "/BhajanBook/images/deities/sai.jpg";
var SARVA_DHARMA_IMAGE = "/BhajanBook/images/deities/sarva_dharma.png";
var SHIRDI_IMAGE = "/BhajanBook/images/deities/shirdi.jpg";
var SHIVA_IMAGE = "/BhajanBook/images/deities/shiva.jpg";
var SUBRAMANYA_IMAGE = "/BhajanBook/images/deities/subramanya.jpg";
var VITTHALA_IMAGE = "/BhajanBook/images/deities/vitthala.jpg";
var SEARCH_IMAGE = "/BhajanBook/images/deities/search.jpg"
var CONTACT_IMAGE = "/BhajanBook/images/deities/contact.jpg";
var PLAYLISTS_IMAGE = "/BhajanBook/images/deities/playlists.jpg";

var userPlaylistList = {};
var showingPlaylist = false;
var currentPlaylist = "";
var FAVORITE = '_FAV_';
var RECENT = "RECENT";
var TOP10 = "TOP10";
var deities_dict = {
		"ALL" : ALL_IMAGE,
		"DEVI" : DEVI_IMAGE,
		"CONTACT" : CONTACT_IMAGE,
		"EASHWARAMBA" : EASHWARAMBA_IMAGE,
		"ENGLISH" : ENGLISH_IMAGE,
		"GANESHA" : GANESHA_IMAGE,
		"GURU" : GURU_IMAGE,
		"HANUMAN" : HANUMAN_IMAGE,
		"KRISHNA" : KRISHNA_IMAGE,
		"NARAYANA" : NARAYANA_IMAGE,
		"PLAYLISTS" : PLAYLISTS_IMAGE,
		"RAMA" : RAMA_IMAGE,
		"SAI" : SAI_IMAGE,
		"SARVA DHARMA" : SARVA_DHARMA_IMAGE,
		"SEARCH" : SEARCH_IMAGE,
		"SHIRDI" : SHIRDI_IMAGE,
		"SHIVA" : SHIVA_IMAGE,
		"SUBRAMANYA" : SUBRAMANYA_IMAGE,
		"VITTHALA" : VITTHALA_IMAGE
};

$(document).ready(function() {
	hideCard("main_panel");
	hideCard("search_bar");
	hideCard("contact_card");
	$.ajax({
		url : "/BhajanBook/rest/ThoughtForTheDay"
	}).then(function(data) {
		$('.TFTD').empty();
		$('.TFTD').append(data.thought);
		$('.TFTD').append(" ~Baba");

		//var myJSON = JSON.stringify(data);
		//alert(myJSON);
		userPlaylistList = data.userPlaylistList;
	});
});

function hideAllCards() {
	hideCard("lyrics_card");
	hideCard("TFTD_card");
	hideCard("main_panel");
	hideCard("bhajans_card");
	hideCard("contact_card");
	hideSection("playlist_options");
}

function hideCard(card) {
	document.getElementById(card).style.display = "none";
}
function showCard(card) {
	document.getElementById(card).style.display = "block";
}

function showDeityBhajans(deity) {
	showingPlaylist = false;
	goBackToBhajans(); // Stop any playing bhajans.
	hideAllCards();
	showCard("main_panel");
	showCard("bhajans_card");
	showDeityPic(deity);
	$('#subhead').html('');
	showBhajanPages(deity);
}


function showMainPage() {
	hideAllCards();
	showCard("TFTD_card");
}

function showContact() {
	hideAllCards();
	showCard("contact_card");
	showDeityPic("CONTACT");
}

function showSearchBhajans(category, field_id) {
	var search_string = document.getElementById(field_id).value;
	hideAllCards();
	showCard("main_panel");
	showCard("bhajans_card")
	showDeityPic("SEARCH");
	searchSong(category, search_string);
	document.getElementById(field_id).value = "";
	document.getElementById(field_id).placeholder = "Search for Bhajan";
}


function showDeityPic(deity) {
	var el = document.getElementById("deity_image");
	var imageref = deities_dict[deity.toUpperCase()];
	if (imageref == undefined) {
		alert("Deity " + deity + " Unrecognized. Please contact support. ");
		return;
	}
	el.innerHTML = "<img class=\"responsive\" src=\"" + imageref + "\">";

	var title = document.getElementById("bhajans_list_title");
	if (title == undefined) {
		return;
	}
	title.innerHTML = deity + " Bhajans";
	if (deity == "SEARCH" || deity == "PLAYLISTS"){
		title.innerHTML = "";
	}
}


function showLyricsCard(bhajanObject) {
	// var myJSON = JSON.stringify(bhajanObject);
	// alert(myJSON);

	hideAllCards();
	showCard("lyrics_card");
	showCard("main_panel");
	var bhajanTitle = document.getElementById("bhajan_title");
	var bhajanLyrics = document.getElementById("bhajan_lyrics");
	var bhajanAudio = document.getElementById("bhajan_audio");
	var bhajanFavorite = document.getElementById("favorite");
	var bhajanAddToPlaylist = document.getElementById("add_to_playlist");
	var bhajanMeaning = document.getElementById("bhajan_meaning");
	var bhajanRaaga = document.getElementById("bhajan_raaga");
	var bhajanBeat = document.getElementById("bhajan_beat");
	var bhajanIdDiv = document.getElementById("bhajan_id_div");
	bhajanTitle.innerHTML = bhajanObject.bhajanTitle;
	bhajanLyrics.innerHTML = bhajanObject.lyrics;
	if (bhajanObject.audioFilePath != undefined && bhajanObject.audioFilePath != "") {
		bhajanAudio.innerHTML = "<audio controls><source src="
			+ bhajanObject.audioFilePath
			+ "\">Your browser does not support the audio element.</audio><br><br>";
	} else {
		bhajanAudio.innerHTML = "";
	}
	if (bhajanObject.favorite == "Y") {
		bhajanFavorite.innerHTML = "<i onclick='addToFav(this, " + bhajanObject.id + 
		")' class='fa fa-heart' style='font-size:25px; color:#e2264d'></i>"		
	} else {
		bhajanFavorite.innerHTML = "<i onclick='addToFav(this, " + bhajanObject.id + 
		")' class='fa fa-heart-o' style='font-size:25px; color:#e2264d'></i>"
	}
	
	bhajanAddToPlaylist.innerHTML = "<button type='button' class='btn btn-info btn-sm'  " +
			"style='background-color:transparent; border: none;' onclick='showPlaylistModel()'>" +
			"<img src='/BhajanBook/images/icons/add_to_playlist_icon.png' " +
			"style='width:30px;height:100%; padding-top: 3px;'></button>";
	
	

	if (bhajanObject.meaning != "") {
		bhajanMeaning.innerHTML = "<p class='text-justify'><b>Meaning: </b><i>"
			+ bhajanObject.meaning + "</i></p>";
	} else {
		bhajanMeaning.innerHTML = "";
	}

	if (bhajanObject.raaga != undefined && bhajanObject.raaga != "") {
		bhajanRaaga.innerHTML = "<p><b>Raaga: </b>" + bhajanObject.raaga
		+ "</p>";
	} else {
		bhajanRaaga.innerHTML = "";
	}

	if (bhajanObject.beat != undefined && bhajanObject.beat != "") {
		bhajanBeat.innerHTML = "<p><b>Beat: </b>" + bhajanObject.beat + "</p>";
	} else {
		bhajanBeat.innerHTML = "";
	}
	
	if (bhajanObject.id != "") {
		bhajanIdDiv.innerHTML = "<input type=hidden id='bhajan_id' name='bhajan_id' value=" + bhajanObject.id + ">";
	} else {
		alert('Error displaying Bhajan');
	}
}

function showPlaylistModel() {
	// repaint the playlist list
	var checkBoxSection = document.getElementById('playlist_checkbox');
	var bhajanId = document.getElementById("bhajan_id").value; 
	checkBoxSection.innerHTML = "";
	var htmlStr = "";
	showSection('create_playlist_hdr');
	showSection('playlist_checkbox');
	hideSection('create_playlist_section');
	hideSection('submit_playlist_new');
	// Iterate through userplaylistList - for each playlist add a checkbox.
	if (userPlaylistList.length == 0) {
		htmlStr = htmlStr + "<h4>No playlists created</h4>";
	} else {	
		// Call Webservice method to get list of playlist that contains the current bhajan id.
		var urlvar = "/BhajanBook/rest/Bhajan/PlaylistWithBhajan?id=" + bhajanId;
		$.ajax({
			url : urlvar,
			dataType : 'json',
			async : false,
			success : function(data) {
				var playlistWithBhajans = data;
				console.log(playlistWithBhajans);
				// In the iteration below, check each playlistKey, whether it is present in the above saved list.
				// if present, show as CHECKED, otherwise not checked.
				
		 		for (let i = 0; i < userPlaylistList.length; i++) {
		 			var checkStr = "";
					// check if userPlaylistList[i].playlistKey is present in playlistWithBhajans, 
	 				if (playlistWithBhajans.indexOf(userPlaylistList[i].playlistKey) != -1) {
		 			// if (userPlaylistList[i].playlistKey in playlistWithBhajans) {
		 				checkStr = " checked ";
		 			}

					htmlStr = htmlStr + "<div class='form-check'>" +
					"<input onchange='updatePlaylist(this)' type='checkbox' class='form-check-input' id='" + 
					userPlaylistList[i].playlistKey + "'" + checkStr +">" +
					"<label class='form-check-label' for='" +  userPlaylistList[i].playlistKey + "' >" + 
					userPlaylistList[i].playlistName + "</label>" +
					"</div>";
				}
				
			},
			error : function(jqXHR, exception) {
				wsErrorHandler(jqXHR, exception);
			}
		});
	}
	checkBoxSection.innerHTML = htmlStr;

	// hide create playlist section.
	// initialize the playlist name
	var new_playlist_name = document.getElementById('new_playlist_name');
	new_playlist_name.value = '';
	
	$('#playlistModal').modal('show');
	hideSection('create_playlist_section');
	showSection('create_playlist_hdr');
}

function updatePlaylist(element) {
	// Find Bhajan Id.
	// Determine whether to Add or Delete
	// Using Jquery, Call webservice to Add to Playlist.
	// When control comes back from server, refresh userPlaylistList.
	var url = "";
	  if (element.checked) {
		  url = "/BhajanBook/rest/Bhajan/AddToPlaylist";
	  } else {
		  url = "/BhajanBook/rest/Bhajan/RemoveFromPlaylist";
	  }
	  var bhajanId = document.getElementById("bhajan_id").value; 
	  // call jquery
	  $.ajaxSetup({async: false});  
		$.post(url,
				{
			playlistId:element.id,
			id:bhajanId
				},
				function(data, status){
					var myJSON = JSON.stringify(data);
					// alert(myJSON);
					userPlaylistList = data;
					return;
				}).fail(function(response) {
					alert('Internal error occured while updating playlist. Please contact support.');
					console.log('Error: ' + response.responseText);
				});	
	  
	  
}

function showSection(section) {
	var x = document.getElementById(section);
        x.style.display = "block";
}


function hideSection(section) {
	var x = document.getElementById(section);
        x.style.display = "none";
}

function goBackToBhajans() {
	var bhajanAudio = document.getElementById("bhajan_audio");
	showCard("bhajans_card");
	hideCard("lyrics_card");
	bhajanAudio.innerHTML = "";
	if (showingPlaylist) {
		execShowPlaylist(currentPlaylist);
	}
}

function showBhajanLyrics(id) {
	var d = new Date();
	var n = d.getTime();
	var urlvar = "/BhajanBook/rest/Bhajan/Show?id=" + id + "&groupId=" + n;
	$.ajax({
		url : urlvar,
		dataType : 'json',
		async : true,
		success : function(data) {
			showLyricsCard(data);
		},
		error : function(jqXHR, exception) {
			wsErrorHandler(jqXHR, exception);
		}
	});
}

$('.scrollable').on(
		'scroll',
		function() {
			var $el = $(this);
			$('.scrolled').text($(this).scrollTop());
			if ($el.innerHeight() + $el.scrollTop() >= this.scrollHeight - 5) {
				var d = new Date();
				$el.append('more text added on ' + d.getHours() + ':'
						+ d.getMinutes() + ':' + d.getSeconds() + '<br>');
			}
		});

$(document).ready(function() {
	$('#nav-icon1,#nav-icon2,#nav-icon3,#nav-icon4').click(function() {
		$(this).toggleClass('open');
	});
});


function showSearchDiv() {
	var nav = document.getElementById("nav");
	var search = document.getElementById("search_bar");
	nav.style.display = (nav.style.display == "none" ? "block" : "none");
	search.style.display = (search.style.display == "none" ? "block" : "none");
	document.getElementById("search_field").value = "";
	document.getElementById("search_field").placeholder = "Search for Bhajan";
	document.getElementById("search_field").focus();
}

function addToFav(x, bhajanId) {
	var url = "/BhajanBook/rest/Bhajan/AddToFavorite";

	$.ajaxSetup({async: false});  
	$.post(url,
			{
		id:bhajanId
			},
			function(data, status){
				var myJSON = JSON.stringify(data);
				//alert(myJSON);
				// Not returning playlist lists anymore.
				// userPlaylistList = data.userPlaylistList;
				/*if (selection_TF == true) {
        			   document.getElementById("fav").value = 'Remove from Favorites';
    			} else {
        			   document.getElementById("fav").value = 'Add to Favorites';    				
    			}*/
				x.classList.toggle("fa-heart-o");
				x.classList.toggle("fa-heart");

				return;
			}).fail(function(response) {
				alert('Error: ' + response.responseText);
			});
}

function toggleDiv(section) {
    var x = document.getElementById(section);
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function showPlaylistMenu() {
	// hide all panels and show bhajan panel
	hideAllCards();
	showCard("main_panel");
	showCard("bhajans_card");
	showSection("playlist_options")
	// clear bhajan panel.
	$('#bhajan_list_page').html('');
	$('#subhead').html('');
	$('#subhead').append(createPlaylistListbox());
	showPlaylist();	
	showDeityPic("PLAYLISTS");
	var title = document.getElementById("bhajans_list_title");
	title.innerHTML = "Playlists";
}

function createPlaylist(playlistNameId) {
	var playlistName = document.getElementById(playlistNameId).value; 
	if (playlistName == "") {
		alert("Empty Playlist Name");
		return;
	}
	if (playlistName.trim() == "") {
		alert("Empty Playlist Name");
		return;
	}

	var bhajanId = document.getElementById("bhajan_id").value; 
	var url = "/BhajanBook/rest/Bhajan/CreatePlaylist";

	$.ajaxSetup({async: false});  
	$.post(url,
			{
		playlistName:playlistName,
		id:bhajanId
			},
			function(data, status){
				var myJSON = JSON.stringify(data);
				// alert(myJSON);
				userPlaylistList = data;
				return;
			}).fail(function(response) {
				alert('Internal error occured while creating playlist. Please contact support.');
				console.log('Error: ' + response.responseText);
			});	
}

function createPlaylistListbox() {
	var htmlStr = "<select id='activitylistbox' onchange='showPlaylist()'>" +
	"<option value='RECENT'>Recently Added</option>" +
	"<option value='TOP10'>Top 10</option>" +
	"<option selected value='" + FAVORITE + "'>My Favorites</option>";

	// function to iterate through the global list userplaylistlist 
	//  and add the options to the above.
	for (let i = 0; i < userPlaylistList.length; i++) {
		htmlStr = htmlStr + "<option value='" + userPlaylistList[i].playlistKey + "'>" + userPlaylistList[i].playlistName + "</option>";
	}
	// Finally, end with appending 			"</select>"

	return htmlStr;
}

function showPlaylist() {
	var option = document.getElementById("activitylistbox").value;
	showingPlaylist = true;
	currentPlaylist = option;
	if (currentPlaylist == RECENT || currentPlaylist == TOP10 || currentPlaylist == FAVORITE) {
		hideSection("playlist_options");
	} else {
		showSection("playlist_options");
	}
	/*if (option ==  FAVORITE) {
		showingMyFavorite = true;
	} else {
		showingMyFavorite = false;
	}*/

	execShowPlaylist(option);
}
	
function execShowPlaylist(option) {
	$pagination = $('#pagination');
	$('.pagination').twbsPagination('destroy');
	var bhajanLoading = document.getElementById("bhajan_list_page");
	bhajanLoading.innerHTML = "<center><img src='/BhajanBook/images/icons/loading_image.gif' style='width:50px; position:center;'></img></center>";
	//urlvar = "/BhajanBook/rest/Bhajan/Activity?activityType=" + option.toUpperCase();
	urlvar = "/BhajanBook/rest/Bhajan/Playlist?playlistId=" + option;

	$.ajax({
		url : urlvar,
		async : true,
		dataType : 'json',
		success : function(data) {
			records = data;

			if (records.length == 0) {
				bhajanLoading.innerHTML = "<h4>No Bhajans added</h4>";
			} else {
				console.log(records);
				totalRecords = records.length;
				totalPages = Math.ceil(totalRecords / recPerPage);
				displayRecords = data;
				$('#bhajan_list_page').html('');
				append_table();
			}
		},
		error : function(jqXHR, exception) {
			wsErrorHandler(jqXHR, exception);
		}

	});
}



function searchSong(category, search_string) {
	if (search_string.length == 0) {
		return;
	} 
	var title = document.getElementById("bhajans_list_title");
	// Clears existing table.
	$('#bhajan_list_page').html('');
	$pagination = $('#pagination');
	$('.pagination').twbsPagination('destroy');
	showingPlaylist = false;
	$('#subhead').html('');
	var bhajanLoading = document.getElementById("bhajan_list_page");
	bhajanLoading.innerHTML = "<center><img src='/BhajanBook/images/icons/loading_image.gif' style='width:50px; position:center;'></img></center>";
	urlvar = "/BhajanBook/rest/Bhajan/Search?searchstr=" + search_string.toUpperCase();
	$.ajax({
		url : urlvar,
		async : true,
		dataType : 'json',
		success : function(data) {
			records = data;
			if (records.length == 0) {
				title.innerHTML = "&nbsp;&nbsp;No results were found.";
				bhajanLoading.innerHTML = "";
			} else {
				console.log(records);
				totalRecords = records.length;
				totalPages = Math.ceil(totalRecords / recPerPage);
				displayRecords = data;
				generate_table();
			}
		},
		error : function(jqXHR, exception) {
			wsErrorHandler(jqXHR, exception);
		}

	});
	showSearch();
}

function wsErrorHandler(jqXHR, exception) {
	var msg = '';
	console.log(jqXHR);
	if (jqXHR.status === 0) {
		msg = 'Connection Error.\n Verify Network.';
	} else if (jqXHR.status == 404) {
		msg = 'Requested page not found. [404]';
	} else if (jqXHR.status == 500) {
		msg = 'Internal Server Error [500].';
	} else if (exception === 'parsererror') {
		msg = 'Requested JSON parse failed.';
	} else if (exception === 'timeout') {
		msg = 'Time out error.';
	} else if (exception === 'abort') {
		msg = 'Ajax request aborted.';
	} else {
		msg = 'Uncaught Error.\n' + jqXHR.responseText;
	}
	alert(msg);	
}

//Setting search listener.
//Get the input field
var input = document.getElementById("search_field");
//Execute a function when the user releases a key on the keyboard
input.addEventListener("keyup", function(event) {
	// Cancel the default action, if needed
	event.preventDefault();
	// Number 13 is the "Enter" key on the keyboard
	if (event.keyCode === 13) {
		// Trigger the button element with a click
		// document.getElementById("search_icon").click();
		showSearchBhajans('bhajan','search_field');
	}
});

function logout() {
	$.ajaxSetup({async: false});  
    $.post("/BhajanBook/rest/Authenticate/Logout",
    	    {
       	    },
    	    function(data, status){
   alert('Logged out. Thanks for using BhajanBookApp.')
 	        window.open("/BhajanBook", "_self");
    	    });
}

function showForm(){
	var selopt = document.getElementById("opts").value;
	if (selopt == 1) {
		document.getElementById("feedback").style.display="block";
		document.getElementById("suggestions").style.display="none";
	}
	if (selopt == 2) {
		document.getElementById("suggestions").style.display="block";
		document.getElementById("feedback").style.display="none";
	}
}