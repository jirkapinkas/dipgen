<%@ include file="layout/static.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<title>Generate</title>

<%@ include file="layout/resources.jsp" %>

</head>
<body>
	<h1>Texts:</h1>
	
	<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags" %>
	
	<form action="" method="post">
	<input type="hidden" name="id" value="${param.id}" />
	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">dynamic texts</a></li>
			<li><a href="#tabs-2">static texts</a></li>
		</ul>
		<div id="tabs-1">
				<c:forEach items="${generatorStrings}" var="gs">
					<c:if test="${gs.enabled eq true}">
						<mytags:generator-string-div gs="${gs}" />
					</c:if>
				</c:forEach>
				<br />
		</div>
		<div id="tabs-2">
			<c:forEach items="${generatorStrings}" var="gs">
				<c:if test="${gs.enabled eq false}">
					<mytags:generator-string-div gs="${gs}" />
				</c:if>
			</c:forEach>
		</div>
		</div>
		
		<br />
		<div id="radio">
			<input type="radio" id="radio1" name="singlePdf" value="true" checked="checked" /><label for="radio1">single PDF file</label>
			<input type="radio" id="radio2" name="singlePdf" value="false" /><label for="radio2">ZIP with PDF files</label>
		</div>
		
		<br />
		<div>
			<input type="submit" class="buttonGenerate" />
		</div>
	</form>

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
					var div = $(".div-" + id);
					htmlInputComponent.remove();
					if(type == 'TEXTFIELD') {
						div.append('<input type="text" name="html-input-' + id + '" id="' + id + '" class="html-input-' + id + '" />');
					}
					if(type == 'TEXTAREA') {
						div.append('<textarea rows="3" cols="20" name="html-input-' + id + '" id="' + id + '" class="html-input-' + id + '"></textarea>');
					}

				});
			});
		    $("#tabs").tabs();
		    $("#radio").buttonset();
		    $(".aRemove").button();
		    
		    $(".aRemove").click(function(e) {
		    	e.preventDefault();
		    	var curr = $(this);
		    	var url = curr.attr("href");
				$.ajax({
					url: url, cache: false
				}).done(function(data) {
			    	var parent = curr.parent();
					var uberParent = parent.parent();
			    	if(uberParent.attr("id") == "tabs-1") {
						$("#tabs-2").append(parent);
			    	} else {
						$("#tabs-1").append(parent);
			    	}
				});
			});

		});
	</script>

</body>
</html>
