<#compress>
<#escape x as x?html>
<div class="page" id="request-detail-page">
	<div id="request-form-top" class="form">
		<div class="form-line">
			<div class="right-side">
				<label id="request-id">${request.relation_request_id}</label>
			</div>
			<div class="left-side">
				<label>依頼ID</label>
			</div>
		</div>
		
		<div class="form-line">
			<div class="right-side">
				<label id="label-current-status">${request.status_description}</label>
			</div>
			<div class="left-side">
				<label>現在のステータス</label>
			</div>
		</div>
		
		<#if view = "OK" || view = "PROCESSING">
			<div class="form-line">
				<div class="right-side">
					<#if view = "OK">
						<select name="new_director" id="select-new-director">
							<#list directors as director>
								<option value="${director.id}">${director.user_name}</option>
							</#list>
						</select>
						<a href="#" class="button-link" id="change-status" onclick="confirmChange(${request.relation_request_id}); return false;">担当ディレクターを依頼</a>
					<#else>
						<label name="current_director" id="current-director"><b>${request.assign_user_name!""}</b></label>
						<a href="#"　id="enable-change-director" >担当ディレクターを変更</a>
						<a href="#"　id="cancel-change-director" style="display: none;">担当ディレクターを変更しない</a>
					</#if>
				</div>
				<div class="left-side">
					<label>担当ディレクター</label>
				</div>
			</div>
			
			<#if view = "PROCESSING">
				<div class="form-line" id="show-more" style="display: none;">
					<div class="right-side">
						<select name="update_director" id="select-update-director">
							<#list directors as director>
								<option value="${director.id}">${director.user_name}</option>
							</#list>
						</select>
						<a href="#" class="button-link" id="update-director" onclick="confirmUpdateDirector(${request.relation_request_id}); return false;">変更する</a>
					</div>
					<div class="left-side">
						<label>新規担当ディレクター</label>
					</div>
				</div>
			</#if>
		</#if>
		
		<div class="form-line">
			<div class="right-side">
				<#if view = "PROCESSING">
					<input type="text" id="crawl-date" name="crawl_date" dapps-ui-datepicker="{'input':{'format':'yy-mm-dd'},'output':{'format':'yy年mm月dd日'}}" />
				<#elseif view = "FINISHED"> <label><b>${request.crawl_date_to_display!""}</b></label>
				<#else> <label><b>未確定</b></label>		
				</#if>
				<#if view = "PROCESSING">
					<a href="#" class="button-link" id="change-status" onclick="confirmChange(${request.relation_request_id}); return false;">連携開始日を登録</a>
				</#if>
			</div>
			<div class="left-side">
				<label for="crawl_date">連携開始日</label>
			</div>
		</div>	
		
		<#if view != "FINISHED" && view != "DEFAULT">
			<div class="form-line">
				<div class="right-side">
					<#if view = "CONFIRMING" || view= "NG">
						<select name="select_next_status" id="select-next-status">
							<#list listNextStatus as status>
								<#if view = "NG"><option value="${status.status_type}">${status.description}</option>
								<#else><option value="${status.status_type}"<#if status.status_type = "OK">selected="selected">ログイン成功<#else>>ログイン失敗</#if></option>
								</#if>
							</#list>
						</select>
					<#elseif view = "NEW" || view = "OK" || view = "PROCESSING">
						<label id="label-next-status">${nextStatus.description}</label>
					</#if>
					<#if view = "NEW" || view = "CONFIRMING" || view = "NG">
						<a href="#" class="button-link" id="change-status" onclick="confirmChange(${request.relation_request_id}); return false;"><#if view = "NEW">依頼を受け付ける<#else>変更する</#if></a>
					</#if>	
				</div>
				<div class="left-side">
					<label><#if view = "CONFIRMING">接続確認の結果<#else>次のステータス</#if></label>
				</div>
			</div>
		</#if>
		
		<div class="form-line-bottom">
			<a href="#"　id="button-destroy" onclick="confirmDestroy(${request.relation_request_id}); return false;" >ｘこの依頼を取消す</a>
		</div>
		
		<input type="hidden" id="view" value="${view}" />
	</div>
	
	<#if view = "CONFIRMING" || view = "OK" || view = "PROCESSING">
		<div id="message">
			<#if view = "CONFIRMING">※ログイン確認を実施してください。
			<#elseif view = "OK">※担当ディレクターを設定してください。
			<#else>※連携開始日を設定してください。
			</#if>
		</div>
	</#if>	
	<h3>申込者情報</h3>
	<#-- ------------------------------------------------->
	<div id="request-form-center" class="form">
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.company_id!""}</label>
			</div>
			<div class="form-col-left">
				<label for="company_id">企業ID</label>
			</div>	
		</div>
		
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.company_name!""}</label>
			</div>
			<div class="form-col-left">
				<label for="company_name">企業名</label>
			</div>	
		</div>
		
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.requester_name!""}</label>
			</div>
			<div class="form-col-left">
				<label for="request_name">ご担当者名</label>
			</div>	
		</div>
		
		<div class="form-line">
			<div class="form-col-right">
				<a href="mailto:${request.requester_mail!""}">${request.requester_mail!""}</a>
			</div>
			<div class="form-col-left">
				<label for="request_mail">ご連絡先メールアドレス</label>
			</div>	
		</div>
		
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.requester_phone!""}</label>
			</div>
			<div class="form-col-left">
				<label for="request_phone">ご連絡先電話番号</label>
			</div>	
		</div>
		
	</div> 
	<h3>媒体アカウント情報</h3>
	<#-- ------------------------------------------------->
	<div id="request-form-bottom" class="form">
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.media_name!""}</label>
			</div>
			<div class="form-col-left">
				<label for="media_name">媒体名</label>
			</div>	
		</div>
		
		<div class="form-line">
			<div class="form-col-right">
				<a href="${request.url!""}" target="_blank">${request.url!""}</a>
			</div>
			<div class="form-col-left">
				<label for="url">管理画面URL</label>
			</div>	
		</div>
		
		<#if mediaLabel?? && mediaLabel.login_id_1??>
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.login_id_1!""}</label>
			</div>
			<div class="form-col-left">
				<label for="login_id_1">${mediaLabel.login_id_1}</label>
			</div>	
		</div>
		</#if>
		
		<#if mediaLabel?? && mediaLabel.login_id_2??>
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.login_id_2!""}</label>
			</div>
			<div class="form-col-left">
				<label for="login_id_2">${mediaLabel.login_id_2}</label>
			</div>	
		</div>
		</#if>
		
		<#if mediaLabel?? && mediaLabel.login_password_1??>
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.login_password_1!""}</label>
			</div>
			<div class="form-col-left">
				<label for="login_password_1">${mediaLabel.login_password_1}</label>
			</div>	
		</div>
		</#if>
		
		<#if mediaLabel?? && mediaLabel.login_password_2??>
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.login_password_2!""}</label>
			</div>
			<div class="form-col-left">
				<label for="login_password_2">パスワード2</label>
			</div>	
		</div>
		</#if>
	</div> </br>
	<h3>その他伝達事項</h3>
	<#-- ------------------------------------------------->
	<div id="request-form-comment">
		<span>${request.other_comment!""}</span></br></br></br>
	</div>	
</div>

<script type="text/javascript">
	me.dapps.global['url.context'] = '${formatter.url("")}';
	me.dapps.global['url.confirm_change'] = '${formatter.url("/request/confirm_change/?ajax")}';
	me.dapps.global['url.confirm_update_director'] = '${formatter.url("/request/confirm_update_director/?ajax")}';
	me.dapps.global['url.confirm_destroy'] = '${formatter.url("/request/confirm_destroy/?ajax")}';
	me.dapps.global['message.assign_director_warning'] = "WRN150";
	me.dapps.global['message.change.not_found'] = 'ERR151';
	me.dapps.global['message.change.forbidden'] = 'ERR152';
	me.dapps.global['message.change.general'] = 'ERR153';
	me.dapps.global['message.update_director_warning'] = "WRN200";
	me.dapps.global['message.update.director.not_found'] = 'ERR201';
	me.dapps.global['message.update.director.forbidden'] = 'ERR202';
	me.dapps.global['message.update.director.general'] = 'ERR203';
	me.dapps.global['message.destroy.not_found'] = 'ERR251';
	me.dapps.global['message.destroy.forbidden'] = 'ERR252';
	me.dapps.global['message.destroy.general'] = 'ERR253';
</script>				
</#escape>
</#compress>
