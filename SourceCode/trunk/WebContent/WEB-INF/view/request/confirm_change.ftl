<#compress>
<#escape x as x?html>
<form id="change-request-form" action="${formatter.url("/request/change/?ajax")}" method="post">
	<input type="hidden" name="relation_request_id" value="${request.relation_request_id}" />
	<span>現在のステータス　　 : ${request.status_description!""}</span></br>
	<span>現在のHRS担当者  : ${request.assign_user_name!""}</span></br></br>
	
	<span>新規のステータス　　 : ${newStatusDescription!""}</span></br>
	<span>新規のHRS担当者  : ${newAssignedPerson!""}</span>
</form>
</#escape>
</#compress>