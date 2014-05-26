<#escape x as x?html>
<#compress>
<p>
	<span>変更日時: ${formatter.formatDate(comment.created_at)}</span>
</p>
<p>
	<span>変更者: ${comment.user_name}</span>
</p>
<p>
	<span>操作: </span>
	<#if comment.action_type=0>
		<span>新規</span>
	<#elseif comment.action_type=1>
		<span>変更</span>
	<#elseif comment.action_type=2>
		<span>削除</span>
	</#if>
</p>
<p>
	<span>変更コメント: </span>
	<pre style="font-family: inherit;"></#compress>${comment.comment_reason}<#compress></pre>
</p>
<br />
<table style="border-collapse: collapse; border-spacing: 0;">
	<thead>
		<th style="border: 1px solid black;">項目</th>
		<#if comment.action_type=1>
			<th style="border: 1px solid black;">変更前</th>
			<th style="border: 1px solid black;">変更後</th>
		<#else>
			<th style="border: 1px solid black;">内容</th>
		</#if>
	</thead>
	<tbody>
		<#list comment.properties as property>
			<#if property.label??>
				<tr<#if property.isDiff()> style="color: red;"</#if>>
					<td style="border: 1px solid black;">
						<span>${property.label}</span>
					</td>
					<#if comment.action_type!=0>
						<td style="border: 1px solid black; width: 250px; max-width: 250px; min-width: 250px;">
							<#if property.oldValue?? && property.oldValue?is_boolean>
								<span>${property.oldValue?string("true", "false")}</span>
							<#else>
								<span>${property.oldValue!""}</span>
							</#if>
						</td>
					</#if>
					<#if comment.action_type!=2>
						<td style="border: 1px solid black; width: 250px; max-width: 250px; min-width: 250px;">
							<#if property.newValue?? && property.newValue?is_boolean>
								<span>${property.newValue?string("true", "false")}</span>
							<#else>
								<span>${property.newValue!""}</span>
							</#if>
						</td>
					</#if>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
</#compress>
</#escape>