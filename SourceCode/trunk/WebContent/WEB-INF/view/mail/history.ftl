<#escape x as x?html>
<#compress>
<p>
	<span>変更日時: ${formatter.formatDate(comment.created_at)}</span>
</p>
<br />
<table style="border-collapse: collapse; border-spacing: 0;">
	<thead>
		<th style="border: 1px solid black;">項目</th>
		<th style="border: 1px solid black;">内容</th>
	</thead>
	<tbody>
		<#list comment.properties as property>
			<#if property.label??>
				<tr>
					<td style="border: 1px solid black;">
						<span>${property.label}</span>
					</td>
					<td style="border: 1px solid black; width: 250px; max-width: 250px; min-width: 250px;">
						<span>${property.newValue!""}</span>
					</td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
</#compress>
</#escape>