<%@page import="web.*"%>
<%@page import="core.global.*"%>
<%@page import="core.support.*"%>

<% 
	// the actual generation is done in the next page
	// but we need a waiting page before the server starts working
	QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");
	UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
	UserSettings settings = crawlManager.getSettings();
%>

<h2>Edit your QuickCrawl</h2>

	<form name="locations" action="doedit.jsp" method="GET">
	Change the number of places you want to visit<br />
			<select name="numberOfPlaces">
			<% for (int i=3; i<9; i++) { %>
				<option value="<%=i %>" <% if (settings.getNumberOfPlaces() == i) { %> selected="selected" <% } %> ><%=i%> Places</option>
			<% } %>
			</select><br />
	AverageRatingWeight <input type="text" name="AverageRatingWeight" value="<%=settings.AverageRatingWeight %>" /><br />
	TotalDistanceWeighting <input type="text" name="TotalDistanceWeighting" value="<%=settings.TotalDistanceWeighting %>" /><br />
	EndPlaceClosenessWeighting <input type="text" name="EndPlaceClosenessWeighting" value="<%=settings.EndPlaceClosenessWeighting %>" /><br />
	DistanceDifferenceWighting <input type="text" name="DistanceDifferenceWighting" value="<%=settings.DistanceDifferenceWighting %>" /><br />
	<input type="submit" value="Edit"/><br />
	</form>
