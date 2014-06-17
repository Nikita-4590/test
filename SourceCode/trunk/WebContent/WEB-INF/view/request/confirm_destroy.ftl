<#compress>
<#escape x as x?html>
<form id="destroy-request-form" action="${formatter.url("/request/destroy/?ajax")}" method="post">
	<input type="hidden" name="relation_request_id" value="${request.relation_request_id}" />
	<div>
		<label><b>＜依頼情報＞</b></label>
	</div>
	<div>
		<label>・${request.company_name!""}</label>
	</div>
	<div>
		<label>・${request.media_name!""}</label>
	</div>
	<div>
		<label>・${request.login_id_1!""}</label>
	</div>
	<div class="form-line">
		<div class="form-col-right-box">
			<textarea placeholder="ここに取消す理由を入力してください。"maxlength="255"　name="destroy-comment"
				dapps-validate-rule="{ 'required': true }" dapps-validate-message-id="WRN100"></textarea>
			<div class="form-input-note">
				<span>255文字以下</span>
			</div>
		</div>
		<div class="form-col-left-box">
			<label>コメント </label>
			<span class="required-note">（*）</span>
		</div>
	</div>
</form>
</#escape>
</#compress>