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

<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
  function initialize() {
    var latlng = new google.maps.LatLng(<%= crawlManager.getCenterOfCrawl() %>);
    var myOptions = {
      zoom: 14,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    var map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions);

    var start = new google.maps.Marker({ 
	position: new google.maps.LatLng(<%= crawlManager.getStart() %>), 
		map: map,
		icon: "./artwork/maps/starticon.png",
		title: "start"
		}); 
	var end = new google.maps.Marker({ 
		position: new google.maps.LatLng(<%= crawlManager.getEnd() %>), 
		map: map, 
		icon: "./artwork/maps/endicon.png",
		title: "end"
		});

	<%
	int i=0;
	for (AggPlace place : places) {	
		String iconFileName = "./artwork/maps/shadowbeer_" + (i+1) + ".png";
		String markerVar = "marker_" + i;
		String inforWindowVar = "infoWindow_" + i;
	%>

	var <%=markerVar%> = new google.maps.Marker({
			position: new google.maps.LatLng(<%=place.Location.toString()%>), 
			map: map,
			title: '<%= place.toString() %>',
			icon: "<%=iconFileName%>"
	});

		
	var <%=inforWindowVar%> = new google.maps.InfoWindow({
	    content: '<img src="<%=place.imageUrl%>" /> <%=place.toString()%> '
	});
		
	google.maps.event.addListener(<%=markerVar%>, 'click', function() {
		closeAll();
		  <%=inforWindowVar%>.open(map,<%=markerVar%>);
		});
	
	<% 
	 i++;
	}
	%>

	function closeAll() {
		<%
		i=0;
		for (AggPlace place : places) {	
			String inforWindowVar = "infoWindow_" + i;
		%>
		<%=inforWindowVar%>.close();
		
		<%
		i++;
		}
		%>
	}
		
}

</script>

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<title>QuickCrawl: <%=crawlManager.getSettings().getTitle() %></title>
</head>
<body onload="initialize()">

<div id="container">
	<jsp:include page="header.jsp" />	
		<div id="content">
		<h2>QuickCrawl: <u><%=crawlManager.getSettings().getTitle() %></u> by <%=crawlManager.getSettings().getUser() %></h2>
		<div style="position:relative; float: left;">		
		Share this with your friends: <input type="text" readonly="readonly" value="<%=fullUrl %>"></input>
		</div>	
		<a style="position:relative; float: right; " href="result_listview.jsp">List View</a>
			<div style="clear: both"></div>
			<div id="map_canvas"></div>
			Log From Generation:
			<p style="font-size: x-small;"><%= crawlManager.getFullLog() %></p>		
	</div>
	<jsp:include page="footer.jsp" />
</div>    
</body>
</html>
