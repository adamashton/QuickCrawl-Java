<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.QuickCrawlWebManager"%>
<%@page import="java.util.List"%>



<%
// new manager class for each pub crawl that is made.
QuickCrawlWebManager manager = (QuickCrawlWebManager) session.getAttribute("manager");
if (manager == null) {
	manager = new QuickCrawlWebManager();
	session.setAttribute("manager", manager);
}
	
%>

<html>
<head>
<link href="style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>A list of generated QuickCrawl's!</title>
</head>
<body>
<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">		

<%
List<String> allCrawls = manager.getAllGeneratedCrawls();
for (String id : allCrawls)	{
%>
	<a href="result.jsp?id=<%=id %>" style="font-size: 30px"><%=id %></a><br />
	
<%	
}
%>
	</div>
	<jsp:include page="footer.jsp" />
</div> 


</body>
</html>