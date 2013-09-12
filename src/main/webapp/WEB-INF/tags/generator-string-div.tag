<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="gs" description="generator string object" required="true" type="com.dipgen.entity.GeneratorString" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<tr class="div-${gs.generatorId}">
<td>
	<a href="${pageContext.request.contextPath}/generator/toggle-enabled.html?id=${gs.generatorId}" id="${gs.generatorId}" class="aRemove btn btn-sm btn-danger" style="color:white" >remove</a>
</td>
<td>
	${gs.string}
</td>
<td>
	<select class="selectedHtmlComponentType" id="${gs.generatorId}">
		<option value="UNDEFINED"
			${gs.htmlComponentType == 'UNDEFINED' ? 'selected' : ''}>undefined</option>
		<option value="TEXTFIELD"
			${gs.htmlComponentType == 'TEXTFIELD' ? 'selected' : ''}>single
			text</option>
		<option value="TEXTAREA"
			${gs.htmlComponentType == 'TEXTAREA' ? 'selected' : ''}>multiple
			text</option>
	</select>
</td>
<td class="td-input-${gs.generatorId}">
	<c:if test="${gs.htmlComponentType == 'TEXTFIELD'}">
		<input type="text" name="html-input-${gs.generatorId}" id="${gs.generatorId}" class="html-input-${gs.generatorId}" />
	</c:if>
	<c:if test="${gs.htmlComponentType == 'TEXTAREA'}">
		<textarea rows="3" cols="20" name="html-input-${gs.generatorId}" id="${gs.generatorId}" class="html-input-${gs.generatorId}"></textarea>
	</c:if>
</td>
</tr>