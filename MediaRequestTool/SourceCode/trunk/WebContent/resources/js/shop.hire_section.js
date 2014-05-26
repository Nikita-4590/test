function openHireSectionList(textbox, hiddenId, hiddenName) {
	if (isUnset(me.dapps.global['shop.hire_section_box'])) {

		me.dapps.global['shop.hire_section_box'] = new me.dapps.box({
			width : 600,
			close_button : false,
			title : '店舗一覧',
			auto_hide : false,
			loading_text : me.dapps.ui.enhanced.locale.text('INF000'),
			button : {
				align : 'right',
				list : [ {
					text : 'キャンセル',
					loading : true,
					action : function(targetBox) {
						targetBox.close();
					}
				}, {
					text : 'キャンセル',
					action : function(targetBox) {
						targetBox.close();
					}
				} ]
			},
			pre_close : function(targetBox) {
				targetBox.table.dispose();
				return true;
			}
		});
	}

	me.dapps.global['shop.hire_section_box'].showFromUrl({
		url : me.dapps.global['url.hire_section_box'],
		method : 'get',
		error : function(box, e) {
			if (isUnset(me.dapps.global['shop.error_box'])) {
				me.dapps.global['shop.error_box'] = new me.dapps.box({
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
									errorBox._parent.close();
									errorBox.close();
								}
							}
						} ]
					}
				});
			}

			var messageId = 'ERR151';

			if (e.status == 403) {
				messageId = 'ERR150';
			}

			message = me.dapps.ui.enhanced.locale.text(messageId);

			me.dapps.global['shop.error_box']._error = e;
			me.dapps.global['shop.error_box']._parent = box;
			me.dapps.global['shop.error_box'].show(message);
		},
		callback : function(targetBox) {
			var table = $('#hire-section-list-tbl').table({
				_box : targetBox,
				width : '100%',
				select_row : false,
				paging : false,
				sortable : false,
				use_hashes : false,
				scrollable : true,
				no_data_message : me.dapps.ui.enhanced.locale.text('INF150'),
				url_patterns : {
					url : me.dapps.global['url.hire_section_list'],
					page : 'page={page}',
					sort : 'sort={sort}',
					sort_direction : 'sort_direction={sort_direction}',
					query : '{query_name}={query_value}'
				},
				data_binds : [ {
					name : 'section_to_hire_id',
					type : 'number',
					comparison : '='
				}, {
					name : 'section_to_hire_name',
					type : 'text',
					comparison : 'ilike'
				} ],
				pre_load : function(targetTable) {
					targetTable.config._box.adjustPosition(false);
					return true;
				},
				post_load : function(targetTable, e) {
					if (isSet(e) && e.status != 200) {
						if (isUnset(me.dapps.global['shop.error_box'])) {
							me.dapps.global['shop.error_box'] = new me.dapps.box({
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

												if (isSet(errorBox._parent)) {
													errorBox._parent.close();
												}
											}
										}
									} ]
								}
							});
						}

						var messageId = 'ERR151';

						if (e.status == 403) {
							messageId = 'ERR150';
						}

						message = me.dapps.ui.enhanced.locale.text(messageId);

						me.dapps.global['shop.error_box']._error = e;
						me.dapps.global['shop.error_box']._parent = targetTable.config._box;
						me.dapps.global['shop.error_box'].show(message);
					} else {
						targetTable.config._box.adjustPosition(true);
					}
				},
				row_selected : function(row, targetTable) {
					var cells = $(row).children('td');
					if (cells.length > 1) {
						var hireSectionId = $($(row).find('.hire-section-id')).text();
						var hireSectionName = $($(row).find('.hire-section-name')).text();

						if (parseInt(hireSectionId, 10) > 0) {
							updateInfo(textbox, hiddenId, hiddenName, hireSectionId, hireSectionName);
							targetTable.config._box.close();
						} else {
							// show message
						}
					}
				}
			});

			targetBox.table = table;

			$('#search-hire-section-form').submit(function(e) {
				e.preventDefault();

				var queryValue = $.trim($('#search-hire-section-value').val());

				table.search([ {
					query_type : 'all',
					query_name : 'query',
					query_value : queryValue
				} ]);
			});
		}
	});
}

function updateInfo(textbox, hiddenId, hiddenName, hireSectionID, hireSectionName) {
	$(textbox).val('(' + hireSectionID + ') ' + hireSectionName);
	$(hiddenId).val(hireSectionID);
	$(hiddenName).val(hireSectionName);
}