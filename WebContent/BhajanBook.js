$(document).ready(function() {
    $.ajax({
        url: "/BhajanBook/rest/ThoughtForTheDay"
    }).then(function(data) {
       $('.TFTD').append(data.thought);
    });
});
