$(me.dapps).bind(
'load',
function() {
	var table = $('#request-ajax-table').table({
		width : '100%',
		select_row : false,
		sortable : true,
		paging : true,
		use_hashes : false,
		sort_data : me.dapps.global['data_bindding'],
		no_data_message : me.dapps.ui.enhanced.locale
				.text('INF100'),
		url_patterns : {
			url : me.dapps.global['url.request_list'],
			page : 'page={page}',
			sort : 'sort={sort}',
			sort_direction : 'sort_direction={sort_direction}',
			query : '{query_name}={query_value}'
		},
		data_binds : [ {
			name : 'id',
			type : 'text',
			comparison : 'ilike',
		}],
		row_selected : function(row) {
			var requestId = $(row).attr('row-id');
			var url = me.dapps.global['url.request_redirect']
					.replace('{request_id}', requestId);
			window.open(url);
		},
		post_load : function(targetTable, e) {
			if (isSet(e) && e.status != 200) {
				if (isUnset(me.dapps.global['relation_request.list_error_box'])) {
					me.dapps.global['relation_request.list_error_box'] = new me.dapps.box({
						auto_hide : false,
						close_button : false,
						button : {
							align : 'right',
							list : [ {
								text : 'OK',
								action : function(errorBox) {
									if (errorBox._error.status == 403) {
										location.href = me.dapps.global['url.context'] + "/";
									} else {
										errorBox.close();
									}
								}
							} ]
						}
					});
				}

				var messageId = 'ERR101';

				if (e.status == 403) {
					messageId = 'ERR100';
				}

				message = me.dapps.ui.enhanced.locale.text(messageId);

				me.dapps.global['relation_request.list_error_box']._error = e;
				me.dapps.global['relation_request.list_error_box'].show(message);
			} else {
				var queries = targetTable.queries;
				if (isArray(queries)) {
					for (index in queries) {
						if (queries[index].query_name === 'query') {
							$('#search-request-query').val(
									queries[index].query_value);
							break;
						}
					}
				}
			}
		}
	});
	
	$('#search-relation-request-form').submit(function(e) {
		e.preventDefault();
		table.search([ {
			query_type : 'all',
			query_name : 'id',
			query_value : $('#input_text_search').val()
		} ]);
	});
});