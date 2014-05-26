$(me.dapps).bind('load', function() {
	var table = $('#company-ajax-table').table({
		width : '100%',
		select_row : false,
		sortable : false,
		paging : true,
		use_hashes : false,
		no_data_message : me.dapps.ui.enhanced.locale.text('INF300'),
		url_patterns : {
			url : me.dapps.global['url.account_list'],
			page : 'page={page}',
			sort : 'sort={sort}',
			sort_direction : 'sort_direction={sort_direction}',
			query : '{query_name}={query_value}'
		},
		data_binds : [ {
			name : 'media_name',
			type : 'text',
			comparison : 'ilike'
		}, {
			name : 'login_id',
			type : 'text',
			comparison : 'ilike'
		}, {
			name : 'shop_name',
			type : 'text',
			comparison : 'ilike'
		}, {
			name : 'status',
			type : 'text'
		} ],
		row_selected : function(row) {
			var accountId = parseInt($(row).attr('account-id'), 10);
			var accountType = parseInt($(row).attr('account-type'), 10);

			if (accountType == ACCOUNT_TYPE_UKERUKUN) {
				var url_in = me.dapps.global['url.view_ukerukun'];
				var url_out = url_in.replace("{ukerukun_id}", accountId);

				$(location).attr('href', url_out);
			} else if (accountType == ACCOUNT_TYPE_TABAITAI) {
				var url_in = me.dapps.global['url.view_tabaitai'];
				var url_out = url_in.replace("{tabaitai_id}", accountId);

				$(location).attr('href', url_out);
	
			} else if (accountType == ACCOUNT_TYPE_FAC) {
				var url_in = me.dapps.global['url.view_fac'];
				var url_out = url_in.replace("{fac_id}", accountId);

				$(location).attr('href', url_out);
					
			} else {
				if (isUnset(me.dapps.global['account.no_support_box'])) {
					me.dapps.global['account.no_support_box'] = new me.dapps.box({
						close_button : false,
						auto_hide : false,
						button : {
							align : 'right',
							list : [ {
								text : 'OK',
								action : function(box) {
									box.close();
								}
							} ]
						}
					});
				}

				var noSupportMessage = me.dapps.ui.enhanced.locale.text('INF001');

				me.dapps.global['account.no_support_box'].show(noSupportMessage);
			}
		},
		post_load : function(targetTable, e) {
			if (isSet(e) && e.status != 200) {
				if (isUnset(me.dapps.global['account.list_error_box'])) {
					me.dapps.global['account.list_error_box'] = new me.dapps.box({
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

				var messageId = 'ERR301';

				if (e.status == 403) {
					messageId = 'ERR300';
				}

				message = me.dapps.ui.enhanced.locale.text(messageId);

				me.dapps.global['account.list_error_box']._error = e;
				me.dapps.global['account.list_error_box'].show(message);
			} else {
				// restore queries value
				var queries = targetTable.queries;

				if (isArray(queries) && queries.length > 0) {
					$('#search-query-name').val(queries[0].query_name).change();
					setQueryValue(queries[0].query_name, queries[0].query_value);
				}
			}
		}
	});

	$('#search-block').submit(function(e) {
		e.preventDefault();

		var queryName = $.trim($('#search-query-name').val());

		table.search([ {
			query_name : queryName,
			query_value : getQueryValue(queryName)
		} ]);
	});

	$('#search-query-name').change(function() {
		var value = this.value.trim();
		switch (value) {
		case 'media_name':
			$('#search-middle input, #search-middle select').hide();
			$('#search-media-select').show();
			break;
		case 'status':
			$('#search-middle input, #search-middle select').hide();
			$('#search-status-select').show();
			break;
		default:
			$('#search-middle select').hide();
			$('#search-query-value').show();
			break;
		}
	}).change();
});

function getQueryValue(queryName) {
	switch (queryName) {
	case 'media_name':
		return $('#search-media-select').val();
	case 'status':
		return $('#search-status-select').val();
	default:
		return $('#search-query-value').val();
	}
}

function setQueryValue(queryName, queryValue) {
	switch (queryName) {
	case 'media_name':
		$('#search-media-select').val(queryValue);
		break;
	case 'status':
		$('#search-status-select').val(queryValue);
		break;
	default:
		$('#search-query-value').val(queryValue);
		break;
	}
}