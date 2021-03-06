<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/header.jsp"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<c:if test="${mode == 'edit' }">
	<spring:url value="/admin/componenttypes/${componentType.id}/edit" var="actionURL" />
	<spring:message code="componenttype.edit.title" var="pageTitle" />
</c:if>
<c:if test="${mode == 'create' }">
	<spring:url value="/admin/componenttypes/create" var="actionURL" />
	<spring:message code="componenttype.new.title" var="pageTitle" />
</c:if>

<div class="container-fluid">
	<div class="content">
		<div class="row-fluid">
			<div class="span3">
				<%@include file="/WEB-INF/jsp/common/include_sidebar.jsp"%>
			</div>
			<div class="span9">

				<div class="row-fluid">
					<div class="span12">

						<%@include file="/WEB-INF/jsp/common/include_background_logo.jsp"%>
						<%@include file="/WEB-INF/jsp/common/messages.jsp"%>

						<h1 class="lead">
							${pageTitle}<br />
						</h1>

						<form:form method="post" modelAttribute="componentType" action="${actionURL}" class="form-horizontal">
							<fieldset>
								<div class="control-group">
									<form:label path="id" class="control-label">
										<spring:message code="componenttype.id" />
									</form:label>
									<div class="controls">
										<c:if test="${mode == 'create' }">
											<form:input path="id" />
										</c:if>
										<c:if test="${mode == 'edit' }">
											<form:input path="id" readonly="true" />
											<form:hidden path="createdAt" />
										</c:if>
										<form:errors path="id" cssClass="error" htmlEscape="false" />
									</div>
								</div>
								<div class="control-group">
									<form:label path="name" class="control-label">
										<spring:message code="componenttype.name" />
									</form:label>
									<div class="controls">
										<form:input path="name" />
										<form:errors path="name" cssClass="error" htmlEscape="false" />
									</div>
								</div>
								<div class="control-group">
									<form:label path="description" class="control-label">
										<spring:message code="componenttype.description" />
									</form:label>
									<div class="controls">
										<form:textarea path="description" />
										<form:errors path="description" cssClass="error" htmlEscape="false" />
									</div>
								</div>
								<%@include file="/WEB-INF/jsp/componentType/include_component_type_icon.jsp"%>
								<div class="control-group">
									<div class="controls">
										<spring:url value="/admin/componenttypes/list" var="backURL" />
										<a href="${backURL}" class="btn"> <spring:message code="button.back" /> </a> <a href="#"
											onclick="$('form#componentType').submit();" class="btn btn-success"> <spring:message code="button.save" />
										</a>
									</div>
								</div>
							</fieldset>

						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<%@include file="/WEB-INF/jsp/common/footer.jsp"%>