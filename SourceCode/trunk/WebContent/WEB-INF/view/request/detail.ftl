<#compress>
<#escape x as x?html>
<div class="page" id="request-detail-page">
	<div id="request-form-top" class="form">
		<div class="form-line">
			<div class="form-col-left-special">
				<label>依頼ID</label>
			</div>
			<div class="form-col-right-special">
				<label>受付日：　${request.created_at}</label>
			</div>
			<div class="form-col-middle-special">
				<label id="request-id">${request.relation_request_id}</label>
			</div>
		</div>
		
		<div class="form-line">
			<div class="form-col-left-special">
				<label>現在のステータス</label>
			</div>
			<div class="form-col-right-special">
			</div>
			<div class="form-col-middle-special">
				<label id="label-current-status">${request.status_description}</label>
				<input type="hidden" id="current-status" value="${request.status}" />
			</div>
		</div>
		
		<div class="form-line">
			<div class="form-col-left-special">
				<label>担当ディレクター</label>
			</div>
			<div class="form-col-right-special">
				<#if view = "NEW">
					<a href="#" class="button-link" id="change-status" onclick="confirmChange(${request.relation_request_id}); return false;">担当ディレクターを依頼</a>
				</#if>
				<#if view = "PROCESSING">
					<a href="#"　id="enable-change-director" >担当ディレクターを変更</a>
					<a href="#"　id="cancel-change-director" style="display: none;">担当ディレクターを変更しない</a>
				</#if>
			</div>
			<div class="form-col-middle-special">
				<#if view = "NEW">
					<select name="new_director" id="select-new-director">
						<#list directors as director>
							<option value="${director.id}">${director.user_name}</option>
						</#list>
					</select>
				<#else>
					<label><b>${request.assign_user_name!""}</b></label>
					<input type="hidden" id="current-director" value="${request.assign_user_id!""}" />
				</#if>
			</div>
		</div>
			
		<#if view = "PROCESSING">
			<div class="form-line" id="show-more" style="display: none;">
				<div class="form-col-left-special">
					<label>新規担当ディレクター</label>
				</div>
				<div class="form-col-right-special">
					<a href="#" class="button-link" id="update-director" onclick="confirmUpdateDirector(${request.relation_request_id}); return false;">変更する</a>
				</div>
				<div class="form-col-middle-special">
					<select name="update_director" id="select-update-director">
						<#list directors as director>
							<option value="${director.id}">${director.user_name}</option>
						</#list>
					</select>
				</div>
			</div>
		</#if>
		
		<div class="form-line">
			<div class="form-col-left-special">
				<label for="crawl_date">連携開始日</label>
			</div>
			<div class="form-col-right-special">
				<#if view = "PROCESSING">
					<a href="#" class="button-link" id="change-status" onclick="confirmChange(${request.relation_request_id}); return false;">連携開始日を登録</a>
				</#if>
			</div>
			<div class="form-col-middle-special">
				<#if view = "PROCESSING">
					<input type="text" id="crawl-date" name="crawl_date" dapps-ui-datepicker="{'input':{'format':'yy-mm-dd'},'output':{'format':'yy年mm月dd日'}}" />
				<#elseif view = "FINISHED"> <label><b>${request.crawl_date_to_display!""}</b></label>
				<#else> <label><b>未確定</b></label>		
				</#if>
			</div>
		</div>	
		
		<#if view != "FINISHED">
			<div class="form-line">
				<div class="form-col-left-special">
					<label>次のステータス</label>
				</div>
				<div class="form-col-right-special">	
				</div>
				<div class="form-col-middle-special">
					<#if view = "NEW" || view = "PROCESSING">
						<label id="label-next-status">${nextStatus.description}</label>
					</#if>
				</div>
			</div>
		</#if>
		
		<#if view != "FINISHED">
			<div class="form-line-bottom">
				<a href="#"　id="button-destroy" onclick="confirmDestroy(${request.relation_request_id}); return false;" >ｘこの依頼を取消す</a>
			</div>
		</#if>
		
		<input type="hidden" id="view" value="${view}" />
	</div>
	
	<#if view = "NEW" || view = "PROCESSING">
		<div id="message">
			<#if view = "NEW">※担当ディレクターを設定してください。
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
				<label for="login_password_2">${mediaLabel.login_password_2}</label>
			</div>	
		</div>
		</#if>
	</div> </br>
	<h3>その他伝達事項</h3>
	<#-- ------------------------------------------------->
	<div id="request-comment">
		<label><pre style="word-wrap: break-word;">${request.other_comment!""}</pre></label>
	</div>
	<div class="center" id="close-button-wrapper">
		<button id="close-button">依頼一覧へ
		</button>
	</div>
</div>

<script type="text/javascript">
    me.dapps.global['url.back_to_list'] = '${formatter.url("/request/list/")}';
	me.dapps.global['url.context'] = '${formatter.url("")}';
	me.dapps.global['url.confirm_change'] = '${formatter.url("/request/confirm_change/?ajax")}';
	me.dapps.global['url.confirm_update_director'] = '${formatter.url("/request/confirm_update_director/?ajax")}';
	me.dapps.global['url.confirm_destroy'] = '${formatter.url("/request/confirm_destroy/?ajax")}';
	me.dapps.global['message.change.not_found'] = 'ERR151';
	me.dapps.global['message.change.forbidden'] = 'ERR152';
	me.dapps.global['message.change.general'] = 'ERR153';
	me.dapps.global['message.assign_director_warning'] = 'ERR155';
	me.dapps.global['message.update.director.not_found'] = 'ERR201';
	me.dapps.global['message.update.director.forbidden'] = 'ERR202';
	me.dapps.global['message.update.director.general'] = 'ERR203';
	me.dapps.global['message.update_director_warning'] = 'ERR204';
	me.dapps.global['message.destroy.not_found'] = 'ERR251';
	me.dapps.global['message.destroy.forbidden'] = 'ERR252';
	me.dapps.global['message.destroy.general'] = 'ERR253';
</script>
</#escape>
</#compress>
