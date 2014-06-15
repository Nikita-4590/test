<#escape x as x?html>
<#compress>
<p>
	<span>対応日時: ${formatter.formatDate(comment.created_at)}</span>
</p>
<p>
	<span>対応者: ${comment.user_name}</span>
</p>
<p>
	<span>対応コメント: </span>
	<pre style="font-family: inherit;"></#compress>${comment.comment_reason!""}<#compress></pre>
</p>
<p>
	<span>対応依頼ID: ${request.relation_request_id}</span>
</p>
<br />
<table style="border-collapse: collapse; border-spacing: 0;">
	<thead>
		<th style="border: 1px solid black;">項目</th>
		<th style="border: 1px solid black;">変更前</th>
		<th style="border: 1px solid black;">変更後</th>
	</thead>
	<tbody>
		<#list comment.properties as property>
			<#if property.label??>
				<tr<#if property.isDiff()> style="color: red;"</#if>>
					<td style="border: 1px solid black; padding: 5px;">
						<span>${property.label}</span>
					</td>
					<td style="border: 1px solid black; padding: 5px; width: 250px; max-width: 250px; min-width: 250px; word-wrap: break-word;">
						<span>${property.oldValue!""}</span>
					</td>
					<td style="border: 1px solid black; padding: 5px; width: 250px; max-width: 250px; min-width: 250px; word-wrap: break-word;">
						<span>${property.newValue!""}</span>
					</td>
				</tr>
			</#if>
		</#list>		
	</tbody>
</table>

<br />
<span>【依頼内容】</span>
<table style="border-collapse: collapse; border-spacing: 0;">
	<thead>
		<th style="border: 1px solid black;"></th>
		<th style="border: 1px solid black;"></th>
		<th style="border: 0px;"></th>
	</thead>
	<tbody>
		<tr>
			<td style="border: 1px solid black; padding: 5px;">申込者情報</td>
			<td style="border: 1px solid black; padding: 5px;">企業ID</td>
			<td style="border: 1px solid black; padding: 5px;">${request.company_id!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;"></td>
			<td style="border: 1px solid black; padding: 5px;">企業名</td>
			<td style="border: 1px solid black; padding: 5px;">${request.company_name!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;"></td>
			<td style="border: 1px solid black; padding: 5px;">ご担当者名</td>
			<td style="border: 1px solid black; padding: 5px;">${request.requester_name!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;"></td>
			<td style="border: 1px solid black; padding: 5px;">ご連絡先メールアドレス</td>
			<td style="border: 1px solid black; padding: 5px;">${request.requester_mail!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;"></td>
			<td style="border: 1px solid black; padding: 5px;">ご連絡先電話番号</td>
			<td style="border: 1px solid black; padding: 5px;">${request.requester_phone!""}</td>
		</tr>
		<tr>
			<td>媒体アカウント情報</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;"></td>
			<td style="border: 1px solid black; padding: 5px;">媒体名</td>
			<td style="border: 1px solid black; padding: 5px;">${request.media_name!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;"></td>
			<td style="border: 1px solid black; padding: 5px;">管理画面URL</td>
			<td style="border: 1px solid black; padding: 5px;">${request.url!""}</td>
		</tr>
		<#if request.label_login_id_1??>
		<tr>
			<td style="border: 1px solid black; padding: 5px;"></td>
			<td style="border: 1px solid black; padding: 5px;">${request.label_login_id_1!""}</td>
			<td style="border: 1px solid black; padding: 5px;">${request.login_id_1!""}</td>
		</tr>
		</#if>
		<#if request.label_login_id_2??>
		<tr>
			<td style="border: 1px solid black; padding: 5px;"></td>
			<td style="border: 1px solid black; padding: 5px;">${request.label_login_id_2!""}</td>
			<td style="border: 1px solid black; padding: 5px;">${request.login_id_2!""}</td>
		</tr>
		</#if>
	</tbody>
</table>
<table style="border-collapse: collapse; border-spacing: 0; width: 800px;">
	<thead>
		<th style="border: 0px; width: 200px;"></th>
		<th style="border: 0px;"></th>
	</thead>
	<tbody>
		<tr>
			<td>その他伝達事項</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td><pre><#if request.other_comment??>${(request.other_comment)}</#if></pre></td>
		</tr>
	</tbody>
</table>
</#compress>
</#escape>