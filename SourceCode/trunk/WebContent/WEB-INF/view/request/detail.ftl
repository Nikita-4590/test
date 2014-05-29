<#compress>
<#escape x as x?html>
<div>
	<div class="form-line">
		<div class="form-col-right">
			<label>Request ID: ${request.relation_request_id}</label>
		</div>
	</div>
	
	<div class="form-line">
		<div class="form-col-right">
			<label>Current status: ${request.status_description}</label>
		</div>
	</div>
	
	<input type="text" id="assign-user-name" name="assign_user_name" maxlength="50" 
		<#if request?? && request.assign_user_name??>value= "${request.assign_user_name}"</#if> />
	</input>
	
	<select name="new_status" id="new-status">
		<#list listStatus as status>
			<#if status?? && status.status_type == request.status>
				<option value="${status.status_type}" selected="selected">${status.description}</option>
			<#else>
				<option value="${status.status_type}">${status.description}</option>
			</#if>	
		</#list>
	</select>
	<a href="#" class="button-link" onclick="confirmChange(${request.relation_request_id}); return false;">変更する</a>
</div>

<script type="text/javascript">
	me.dapps.global['url.context'] = '${formatter.url("")}';
	me.dapps.global['url.confirm_change'] = '${formatter.url("/request/confirm_change/?ajax")}';
</script>				
</#escape>
</#compress>
