<%@ include file="layout/static.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="layout/header.jsp">
	<jsp:param value="Diploma Generator: Premium" name="title" />
	<jsp:param value="premium" name="page" />
</jsp:include>

<div>

	<h1>Say no to limitations</h1>

	<p>Because maintaining this server and developing this web app
		costs money, there are some limitations in free account. Become
		premium, unleash full power and support further development:</p>

	<table class="table table-striped table-bordered table-hover">
		<thead>
			<tr>
				<th>free account</th>
				<th>premium account</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>You can generate max. 5 diplomas at one time</td>
				<td>No limitation</td>
			</tr>
			<tr>
				<td>Just diplomas ready for print</td>
				<td>Diplomas ready for print &amp; dynamic diplomas<br />
					which can be downloaded by user (or sent via email)
				</td>
			</tr>
			<tr>
				<td>You must host your own images</td>
				<td>Images can be hosted on our server</td>
			</tr>
			<tr>
				<td>No sending emails</td>
				<td>Sending emails with diploma as attachment</td>
			</tr>
			<tr>
				<td>No programmatic API</td>
				<td>Programmatic API</td>
			</tr>
			<tr>
				<td><strong>Cost: Free</strong></td>
				<td><strong>Cost: $24/year</strong></td>
			</tr>
			<tr>
				<td></td>
				<td><br />
				<a class="btn btn-lg btn-success"
					href="user-details.html?upgrade=true">Upgrade to Premium</a> <br />
				<br />
					<p>
						Do you miss some key feature in Premium? <a href="contact.html">Let
							us know so we can fix it</a>
					</p></td>
			</tr>
		</tbody>
	</table>

</div>

<jsp:include page="layout/footer.jsp" />