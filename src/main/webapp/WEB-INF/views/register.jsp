<%@ include file="layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="layout/header.jsp">
	<jsp:param value="Diploma Generator: Register" name="title" />
	<jsp:param value="register" name="page" />
</jsp:include>


<form:form commandName="user" cssClass="registerForm form-signin">
	<h2 class="form-signin-heading">Registration:</h2>
	<c:if test="${param.success}">
		<div class="alert alert-success">
			Registration successfull. Now you can <a href="diplomas.html">login</a>
		</div>
	</c:if>
	<div class="alert alert-info">
		All fields are required.
	</div>
	<div class="nameError"></div>
	<form:input path="name" cssClass="name form-control" placeholder="Username" /><br />
	<form:password path="password" cssClass="password1 form-control" placeholder="Password" />
	<input type="password" name="password2" id="password2" class="password2 form-control" placeholder="Password again" />
	<form:input path="email" cssClass="email form-control" placeholder="Email" /><br />
	<input type="checkbox" name="tos" class="tos" /> I aggree to <a href="tos.html" onclick='window.open(this.href, null, "height=768,width=1024,status=no,toolbar=no,menubar=no,location=no"); return false'>Terms of Service</a><br /><br />
	<input type="submit" class="btn btn-lg btn-primary btn-block" />
</form:form>
	
	<script type="text/javascript">
		var availableUsername = false;
		$(document).ready(function() {
			
			$(".name").focusout(function(e) {
				var name = $(this).val();
				if(name == "") {
					return;
				}
				$.ajax({
					url: "${pageContext.request.contextPath}/register/check.html?name=" + name,
							cache: false
				}).done(function(data) {
					if(data == "free") {
						$(".nameError").html("<div class='alert alert-success'>This username is ok.</div>");
						availableUsername = true;
					} else {
						$(".nameError").html("<div class='alert alert-danger'>This username is already registered!</div>");
						availableUsername = false;
					}
				});
			});
			
			$(".registerForm").submit(function(e) {
				var p1 = $(".password1").val();
				var p2 = $(".password2").val();
				var name = $(".name").val();
				var email = $(".email").val();
				var tos = $(".tos").is(":checked");

				if (p1 == "" || p2 == "" || name == "" || email == "") {
					e.preventDefault();
					alert("please fill out all fields");
					return;
				}
				
				if(tos == false) {
					e.preventDefault();
					alert("you must aggree to site's terms of service!");
					return;
				}

				if (p1 != p2) {
					e.preventDefault();
					alert("passwords must match!");
					return;
				}
				
				if(!availableUsername) {
					e.preventDefault();
					alert("this username is already registered!");
					return;
				}
			})
		});
	</script>

<jsp:include page="layout/footer.jsp" />