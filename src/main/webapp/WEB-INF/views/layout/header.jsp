<%@ include file="static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>dipgen = Diploma Generator!</title>
<c:url value="/favicon.ico" var="faviconUrl" />
<link rel="shortcut icon" href="${faviconUrl}">

<%@ include file="resources.jsp"%>

<style>
body {
padding: 20px;
}
.navbar {
margin-bottom: 40px;
}
</style>

</head>
<body>

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-44217458-1', 'dipgen.com');
  ga('send', 'pageview');

</script>

	<div class="container">
	
	
	  <!-- Static navbar -->
      <div class="navbar navbar-default">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>

			<c:url var="homeUrl" value="/" />
          <a class="navbar-brand" href="${homeUrl}">
				<c:url value="/resources/images/logo.png" var="logoUrl" />
				<img src="${logoUrl}" alt="logo" title="logo" border="0" style="position: absolute; left: 4px; top: 10px;" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Diploma Generator
				
          </a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
				<c:url var="diplomasUrl" value="/diplomas.html" />
				<li class="${param.page == 'diploma-list' ? 'active' : ''}">
					<a href="${diplomasUrl}">Diplomas</a>
				</li>

				<c:url var="premiumUrl" value="/premium.html" />
				<li class="${param.page == 'premium' ? 'active' : ''}">
					<a href="${premiumUrl}">Premium</a>
				</li>

				<c:url var="contactUrl" value="/contact.html" />
				<li class="${param.page == 'contact' ? 'active' : ''}">
					<a href="${contactUrl}">Contact us</a>
				</li>

				<security:authorize access="hasRole('ROLE_ADMIN')">
					<c:url var="adminUsersUrl" value="/admin/users.html" />
					<li class="${param.page == 'admin-users' ? 'active' : ''}">
						<a href="${adminUsersUrl}">Users</a>
					</li>
					<c:url var="adminSettingsUrl" value="/admin/settings.html" />
					<li class="${param.page == 'admin-settings' ? 'active' : ''}">
						<a href="${adminSettingsUrl}">Settings</a>
					</li>
				</security:authorize>
          </ul>
          <ul class="nav navbar-nav navbar-right">
				<security:authorize access="isAuthenticated()" var="loggedIn" />
				<c:choose>
					<c:when test="${loggedIn}">
						<c:url var="userDetailsUrl" value="/user-details.html" />
						<li class="${param.page == 'user-details' ? 'active' : ''}">
							<a href="${userDetailsUrl}">My Profile</a>
						</li>
						<c:url var="logoutUrl" value="/logout" />
						<li>
							<a href="${logoutUrl}">Logout ${pageContext.request.userPrincipal.name}</a>
						</li>
					</c:when>
					<c:otherwise>
						<c:url var="loginUrl" value="/login.jsp" />
						<li class="${param.page == 'login' ? 'active' : ''}">
							<a href="${loginUrl}">Sign in</a>
						</li>
						<c:url var="registerUrl" value="/register.html" />
						<li class="${param.page == 'register' ? 'active' : ''}">
							<a href="${registerUrl}">Register</a>
						</li>
					</c:otherwise>
				</c:choose>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
