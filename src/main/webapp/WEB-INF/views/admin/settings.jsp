<%@ include file="../layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp">
	<jsp:param value="Diploma Generator: Settings" name="title" />
	<jsp:param value="admin-settings" name="page" />
</jsp:include>

<h1>Email settings:</h1>

<form:form commandName="dipgenSettings" cssClass="registerForm form-signin" action="/admin/settings.html">
	<c:if test="${param.success == true}">
		<div class="alert alert-success">
			Settings has been updated successfully.
		</div>
	</c:if>
	<form:input path="emailHost" cssClass="name form-control" placeholder="Email Host" /><br />
	<form:input path="emailPort" cssClass="name form-control" placeholder="Email Port" /><br />
	<form:input path="emailSmtpUsername" cssClass="name form-control" placeholder="Email SMTP Username" /><br />
	<form:input path="emailSmtpPassword" cssClass="name form-control" placeholder="Email SMTP Password" /><br />
	<form:input path="emailCentralAddress" cssClass="name form-control" placeholder="Email Central Address" /><br />
	<input type="submit" class="btn btn-lg btn-primary btn-block" value="Update settings" />
</form:form>

<jsp:include page="../layout/footer.jsp" />