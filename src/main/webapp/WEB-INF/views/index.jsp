<%@ include file="layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="layout/header.jsp">
	<jsp:param value="Diploma Generator" name="title" />
	<jsp:param value="index" name="page" />
</jsp:include>

<!-- Jumbotron -->
<div class="jumbotron">
	<h1>Diploma generator!</h1>
	<p class="lead">Create beautiful diploma, certificate or award of
		excellence using this free online generator. It is fully customizable
		and the output is a simple PDF file ready to print (or you can send
		emails to your customers via email).</p>
	<p>
		<a class="btn btn-lg btn-success" href="register.html">Get started
			today</a>
	</p>
</div>


<jsp:include page="layout/footer.jsp" />