<%@ include file="layout/static.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<title>Generate</title>

<%@ include file="layout/resources.jsp" %>

</head>
<body>

	<div class="container">
	
	<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags" %>
	
	<form action="" method="post">
	<input type="hidden" name="id" value="${param.id}" />
	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">dynamic texts</a></li>
			<li><a href="#tabs-2">static texts</a></li>
		</ul>
		<div id="tabs-1">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>operations</th>
						<th>name</th>
						<th>type</th>
						<th>value</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${generatorStrings}" var="gs">
						<c:if test="${gs.enabled eq true}">
							<mytags:generator-string-div gs="${gs}" />
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div id="tabs-2">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>operations</th>
						<th>name</th>
						<th>type</th>
						<th>value</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${generatorStrings}" var="gs">
						<c:if test="${gs.enabled eq false}">
							<mytags:generator-string-div gs="${gs}" />
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
		</div>
		
		<br />
		<div id="radio">
			<input type="radio" id="radio1" name="singlePdf" value="true" checked="checked" /><label for="radio1">single PDF file</label>
			<input type="radio" id="radio2" name="singlePdf" value="false" /><label for="radio2">ZIP with PDF files</label>
		</div>
		
		<br />
		<div>
			<input type="submit" class="buttonGenerate btn btn-lg btn-success" />
		</div>
	</form>
	
	<c:choose>
		<c:when test="${isPremium}">
			<script type="text/javascript">
				var isPremium = true;
			</script>
		</c:when>
		<c:otherwise>
			<script type="text/javascript">
				var isPremium = false;
			</script>
		</c:otherwise>
	</c:choose>

	<script type="text/javascript">
		$(document).ready(function() {
			
			$(".buttonGenerate").click(function(e) {
				var types = $("#tabs-1 .selectedHtmlComponentType");
				var textareaCount = 0;
				$.each(types, function(index, value) {
					if($(value).val() == 'TEXTAREA') {
						textareaCount++;
					}
				});
				if(textareaCount > 1) {
					e.preventDefault();
					alert("only one dynamic text can be multiple text!");
				} else if(textareaCount == 1) {
					var textarea = $("textarea");
					var diplomas = textarea.val().split('\n');
					var diplomasCount = diplomas.length;
					if(diplomasCount > 5 && isPremium == false) {
						alert("I'm sorry, but free users can generate max. 5 diplomas at one time. Premium users have no limit.");
						e.preventDefault();
					}
				}
			});
			
			$(".selectedHtmlComponentType").change(function(data) {
				var curr = $(this);
				var id = curr.attr("id");
				var type = curr.val();
				$.ajax({
					url: "${pageContext.request.contextPath}/generator/toggle-type.html?id=" + id + "&type=" + type,
							cache: false
				}).done(function(data) {
					var htmlInputComponent = $(".html-input-" + id);
					var td = $(".td-input-" + id);
					htmlInputComponent.remove();
					if(type == 'TEXTFIELD') {
						td.append('<input type="text" name="html-input-' + id + '" id="' + id + '" class="html-input-' + id + '" />');
					}
					if(type == 'TEXTAREA') {
						td.append('<textarea rows="3" cols="20" name="html-input-' + id + '" id="' + id + '" class="html-input-' + id + '"></textarea>');
					}

				});
			});
		    $("#tabs").tabs();

		});
	</script>

	</div>

</body>
</html>
