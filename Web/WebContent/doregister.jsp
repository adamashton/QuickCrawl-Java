<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
if (userManager == null) {
	userManager = new UserWebManager(request.getCookies());
	session.setAttribute("userManager", userManager);
}

String user = request.getParameter("newUsername");
String email = request.getParameter("newEmail");
String password = request.getParameter("newPassword");

int result = userManager.register(user, password, email);

String fwdPage = "step1.jsp";

if (result == 0) {
	userManager.login(user, password);
} else if (result == 1) {
	fwdPage = "login.jsp?reg_err=1";
} else if (result == 2) {
	fwdPage = "login.jsp?reg_err=2";
}

%>


<html>
<head>
<meta http-equiv="REFRESH" content="0;url=<%=fwdPage %>">
<title>Login</title>
</head>
<body>
Please wait, Registering User...
</body>
</html>