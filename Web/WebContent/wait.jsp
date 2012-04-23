<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%@page import="core.global.*"%>


<%
QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");

if (!userManager.isLoggedIn()) {
	throw new UHException("You must be logged in to generate crawls at this moment.");
}

crawlManager.generate();
String id;
if (crawlManager.getCrawlId() == null || crawlManager.getCrawlId().equals("")) {
	//persist as it's got no id, hence it's not been saved yet
	id = crawlManager.persist(userManager.getUser());
	userManager.getCrawls().add(id);
} else {
	id = crawlManager.getCrawlId();
}

%>

<html>
<head>
<meta http-equiv="REFRESH" content="0;url=crawl.jsp?id=<%=id%>">
<link href="./style.css" rel="stylesheet" type="text/css" />
<title>Please wait</title>
</head>
<body>
<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">		
		<jsp:include page="loading.jsp" />		
	</div>
	<jsp:include page="footer.jsp" />
</div>
</body>
</html>