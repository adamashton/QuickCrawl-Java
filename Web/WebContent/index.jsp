<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
if (userManager == null) {
	userManager = new UserWebManager(request.getCookies());
	session.setAttribute("userManager", userManager);
}

String getStartedLink = "step1.jsp";
if (!userManager.isLoggedIn())
	getStartedLink = "login.jsp";
%>

<html>
<head>
<link href="style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Purple Dinosaur Presents QuickCrawl</title>
</head>
<body>

<div id="container">

	<jsp:include page="header.jsp" />
	
	<div id="content">
		<p style="font-size: large">
		<a href="<%=WebSupport.getURLWithContextPath(request) %>">QuickCrawl</a> generates pub crawls 
		on-demand for you. Using pub and bar data provided by 
		<a href="http://www.qype.com/">Qype</a> and <a href="http://www.yelp.com/">Yelp</a> we can search
		up to hundreds of places, generating up to thousand's of QuickCrawls before selecting the best one for you.<br />
		</p> 
		
		<a href="<%=getStartedLink %>" style="font-size: 30px; text-align:center;">&gt;&gt; Get QuickCrawling Now! &gt;&gt;</a>
		<div style="padding-top: 20px">
		<img style="margin:20px; border:thin solid white" src="photos/joeadampaul.png"  height="200px" width="266px"  />		
		<img style="margin:20px; border:thin solid white" src="photos/ashtriangle.jpg"  height="200px" width="266px"  />
		<img style="margin:20px; border:thin solid white" src="photos/pauljoeadam.jpeg"  height="200px" width="266px"  />
		</div>
		<a href="<%=getStartedLink %>" style="font-size: 30px; text-align:center;">&gt;&gt; Get QuickCrawling Now! &gt;&gt;</a>
		
	</div>

	<jsp:include page="footer.jsp" />
</div>

</body>
</html>