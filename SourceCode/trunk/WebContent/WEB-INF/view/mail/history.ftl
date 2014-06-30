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
	<span style="color:red">対応依頼ID: ${request.relation_request_id}</span>
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
					<td style="border: 1px solid black; padding: 5px; width: 150px;">
						<span>${property.label}</span>
					</td>
					<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">
						<span>${property.oldValue!""}</span>
					</td>
					<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">
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
		<th style="border: 0px;"></th>
		<th style="border: 0px;"></th>
		<th style="border: 0px;"></th>
	</thead>
	<tbody>
		<tr>
			<td style="border: 1px solid black; padding: 5px; width: 150px;" rowspan="5">申込者情報</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">企業ID</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; max-width: 250px; min-width: 150px; word-wrap: break-word;">${request.company_id!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">企業名</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; max-width: 250px; min-width: 150px; word-wrap: break-word;">${request.company_name!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">ご担当者名</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; max-width: 250px; min-width: 150px; word-wrap: break-word;">${request.requester_name!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">ご連絡先メールアドレス</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; max-width: 250px; min-width: 150px; word-wrap: break-word;">${request.requester_mail!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">ご連絡先電話番号</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; max-width: 250px; min-width: 150px; word-wrap: break-word;">${request.requester_phone!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px; width: 150px;" rowspan="2">媒体アカウント情報</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">媒体名</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; max-width: 250px; min-width: 150px; word-wrap: break-word;">${request.media_name!""}</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px; width: 200px; word-wrap: break-word;">管理画面URL</td>
			<td style="border: 1px solid black; padding: 5px; width: 200px; max-width: 250px; min-width: 150px; word-wrap: break-word;">${request.url!""}</td>
		</tr>
	</tbody>
</table>
<br />
<span>その他伝達事項</span>
<div id="request-comment" style="font-size: 13px; width: 600px;">
	<label><pre style="word-wrap: break-word;">${request.other_comment!""}</pre></label>
</div>
</#compress>
</#escape>