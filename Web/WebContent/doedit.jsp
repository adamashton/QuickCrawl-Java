<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="web.*"%>
<%@page import="core.global.*" %>
<%
UserWebManager userManager = (UserWebManager) session.getAttribute("userManager");
QuickCrawlWebManager crawlManager = (QuickCrawlWebManager) session.getAttribute("crawlManager");
UserSettings settings = crawlManager.getSettings();

crawlManager.setNumberOfPlaces(request.getParameter("numberOfPlaces"));

double AverageRatingWeight = Double.parseDouble(request.getParameter("AverageRatingWeight"));
settings.AverageRatingWeight = AverageRatingWeight;

double TotalDistanceWeighting = Double.parseDouble(request.getParameter("TotalDistanceWeighting"));
settings.TotalDistanceWeighting = TotalDistanceWeighting;

double DistanceDifferenceWighting = Double.parseDouble(request.getParameter("DistanceDifferenceWighting"));
settings.DistanceDifferenceWighting = DistanceDifferenceWighting;

double EndPlaceClosenessWeighting = Double.parseDouble(request.getParameter("EndPlaceClosenessWeighting"));
settings.EndPlaceClosenessWeighting = EndPlaceClosenessWeighting;
%>

<html>
<head>
<meta http-equiv="REFRESH" content="0;url=wait.jsp">
<title>Please wait</title>
</head>
<body>
Please wait...
</body>
</html>