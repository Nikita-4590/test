var passwordChanged = false;

$(me.dapps).bind('load', function() {
	// bind
	$('#open-shop-list-button').click(function(e) {
		e.preventDefault();

		if (isFunction(openHireSectionList)) {
			openHireSectionList('#shop-text', '#shop-id', '#shop-name');
		}
	});

	$('#ukerukun-form').validator({
		static_inputs : false,
		pre_submit : function(e, validator) {
			var syncEntryDate = $('#sync-entry-date').time();
			var crawlDate = $('#crawl-date').time();
			var currentDate = new Date();
			//currentDate.setDate(currentDate.getDate() + 1);
			currentDate.setHours(0, 0, 0, 0);
			crawlDate.setHours(0, 0, 0, 0);

			var actionType = me.dapps.global['action.type'];
			var dateChanged = $('#date-changed').val();

			if (($.trim(actionType) == 'edit' || $.trim(actionType) == 'add' ) && $.trim(dateChanged) == '1') {
				if ($('#emergency').is(':checked')) {
					if (currentDate.getTime() > crawlDate.getTime()) {
						e.preventDefault();

						if (isUnset(me.dapps.global['ukerukun.crawl_date_warning_box'])) {
							me.dapps.global['ukerukun.crawl_date_warning_box'] = new me.dapps.box({
								auto_hide : true,
								close_button : false,
								button : {
									align : 'right',
									list : [ {
										text : 'OK',
										action : function(targetBox) {
											targetBox.close();
											validator.warning('#crawl-date');
										}
									} ]
								}
							});
						}

						var messageId = me.dapps.global['message.crawl_date_warning'];
						var localMessage = me.dapps.ui.enhanced.locale.text(messageId);
						me.dapps.global['ukerukun.crawl_date_warning_box'].show(localMessage);
					} else if (syncEntryDate.getTime() > crawlDate.getTime()) {
						e.preventDefault();
						if (isUnset(me.dapps.global['ukerukun.sync_entry_date_warning_box'])) {
							me.dapps.global['ukerukun.sync_entry_date_warning_box'] = new me.dapps.box({
								auto_hide : true,
								close_button : false,
								button : {
									align : 'right',
									list : [ {
										text : 'OK',
										action : function(targetBox) {
											targetBox.close();
											validator.warning('#sync-entry-date');
										}
									} ]
								}
							});
						}

						var messageId = me.dapps.global['message.sync_entry_date_warning'];
						var localMessage = me.dapps.ui.enhanced.locale.text(messageId);
						me.dapps.global['ukerukun.sync_entry_date_warning_box'].show(localMessage);
					}
				} else {
					if (currentDate.getTime() >= crawlDate.getTime()) {
						e.preventDefault();

						if (isUnset(me.dapps.global['ukerukun.crawl_date_warning_box'])) {
							me.dapps.global['ukerukun.crawl_date_warning_box'] = new me.dapps.box({
								auto_hide : true,
								close_button : false,
								button : {
									align : 'right',
									list : [ {
										text : 'OK',
										action : function(targetBox) {
											targetBox.close();
											validator.warning('#crawl-date');
										}
									} ]
								}
							});
						}

						var messageId = me.dapps.global['message.crawl_date_warning'];
						var localMessage = me.dapps.ui.enhanced.locale.text(messageId);
						me.dapps.global['ukerukun.crawl_date_warning_box'].show(localMessage);
					} else if (syncEntryDate.getTime() > crawlDate.getTime()) {
						e.preventDefault();
						if (isUnset(me.dapps.global['ukerukun.sync_entry_date_warning_box'])) {
							me.dapps.global['ukerukun.sync_entry_date_warning_box'] = new me.dapps.box({
								auto_hide : true,
								close_button : false,
								button : {
									align : 'right',
									list : [ {
										text : 'OK',
										action : function(targetBox) {
											targetBox.close();
											validator.warning('#sync-entry-date');
										}
									} ]
								}
							});
						}

						var messageId = me.dapps.global['message.sync_entry_date_warning'];
						var localMessage = me.dapps.ui.enhanced.locale.text(messageId);
						me.dapps.global['ukerukun.sync_entry_date_warning_box'].show(localMessage);
					}
				}
			} 

			validator.target.trigger('post_validate', e);
		}
	});

	// create datetimepicker
	me.dapps.ui.enhanced.scanDatetimePickers();

	$('#view-history-button').click(function(e) {
		e.preventDefault();

		if (isFunction(openHistory)) {
			openHistory();
		}
	});

	$('#matching-type').change(function() {
		var str = $(this).val();

		if ($.trim(str) == 'byaddress') {
			$('#shop').show();
			$("#shop-text, #shop-id, #shop-name").prop('disabled', false);
		} else {
			$('#shop').hide();
			$("#shop-text, #shop-id, #shop-name").prop('disabled', true);
		}

	}).change();

	$('#password').change(function() {
		$('#password-changed').val("1");

		$(this).unbind('change');
	});

	// validate datetime
	$('#crawl-date').datepicker('option', 'onSelect', function() {
		$('#crawl-date').trigger('change');
	});

	$('#crawl-date').change(function() {
		var crawDate = $('#crawl-date').time();
		$('#sync-entry-date').datetimepicker('option', 'maxDate', crawDate).change();
	});

	$('#crawl-date, #sync-entry-date').change(function() {
		$('#date-changed').val(1);
	});

	$('#emergency').change(function() {
		if ($(this).is(':disabled')) {
			return;
		}

		var actionType = me.dapps.global['action.type'];
		var crawlDate = $('#crawl-date');
		var syncEntryDate = $('#sync-entry-date');
		if ($(this).is(':checked')) {
			crawlDate.prop('disabled', true);
			syncEntryDate.prop('disabled', false);

			var today = new Date();

			// set crawl_date: minDate = today
			crawlDate.datepicker('option', 'minDate', today);

			if (!me.dapps.global['datepicker.skip_default']) {
				// set crawl_date: default = today
				crawlDate.datepicker('setDate', today).change();

				// set sync_entry_date: default = today 00:00
				today.setHours(0, 0, 0, 0);
				syncEntryDate.datetimepicker('setDate', today);

				// set comment
				$('#comment').val('緊急対応');
			}
			crawlDate.change();
		} else {
			crawlDate.prop('disabled', false);
			syncEntryDate.prop('disabled', false);

			// set crawl_date: minDate = today + 1
			var minDate = new Date();
			minDate.setDate(minDate.getDate() + 1);
			crawlDate.datepicker('option', 'minDate', minDate);

			if (!me.dapps.global['datepicker.skip_default']) {
				if ($.trim(actionType) == 'add') {
					// set crawl_date: default = (today + 2) 00:00
					var defaultValue = new Date();
					defaultValue.setDate(defaultValue.getDate() + 2);
					crawlDate.datepicker('setDate', defaultValue).change();

					// set sync_entry_date: default = (today + 2) 00:00
					defaultValue.setHours(0, 0, 0, 0);
					syncEntryDate.datetimepicker('setDate', defaultValue);
					crawlDate.change();
				} else {
					if ($('#crawl-date-original').length > 0) {
						$('#crawl-date').val($('#crawl-date-original').val());

						me.dapps.ui.enhanced.formatDateTime($('#crawl-date')[0], true, 'dapps-ui-datepicker');
						$('#crawl-date').change();
					} else {
						$('#crawl-date').val(lastCrawlDateValue).change();
					}

					if ($('#sync-entry-date-original').length > 0) {
						$('#sync-entry-date').val($('#sync-entry-date-original').val());
						me.dapps.ui.enhanced.formatDateTime($('#sync-entry-date')[0], false, 'dapps-ui-datetimepicker');
						$('#sync-entry-date').change();
					} else {
						$('#sync-entry-date').val(lastSyncEntryDateValue).change();
					}
				}
			}
		}
		me.dapps.global['datepicker.skip_default'] = false;
	}).change();

	var lastCrawlDateValue = "";
	var lastSyncEntryDateValue = "";

	$("#enable-edit-date").click(function(e) {
		lastCrawlDateValue = $('#crawl-date').val();
		lastSyncEntryDateValue = $('#sync-entry-date').val();

		$('#emergency').prop('disabled', false).change();
		// $('#crawl-date').prop('disabled', false);
		// $('#sync-entry-date').prop('disabled', false);
		$('#enable-edit-date').hide();
		$('#cancel-edit-date').show();

		$('#date-changed').val(1);

		e.preventDefault();
	});

	$("#cancel-edit-date").click(function() {
		$('#emergency, #crawl-date, #sync-entry-date').prop('disabled', true);
		$('#emergency').prop('checked', false);
		$('#enable-edit-date').show();
		$('#cancel-edit-date').hide();

		if ($('#crawl-date-original').length > 0) {
			$('#crawl-date').val($('#crawl-date-original').val());

			me.dapps.ui.enhanced.formatDateTime($('#crawl-date')[0], true, 'dapps-ui-datepicker');
			$('#crawl-date').change();
		} else {
			$('#crawl-date').val(lastCrawlDateValue).change();
		}

		if ($('#sync-entry-date-original').length > 0) {
			$('#sync-entry-date').val($('#sync-entry-date-original').val());
			me.dapps.ui.enhanced.formatDateTime($('#sync-entry-date')[0], false, 'dapps-ui-datetimepicker');
			$('#sync-entry-date').change();
		} else {
			$('#sync-entry-date').val(lastSyncEntryDateValue).change();
		}
		$('#date-changed').val(0);
	});
});