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

// save start and end
String start = request.getParameter("start");
if (!Helper.nullOrEmpty(start))
	crawlManager.setStart(start);
	
String end = request.getParameter("end");
if (!Helper.nullOrEmpty(end))
	crawlManager.setEnd(end);

if (!Helper.nullOrEmpty(start) && crawlManager.getDistance(start, end) > 8)
	throw new UHException("We don't support straight line distances greater than 8km yet, sorry.");

%>

<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link href="./style.css" rel="stylesheet" type="text/css" />
<title>Please wait</title>
</head>
<body>
<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">	
	<h3>Step 2: Configure your QuickCrawl</h3>
	<form name="options" action="generate.jsp" method="GET">
		<p>
		Title: <input type="text" name="title" />
		</p>
		
		<p>
		Number of places:
			<select name="numberOfPlaces">
				<option value="3">3 Places</option>
				<option value="4">4 Places</option>
				<option value="5">5 Places</option>
				<option value="6">6 Places</option>
				<option value="7">7 Places</option>
				<option value="8">8 Places!</option>
			</select>
		</p>
		<input type="submit" value="Generate my crawl!"/>
		</form>
	</div>
	<jsp:include page="footer.jsp" />
</div>
</body>
</html>