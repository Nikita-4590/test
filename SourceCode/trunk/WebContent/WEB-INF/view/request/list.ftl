<#compress>
<#escape x as x?html>
<div id="company-list-page" class="page">
    <div id="search-form">
        <form id="search-relation-request-form">
            <div id="status_search_form">
                <label class="label_control">ステータス検索</label>
                <select id="status_select_option" class="input_text_search">
                    <option value=""></option>
                 <#list lstStatus as status>
                    <option value="${status.status_type}">${status.description}</option>
                 </#list>
                </select>
            </div>
            <div class="free_text_search_form">
                <label class="label_control">フリーワード検索</label>
                <input type="text" id="input_text_search" placeholder="ここに検索文字を入力してください"/>
                <input type="submit" id="button_search" class="button orange" value="検索"/>
            </div>
        </form>
    </div>
    <table id="request-ajax-table" cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th class="status">ステータス</th>
                <th class="time_common">登録日時</th>
                <th class="time_common_date">連携開始日</th>
                <th class="company_id">企業ID</th>
                <th class="company_name">社名</th>
                <th class="media_name">媒体</th>
                <th class="request_id">依頼ID</th>
                <th>担当ディレクタ</th>
            </tr>
        </thead>
    </table>
</div>
<dapps-locale-text>ERR001</dapps-locale-text>
<script type="text/javascript">
    me.dapps.global['url.request_list'] = '${formatter.url("/request/ajax_list/?ajax")}';
    me.dapps.global['data_bindding'] = ['status','created_at','crawl_date',,,,,,];
    me.dapps.global['status_load'] = '${formatter.url("/request/load_status/?ajax")}';
    me.dapps.global['url.request_redirect'] = '${formatter.url("/request/view_request/{request_id}/")}';
</script>
</#escape>
</#compress>