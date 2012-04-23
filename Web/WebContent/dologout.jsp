<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
userManager.logout();

//delete those cookies
Cookie[] cookies = request.getCookies();
if (cookies != null && cookies.length > 0) {
	for (int i=0; i<cookies.length; i++) {
		Cookie c = cookies[i];
		if (c.getName().equals("username") || c.getName().equals("password")) {
			c.setMaxAge(0);
			response.addCookie(c);
		}
	}
}

%>


<html>
<head>
<meta http-equiv="REFRESH" content="0;url=index.jsp">
<title>Logout</title>
</head>
<body>
Please wait, Logging Out...
</body>
</html>