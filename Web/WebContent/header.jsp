<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="web.*"%>
<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
if (userManager == null || !userManager.isLoggedIn()) {
	userManager = new UserWebManager(request.getCookies());
	session.setAttribute("userManager", userManager);
}
%>
<div id="header">

<!--  Logo  -->
<div id="headercontent">
<a href="<%=WebSupport.getURLWithContextPath(request)%>"><img src="./artwork/header/header.png" alt="QuickCrawl" id="headerimage" /></a>

<!-- nav bar -->
<a class="nav" href="index.jsp">Home</a> 
<a class="nav" href="myaccount.jsp">My Crawls</a>
<a class="nav" href="step1.jsp">New Crawl</a>

<!--  login status -->
<p class="loginstatus">  
<% if (userManager.isLoggedIn()) { %>
<a href="myaccount.jsp"><%= userManager.getUser() %></a> | <a href="dologout.jsp">Logout</a>
<% } else { %>
<a href="login.jsp">Login</a>
<% } %>
</p>

</div>

<hr style="color: black; margin-top: 1px" />
</div>