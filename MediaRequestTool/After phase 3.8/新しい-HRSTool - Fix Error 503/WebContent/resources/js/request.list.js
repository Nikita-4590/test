var table = undefined;
var searchParam = undefined;
var status = undefined;
$(me.dapps).bind(
'load',
function() {
	function reloadSize() {
		var size = $('#request-ajax-table').width() - 260;
		$('#input_text_search').attr('style','width:' + size + 'px;');
	}
	window.onresize = function(event) {
		reloadSize();
	};
	table = $('#request-ajax-table').table({
		width : '100',
		select_row : false,
		sortable : true,
		paging : true,
		method : 'Post',
		is_restore_data : true,
		use_hashes : false,
		sort_data : me.dapps.global['data_binding'],
		default_sort : 'status',
		url : me.dapps.global['url.request_list'],
		no_data_message : me.dapps.ui.enhanced.locale
				.text('INF100'),
		row_selected : function(row) {
			var requestId = $(row).attr('row-id');
			var url = me.dapps.global['url.request_redirect'].replace('{request_id}', requestId);
			table.storageRowClick('requestId', requestId);
			window.location.href = url;
		},
		control_reload_page : false,
		showMessage : function(message) {
			$('#span_info_last_view_request').text(message);
		},
		store_data : [{
			'id' : 'input_text_search',
			'query_name' : 'searchText'
		},{
			'id' : 'status_select_option',
			'query_name' : 'status'
		},{
			'id' : 'more_status_checkbox',
			'query_name' : 'getMoreFinishedRecord'
		}],
		isResetSortAfterSearch : true,
		post_load : function(targetTable, e) {
			if (isSet(e) && e.status != 200) {
				if (isUnset(me.dapps.global['relation_request.list_error_box'])) {
					me.dapps.global['relation_request.list_error_box'] = new me.dapps.box({
						auto_hide : false,
						title : '!!! 警告  !!!',
						type : 'ERROR',
						close_button : false,
						button : {
							align : 'right',
							list : [ {
								text : 'OK',
								action : function(errorBox) {
									if (errorBox._error.status == 429) {
										window.close();
									} else {
										errorBox.close();
									}
								}
							}, {
								text : 'Reload',
								action : function(errorBox) {
									if (errorBox._error.status == 429) {
										window.location.href = window.location.reload;
									} else {
										errorBox.close();
									}
								}
							}]
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
		var more_status_checkbox = undefined;
		if($("#more_status_checkbox").is(':checked')) {
			more_status_checkbox = true;
		} else {
			more_status_checkbox = false;
		}
		
		status = $("#status_select_option option:selected").val();
		searchParam = $('#input_text_search').val();
		table.search([ {
			query_name : 'searchText',
			query_value : searchParam
		} , {
			query_name : 'status',
			query_value : status
		},{
			query_name : 'getMoreFinishedRecord',
			query_value : more_status_checkbox
		}], 'created_at');
	});
});