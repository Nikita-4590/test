<#compress>
<#escape x as x?html>
<form id="update-director-form" action="${formatter.url("/request/update_director/?ajax")}" method="post">
	<input type="hidden" name="relation_request_id" value="${request.relation_request_id}" />
	<input type="hidden" name="current_director_id" value="${request.assign_user_id!""}" />
	<input type="hidden" name="new_director_id" value="${newDirectorId!""}" />
</form>
<script type="text/javascript">
    me.dapps.global['url.back_to_list'] = '${formatter.url("/request/list/")}';
</script>
</#escape>
</#compress>