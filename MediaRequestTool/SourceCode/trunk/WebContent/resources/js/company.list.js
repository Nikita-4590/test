$(me.dapps).bind('load', function() {
	var table = $('#company-ajax-table').table({
		width : '100%',
		select_row : false,
		sortable : false,
		paging : false,
		use_hashes : false,
		no_data_message: me.dapps.ui.enhanced.locale.text('INF100'),
		url_patterns : {
			url : me.dapps.global['url.company_list'],
			page : 'page={page}',
			sort : 'sort={sort}',
			sort_direction : 'sort_direction={sort_direction}',
			query: '{query_name}={query_value}'
		},
		data_binds : [ { 
			name: 'company_id', 
			type: 'text',
			comparison: 'ilike',
		}, {
			name: 'company_name',
			type: 'text',
			comparison: 'ilike'
		} ],
		row_selected : function(row) {
			var companyId = $(row).attr('company-id');
			var url = me.dapps.global['url.account_list'].replace('{company_id}', companyId);
			
			window.location.href = url;
		},
		post_load: function (targetTable) {
			// restore queries value
			var queries = targetTable.queries;
			
			if (isArray(queries)) {
				for (index in queries) {
					if (queries[index].query_name == 'query') {
						$('#search-company-query').val(queries[index].query_value);
						break;
					}
				}
			}
		}
	});
	
	$('#search-company-form').submit(function (e) {
		e.preventDefault();
		var queryValue = $.trim($('#search-company-query').val());
		
		table.search([ {
			query_type : 'all',
			query_name : 'query',
			query_value : queryValue
		} ]);
	});
});