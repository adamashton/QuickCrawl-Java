<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="web.*"%>
<%@page import="core.global.*"%>
<%@page import="core.support.*"%>
<%@page import="java.util.*"%>

<%
	QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");
	UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
	
	if (crawlManager == null) {
		crawlManager = new QuickCrawlWebManager();
		session.setAttribute("crawlManager", crawlManager);		
	}
	
	
	if (userManager == null) {
		userManager = new UserWebManager(request.getCookies());
		session.setAttribute("userManager", userManager);
	}
	
	// get crawl
	String id = request.getParameter("id");
	if (Helper.nullOrEmpty(id)) {
		throw new UHException("No crawl ID was given; cannot find crawl.");
	}
	
	if (!crawlManager.isCrawlLoaded() || !crawlManager.getCrawlId().equals(id)) {		
		crawlManager.load(id);
	}
	
	List<AggPlace> places = crawlManager.getCrawl().getPlaces();
	String fullUrl = WebSupport.getURLWithContextPath(request) + "/crawl.jsp?id=" + crawlManager.getCrawlId();
	
	boolean loadMap = true;
	boolean loadEdit = false;
	boolean loadPlace = false;
	
	boolean canEdit = userManager.isLoggedIn() 
		&& crawlManager.getSettings().getUser().equals(userManager.getUser());
	int place = -1;
	
	String action = request.getParameter("action"); 
	if (!Helper.nullOrEmpty(action) && action.equals("edit") && canEdit) {
		loadEdit = true;
		loadMap = false;
		loadPlace = false;
	} else if (!Helper.nullOrEmpty(action) && action.equals("place")) {
		loadPlace = true;
		loadMap = false;
		loadEdit = false;
		
		try {
			place = Integer.parseInt(request.getParameter("place"));
		}
		catch (NumberFormatException e) {
			loadPlace = false;
			loadMap = true;
		}
	}
%>

<html>
<head>
<link href="./style.css" rel="stylesheet" type="text/css" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
<script type="text/javascript">


function initialize() {
	<% if (loadMap) { %>

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
	for (AggPlace p : places) {	
		String iconFileName = "./artwork/maps/shadowbeer_" + (i+1) + ".png";
		String markerVar = "marker_" + i;
		String inforWindowVar = "infoWindow_" + i;

	%>
	
	var <%=markerVar%> = new google.maps.Marker({
			position: new google.maps.LatLng(<%=p.Location.toString()%>), 
			map: map,
			title: '<%=WebSupport.eJS(p.Title)%>',
			icon: "<%=iconFileName%>"
	});
	
		
	var <%=inforWindowVar%> = new google.maps.InfoWindow({
	    content: '<h3><%=WebSupport.eJS(p.Title)%></h3><p>'
	    	+ '<% if (!Helper.nullOrEmpty(p.Address1)) { %><%=WebSupport.eJS(p.Address1)%><br /> <% } %>'
	    	+ '<% if (!Helper.nullOrEmpty(p.Address2)) { %><%=WebSupport.eJS(p.Address2)%><br /> <% } %>'
	    	+ '<% if (!Helper.nullOrEmpty(p.Address3)) { %><%=WebSupport.eJS(p.Address3)%><br /> <% } %>'
	    	+ '<% if (!Helper.nullOrEmpty(p.PhoneNumber)) { %><%=WebSupport.eJS(p.PhoneNumber)%><br /> <% } %>'
		    + '<a href="crawl.jsp?id=<%=crawlManager.getCrawlId()%>&action=place&place=<%=i%>">See more</a></p>'
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
		for (i=0; i<places.size(); i++) {	
			String inforWindowVar = "infoWindow_" + i;
		%>
		<%=inforWindowVar%>.close();
		
		<%
		
		}
		%>
	}

		
	<% } else if (loadPlace) { %>
	showPlaceOnMap();
	<% } %>
	}


(function(d, s, id) {
	  var js, fjs = d.getElementsByTagName(s)[0];
	  if (d.getElementById(id)) {return;}
	  js = d.createElement(s); js.id = id;
	  js.src = "//connect.facebook.net/en_US/all.js#appId=289853287697575&xfbml=1";
	  fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
</script>
<title>Your QuickCrawl</title>
</head>
<body onload="initialize()">

<div id="container">
	<jsp:include page="header.jsp" />		
	<div id="content">
		<div id="resultLeft">
		<h2><u><%=crawlManager.getSettings().getTitle() %></u> by <%=crawlManager.getSettings().getUser() %></h2>
		
			<p style="font-size: large" ><a href="crawl.jsp?id=<%=crawlManager.getCrawlId()%>">Overview</a></p>
			
			<% if (canEdit) { %>
			<p style="font-size: large" ><a href="crawl.jsp?id=<%=crawlManager.getCrawlId()%>&action=edit">Edit</a></p>
			<% } %>
			<hr />	
			
			<% for (int i=0; i<places.size(); i++) { 
				AggPlace p = places.get(i); 
			%>
				<p style="font-size: medium;" >
					<%=i+1%>) <a href="crawl.jsp?id=<%=crawlManager.getCrawlId()%>&action=place&place=<%=i%>"><%=p.Title%></a>
				</p>
			<% } %>
			<hr />
			
			<p>Share this QuickCrawl!<br />
			<a href="<%=fullUrl %>"><%=fullUrl %></a></p>
			
			<div id="fb-root"></div>
			<div class="fb-like" data-href="www.adamashton.com/quickcrawl" data-send="false" data-layout="button_count" data-width="300" data-show-faces="false" data-font="arial"></div>
		 
		</div>
		
		<div id="resultDetails">
			<% if (loadEdit) { %>
				<jsp:include page="edit.jsp"></jsp:include>
			<% } else if (loadMap) { %>			
				<div id="map_canvas"></div>
			<% } else if (loadPlace) { %>
				<jsp:include page="place.jsp">
					<jsp:param value="<%=place%>" name="pos"/>
				</jsp:include>
			<% } %>
			
		</div>
		
	</div>
	
		<div style="clear: both;"></div>
		<p style="font-size: small"><%=crawlManager.getFullLog() %></p>
	
</div>	 
</body>
</html>