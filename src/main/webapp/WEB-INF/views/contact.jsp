<%@ include file="layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="layout/header.jsp">
	<jsp:param value="Diploma Generator: Contact" name="title" />
	<jsp:param value="contact" name="page" />
</jsp:include>

<h1>Contact us</h1>

<p>Do you miss some key feature? Have you found a bug? Contact us so
	we can fix it.</p>

<form class="form-signin" method="post" action="contact.html">
	<c:if test="${param.sent != null}">
		<div class="alert alert-success">Thank you, your message has
			been sent</div>
	</c:if>

	<security:authorize access="isAuthenticated()" var="loggedIn" />
	<c:choose>
		<c:when test="${!loggedIn}">
			<input type="text" name="email" id="email" class="form-control"
				placeholder="Email (if you want answer)" />
			<br />
		</c:when>
	</c:choose>

	<textarea rows="10" id="question" name="question" class="form-control"
		placeholder="Question"></textarea>

	<br /> <input type="submit" class="btn btn-lg btn-primary btn-block" />
</form>

<script type="text/javascript">
	$(document).ready(function() {
		$(".form-signin").submit(function(e) {
			if($("#question").val().trim() == "") {
				alert("You must enter your question");
				e.preventDefault();
			}
		});
	});
</script>

<jsp:include page="layout/footer.jsp" />