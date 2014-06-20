<div id="page-header">
	<span>${pageTitle!""}</span>
</div>
<div id="header-line" class="clearfix">
	<div id="header-buttons">
		<#if user??>
			<!--<a href="${formatter.url("/request/")}" class="button-link">依頼一覧へ</a>-->
			<a href="${formatter.url("/auth/logout/")}" class="button-link">ログアウト</a>
		</#if>
	</div>
	<div id="breadcrumbs">
		${formatter.createBreadcrumbs(breadcrumbs)}
	</div>
</div>
<#if splash_message??>
	<script type="text/javascript">
		$(document).ready(function() {
			showFlashMessage('${splash_message?html}');
		});
	</script>
</#if>