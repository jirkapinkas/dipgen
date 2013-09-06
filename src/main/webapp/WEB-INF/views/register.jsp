<%@ include file="layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register</title>

<%@ include file="layout/resources.jsp"%>

</head>
<body>

<c:if test="${param.success}">
Registration successfull. Now you can <a href="diplomas.html">login</a>
<br/>
</c:if>

	register new user:

	<form:form commandName="user" cssClass="registerForm">
		username: <form:input path="name" cssClass="name" />
		<span class="nameError"></span>
		<br />
		password: <form:password path="password" cssClass="password1" />
		<br />
		password (retype): <input type="password" name="password2" id="password2" class="password2" />
		<br />
		email: <form:input path="email" cssClass="email" />
		<br />
		<input type="submit" />
	</form:form>
	
	all fields are required

	<script type="text/javascript">
		$(document).ready(function() {
			
			$(".name").focusout(function(e) {
				var name = $(this).val();
				$.ajax({
					url: "${pageContext.request.contextPath}/register/check.html?name=" + name,
							cache: false
				}).done(function(data) {
					if(data == "free") {
						$(".nameError").text("ok");
					} else {
						$(".nameError").text("already taken! choose another name");
					}
				});
			});
			
			$(".registerForm").submit(function(e) {
				var p1 = $(".password1").val();
				var p2 = $(".password2").val();
				var name = $(".name").val();
				var email = $(".email").val();

				if (p1 == "" || p2 == "" || name == "" || email == "") {
					e.preventDefault();
					alert("please fill out all fields");
					return;
				}

				if (p1 != p2) {
					e.preventDefault();
					alert("passwords must match!");
					return;
				}
			})
		});
	</script>

</body>
</html>