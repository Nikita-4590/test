$(me.dapps).bind('load', function() {
	var table = $('#media-ajax-table').table({
		width : '100%',
		select_row : false,
		sortable : false,
		paging : false,
		use_hashes : false,
		no_data_message : me.dapps.ui.enhanced.locale.text('INF200'),
		url_patterns : {
			url : me.dapps.global['url.media_list']
		},
		data_binds : [ {
			name : 'media_name',
			type : 'text'
		} ],
		row_selected : function(row) {
			var type = parseInt($(row).attr('type'), 10);
			
			switch (type) {
			case ACCOUNT_TYPE_UKERUKUN:
				location.href = me.dapps.global['url.add_ukerukun'];
				return;
			case ACCOUNT_TYPE_TABAITAI:
				var mediaID = parseInt($(row).attr('media-id'), 10);
				location.href = me.dapps.global['url.add_tabaitai'] + mediaID + "/";
				return;
			case ACCOUNT_TYPE_FAC:
				location.href = me.dapps.global['url.add_fac'];
				return;
			}

		}
	});
});