<#compress>
<#escape x as x?html>
<form id="change-request-form" action="${formatter.url("/request/change/?ajax")}" method="post">
	<input type="hidden" name="relation_request_id" value="${request.relation_request_id}" />
	<input type="hidden" name="selected_next_status" value="${nextStatus!""}" />
	<input type="hidden" name="new_director_id" value="${newDirectorId!""}" />
	<input type="hidden" name="crawl_date" value="${crawlDate!""}" />
	<script type="text/javascript">
	   me.dapps.global['url.back_to_list'] = '${formatter.url("/request/list/")}';
	</script>
</form>
</#escape>
</#compress>