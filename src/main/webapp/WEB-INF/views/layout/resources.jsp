<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="static.jsp" %>

<!-- JQuery -->
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery-2.0.3.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery-2.0.3.min.map" />"></script>

<!-- JQuery UI -->
<c:url value="/resources/js/jquery-ui.min.js" var="jqueryUiUrl" />
<script type="text/javascript" src="${jqueryUiUrl}"></script>
<c:url value="/resources/css/jquery-ui.css" var="jqueryUiCssUrl" />
<link href="${jqueryUiCssUrl}" rel="stylesheet" type="text/css" />


<!-- Twitter Bootstrap -->
<!-- Latest compiled and minified CSS -->
<c:url value="/resources/css/bootstrap.min.css" var="bootstrapCss" />
<link rel="stylesheet" href="${bootstrapCss}">

<c:url value="/resources/css/signin.css" var="bootstrapSigninCss" />
<link href="${bootstrapSigninCss}" rel="stylesheet">

<!-- Optional theme -->
<c:url value="/resources/css/bootstrap-theme.min.css" var="bootstrapThemeCss" />
<link rel="stylesheet" href="${bootstrapThemeCss}">

<!-- Glyphicons -->
<c:url value="/resources/css/bootstrap-glyphicons.css" var="bootstrapGlyphiconsCss" />
<link rel="stylesheet" href="${bootstrapGlyphiconsCss}">



<!-- Latest compiled and minified JavaScript -->
<c:url value="/resources/js/bootstrap.min.js" var="bootstrapJs" />
<script src="${bootstrapJs}"></script>