<#compress>
<#escape x as x?html>
<div id="company-list-page" class="page">
    <form id="search-relation-request-form">
        <div id="search_form_control">
            <div class="search_form_type" id="search_form_type">
            <label>検索タイプ</label>
            <select id="select_search" class="form-control">
                <option value="id">依頼ID</option>
                <option value="company_id">企業ID,社名</option>
                <option value="status">ステータス</option>
                <option value="media_id">その他</option>
            </select>
        </div>
    
        <div class="search_form_text" id="search_form_text">
            <div id="search_form_label"></div>
            <div id="search_form_input"></div>
            <div id="search_form_btn">
                <input type="submit" class="button orange" value="検索">
            </div>    
        </div>
    </form>
    
    </div>  
    <table id="request-ajax-table" cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th class="status">ステータス</th>
                <th class="time_common">登録日時</th>
                <th class="time_common">希望連携開始日</th>
                <th class="company_id">企業ID</th>
                <th class="company_name">社名</th>
                <th class="media_name">媒体</th>
                <th class="request_id">依頼ID</th>
                <th>HRS担当者</th>
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