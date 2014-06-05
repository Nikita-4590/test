<#escape x as x?html>
<#compress>
<p>
	<div>
		<label>${comment.properties[1].value}　株式会社 </label>
	</div>
	<div>
		<label>${comment.properties[2].value}　様 </label>
	</div>
	<div>
		<label>「Recop」 をご利用いただき、ありがとうございます。</label>
	</div>
	<div>
		<label>連携設定依頼を承ったことをお知らせいたします。</label>
	</div><br>
	<div>
		<label>【依頼内容】</label>
	</div><br>
	<div>
		<label>申込者情報</label>
	</div>
</p>
<table style="border-collapse: collapse; border-spacing: 0;">
	<thead>
	</thead>
	<tbody>
		<tr>
			<td style="border: 1px solid black; padding: 5px;">
				<span>${comment.properties[0].label}</span>
			</td>
			<td style="border: 1px solid black; padding: 5px; width: 250px; max-width: 250px; min-width: 250px; word-wrap: break-word;">
				<span>${comment.properties[0].value}</span>
			</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;">
				<span>${comment.properties[1].label}</span>
			</td>
			<td style="border: 1px solid black; padding: 5px; width: 250px; max-width: 250px; min-width: 250px; word-wrap: break-word;">
				<span>${comment.properties[1].value}</span>
			</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;">
				<span>${comment.properties[2].label}</span>
			</td>
			<td style="border: 1px solid black; padding: 5px; width: 250px; max-width: 250px; min-width: 250px; word-wrap: break-word;">
				<span>${comment.properties[2].value}</span>
			</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;">
				<span>${comment.properties[3].label}</span>
			</td>
			<td style="border: 1px solid black; padding: 5px; width: 250px; max-width: 250px; min-width: 250px; word-wrap: break-word;">
				<span>${comment.properties[3].value}</span>
			</td>
		</tr>
		<tr>
			<td style="border: 1px solid black; padding: 5px;">
				<span>${comment.properties[4].label}</span>
			</td>
			<td style="border: 1px solid black; padding: 5px; width: 250px; max-width: 250px; min-width: 250px; word-wrap: break-word;">
				<span>${comment.properties[4].value}</span>
			</td>
		</tr>		
	</tbody>
</table>
</#compress>
</#escape>