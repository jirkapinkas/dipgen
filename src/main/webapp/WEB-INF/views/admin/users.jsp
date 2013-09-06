<%@ include file="../layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Users</title>

<%@ include file="../layout/resources.jsp"%>

</head>
<body>

	<h1>Users:</h1>

	<c:forEach items="${users}" var="user">
		<a href="users/delete.html?userId=${user.userId}">delete</a>
		${user.name}
		roles:
		<c:forEach items="${user.roles}" var="role">
			${role.name}
		</c:forEach>
		registration date: ${user.registrationDate}
		last login date: ${user.lastLoginDate}
		invalid logins count: ${user.invalidLoginCount}
		<br />
	</c:forEach>

</body>
</html>