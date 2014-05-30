$(me.dapps).bind(
	'load',
	function() {
		/*
		 * global status data
		 */
		var status_data = undefined;

		/*
		 * global search by
		 */
		var search_by = undefined;

		var table = $('#request-ajax-table').table(
			{
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
				}, {
					name : 'company_id',
					type : 'text',
					comparison : 'ilike',
				}, {
					name : 'status',
					type : 'text',
					comparison : 'ilike'
				}, {
					name : 'media_id',
					type : 'text',
					comparison : 'ilike'
				} ],
				row_selected : function(row) {
					var requestId = $(row).attr('row-id');
					var url = me.dapps.global['url.request_redirect']
							.replace('{request_id}', requestId);
					window.open(url);
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

		generatedTextSearch(null);

		/*
		 * clear form begin create form
		 */

		function clearFormSearch() {
			$('#search_form_input').empty();
			$('#search_form_label').empty();
		}

		/*
		 * load status from sever
		 */

		function loadStatus() {
			if (typeof status_data === 'undefined') {
				$.ajax({
					url : me.dapps.global['status_load'],
					type : 'get',
					contentType : 'application/json;charset=utf-8',
					success : function(response) {
						status_data = response;
						generatedStatusBoxSearch();
					},
					error : function(e) {
						window.alert('connect to sever error');
						/*
						 * process when call sever error
						 */
						// TODO
					}
				});
			} else {
				generatedStatusBoxSearch();
			}
		}

		/*
		 * generated status combobox
		 */

		function generatedStatusBoxSearch() {
			if ($.isArray(status_data)) {
				var label = $('<label />').text('検索文字').addClass(
						'label_control');
				var search_select = $('<select />')
						.addClass('input_search').attr({
							'id' : 'search_value',
							'type' : 'submit'
						});
				for ( var i in status_data) {
					var option = $('<option />').attr({
						'value' : status_data[i].status_type
					}).text(status_data[i].description);
					search_select.append(option);
				}
				$('#search_form_label').append(label);
				$('#search_form_input').append(search_select);
				search_by = 'status';
			}
		}
		;
		/*
		 * generated box search by input text
		 */
		function generatedTextSearch(searchtype) {
			var label = '検索文字';
			$('#search_form_label').append(
				$('<label />').text(label).addClass('label_control'));
			$('#search_form_input').append(
				$('<input/>').addClass('input_search').attr({
					id : 'search_value',
					'type' : 'text',
					'placeholder' : 'ここに検索文字を入力してください'
				}));
		}
		/*
		 * generated button submit for form
		 */
		$('#select_search').change(function() {
			clearFormSearch();
			if ($(this).val() == 'status') {
				loadStatus();
			} else {
				generatedTextSearch($(this).val());
			}
		});

		/*
		 * function when click submit form or enter
		 */
		$('#search-relation-request-form').submit(function(e) {
			e.preventDefault();
			var queryValue = undefined;
			if ($('#select_search').val() === 'status') {
				queryValue = $.trim($('#search_value').val());
			} else {
				queryValue = $.trim($('#search_value').val());
			}
			table.search([ {
				query_type : 'all',
				query_name : $('#select_search').val(),
				query_value : queryValue
			} ]);
		});
	});