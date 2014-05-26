<#compress>
<#escape x as x?html>
<div id="company-list-page" class="page">
	<form id="search-company-form">
		<label>検索文字</label>
		<input id="search-company-query" type="text" size="40" placeholder="ここに検索文字を入力してください" />
		<input type="submit" class="button orange" value="検索" />
	</form>
	<table id="company-ajax-table" cellpadding="0" cellspacing="0">
		<thead>
			<tr>
				<th width="300">
					<span>企業ID</span>
				</th>
				<th>
					<span>企業名</span>
				</th>
			</tr>
		</thead>
	</table>
</div>
<dapps-locale-text>ERR001</dapps-locale-text>
<script type="text/javascript">
	me.dapps.global['url.company_list'] = '${formatter.url("/company/ajax_list/?ajax")}';
	me.dapps.global['url.account_list'] = '${formatter.url("/account/list/{company_id}/")}';
</script>
</#escape>
</#compress>