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
