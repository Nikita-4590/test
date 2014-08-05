<div id="page-header">
	<span>${pageTitle!""}</span>
</div>
<div id="header-line" class="clearfix">
	<div id="header-buttons">
		<#if user??>
			<#if show_button_list?? && show_button_list = "on">
				<a href="${formatter.url("/request/list/")}" id="back-to-list" class="button-link">依頼一覧へ</a>
			</#if>
			<a href="#" id="logout-btn" logout-href="${formatter.url("/auth/logout/")}" onclick="event.preventDefault();logout()" class="button-link">ログアウト</a>
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