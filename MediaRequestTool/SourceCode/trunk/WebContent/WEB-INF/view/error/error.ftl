<h3>システムにて処理を続行出来ない問題が発生しました。しばらく時間を置いてから再度、初めから操作をお願いします。</h3>
<#if formatter??>
	<a href="${formatter.url("/request/list/")}">依頼一覧へ</a>
	<#if exception??>
		<hr />
		<ul>
			<li>
				<b>Exception Message</b><br />
				<pre>${err_utils.getMessage(exception)}</pre>
			</li>
			<#--
			<li>
				<b>Stack Trace</b><br />
				<pre>${err_utils.getStackTrace(exception)}</pre>
			</li>
			-->
		</ul>
	</#if>
</#if>