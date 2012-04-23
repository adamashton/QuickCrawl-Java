<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html>

<%@page import="web.*"%>
<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
if (userManager == null) {
	userManager = new UserWebManager(request.getCookies());
	session.setAttribute("userManager", userManager);
}

String logErrCodeStr = request.getParameter("log_err");
int logErrCode = 0;
if (logErrCodeStr != null && !logErrCodeStr.isEmpty()) {
	logErrCode = Integer.parseInt(logErrCodeStr);
} 

if (logErrCode == 0)
	logErrCodeStr = "";
else if (logErrCode == 1)
	logErrCodeStr = "Login Error: Username not found";
else if (logErrCode == 2)
	logErrCodeStr = "Login Error: Password is incorrect";
else
	logErrCodeStr = "Login Error: Unknown error.";


String regErrCodeStr = request.getParameter("reg_err");
int regErrCode = 0;
if (regErrCodeStr != null && !regErrCodeStr.isEmpty()) {
	regErrCode = Integer.parseInt(regErrCodeStr);
} else {
	regErrCodeStr = "If you have an invite key you can register here.";
}

if (regErrCode == 0)
	regErrCodeStr = "If you have an invite key you can register here.";
else if (regErrCode == 1)
	regErrCodeStr = "Registration error: User already exists";
else
	regErrCodeStr = "Unknown error.";

%>

<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link href="./style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/sha256.js"></script>
<script type="text/javascript">
function handleClickLogin()
{
	var username = document.getElementById('username').value;
	var password = document.getElementById('password').value;
	var remember = document.getElementById('remember').checked;

	if (username == '') {
		alert('You must enter a username.');
		return;
	}

	if (password == '') {
		alert('You must enter a valid password.');
		return;
	}

 	setFormValue('login', 'usernameF', username);
  	password = SHA256(password);
  	setFormValue('login', 'passwordF', password);
  	setFormValue('login', 'rememberF', remember);
  	
  	document.forms['login'].submit();
}

function handleClickRegister()
{
	var username = document.getElementById('newUsername').value;
	var password = document.getElementById('newPassword').value;
	var email = document.getElementById('newEmail').value;
	var invite = document.getElementById('newInvite').value;
	
	if (username == '') {
		alert('You must choose a username.');
		return;
	}

	if (password == '') {
		alert('You must choose a valid password.');
		return;		
	} 

	if (email == '') {
		alert('You must choose a valid email.');
		return;
	}

	if (invite == '') {
		alert('You must enter a valid invite key.\r\nSorry! This is a closed beta until we iron out all the bugs and make it look nice.\r\nYou can look at other peoples crawls though!');
		return;
	}

	// Oh, Herro! Level 1 hacking here. If you've come this far you can register :)
	// Do provide constructive criticism and bug reports to me.
	if (invite != 'qwerty') {		
		alert('Invite key was incorrect\r\nSorry! This is a closed beta until we iron out all the bugs and make it look nice.\r\nYou can look at other peoples crawls though!');		
		return;
	}
	
	setFormValue('register', 'newUsername', username);
  	setFormValue('register','newEmail', email);
  	password = SHA256(password);
  	setFormValue('register', 'newPassword', password);

  	document.forms['register'].submit()
}

function setFormValue(form, element, value) {
	document.forms[form].elements[element].value = value;
}
</script>
</head>

<body>
<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">
	<table style="margin-left: auto; margin-right: auto">
	<tr>
	<td style="border-right: thin solid; padding-right: 50px">
	Already a member? Login...<br />
	<% if (logErrCode > 0) { %>
	<p style="color: red"><%=logErrCodeStr %></p>
	<% } %>
	<br />
	
	<table>
	<tr>
	<td>Username:</td>
	<td><input type="text" id="username" /></td>
	</tr>
	<tr>
	<td>Password:</td>
	<td><input type="password" id="password" /></td>
	</tr>
	<tr>
	<td><form name="login" action="dologin.jsp" method="post" onClick="handleClickLogin()">
			<input type="hidden" name="usernameF" />
			<input type="hidden" name="passwordF" />
			<input type="hidden" name="rememberF" />
			<input type="button" value="Login"/>			
		</form>		
	</td>
	<td><input type="checkbox" id="remember" checked="checked" />Stay logged in.</td>
	</tr>
	<tr>
	
	</tr>
	</table>	
	</td>
	
	<td style="padding-left: 50px">
	<!--  right hand side --><br />
	<%=regErrCodeStr %><br /><br />
	<table>
	<tr>
	<td>Username:</td>
	<td><input type="text" id="newUsername" /></td>
	</tr>
	<tr>
	<td>Password:</td>
	<td><input type="password" id="newPassword" /></td>
	</tr>
	<tr>
	<td>Email:</td>
	<td><input type="text" id="newEmail" /></td>
	</tr>
	<tr>
	<td>Invite Key:</td>
	<td><input type="text" id="newInvite" /></td>
	</tr>
	<tr>
	<td><form name="register" action="doregister.jsp" method="post" onClick="handleClickRegister()">
			<input type="hidden" name="newUsername" />
			<input type="hidden" name="newPassword" />
			<input type="hidden" name="newEmail" />	
			<input type="button" value="Register"/><br />		
		</form>
		<br /></td>
	</tr>
	</table>
	
	</td>
	</tr>
	</table>
	</div>
	<jsp:include page="footer.jsp" />
</div>
</body>
</html>