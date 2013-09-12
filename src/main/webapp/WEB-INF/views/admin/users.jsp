<%@ include file="../layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp">
	<jsp:param value="Diploma Generator: Users" name="title" />
	<jsp:param value="admin-users" name="page" />
</jsp:include>

<h1>Users:</h1>

<table class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>delete</th>
			<th>name</th>
			<th>roles</th>
			<th>registration date</th>
			<th>last login date</th>
			<th>invalid login count</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${users}" var="user">
			<tr>
				<td><a href="users/delete.html?userId=${user.userId}"
					class="aDelete btn btn-md btn-danger">delete</a></td>
				<td>${user.name}</td>
				<td><c:forEach items="${user.roles}" var="role">${role.name}<br />
					</c:forEach></td>
				<td>${user.registrationDate}</td>
				<td>${user.lastLoginDate}</td>
				<td>${user.invalidLoginCount}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<script type="text/javascript">
	$(document).ready(function() {
		$(".aDelete").click(function(e) {
			var conf = confirm("Are you sure you want to delete this user?");
			if (conf == false) {
				e.preventDefault();
			}
		});
	});
</script>

<jsp:include page="../layout/footer.jsp" />