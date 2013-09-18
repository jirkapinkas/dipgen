<%@ include file="layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="layout/header.jsp">
	<jsp:param value="Diploma Generator: My Profile" name="title" />
	<jsp:param value="user-details" name="page" />
</jsp:include>


<form:form commandName="user" cssClass="registerForm form-signin">
	<h2 class="form-signin-heading">My Profile:</h2>
	<c:if test="${param.success}">
		<div class="alert alert-success">
			Your profile has been updated successfully.
		</div>
	</c:if>
	<div class="alert alert-info">
		All fields are required.
	</div>
	<div class="nameError"></div>
	<label for="password">Password:</label>
	<form:password path="password" cssClass="password1 form-control" placeholder="Password" />
	<input type="password" name="password2" id="password2" class="password2 form-control" placeholder="Password again" />
	<label for="email">Email:</label>
	<form:input path="email" id="email" cssClass="email form-control" placeholder="Email" /><br />
	<input type="submit" class="btn btn-lg btn-primary btn-block" value="Edit Profile" /><br />
	<a href="user-details.html?upgrade=true" class="btn btn-lg btn-block btn-success">Upgrade to Premium</a><br />
	<a href="user-details/delete-account.html" class="btn btn-lg btn-block btn-danger deleteAccount" onclick="return confirm('Are you really really really sure you want to delete your account? There\'s no going back.')">Delete Account</a>
</form:form>

	
	<script type="text/javascript">
		$(document).ready(function() {

			$(".registerForm").submit(function(e) {
				var p1 = $(".password1").val();
				var p2 = $(".password2").val();
				var email = $(".email").val();

				if (p1 == "" || p2 == "" || email == "") {
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

<jsp:include page="layout/footer.jsp" />