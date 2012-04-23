<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%@page import="core.support.*" %>
<%@page import="core.global.*" %>

<%
QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");

if (!userManager.isLoggedIn()) {
	throw new UHException("You must be logged in to generate crawls at this moment.");
}

// set-up the settings
String start = request.getParameter("start");
if (!Helper.nullOrEmpty(start))
	crawlManager.setStart(start);
	
String end = request.getParameter("end");
if (!Helper.nullOrEmpty(end))
	crawlManager.setEnd(end);

crawlManager.setNumberOfPlaces(request.getParameter("numberOfPlaces"));
crawlManager.setTitle(request.getParameter("title"));

crawlManager.setUser(userManager.getUser());

if (!Helper.nullOrEmpty(start) && crawlManager.getDistance(start, end) > 8)
	throw new UHException("We don't support straight line distances greater than 8km yet, sorry.");

%>

<html>
<head>
<meta http-equiv="REFRESH" content="0;url=wait.jsp">
<link href="./style.css" rel="stylesheet" type="text/css" />
<title>Please wait</title>
</head>
<body>
<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">		
		<jsp:include page="loading.jsp" />		
	</div>
	<jsp:include page="footer.jsp" />
</div> 
</body>
</html>