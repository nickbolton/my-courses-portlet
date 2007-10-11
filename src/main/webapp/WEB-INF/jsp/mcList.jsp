<%@ include file="include.jsp" %>

<style media="screen" type="text/css">
	@import '..${pageContext.request.contextPath}/media/mycourses.css';			            
</style>
<script src="..${pageContext.request.contextPath}/media/jquery.js" language="JavaScript" type="text/javascript">// Included JS file</script>  
<script src="..${pageContext.request.contextPath}/media/mycourses.js" language="JavaScript" type="text/javascript">// Included JS file</script> 
<script language="JavaScript" type="text/javascript">
	var mcCourse = "${model.siteCourse}";
	var mcProject = "${model.siteProject}";	
	var mc${model.portletUID} = new mycourses();

	$(document).ready( function() {mc${model.portletUID}.init("#mc${model.portletUID} ") });
</script> 

<div id="mc${model.portletUID}" class="my-courses">

    <!--
    <div>
      <a href="<portlet:renderURL portletMode="view"><portlet:param name="selectedTerm" value="${model.selectedTerm}"/></portlet:renderURL>">
        <spring:message code="mycourses.refresh_link" />
      </a>
    </div>
    -->
    
    <div>
      <a href="${model.refreshURL}"/><spring:message code="mycourses.refresh_link" /></a>
    </div>

	<c:choose>
		<c:when test="${!empty model.errorMessage}">
			<div class="error-message">
				<spring:message code="mycourses.exception.${model.errorMessage}" />
			</div>
			<div class="my-workspace">
				<a href="${model.baseURL}${model.worksiteURL}" target="_blank"><spring:message code="mycourses.sakai_workspace_link" /></a>
			</div>			
		</c:when>
		<c:otherwise>
			<div class="tab-list hidden">
				<ul>
					<li class="view-course"><a href="#course"><spring:message code="mycourses.course_title" /></a></li>
					<li class="view-project"><a href="#project"><spring:message code="mycourses.project_title" /></a></li>
				</ul>
			</div>							
			<div>
				<div class="date-filter">
					<form action="<portlet:renderURL />" name="mcForm${model.portletUID}" id="mcForm${model.portletUID}">
						<span><spring:message code="mycourses.semester_text" /></span>
						<select name="selectedTerm" id="dateSelect" class="date-select" onchange="this.form.submit();">
							<option value="default_term"><spring:message code="mycourses.term_default" /></option>
							<c:forEach var="trm" items="${model.terms}">
								<option value="${trm.id}" <c:if test="${trm.id eq model.selectedTerm}">selected="selected"</c:if>>${trm.name}</option>
							</c:forEach>
						</select>
						<noscript>
							<input type="submit" name="submit" value="Update" />
						</noscript>
					</form>
				</div>
				<table class="sites-table" cellspacing="0" cellpadding="0">
					<caption><spring:message code="mycourses.caption" /></caption>
					<tbody>
						<c:forEach var="site" items="${model.sites}" varStatus="status">
							<c:if test="${model.siteCourse == site.type or model.siteProject == site.type}">
								<tr class="${site.type} <c:if test="${status.count%2 == 0}">shade</c:if>">
									<td class="details"><a href="${model.baseURL}${site.url}" target="_blank">${site.name}</a></td>
								</tr>
							</c:if>									
						</c:forEach>		
					</tbody>			
				</table>	
				<div class="emptyList">
					<h4><spring:message code="mycourses.empty_list" /></h4>
					<a href="${model.baseURL}${model.worksiteURL}" target="_blank"><spring:message code="mycourses.sakai_workspace_link" /></a>
				</div>		
			</div>
		</c:otherwise>
	</c:choose>
	<div class="additional-links">
		<div class="join-sites">
			<a href="${model.baseURL}${model.membershipURL}/login" target="_blank"><spring:message code="mycourses.join_sites" /></a>
		</div>
	</div>
</div>