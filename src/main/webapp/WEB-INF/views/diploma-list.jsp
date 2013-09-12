<%@ include file="layout/static.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="layout/header.jsp">
	<jsp:param value="Diploma Generator: Diplomas" name="title" />
	<jsp:param value="diploma-list" name="page" />
</jsp:include>

	<h1>Diplomas list:</h1>

	<script type="text/javascript">
		$(document).ready(function() {
			$(".new").click(function(e) {
				e.preventDefault();
				window.open($(this).attr("href"),null,
				"height=768,width=1024,status=no,toolbar=no,menubar=no,location=no");
			});
			
			var load = function load() {
				$.ajax({
					url: "diplomas.json?json",
							cache: false
				}).done(function(data) {
					var list = "";
					$.each(data, function(index, value) {
						list += " <tr>";
						list += " <td>";
						list += " <a href='diplomas/delete.html?id=" + value.diplomaId + "' class='btn btn-md btn-danger' onclick='return confirm(\"Are you sure you want to delete this diploma?\")'>delete</a>";
						list += " <a href='diplomas/edit.html?id=" + value.diplomaId + "' class='btn btn-md btn-warning' onclick='window.open(this.href, null, \"height=768,width=1024,status=no,toolbar=no,menubar=no,location=no\"); return false'>edit</a>";
						list += " <a href='diplomas/generate.html?id=" + value.diplomaId + "' class='btn btn-md btn-primary' onclick='window.open(this.href, null, \"height=768,width=1024,status=no,toolbar=no,menubar=no,location=no\"); return false'>generate</a>";
						list += " </td>";
						list += " <td>" + value.name + "</td>";
						var d = new Date();
						list += " <td><img border='1' src='?image=" + value.diplomaId + "&date=" + d.getTime() + "' /></td>";
						list += " </tr>";
					})
					$(".diplomaList").html(list);
				});
			}
			
			window.onfocus = load;
			
			load();
			
		});
	</script>

	<a href="diplomas/new.html" class="new btn btn-lg btn-success">create new</a>

	<br /><br />
	
	<table class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>operations</th>
			<th>name</th>
			<th>thumbnail</th>
		</tr>
	</thead>
	<tbody class="diplomaList">
	</tbody>
	</table>

<jsp:include page="layout/footer.jsp" />