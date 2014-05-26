<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>Error</title>
<style>
body,pre {
	font-family: 'ms gothic', 'arial';
	font-size: 12px;
}

a {
	text-decoration: none;
	color: blue;
	margin-bottom: 10px;
}
pre {
	margin-bottom: 25px;
}
</style>
</head>
<body>
	<tiles:insertAttribute name="body" />
</body>
</html>