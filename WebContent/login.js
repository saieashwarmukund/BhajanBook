// JQUERY
var userPlaylistList = "";

$(function() {
	var images = [
		"images/sky.jpg",
		"images/lotus.jpg",
		"images/lamp.jpg",
		"images/elephant.jpg",
		"images/temple.jpg",
	];
	
	hideSection("registerForm");
	$("#container").append(
		"<style>#container, .acceptContainer:before, #logoContainer:before {background: url(" +
		images[Math.floor(Math.random() * images.length)] +
		") center fixed }"
	);
	
	setTimeout(function() {
		$(".logoContainer").transition({ scale: 1 }, 700, "ease");
		setTimeout(function() {
			$(".logoContainer .logo").addClass("loadIn");
			setTimeout(function() {
				$(".logoContainer .text").addClass("loadIn");
				setTimeout(function() {
					$(".acceptContainer").transition({ height: "431.5px" });
					setTimeout(function() {
						$(".acceptContainer").addClass("loadIn");
						setTimeout(function() {
							$(".formDiv, form h1").addClass("loadIn");
						}, 500);
					}, 500);
				}, 500);
			}, 500);
		}, 1000);
	}, 10);
});

function unfade(element) {
    var op = 0.1;  // initial opacity
    element.style.display = 'block';
    var timer = setInterval(function () {
        if (op >= 1){
            clearInterval(timer);
        }
        element.style.opacity = op;
        element.style.filter = 'alpha(opacity=' + op * 100 + ")";
        op += op * 0.2;
    }, 5);
}

function showSection(section) {
	var x = document.getElementById(section);
        unfade(x);
}


function hideSection(section) {
	var x = document.getElementById(section);
        x.style.display = "none";
}

function forgotPassword() {
	alert ("Please send an email from your registered email account to saibhajanbookapp@gmail.com with the subject 'Forgot Password'")
}


function login(){
		var userId = document.getElementById("userId").value;
		var passwd = document.getElementById("passwd").value;
		var rememberMeObj = document.getElementById("rememberMe");
		var rememberMe = "N";
		if (rememberMeObj.checked) {
			rememberMe = "Y";
		};
		$.ajaxSetup({async: false});  
	    $.post("/BhajanBook/rest/Authenticate",
	    	    {
	    	        userId: userId,
	    	        passwd: passwd,
	    	        rememberMe: rememberMe,
	    	    },
	    	    function(data, status){
	    	    	if (status == 'success') {
	    	    		// global variable 
		    	    	//alert(data.roleList);	 
	    	    		//var myJSON = JSON.stringify(data);
	    	    		//alert(myJSON);
	    	    		if (data.status == 0) {
	    	    			userPlaylistList = data.userPlaylistList;
	    	    			window.open("/BhajanBook/MAIN", "_self");
	    	    			return;
	    	    		}
	    	    		alert(data.mesg);
	    	    		document.getElementById("userId").style.border = "1px solid rgba(245,137,137,0.8)";
	    	    		document.getElementById("passwd").style.border = "1px solid rgba(245,137,137,0.8)";
	    	    		return false;
	    	    	} 	 
	    	    }).fail(function(response) {
	    	        alert('Error authenticating. Please contact support.');
	    	    });
		/*$.ajax({
			  type: "POST",
			  url: "/BhajanBook/rest/Authenticate",
			  data: "userId="+ userId +"&passwd=" + passwd + "&rememberMe=" + rememberMe,
  		      success: function(data){
  		    	 if (data.status == 0) {
  		    		 alert("login success")
  		    	 } else {
  		    		 alert(data.msg);
  		    	 }
			        alert(data.firstName)
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
			}); */ 
	    }

function logout() {
	$.ajaxSetup({async: false});  
    $.post("/BhajanBook/rest/Authenticate/Logout",
    	    {
   
    	    },
    	    function(data, status){
    	        window.open("/BhajanBook", "_self");
    	    });
}

function registerAccount() {
	var firstName = document.getElementById("firstName").value;
	var lastName = document.getElementById("lastName").value;
	var email = document.getElementById("rEmail").value;
	var passwd = document.getElementById("rPasswd").value;
	var confirmPasswd = document.getElementById("rConfirmPasswd").value;
	var minlength = 5;
	if (passwd != confirmPasswd) {
		alert("Passwords do not match");
		return
	}
	
	if (passwd.trim() == "") {
		alert("Password cannot be empty");
		return;
	}
	
	if (passwd.length < minlength) {
		alert("Password must have minimum of " + minlength + " characters")
		return
	}
	
	$.ajaxSetup({async: false});  
    $.post("/BhajanBook/rest/Authenticate/Register",
    	    {
    			firstName: firstName,
    			lastName: lastName,
    			email: email,
    	        passwd: passwd
    	    },
    	    function(data, status){
    	    	if (status == 'success') {
    	    		// global variable 
	    	    	//alert(data.roleList);	 
    	    		//var myJSON = JSON.stringify(data);
    	    		//alert(myJSON);
    	    		if (data.status == 0) {
    	    			alert("User information registered. This is a beta version so user accounts are created in waves. We will email you when the account is activated.")
    	    			return;
    	    		}
    	    		alert(data.mesg);
    	    		return false;
    	    	} 	 
    	    }).fail(function(response) {
    	        alert('Error processing request. Please contact support.');
    	    });
}

function showRegisterForm() {
	showSection("registerForm");
	hideSection("loginForm");
}

function showLoginForm() {
	hideSection("registerForm");
	showSection("loginForm");
}


function mouseoverPass() {
	var icon = document.getElementById("icon")
	var pass = document.getElementById('rPasswd');
	var confirmPass = document.getElementById('rConfirmPasswd');
	pass.type = "text";
	confirmPass.type = "text";
	icon.style.color='lightgray';
}

function mouseoutPass() {
	var icon = document.getElementById("icon")
	var pass = document.getElementById('rPasswd');
	var confirmPass = document.getElementById('rConfirmPasswd');
	pass.type = "password";
	confirmPass.type = "password";
	icon.style.color='white';
}

