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
				<label id="current-status">${request.status_description}</label>
			</div>
			<div class="left-side">
				<label>現在のステータス</label>
			</div>
		</div>
		
		<#if displayListDirectors = "yes">
			<div class="form-line">
				<div class="right-side">
					<#if directors?has_content>
						<#list directors as director>
							<option value="${director.id}">${director.user_name}</option>
						</#list>
					<#else>
						<option></option>	
					</#if>	
				</div>
				<div class="left-side">
					<label>担当ディレクター</label>
				</div>
			</div>
		</#if>
			
		<div class="form-line">
			<div class="right-side">
				<#if listNextStatus??>
					<select name="new_status" id="select-new-status">
						<#list listNextStatus as status>
							<option value="${status.status_type}">${status.description}</option>	
						</#list>
					</select>
				</#if>
				<#if nextStatus??>
					<label id="new-status">${nextStatus.description}</label>
				</#if>
				<a href="#" class="button-link" id="change-status" onclick="confirmChange(${request.relation_request_id}); return false;">変更する</a>
			</div>
			<div class="left-side">
				<label>次のステータス</label>
			</div>
		</div>	
	</div>	
	<h3>申込者情報</h3>
	<#-- ------------------------------------------------->
	<div id="request-form-center" class="form">
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.company_id!""}</label>
			</div>
			<div class="form-col-left">
				<label for="company_id">御社ID</label>
			</div>	
		</div>
		
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.company_name!""}</label>
			</div>
			<div class="form-col-left">
				<label for="company_name">御社名</label>
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
				<label>${request.requester_mail!""}</label>
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
				<label for="media_name">媒体</label>
			</div>	
		</div>
		
		<div class="form-line">
			<div class="form-col-right">
				<label>${request.url!""}</label>
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
		<#--
		<div class="form-line">
			<div class="form-col-right">
				<input type="text" id="crawl-date" name="crawl_date" <#if request?? && request.crawl_date??>value="${request.crawl_date}"</#if> 
					dapps-ui-datepicker="{'input':{'format':'yy-mm-dd'},'output':{'format':'yy年mm月dd日'}}" />	
				<a href="#" class="button-link" onclick="confirmChangeRenkeiDate(${request.relation_request_id}); return false;">変更する</a>
			</div>
			<div class="form-col-left">
				<label for="crawl_date">連携開始日</label>
			</div>	
		</div>
		-->
	</div> </br>
	<h3>その他伝達事項</h3>
	<#-- ------------------------------------------------->
	<div id="request-form-comment" class="form">
		<span>${request.other_comment!""}</span></br></br></br>
	</div>	
</div>

<script type="text/javascript">
	me.dapps.global['url.context'] = '${formatter.url("")}';
	me.dapps.global['url.confirm_change'] = '${formatter.url("/request/confirm_change/?ajax")}';
	me.dapps.global['url.confirm_change_renkei_date'] = '${formatter.url("/request/confirm_change_renkei_date/?ajax")}';
	me.dapps.global['message.assign_person_warning'] = "WRN1";
</script>				
</#escape>
</#compress>
