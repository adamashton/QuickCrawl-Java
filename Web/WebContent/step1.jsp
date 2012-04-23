<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="error.jsp" %>


<%@page import="web.*"%>
<%@page import="core.global.UHException" %>

<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");

//if they came here direct, or they have no settings set fwd to the start page.
if (userManager == null || !userManager.isLoggedIn()) {
	String url = response.encodeRedirectURL("login.jsp");
    response.sendRedirect(url);
}

//new manager class for each pub crawl that is made.
QuickCrawlWebManager crawlManager = new QuickCrawlWebManager();
session.setAttribute("crawlManager", crawlManager);

%>

<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link href="./style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.2.min.js"> </script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
  var map;
  var start = null;
  var end = null;
  var geocoder;

  function initialize() {
    var latlng = new google.maps.LatLng(52.490914,-0.206341);
    var myOptions = {
      zoom: 14,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions);
    geocoder = new google.maps.Geocoder();	
    google.maps.event.addListener(map, 'click', function(event) { placeMarker(event.latLng); });
    get_location();     
  }

  function get_location() {
	  navigator.geolocation.getCurrentPosition(updateMap);	  
  }

  function updateMap(position) {
	  var latitude = position.coords.latitude;
	  var longitude = position.coords.longitude;
	  var latlng = new google.maps.LatLng(latitude, longitude);
	  map.setCenter(latlng);
	}
  
  
  function placeMarker(location) {
    if (start == null) {
      start = new google.maps.Marker({ 
          position: location, 
          map: map, 
          draggable: true,
          icon : "./artwork/maps/starticon.png", 
          title: "Start" 
         });
      google.maps.event.addListener(start, 'dragend', function(event) { setFormValue('start', event.latLng); });
      setFormValue('start', location);
    } else if (end == null) {
      end = new google.maps.Marker({ 
          position: location, 
          map: map, 
          draggable: true, 
          title: "End",
          icon: "./artwork/maps/endicon.png" 
          });
      google.maps.event.addListener(end, 'dragend', function(event) { setFormValue('end', event.latLng); });
      setFormValue('end', location);
    }
  }

  function codeAddress(search) {	
	    geocoder.geocode( { 'address': search}, function(results, status) {
	      if (status == google.maps.GeocoderStatus.OK) {
	        map.setCenter(results[0].geometry.location);
	        var marker = new google.maps.Marker({
	            map: map,
	            position: results[0].geometry.location
	        });
	      } else {
	        alert("Could not find location. Error message: " + status);
	      }
	    });
	  }

  
	  

  function setFormValue(element, location) {
	document.forms['locations'].elements[element].value = location;
  }


  function handleFind() {
	var search = document.getElementById("search").value;
	if (search != '') { 
	  codeAddress(search);
	}
  }

  function next()
  {
	  var start = document.getElementById('start').value;
	  var end = document.getElementById('end').value;
	  if (start == '') {
		 alert("Error: You must click a point on the map to choose a start and end point.");
	  } else if (end == '') {
		alert("Error: You must click a point on the map to choose an end point.");
	  } else {
		document.locations.submit();
	  }
  }

</script>

</head>

<body onload="initialize()">

<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">
	<h3>Step 1: Choose a start and end point by clicking on the map. <i>[Hint: They're draggable.]</i></h3>		
			
		
		<div style="float: left; margin: 5px">
		<form name="gmapsearch">
		Location:
		<input type="text" name="search" id="search" />
		<input type="button" value="Find" onclick="handleFind()"/>
		</form>
		</div>
		
		<form name="locations" action="step2.jsp" method="post">
		<input type="hidden" name="start" id="start" />
		<input type="hidden" name="end" id="end"/>
		</form>
		
		<div style="float: right; margin: 5px">
			<button onclick="javascript:next()" >Next ></button>
		</div>
		
		
		<div style="clear: both"></div>
		<div id="map_canvas" style="height: 500px; margin: thin black solid"></div>
		
		<div style="float: right; margin: 5px">
			<button onclick="javascript:next()" >Next ></button>
		</div>
				
	</div>
	<jsp:include page="footer.jsp" />
</div> 
</body>
</html>