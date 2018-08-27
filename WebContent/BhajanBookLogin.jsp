<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>



<link rel="normalized css" href="https://necolas.github.io/normalize.css/8.0.0/normalize.css">
<link href='https://fonts.googleapis.com/css?family=Great Vibes|Acme' rel='stylesheet'>
<link rel="icon" href="https://i.imgur.com/wEVYj2v.png" type="image" sizes="16x16">


<script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>
<script defer src="https://use.fontawesome.com/releases/v5.0.1/js/all.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.transit/0.9.12/jquery.transit.js" integrity="sha256-mkdmXjMvBcpAyyFNCVdbwg4v+ycJho65QLDwVE3ViDs=" crossorigin="anonymous"></script>


<title>BhajanBook Login</title>
<link rel="stylesheet" type="text/css" href="login.css">
<script src="login.js"></script>
</head>

<body>
<!-- NORMALIZED CSS INSTALLED-->
<!-- View settings for more info.-->
<div id="container">
  <div id="inviteContainer">
    <div class="logoContainer"><img class="logo" src="images/sai_icon.png"/><img class="text" src="images/BhajanBook_logo_text.png"/></div>
    <div class="acceptContainer">
      <form onSubmit="return false;">
        <h1 style="font-size: 36px">Sai Ram</h1>
        <div class="formContainer">
          <div class="formDiv" style="transition-delay: 0.2s;">
            <input id="userId" type="text" required="" placeholder="EMAIL" style="padding-left:10px;"/>
          </div>
          <div class="formDiv" style="transition-delay: 0.4s;">
            <input id="passwd" type="password" required="" placeholder="PASSWORD" style="padding-left:10px;"/><a class="forgotPas" href="#"style="padding-top:10px;">FORGOT YOUR PASSWORD?</a>
            <div class="checkbox formDiv" style="color:#aaa">
	    		<label class="rememberMe"><input id="rememberMe" type="checkbox"/>REMEMBER ME</label>
			  </div>
          </div>
		  <div class="formDiv" style="transition-delay: 0.6s;">
            <button class="acceptBtn" type="submit" style="font-family: 'Montserrat' !important" onclick="login()">LOGIN</button><span class="register">Need an account?<a href="#" style="font-family: 'Montserrat' !important;">Register</a></span>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
</html>