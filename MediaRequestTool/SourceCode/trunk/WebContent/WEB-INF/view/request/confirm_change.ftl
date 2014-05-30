<#compress>
<#escape x as x?html>
<form id="change-request-form" action="${formatter.url("/request/change/?ajax")}" method="post">
	<input type="hidden" name="relation_request_id" value="${request.relation_request_id}" />
	<input type="hidden" name="new_status" value="${changedStatus.status_type!""}" />
	<input type="hidden" name="assign_user_name" value="${newAssignedPerson!""}" />
	
	<label>現在のステータス　　 : ${request.status_description!""}</label></br>
	<label>現在のHRS担当者  : ${request.assign_user_name!""}</label></br></br>
	
	<label>新規のステータス　　 : ${changedStatus.description!""}</label></br>
	<label>新規のHRS担当者  : ${newAssignedPerson!""}</label>
</form>
</#escape>
</#compress>