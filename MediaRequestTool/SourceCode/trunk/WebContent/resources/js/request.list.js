var table = undefined;
var searchParam = undefined;
var status = undefined;
var inpTextSearchWidth = undefined;
var screenWidth = undefined;
var marginRightForm = undefined;
$(me.dapps).bind(
'load',
function() {
	
	screenWidth = window.screen.availWidth;
	marginRightForm = (screenWidth - $('#company-list-page').outerWidth())/2;
	inpTextSearchWidth = $('#input_text_search').outerWidth();
	setHeight();
	table = $('#request-ajax-table').table({
		width : '100',
		select_row : false,
		sortable : true,
		paging : true,
		method : 'Post',
		use_hashes : false,
		sort_data : me.dapps.global['data_binding'],
		default_sort : 'status',
		url : me.dapps.global['url.request_list'],
		no_data_message : me.dapps.ui.enhanced.locale
				.text('INF100'),
		row_selected : function(row) {
			var requestId = $(row).attr('row-id');
			var url = me.dapps.global['url.request_redirect']
					.replace('{request_id}', requestId);
			ajaxPostFormSubmit(url);
		},
		store_data : [{
			'id' : 'input_text_search',
			'query_name' : 'searchText'
		},{
			'id' : 'status_select_option',
			'query_name' : 'status'
		}],
		flow_id : 'stored_httprequestid_input',
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
	table.setDefaultText([{
		'id' : 'input_text_search',
		'query_name' : 'searchText'
	},{
		'id' : 'status_select_option',
		'query_name' : 'status'
	}]);
	$('#search-relation-request-form').submit(function(e) {
		e.preventDefault();
		status = $("#status_select_option option:selected").val();
		searchParam = $('#input_text_search').val();
		table.setDefaultSort('created_at');
		table.search([ {
			query_name : 'searchText',
			query_value : searchParam
		} , {
			query_name : 'status',
			query_value : status
		}]);
	});
	
	$(window).on('resize', function() {
		setHeight();
	});
	 
	function setHeight() {
	    
	    var currentWindowWidth = $(window).outerWidth();
//	    var newWidth = inpTextSearchWidth*currentWindowWidth/screenWidth <= screenWidth/2 ? currentWindowWidth - 210 : inpTextSearchWidth*currentWindowWidth/screenWidth;
	    var newWidth = currentWindowWidth - 210;
	    if(newWidth >= 20) {
	    	$('#input_text_search').css({'width' : inpTextSearchWidth*currentWindowWidth/screenWidth + 'px'});
	    } else {
	    	$('#input_text_search').css({'width' : '20px'});
	    }
	    
	    if(currentWindowWidth <= 1100) {
	    	$('#button_search').css({'margin-left' : '10px', 'float' : 'left'});
	    } else {
	    	$('#button_search').css({'margin-left' : '0px', 'float' : 'right'});
	    }
	    
	    
	  };
});