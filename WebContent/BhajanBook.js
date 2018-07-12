
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

var deities_dict = {
	"ALL" : ALL_IMAGE,
	"DEVI" : DEVI_IMAGE,
	"EASHWARAMBA" : EASHWARAMBA_IMAGE,
	"ENGLISH" : ENGLISH_IMAGE,
	"GANESHA" : GANESHA_IMAGE,
	"GURU" : GURU_IMAGE,
	"HANUMAN" : HANUMAN_IMAGE,
	"KRISHNA" : KRISHNA_IMAGE,
	"NARAYANA" : NARAYANA_IMAGE,
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
	$.ajax({
		url : "/BhajanBook/rest/ThoughtForTheDay"
	}).then(function(data) {
		$('.TFTD').empty();
		$('.TFTD').append(data.thought);
		$('.TFTD').append(" ~Baba");

	});
});

function hideCard(card) {
	document.getElementById(card).style.display = "none";
}
function showCard(card) {
	document.getElementById(card).style.display = "block";
}

function showDeityBhajans(deity) {
	hideCard("lyrics_card");
	hideCard("TFTD_card");
	showCard("main_panel");
	showCard("bhajans_card")
	showDeityPic(deity);
	showBhajanPages(deity);
}

function showSearchBhajans(category, field_id) {
	var search_string = document.getElementById(field_id).value;
	hideCard("lyrics_card");
	hideCard("TFTD_card");
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
	if (deity == "SEARCH") {
		title.innerHTML = "";
	}
}

function showLyricsCard(bhajanObject) {
	showCard("lyrics_card");
	hideCard("TFTD_card");
	showCard("main_panel");
	hideCard("bhajans_card");
	var bhajanTitle = document.getElementById("bhajan_title");
	var bhajanLyrics = document.getElementById("bhajan_lyrics");
	var bhajanAudio = document.getElementById("bhajan_audio");
	var bhajanMeaning = document.getElementById("bhajan_meaning");
	var bhajanRaaga = document.getElementById("bhajan_raaga");
	var bhajanBeat = document.getElementById("bhajan_beat");
	bhajanTitle.innerHTML = bhajanObject.bhajanTitle;
	bhajanLyrics.innerHTML = bhajanObject.lyrics;
	if (bhajanObject.audioFilePath != "") {
		bhajanAudio.innerHTML = "<audio controls><source src="
				+ bhajanObject.audioFilePath
				+ "\">Your browser does not support the audio element.</audio><br><br>";
	} else {
		bhajanAudio.innerHTML = "";
	}

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
}

function goBackToBhajans() {
	var bhajanAudio = document.getElementById("bhajan_audio");
	showCard("bhajans_card");
	hideCard("lyrics_card");
	bhajanAudio.innerHTML = "";
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
			var msg = '';
			console.log(jqXHR);
			if (jqXHR.status === 0) {
				msg = 'Not connect.\n Verify Network.';
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

function searchSong(category, search_string) {
	if (search_string.length == 0) {
		return;
	} 
	var title = document.getElementById("bhajans_list_title");
	// Clears existing table.
	$('#bhajan_list_page').html('');
	$pagination = $('#pagination');
	$('.pagination').twbsPagination('destroy');
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

	});
	showSearch();
}

// Setting search listener.
// Get the input field
var input = document.getElementById("search_field");
// Execute a function when the user releases a key on the keyboard
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