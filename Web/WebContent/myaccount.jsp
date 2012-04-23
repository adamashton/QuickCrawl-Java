<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%@page import="core.global.CrawlManager" %>
<%@page import="java.util.List"%>

<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");

List<String> allCrawls = null;
if (userManager == null || !userManager.isLoggedIn()) {
	String url = response.encodeRedirectURL("login.jsp");
    response.sendRedirect(url);
}
%>

<html>
<head>
<link href="style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>A list of <%=userManager.getUser()%>'s generated QuickCrawl's!</title>
</head>
<body>
<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">
		<div style="float: left; width: 50%">
			<h2>Your Personal Details</h2>
			<p>Username: <%=userManager.getUser() %></p>
			<p>E-mail: <%=userManager.getEmail() %></p>
			
			<p><a href="#">Delete account ;-(</a></p>
		</div>
		
		<div style="float: right; width: 50%">
			<jsp:include page="mycrawls.jsp"></jsp:include>
		</div>	
	</div>
	<jsp:include page="footer.jsp" />
</div>
</body>
</html>