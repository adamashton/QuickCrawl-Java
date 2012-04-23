<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");

String crawlId = request.getParameter("id");

userManager.deleteCrawl(crawlId);

String fwdPage = "../myaccount.jsp";

%>


<html>
<head>
<meta http-equiv="REFRESH" content="0;url=<%=fwdPage %>">
<title>Login</title>
</head>
<body>
Please wait, Deleting crawl...
</body>
</html>