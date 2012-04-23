<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="web.*"%>
<%@page import="core.global.*"%>
<%@page import="core.support.*"%>
<%@page import="java.util.*" %>

<%
QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");
int pos = Integer.parseInt(request.getParameter("pos"));
AggPlace p = crawlManager.getPlace(pos);
%>

<div id="details">

	<img style="float: right; margin: 5px" src="<%=p.imageUrl%>" alt="<%=p.Title%>" />
	<h2><%=p.Title %></h2>
	<p>
	<% if (!Helper.nullOrEmpty(p.Address1)) { %>
		<%=p.Address1%>, 
		<%=p.PostCode%><br />
	<% } %>
	<% if (!Helper.nullOrEmpty(p.PlaceUrl)) { %>
	<%=p.PhoneNumber %><br />
	<a href="<%=p.PlaceUrl %>"><%=p.PlaceUrl%></a>
	<% } %>	
	</p>
	
	<% if (p.hasOwnerDescription()) { %>
		<p style="font-size: small"><i><%=p.OwnerDescription %></i></p>
	<% } %>
	
	<div style="clear:both"></div>
	
	<div id="map_canvas_place" style="width:200px; height:200px; float: left; margin: 5px"></div>
		
	<div style="margin: 5px; float: left">
	<% for (Place aP : p.getAggregatedPlaces()) { %>
	<p style="margin: 0px; font-size: large">
		<img src="<%=aP.RatingImgUrl %>" style="width: 75px" alt="<%=Double.toString(Helper.twoDecimals(aP.AvgRating)) %>"></img>
		<a href="<%=aP.DataSourceUrl%>"><%=aP.NumReviews%> reviews</a>
		provided by <a href="<%=aP.getDataSourceBaseURL() %>"><img src="<%=aP.getPoweredByImgURL()%>" style="max-width: 100px;"></img></a>
	</p>
	<% } %>
	
	
	<% 
	SimpleLocation a;
	SimpleLocation b = p.Location;
	String aDesc;
	String bDesc = p.Title;
	if (pos == 0) {
		a = crawlManager.getSettings().getStart();
		aDesc = "QuickCrawl Start";
	} else {
		AggPlace prev = crawlManager.getPlace(pos - 1);
		a = prev.Location;
		aDesc = prev.Title;
	}
	%>
	
	<!-- DIRECTIONS -->
	<p style="font-size: large"><a href="<%=WebSupport.getGoogleDirectionsURL(a, aDesc, b, bDesc)%>">Directions to <%=p.Title%></a></p>
	
	</div>
	
	<div style="clear: both"></div>
	
	
	<!--  REVIEWS -->
	<%
	List<Review> yelpReviews = new ArrayList<Review>();
	List<Review> qypeReviews = new ArrayList<Review>();
	for (Review r : p.Reviews) {
		if (r.Source.equals("Yelp")) {
			yelpReviews.add(r);
		} else {
			qypeReviews.add(r);
		}
	}
	
	String yelpWidth = "100%";
	String qypeWidth = "100%";
	String marginLeft = "0%";
	
	if (yelpReviews.size() > 0 && qypeReviews.size() > 0) {
		yelpWidth = qypeWidth = marginLeft = "50%";
	} else if (yelpReviews.size() == 0) {
		yelpWidth = "0%";
	} else  if (qypeReviews.size() == 0) {
		qypeWidth = "0%";
	}
	%>
	
	
	<% if (yelpReviews.size() > 0) { %>
	<!--  YELP REVIEWS -->
	<div style="width: <%=yelpWidth%>; float: left; margin: 5px">
	<a href="http://yelp.com/">
	<img src="http://media3.px.yelpcdn.com/static/20101216276394575/img/developers/reviewsFromYelpRED.gif" />
	</a> 
	<% for (Review review : yelpReviews) { %>
	<p style="font-size: small;">
	<img src="<%=review.RatingImg%>" />
	<%= review.Excerpt %> <a href="<%=review.Url %>">read more</a>
	</p>
	<% } %>
	</div>	
	<% } %>
	
	<% if (qypeReviews.size() > 0) { %>
	<!--  QYPE Reviews -->
	<div style="width: <%=qypeWidth%>; margin-left: <%=marginLeft%>">
	<a href="http://qype.co.uk"><img src="<%=WebSupport.getPoweredByImgURL("Qype") %>" style="max-width: 100px"/></a> 
	<% for (Review review : qypeReviews) { %>
	<p style="font-size: small;">
	<img src="<%=review.RatingImg%>" />
	<%= review.Excerpt %> <a href="<%=review.Url %>">read more</a>
	</p>
	<% } %>
	</div>
	<% } %>
	
</div> 	

<script type="text/javascript">

function showPlaceOnMap() {
    var latlng = new google.maps.LatLng(<%=p.Location.toString()%>);
    var myOptions = {
      zoom: 16,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    var map = new google.maps.Map(document.getElementById("map_canvas_place"),
        myOptions);

    var place = new google.maps.Marker({ 
	position: new google.maps.LatLng(<%=p.Location.toString()%>), 
		map: map,
		title: "<%=p.Title%>",
		icon: "./artwork/maps/shadowbeer.png",
		});
}
</script>