<%@ page isErrorPage = "true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>

<% 
	// the actual generation is done in the next page
	// but we need a waiting page before the server starts working
	QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");
	UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="style.css" rel="stylesheet" type="text/css" />
<title>Whoopsies</title>
</head>
<body>
<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">
	<h1>Error, argh!</h1>
	<h2>Exception</h2>
	<p>
	<% if (exception == null) { %>
		Exception is NULL.
	<% } else { %>
				
		<%=exception.toString() %><br />
		<% for (StackTraceElement element : exception.getStackTrace()) { %>
			<%=element.toString() %><br />
		<% } %>
	<% } %>
	</p>
		<h2>UserManager</h2>
		<p>
		<% if (userManager == null) { %>
			UserManager is NULL.
		<% } else { %>
			UserManager loaded.<br />
			User: <%= userManager.getUser() %><br />
			LoggedIn: <%= userManager.isLoggedIn() %><br />		
		<% } %>
		</p>
		
		<h2>CrawlManager</h2>
		<p>
		<% if (crawlManager == null) { %>
			crawlManager is NULL.
		<% } else if (crawlManager.isCrawlLoaded()) { %>
			Crawl Loaded.<br />
			ID: <%=crawlManager.getCrawlId() %><br />
			Start: <%=crawlManager.getStart() %><br />
			End: <%=crawlManager.getEnd() %><br />
			<% if (crawlManager.getCrawl() != null) { %>
				Crawl: <%=crawlManager.getCrawl().toString() %><br />			
			<% } else { %>
				Crawl is NULL.
			<% } %>
			
		<% } else { %>
			Crawl Loaded.<br />
			Crawl is NULL.<br />
		<% } %>
		</p>
		
		<h2>Log</h2>
		<p>
		<% if (crawlManager != null) { %>
			<%=crawlManager.getFullLog() %>
		<% } else { %>
			No log as crawlManager is NULL.
		<% } %>
		</p>
	</div>
	<jsp:include page="footer.jsp" />
</div>
</body>
</html>