<#compress>
<title>${pageTitle!""}</title>
<script type="text/javascript" src="${formatter.url("/js/jquery/jquery-1.9.1.min.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/jquery/jquery.placeholder.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/jquery/jquery-ui-1.10.3.custom.min.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/jquery/jquery-ui-timepicker-addon.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/jquery/jquery-ui-timepicker-locale-jp.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/me.dapps/me.dapps.core.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/me.dapps/me.dapps.table.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/me.dapps/me.dapps.box.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/me.dapps/me.dapps.validator.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/me.dapps/me.dapps.ui.enhanced.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/global.js")}"></script>
<script type="text/javascript" src="${formatter.url("/js/locale/jp.js")}"></script>
<script type="text/javascript">
    me.dapps.global['message.no_data'] = '該当データが存在しません';
    $(document).ready(function () {
        me.dapps.ui.enhanced.scanLocaleTexts();
        
        $(me.dapps).trigger('load');
    });
</script>
<#if scripts??>
    <#list scripts as script>
        <script type="text/javascript" src="${formatter.url("/js/" + script)}"></script>    
    </#list>
</#if>
<link rel="stylesheet" type="text/css" href="${formatter.url("/css/jquery/ui-lightness/jquery-ui-1.10.3.custom.min.css")}" />
<link rel="stylesheet" type="text/css" href="${formatter.url("/css/jquery/jquery-ui-timepicker-addon.css")}" />
<link rel="stylesheet" type="text/css" href="${formatter.url("/css/me.dapps/me.dapps.table.css")}" />
<link rel="stylesheet" type="text/css" href="${formatter.url("/css/me.dapps/me.dapps.box.css")}" />
<link rel="stylesheet" type="text/css" href="${formatter.url("/css/me.dapps/me.dapps.validator.css")}" />
<link rel="stylesheet" type="text/css" href="${formatter.url("/css/me.dapps/me.dapps.ui.enhanced.css")}" />
<link rel="stylesheet" type="text/css" href="${formatter.url("/css/global.css")}" />
<#if stylesheets??>
    <#list stylesheets as stylesheet>
        <link rel="stylesheet" type="text/css" href="${formatter.url("/css/" + stylesheet)}" /> 
    </#list>
</#if>
</#compress>