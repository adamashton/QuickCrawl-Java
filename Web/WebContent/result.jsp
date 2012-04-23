<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="error.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="web.*"%>
<%@page import="core.global.*"%>
<%@page import="java.util.*"%>

<%
QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");

// if they came here direct, or they have no settings fwd to the start page.
if (crawlManager == null || !crawlManager.isCrawlLoaded()) {
	String url = response.encodeRedirectURL("step1.jsp");
    response.sendRedirect(url);
}

List<AggPlace> places = crawlManager.getCrawl().getPlaces();
String fullUrl = WebSupport.getURLWithContextPath(request) + "/crawl.jsp?id=" + crawlManager.getCrawlId(); 

%>

<html>
<head>
<link href="./style.css" rel="stylesheet" type="text/css" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
<script type="text/javascript">

function initialize() {	

}

function initialize_place(title,location,id) {
	
}
</script>
<title>Your QuickCrawl</title>
</head>
<body onload="initialize()">

<div id="container">
	<jsp:include page="header.jsp" />		
	<div id="content">
		
		<div id="resultLeft">
		
		<% for (AggPlace place : places) { %>
			<p style="font-size: large" ><%=place.Title %></p>
			<p>
			<% for (Place p : place.getAggregatedPlaces()) { %> 
				<%= p.AvgRating %> (<%=p.NumReviews%> ratings.)<br />
			<% } %>
			</p>
			<hr />
		<% } %> 
		</div>
		
		<div id="resultDetails">
			details
		</div>
		<p style="font-size: x-small;"><%=crawlManager.getFullLog() %></p>
	</div>
</div>	 
</body>
</html>
