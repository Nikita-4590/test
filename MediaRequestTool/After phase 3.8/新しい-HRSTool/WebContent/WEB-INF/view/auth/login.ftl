<#compress>
<#escape x as x?html>
<div id="login-form">
	<div id="login-message-inform">
	<#if message??>
		<p><dapps-locale-text>${message}</dapps-locale-text></p>
	</#if>
	</div>
	<form action="${formatter.url("/auth/process/")}" method="POST" autocomplete="off">
		<table id="login-inputs">
			<tr>
				<td width="100">
					<label for="j_username">ユーザー名</label>
				</td>
				<td>
					<input type="text" name="j_username" autofocus="autofocus">
				</td>
			</tr>
			<tr>
				<td>
					<label for="j_password">パスワード</label>
				</td>
				<td>
					<input type="password" name="j_password" />
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input name="submit" type="submit" value="ログイン" class="button" />
				</td>
			</tr>
		</table>
	</form>
</div>
</#escape>
</#compress>