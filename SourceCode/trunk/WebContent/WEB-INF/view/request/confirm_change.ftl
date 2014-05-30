<#compress>
<#escape x as x?html>
<form id="change-request-form" action="${formatter.url("/request/change/?ajax")}" method="post">
	<input type="hidden" name="relation_request_id" value="${request.relation_request_id}" />
	<label>現在のステータス　　 : ${request.status_description!""}</label></br>
	<label>現在のHRS担当者  : ${request.assign_user_name!""}</label></br></br>
	
	<label name="new_status">新規のステータス　　 : ${newStatusDescription!""}</label></br>
	<label name="assign_user_name">新規のHRS担当者  : ${newAssignedPerson!""}</label>
</form>
</#escape>
</#compress>