<#compress>
<#escape x as x?html>
<div id="company-list-page" class="page">
    <div id="search-form">
        <form id="search-relation-request-form">
            <div id="status_search_form">
                <label class="label_search">ステータス検索</label>
                <select id="status_select_option" class="input_text_search">
                    <option value=""></option>
                 <#list listStatus as status>
                    <option value="${status.status_type}">${status.description}</option>
                 </#list>
                </select>
            </div>
            <div class="free_text_search_form">
                <label class="label_search">フリーワード検索</label>
                <input type="text" id="input_text_search" placeholder="企業ID、企業名、媒体名、依頼ID、担当ディレクターで検索"/>
                <input type="submit" id="button_search" class="button orange" value="検索"/>
            </div>
        </form>
    </div>
    <table id="request-ajax-table" cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th class="column_status">ステータス</th>
                <th class="column_registered_time">登録日時</th>
                <th class="column_renkei_date">連携開始日</th>
                <th class="column_company_id">企業ID</th>
                <th class="column_company_name">企業名</th>
                <th class="column_media_name">媒体名</th>
                <th class="column_request_id">依頼ID</th>
                <th class="column_tantosha">担当ディレクター</th>
            </tr>
        </thead>
    </table>
</div>
<dapps-locale-text>ERR001</dapps-locale-text>
<script type="text/javascript">
    me.dapps.global['url.request_list'] = '${formatter.url("/request/ajax_list/?ajax")}';
    me.dapps.global['data_binding'] = ['status','created_at','crawl_date',,,,,,];
    me.dapps.global['url.request_redirect'] = '${formatter.url("/request/view_request/{request_id}/")}';
    me.dapps.global['url.ajax_call'] = '${formatter.url("/request/redirect/")}';
</script>
</#escape>
</#compress>