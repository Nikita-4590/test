<div id="page-header">
	<span>${pageTitle!""}</span>
</div>
<div id="header-line" class="clearfix">
    <#if httpRequestId??>
        <form id="http_request_id" type="submit" method="POST" action="http://localhost:8080/MediaRequestTool/request/view_request/204/">
            <input type="hidden" id="stored_httprequestid_input" name="stored_httprequestid_input" value="${httpRequestId}">
        </form>
    </#if>
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