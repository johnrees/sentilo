<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<spring:url value="/admin/sensor/" var="detailPrefix" />

<%@include file="/WEB-INF/jsp/common/include_script_tables.jsp"%>

<script type="text/javascript">
$(document).ready(function() {	
	makeTableAsync('${sensorsAjaxSource}', '#sensorTable', '${detailPrefix}');
});
</script>

<form:form method="post" onkeypress="return preventEnterSubmit(event);" modelAttribute="sensors"
	action="${deleteSensorsURL}">

	<c:if test="${empty providerId && empty componentId}">
		<input type="hidden" name="origin" value="sensor" />
	</c:if>

	<c:if test="${not empty providerId && empty componentId}">
		<input type="hidden" name="origin" value="provider" />
		<input type="hidden" name="providerId" value="${providerId}" />
	</c:if>

	<c:if test="${not empty componentId && empty providerId}">
		<input type="hidden" name="origin" value="component" />
		<input type="hidden" name="componentId" value="${componentId}" />
	</c:if>

	<table class="table table-striped" id="sensorTable">
		<thead>
			<tr>
				<td>&nbsp;</td>
				<td><strong><spring:message code="sensor.sensorId" /> </strong></td>
				<td><strong><spring:message code="sensor.providerId" /> </strong></td>
				<td><strong><spring:message code="sensor.type" /> </strong></td>
				<td><strong><spring:message code="sensor.createdAt" /> </strong></td>
			</tr>
		</thead>
		<tbody />
	</table>
</form:form>
