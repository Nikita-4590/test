<#compress>
<#escape x as x?html>
<form id="change-request-form" action="${formatter.url("/request/change/?ajax")}" method="post">
	<input type="hidden" name="relation_request_id" value="${request.relation_request_id}" />
	<input type="hidden" name="current_status" value="${request.status}" />
	<input type="hidden" name="selected_next_status" value="${nextStatus!""}" />
	<input type="hidden" name="new_director_id" value="${newDirectorId!""}" />
	<input type="hidden" name="crawl_date" value="${crawlDate!""}" />
	<input type="hidden" name="current_director_id" value="${request.assign_user_id!""}" />
	
	<#if request.status = "NG" && nextStatus = "CONFIRMING">
		<div>
			<label><b>＜依頼情報＞</b></label>
		</div>
		<div>
			<label>・${request.company_name!""}</label>
		</div>
		<div>
			<label>・${request.media_name!""}</label>
		</div>
		<#if request.login_id_1??>
			<div>
				<label>・${request.login_id_1}</label>
			</div>
		</#if>	
		<div class="form-line">
			<div class="form-col-right-box">
				<textarea placeholder="ここに戻す理由を入力してください。"maxlength="255"　name="backToConfirming-comment"
					dapps-validate-rule="{ 'required': true }" dapps-validate-message-id="WRN151"></textarea>
				<div class="form-input-note">
					<span>255文字以下</span>
				</div>
			</div>
			<div class="form-col-left-box">
				<label>コメント </label>
				<span class="required-note">（*）</span>
			</div>
		</div>
	</#if>
	<script type="text/javascript">
	   me.dapps.global['url.back_to_list'] = '${formatter.url("/request/list/")}';
	</script>
</form>
</#escape>
</#compress>