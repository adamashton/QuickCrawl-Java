<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%@page import="core.support.*" %>
<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");

String user = request.getParameter("usernameF");
String password = request.getParameter("passwordF");
boolean remember = request.getParameter("rememberF").equals("true");

int result = userManager.login(user, password);

String fwdPage = "myaccount.jsp";

if (result == 0) {
	if (remember) {
		// cookies!
		Cookie c = new Cookie("username", user);
		c.setMaxAge(31000000);
		response.addCookie(c);
		
		c = new Cookie("password", password);
		c.setMaxAge(31000000);
		response.addCookie(c);
	}
	
} else if (result == 1) {
	fwdPage = "login.jsp?log_err=1";
} else if (result == 2) {
	fwdPage = "login.jsp?log_err=2";
}

%>

<html>
<head>
<meta http-equiv="REFRESH" content="0;url=<%=fwdPage %>">
<title>Login</title>
</head>
<body>
Please wait, Logging In...
</body>
</html>