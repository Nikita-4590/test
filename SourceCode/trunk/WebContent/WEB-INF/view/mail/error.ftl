<#escape x as x?html>
<#compress>
<p>
	<span>エラー発生日時： ${formatter.formatDate(error.error_date)}</span>
</p>
<p>
	<span>発報元　　　　 ： ${error.error_system}</span>
</p>
<p>
	<span>重要度 　　　　： </span>
	<#if error.error_level=0>
		<span>WARNING（警告）</span>
	<#elseif error.error_level=1>
		<span>ERROR（エラー）</span>
	<#elseif error.error_level=2>
		<span>FATAL（重大なエラー）</span>
	</#if>
</p>
<p>
	<span>エラーコード　 ： ${error.error_code}</span>
</p>
<p>
	<span>詳細　　　　　：  ${error.error_detail}</span>
</p>
<p>
	<span>その他　　　　： ${error.error_message} </span>
</p>
<#if error?? && error.error_level=2>
	<b>Exception Message</b><br />
	<pre>${err_utils.getMessage(exception)}</pre>
	<b>Stack Trace</b><br />
	<pre>${err_utils.getStackTrace(exception)}</pre>	
<#elseif error?? && error.error_level=1 && error.error_type=2>
	<table style="border-collapse: collapse; border: 1px solid black; border-spacing: 0;">
		<thead>
			<th style="border: 1px solid black;">項目</th>
			<th style="border: 1px solid black;">企業DBの媒体レコード</th>
			<th style="border: 1px solid black;">PublicDBの媒体レコード</th>
		</thead>
		<tbody>
			<tr>
				<td style="border: 1px solid black;">媒体ID</td>
				<td style="border: 1px solid black;">${companyEntryMedia.media_id}</td>
				<td style="border: 1px solid black;">${publicEntryMedia.media_id}</td>
			</tr>
			<tr <#if companyEntryMedia.media_name != publicEntryMedia.media_name> style="color:red"</#if>>
				<td style="border: 1px solid black;">媒体名</td>
				<td style="border: 1px solid black;">${companyEntryMedia.media_name}</td>
				<td style="border: 1px solid black;">${publicEntryMedia.media_name}</td>
			</tr>
			<tr<#if companyEntryMedia.enable != publicEntryMedia.enable> style="color:red"</#if>>
				<td style="border: 1px solid black;">有効</td>
				<td style="border: 1px solid black;">${companyEntryMedia.enable}</td>
				<td style="border: 1px solid black;">${publicEntryMedia.enable}</td>
			</tr>
			<tr<#if companyEntryMedia.display_order != publicEntryMedia.display_order> style="color:red"</#if>>
				<td style="border: 1px solid black;">表示順</td>
				<td style="border: 1px solid black;">${companyEntryMedia.display_order}</td>
				<td style="border: 1px solid black;">${publicEntryMedia.display_order}</td>
			</tr>
			<tr<#if companyEntryMedia.media_group_id != publicEntryMedia.media_group_id> style="color:red"</#if>>
				<td style="border: 1px solid black;">グルップID</td>
				<td style="border: 1px solid black;">${companyEntryMedia.media_group_id}</td>
				<td style="border: 1px solid black;">${publicEntryMedia.media_group_id}</td>
			</tr>
		</tbody>
	</table>
</#if>
</#compress>
</#escape>