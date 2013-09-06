<%@ include file="layout/static.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<title>Diplomas</title>

<%@ include file="layout/resources.jsp" %>

</head>
<body>

<a href="<c:url value='/logout' />">logout ${pageContext.request.userPrincipal.name}</a>

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
						list += " <a href='diplomas/delete.html?id=" + value.diplomaId + "'>delete</a>";
						list += " <a href='diplomas/edit.html?id=" + value.diplomaId + "' onclick='window.open(this.href, null, \"height=768,width=1024,status=no,toolbar=no,menubar=no,location=no\"); return false'>edit</a>";
						list += " <a href='diplomas/generate.html?id=" + value.diplomaId + "' onclick='window.open(this.href, null, \"height=768,width=1024,status=no,toolbar=no,menubar=no,location=no\"); return false'>generate</a>";
						list += " " + value.name;
						var d = new Date();
						list += " <img border='1' src='?image=" + value.diplomaId + "&date=" + d.getTime() + "' />";
						list += " <br>";
					})
					$(".diplomaList").html(list);
				});
			}
			
			window.onfocus = load;
			
			load();
			
		});
	</script>

	<a href="diplomas/new.html" class="new">create new</a>

	<br /><br />
	
	<div class="diplomaList"></div>

</body>
</html>
