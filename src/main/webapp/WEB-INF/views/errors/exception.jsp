<%@ include file="../layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>

<%@ include file="../layout/header.jsp"%>


<h1>Oops! An error has occured! :(</h1>

<div class="alert alert-danger">Error message: ${pageContext.exception.message}<br />Error type: <%= exception.getClass().getSimpleName()  %> 
<%-- ${pageContext.exception.class.simpleName} --%>
</div>

<jsp:include page="../layout/footer.jsp" />