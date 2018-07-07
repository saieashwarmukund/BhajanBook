	
	var $pagination = $('#pagination'),
		totalRecords = 0,
		records = [],
		displayRecords = [],
		recPerPage = 50,
		page = 1,
		totalPages = 0;

function showBhajanPages(deity) { 
	// Clears existing table.
	$('#bhajan_list_page').html(''); 
	$pagination = $('#pagination');
	$('.pagination').twbsPagination('destroy');
	var bhajanLoading = document.getElementById("bhajan_list_page");
	bhajanLoading.innerHTML = "<center><img src='/BhajanBook/images/icons/loading_image.gif' style='width:50px; position:center;'></img></center>";
	urlvar = "/BhajanBook/rest/Bhajan?deity="+deity.toUpperCase();
	$.ajax({
		url: urlvar,
		async: true,
		dataType: 'json',
		success: function (data) {
			records = data;
			console.log(records);
			totalRecords = records.length;
			totalPages = Math.ceil(totalRecords / recPerPage);
			displayRecords = data;
			generate_table();
		},
		error: function (jqXHR, exception) {
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
}


	function generate_table() {
		var tr;
		$('#bhajan_list_page').html(''); 
		for (var i = 0; i < displayRecords.length; i++) {
			tr = $('<tr/>');
			tr.append("<td onclick='showBhajanLyrics(" + displayRecords[i].id + ")'>" + displayRecords[i].bhajanTitle  + "</td>");
			$('#bhajan_list_page').append(tr);
		}
	}

	function apply_pagination() {
		$pagination.twbsPagination({
			totalPages: totalPages,
			visiblePages: 9,
			onPageClick: function (event, page) {

				displayRecordsIndex = Math.max(page - 1, 0) * recPerPage;
				endRec = (displayRecordsIndex) + recPerPage;
				console.log(displayRecordsIndex + 'ssssssssss'+ endRec);
				displayRecords = records.slice(displayRecordsIndex, endRec);
				generate_table();
			}
		});
	}
 
 