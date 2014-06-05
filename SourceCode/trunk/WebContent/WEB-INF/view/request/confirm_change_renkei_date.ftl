<#compress>
<#escape x as x?html>
<form id="change-renkei-form" action="${formatter.url("/request/change_renkei/?ajax")}" method="post">
	<input type="hidden" name="relation_request_id" value="${request.relation_request_id}" />
	<input type="hidden" name="crawl_date" value="${crawlDate}" />
</form>
</#escape>
</#compress>