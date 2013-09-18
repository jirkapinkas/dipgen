<%@ include file="WEB-INF/views/layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="WEB-INF/views/layout/header.jsp">
	<jsp:param value="Diploma Generator" name="title" />
	<jsp:param value="login" name="page" />
</jsp:include>

<form name="f" action="<c:url value='j_spring_security_check'/>"
	method="POST" class="form-signin">
<c:if test="${not empty param.login_error}">
	<div class="alert alert-danger">
		Your login attempt was not successful, try again.
	</div>
</c:if>
<h2 class="form-signin-heading">Please sign in</h2>
<c:url var="registerUrl" value="/register.html" />
<div class="alert alert-warning">Not registered? Register <a href="${registerUrl}">here</a></div>
<input type="text" name="j_username" class="form-control" placeholder="Username" value="${username}" autofocus>
<input type="password" name="j_password" class="form-control" placeholder="Password">
<label class="checkbox">
  <input type="checkbox" name="_spring_security_remember_me"> Remember me
</label>
<button class="btn btn-lg btn-primary btn-block" type="submit" name="submit">Sign in</button>
</form>

<jsp:include page="WEB-INF/views/layout/footer.jsp" />