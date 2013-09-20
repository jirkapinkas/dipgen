<%@ include file="layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="layout/header.jsp">
	<jsp:param value="Diploma Generator: My Profile" name="title" />
	<jsp:param value="user-details" name="page" />
</jsp:include>

<style>
	#tabs { 
	    padding: 0px; 
	    background: none; 
	    border-width: 0px; 
	} 
	#tabs .ui-tabs-nav { 
	    padding-left: 0px; 
	    background: transparent; 
	    border-width: 0px 0px 1px 0px; 
	    -moz-border-radius: 0px; 
	    -webkit-border-radius: 0px; 
	    border-radius: 0px; 
	} 
	#tabs .ui-tabs-anchor { 
	    background: #f9f9f9; 
	}
</style>

<div id="tabs">
	<ul>
		<li><a href="#tabs-1">profile</a></li>
		<li ${param.billing == true ? 'class="selected ui-tabs-active"' : ''}><a href="#tabs-2">billing information</a></li>
	</ul>
	<div id="tabs-1">
		<form:form commandName="user" cssClass="registerForm form-signin" action="user-details.html">
			<h2 class="form-signin-heading">My Profile:</h2>
			<c:if test="${param.success == true && param.billing != true}">
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
			<a href="user-details/delete-account.html" class="btn btn-lg btn-block btn-danger deleteAccount" onclick="return confirm('Are you really really really sure you want to delete your account? There\'s no going back.')">Delete Account</a>
		</form:form>
	</div>
	<div id="tabs-2">
		<form:form commandName="user" cssClass="billingForm form-signin" action="user-details.html?billing=true">
			<c:if test="${param.billing == true and param.success == true}">
				<div class="alert alert-success">
					Thank you, you just applied for Premium accout. 
					Our upgrade process is not yet automated, so 
					we will send you some options how to pay for 
					premium and shortly afterwards you will be 
					granted access to premium area.
				</div>
			</c:if>
			<div class="alert alert-info">
				To upgrade please fill in your billing information. None of them is requiered, fill in what you want on invoice.
			</div>
			<h2 class="form-signin-heading">Billing Information:</h2>
			<form:input path="firstName" placeholder="First name" cssClass="form-control" /><br />
			<form:input path="lastName" placeholder="Last name" cssClass="form-control" /><br />
			<form:input path="address1" placeholder="Address 1" cssClass="form-control" /><br />
			<form:input path="address2" placeholder="Address 2" cssClass="form-control" /><br />
			<form:input path="city" placeholder="City" cssClass="form-control" /><br />
			<form:input path="state" placeholder="State/Province" cssClass="form-control" /><br />
			<form:input path="postalCode" placeholder="Postal code" cssClass="form-control" /><br />
			<form:input path="country" placeholder="Country" cssClass="form-control" /><br />
			<form:textarea rows="3" path="additionalInformation" placeholder="Additional information (Company name, tax ID, etc ...)" cssClass="form-control" /><br />
		
			<c:if test="${isPremium == false}">
				<input type="submit" class="btn btn-lg btn-success btn-block" value="Upgrade to Premium ($24 / year)" onclick="return confirm('Please confirm that you want to upgrade to premium account')"/><br />
			</c:if>
		</form:form>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {

		$("#tabs").tabs();

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