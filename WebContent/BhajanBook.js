

var ALL_IMAGE = "/BhajanBook/images/deities/all.jpg"
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
var SHIRDI_IMAGE = "/BhajanBook/images/deities/shirdi.jpg";
var SHIVA_IMAGE = "/BhajanBook/images/deities/shiva.jpg";
var SUBRAMANYA_IMAGE = "/BhajanBook/images/deities/subramanya.jpg";
var VITTHALA_IMAGE = "/BhajanBook/images/deities/vitthala.jpg";

var APP_BASE_URL = "http://192.168.1.136:8080/BhajanBook/"

var deities_dict = {"ALL": ALL_IMAGE,"DEVI": DEVI_IMAGE, "EASHWARAMBA": EASHWARAMBA_IMAGE, "GANESHA": GANESHA_IMAGE,
		"GURU": GURU_IMAGE, "HANUMAN": HANUMAN_IMAGE, "KRISHNA": KRISHNA_IMAGE, "NARAYANA": NARAYANA_IMAGE,
		"RAMA": RAMA_IMAGE, "SAI": SAI_IMAGE, "SARVA DHARMA": SARVA_DHARMA_IMAGE, 
		"SHIRDI": SHIRDI_IMAGE, "SHIVA": SHIVA_IMAGE, "SUBRAMANYA": SUBRAMANYA_IMAGE, "VITTHALA": VITTHALA_IMAGE};



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
	showDeityPic(deity);
	showBhajanPages(deity);
}

function showDeityPic(deity) {
	var el = document.getElementById("deity_image");
	var imageref = deities_dict[deity.toUpperCase()];
	if (imageref == undefined) {
		alert("Deity " + deity + " Unrecognized. Please contact support. ");
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
	var bhajanAudio = document.getElementById("bhajan_audio");
	var bhajanMeaning = document.getElementById("bhajan_meaning");
	var bhajanRaaga = document.getElementById("bhajan_raaga");
	var bhajanBeat = document.getElementById("bhajan_beat");
	bhajanTitle.innerHTML = bhajanObject.bhajanTitle;
	bhajanLyrics.innerHTML = bhajanObject.lyrics;
	if (bhajanObject.audioFilePath != "") {
			bhajanAudio.innerHTML = 
				"<audio controls><source src=" + bhajanObject.audioFilePath + "\">Your browser does not support the audio element.</audio>";		
	} else {
		bhajanAudio.innerHTML = "<br>" ;
	}
	
	if (bhajanObject.meaning != "") {
		bhajanMeaning.innerHTML = "<p class='text-justify'><b>Meaning: </b><i>" + bhajanObject.meaning + "</i></p>";
	} else {
		bhajanMeaning.innerHTML = "<br>" ;
	}
	
	if (bhajanObject.raaga != undefined && bhajanObject.raaga != "") {
		bhajanRaaga.innerHTML   = "<p><b>Raaga: </b>" + bhajanObject.raaga + "</p>";
	} else {
		bhajanRaaga.innerHTML = "<br>" ;
	}
 
	if (bhajanObject.beat != undefined && bhajanObject.beat != "") {
		bhajanBeat.innerHTML    = "<p><b>Beat: </b>" + bhajanObject.beat + "</p>";
	} else {
		bhajanBeat.innerHTML = "<br>" ;
	}
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


$('.scrollable').on('scroll', function(){
	  var $el = $(this);
	  $('.scrolled').text($(this).scrollTop());
	  if( $el.innerHeight()+$el.scrollTop() >= this.scrollHeight-5 ){
	    var d = new Date();
	    $el.append('more text added on '+d.getHours()+':'+d.getMinutes()+':'+d.getSeconds()+'<br>');
	  }
	});



