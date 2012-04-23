<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="error.jsp" %>


<%@page import="web.*"%>
<%@page import="core.global.UHException" %>

<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link href="./style.css" rel="stylesheet" type="text/css" />
</head>

<body onload="initialize()">

<div id="container">
	<jsp:include page="header.jsp" />	
	<div id="content">
	
	<div style="float: left; width: 490px">
		<p style="font-size: large">
		* New to a city or on holiday somewhere?<br />
		* Are you bored of the same pubs you visit?<br />
		* Looking for the coolest pubs/bars in your area?<br />
		</p>
		<p>QuickCrawl is a free service that creates a pub crawl in seconds only picking the best pubs and bars that are on your chosen route. After it's created you can share it with your friends on facebook or twitter.
		<a href="step1.jsp" style="font-size: x-large">Get QuickCrawling now!</a>
	</div>
	
	<div style="float: right; width: 490px" >
		<p style="font-size: large">* Know where you want to go?<br />
		* Want to easily create a pub crawl?<br />
		* Want some recommendations while you create?<br />
		</p>
		<p>CustomCrawl is a free service that lets you easily create a pub crawl either from our recommendations or your own choice. After it's created you can share it with your friends on facebook or twitter.</p>
		<a href="#" style="font-size: x-large">Coming soon!</a>
	</div>
	
	<div style="clear: both;"></div>
	</div>
	<jsp:include page="footer.jsp" />
</div> 
</body>
</html>