	var $pagination = $('#pagination'),
		totalRecords = 0,
		records = [],
		displayRecords = [],
		recPerPage = 9,
		page = 1,
		totalPages = 0;

function showBhajanPages(ldeity) {  
alert(ldeity);
	$pagination = $('#pagination');
	totalRecords = 0;
	records = [];
	displayRecords = [];
	recPerPage = 9;
	page = 1;
	totalPages = 0;
	urlvar =  "/BhajanBook/rest/Bhajan?deity="+ldeity;
alert(urlvar);
	$.ajax({
		url: urlvar,
		async: true,
		dataType: 'json',
		success: function (data) {
			records = data;
			console.log(records);
			totalRecords = records.length;
			totalPages = Math.ceil(totalRecords / recPerPage);

			apply_pagination();
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


	function generate_table() {
		var tr;
		$('#bhajan_list_page').html('');
		for (var i = 0; i < displayRecords.length; i++) {
			tr = $('<tr/>');
			alert(displayRecords[i].bhajanTitle);
			tr.append("<td>" + displayRecords[i].bhajanTitle + "</td>");
			$('#bhajan_list_page').append(tr);
		}
	}
	function apply_pagination() {
		$pagination.twbsPagination({
			totalPages: totalPages,
			visiblePages: 6,
			onPageClick: function (event, page) {

				displayRecordsIndex = Math.max(page - 1, 0) * recPerPage;
				endRec = (displayRecordsIndex) + recPerPage;
				console.log(displayRecordsIndex + 'ssssssssss'+ endRec);
				displayRecords = records.slice(displayRecordsIndex, endRec);
				generate_table();
			}
		});
	}
 
 