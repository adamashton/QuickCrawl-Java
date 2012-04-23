<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="error.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="web.*"%>
<%@page import="core.global.*"%>
<%@page import="java.util.*"%>

<%
QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");

// if they came here direct, or they have no settings set fwd to the start page.
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
	<%
	for (int i=0; i<places.size(); i++) {
		AggPlace p = places.get(i);
		String map_id = "map_" + i;
	%>		
	var latlng_<%=i%> = new google.maps.LatLng(<%=p.Location.toString() %>);
	var myOptions_<%=i%> = { zoom: 15, center: latlng_<%=i%>, mapTypeId: google.maps.MapTypeId.ROADMAP };
	var map_<%=i%> = new google.maps.Map(document.getElementById('<%=map_id %>'), myOptions_<%=i%>);
	var place_<%=i%> = new google.maps.Marker({ 
 		position: new google.maps.LatLng(<%=p.Location.toString() %>), 
 		map: map_<%=i%>,
 		title: "<%=p.Title %>",
 		icon: "./artwork/maps/shadowbeer.png"
	});	
<% } %>
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
		<p style="font-size: medium; margin: 5px">
		Your QuickCrawl, sir! Share this with your friends: <input type="text" readonly="readonly" value="<%=fullUrl %>"></input>
		<span style="float: right; margin-right: 5px;" >
		<a href="result_mapview.jsp">Map View</a></span>
		</p>
		<br />
		
		<div style="margin-left: 10px;" >
		Distance to the first place: <%= crawlManager.getDistance(crawlManager.getSettings().getStart(), places.get(0).Location) %>km
		(<a href="#">Get Directions</a>)
		</div>
		
		<div id="container">
		  
		  <%
		  for (int i=0; i<places.size(); i++) {
			String pageLink = "place_listview.jsp?pos=" + i;
			AggPlace p = places.get(i);
		  %>  	
		  	<jsp:include page="<%=pageLink %>" />
		  	
		  	<% 
		String distance;
		if (i < crawlManager.getCrawl().getPlaces().size() - 1) {
		distance = crawlManager.getDistance(p.Location, crawlManager.getCrawl().getPlaces().get(i + 1).Location);
		%>
		 
		<div style="margin-left: 10px">
		
		Distance to next: <%=distance%>km (<a href="#">Get Directions</a>) 
		
		<div style="position: relative; top:-14px; left: 280px; max-width: 720px">
		<hr />
		</div>
		
		</div>
		 
		<% } else { 
		 	distance = crawlManager.getDistance(p.Location, crawlManager.getSettings().getEnd());
		%>
		<div style="margin-left: 10px">
		Distance to end: <%=distance%>km (<a href="#">Get Directions</a>)
		</div>	
		<% }  %>
		  	
		  	
		  	
		   
		   <% } %>
		
		
		  <br />
		  <p style="font-size: small"><%= crawlManager.getFullLog() %></p>
		  </div>		
	</div>
	<jsp:include page="footer.jsp" />
</div>	 
</body>
</html>
