


var DEVI_IMAGE = "/BhajanBook/images/deities/devi.jpg";
var EASHWARAMBA_IMAGE = "/BhajanBook/images/deities/eashwaramba.jpg";
var GANESHA_IMAGE = "/BhajanBook/images/deities/ganesha.jpg";
var GURU_IMAGE = "/BhajanBook/images/deities/guru.jpg";
var HANUMAN_IMAGE = "/BhajanBook/images/deities/hanuman.jpg";
var KRISHNA_IMAGE = "/BhajanBook/images/deities/krishna.jpg";
var NARAYANA_IMAGE = "/BhajanBook/images/deities/narayana.jpg";
var RAMA_IMAGE = "/BhajanBook/images/deities/rama.jpg";
var SAI_IMAGE = "/BhajanBook/images/deities/sai.jpg";
var SARVA_DHARMA_IMAGE = "/BhajanBook/images/deities/sarva_dharma.jpg";
var SHIVA_IMAGE = "/BhajanBook/images/deities/shiva.jpg";
var SUBRAMANYA_IMAGE = "/BhajanBook/images/deities/subramanya.jpg";
var VITTHALA_IMAGE = "/BhajanBook/images/deities/vitthala.jpg";

var APP_BASE_URL = "http://192.168.1.136:8080/BhajanBook/"

	var deities_dict = {"DEVI": DEVI_IMAGE, "EASHWARAMBA": EASHWARAMBA_IMAGE, "GANESHA": GANESHA_IMAGE,
		"GURU": GURU_IMAGE, "HANUMAN": HANUMAN_IMAGE, "KRISHNA": KRISHNA_IMAGE, "NARAYANA": NARAYANA_IMAGE,
		"RAMA": RAMA_IMAGE, "SAI": SAI_IMAGE, "SARVA DHARMA": SARVA_DHARMA_IMAGE, 
		"SHIVA": SHIVA_IMAGE, "SUBRAMANYA": SUBRAMANYA_IMAGE, "VITTHALA": VITTHALA_IMAGE};

 


$(document).ready(function() {
	hideCard("main_panel");
	$.ajax({
		url: "/BhajanBook/rest/ThoughtForTheDay"
	}).then(function(data) {
		$('.TFTD').empty();
		$('.TFTD').append(data.thought);
		$('.TFTD').append(" ~Baba");

	});
});

function hideCard(card) {
	document.getElementById(card).style.display="none";
}
function showCard(card) {
	document.getElementById(card).style.display="block";
}

function showDeityBhajans(deity) {
	hideCard("lyrics_card");
	hideCard("TFTD_card");
	showCard("main_panel");
	showCard("bhajans_card")
	deityUpper = deity.toUpperCase();
	alert('DEITYUPPER IS '+ deityUpper);
	showDeityPic(deityUpper);
	showBhajanPages(deityUpper);
}

function showDeityPic(deity) {
	var el = document.getElementById("deity_image");
	var imageref = deities_dict[deity];
	if (imageref == undefined) {
		alert("Deity Unrecognized. Please contact support. ");
		return;
	}
	el.innerHTML="<img class=\"responsive\" src=\"" + imageref + "\">";

	var title = document.getElementById("bhajans_list_title");
	title.innerHTML = deity +" Bhajans";
}

function showLyricsCard(bhajanObject) {
	showCard("lyrics_card");
	hideCard("TFTD_card");
	showCard("main_panel");
	hideCard("bhajans_card");
	var bhajanTitle = document.getElementById("bhajan_title");
	var bhajanLyrics = document.getElementById("bhajan_lyrics");
	bhajanTitle.innerHTML = bhajanObject.bhajanTitle;
	bhajanLyrics.innerHTML = bhajanObject.lyrics;
}

function goBackToBhajans(){
	showCard("bhajans_card");
	hideCard("lyrics_card");
}

function showBhajanLyrics(id) {
	var urlvar = "/BhajanBook/rest/Bhajan/show?id="+id;
	$.ajax({
		url: urlvar,
		dataType: 'json',
		async: true,
		success: function (data) {
			showLyricsCard(data);
		},
		error: function (jqXHR, exception) {
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





