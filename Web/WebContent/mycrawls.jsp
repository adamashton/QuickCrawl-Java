<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="web.*"%>
<%@page import="core.global.CrawlManager" %>
<%@page import="java.util.List"%>

<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
List<QuickCrawlWebManager> allCrawls = userManager.downloadCrawls();
%>

<% if (allCrawls != null && allCrawls.size() > 0) { %>
	<h2>Your Crawls.</h2>
	<%
	for (QuickCrawlWebManager crawlManager : allCrawls)	{
		String fullUrl = WebSupport.getURLWithContextPath(request) + "/crawl.jsp?id=" + crawlManager.getCrawlId();
	%>
		<p>
		<a style="font-size: large" href="<%=fullUrl %>"><%=crawlManager.getSettings().getTitle() %></a>
		<a style="margin-left: 10px" href="action/deletecrawl.jsp?id=<%=crawlManager.getCrawlId()%>"><img src="artwork/icon-delete.png" alt="Delete"></img></a>
		<br />
		Created: <%=crawlManager.getCrawl().getCreated().toLocaleString() %>.<br />  
		Number of Places: <%=crawlManager.getCrawl().getPlaces().size() %>.<br /> 
		</p>		
	<% } %>
	
<% } else { %>
	You haven't made any QuickCrawl's yet ... <a href="step1.jsp" style="font-size: 40px">Get QuickCrawling Now!</a>
<% } %>