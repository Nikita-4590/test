$(me.dapps).bind('load', function() {
	// bind
	$('#open-shop-list-button').click(function(e) {
		e.preventDefault();

		if (isFunction(openHireSectionList)) {
			openHireSectionList('#shop-text', '#shop-id', '#shop-name');
		}
	});

	$('#fac-form').validator({
		static_inputs : false,
		pre_submit : function(e, validator) {
			var syncEntryDate = $('#sync-entry-date').time();
			var crawlDate = $('#crawl-date').time();
			var crawlDateTime = $('#crawl-date').time();
			var currentDate = new Date();
			//currentDate.setDate(currentDate.getDate() + 1);
			currentDate.setHours(0, 0, 0, 0);
			crawlDate.setHours(0, 0, 0, 0);

			var actionType = me.dapps.global['action.type'];
			var dateChanged = $('#date-changed').val();

			// In case update and crawl date was changed, check crawl date and sync entry date
			if ($.trim(actionType) == 'edit' && $.trim(dateChanged) == '1') {
				// In case crawl date <= current date -> error
				if (currentDate >= crawlDate) {
					e.preventDefault();

					if (isUnset(me.dapps.global['fac.crawl_date_warning_box'])) {
						me.dapps.global['fac.crawl_date_warning_box'] = new me.dapps.box({
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

					me.dapps.global['fac.crawl_date_warning_box'].show(localMessage);

				// In case sync entry date greater than crawl date -> error
				} else if (syncEntryDate.getTime() > crawlDateTime.getTime()) {
					e.preventDefault();

					if (isUnset(me.dapps.global['fac.sync_entry_date_warning_box'])) {
						me.dapps.global['fac.sync_entry_date_warning_box'] = new me.dapps.box({
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

					me.dapps.global['fac.sync_entry_date_warning_box'].show(localMessage);
				}
			} else if (syncEntryDate.getTime() > crawlDateTime.getTime()) {
				e.preventDefault();

				if (isUnset(me.dapps.global['fac.sync_entry_date_warning_box'])) {
					me.dapps.global['fac.sync_entry_date_warning_box'] = new me.dapps.box({
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

				me.dapps.global['fac.sync_entry_date_warning_box'].show(localMessage);
			}
			validator.target.trigger('post_validate', e);
		}
	});

	// create datetimepicker
	me.dapps.ui.enhanced.scanDatetimePickers();
	
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
	
	var actionType = me.dapps.global['action.type'];
	var lastCrawlDateValue = "";
	var lastSyncEntryDateValue = "";
	
	function setDefaultDateTime() {
		if (!me.dapps.global['datepicker.skip_default']) {
			if ($.trim(actionType) == 'add') {
				var crawlDate = $('#crawl-date');
				var syncEntryDate = $('#sync-entry-date');
				var defaultValue = new Date();
				defaultValue.setDate(defaultValue.getDate() + 1);
				defaultValue.setHours(0, 0, 0, 0);
				crawlDate.datetimepicker('setDate', defaultValue).change();
				syncEntryDate.datetimepicker('setDate', defaultValue).change();
			} else {
				if ($('#crawl-date-original').length > 0) {
					$('#crawl-date').val($('#crawl-date-original').val());

					me.dapps.ui.enhanced.formatDateTime($('#crawl-date')[0], false, 'dapps-ui-datetimepicker');
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
	
	// validate datetime
	$('#crawl-date').datetimepicker('option', 'onSelect', function() {
		$('#crawl-date').trigger('change').trigger('date_changed');
	});

	// set restrict DateTime, CrawlDate must >= tomorrow
	function restrictDateTime() {
		var tomorrow = new Date();
		tomorrow.setDate(tomorrow.getDate() + 1);
		$('#crawl-date').datetimepicker('option', 'minDate', tomorrow).trigger('change').trigger('date_changed');
	}

	// SyncEntryDate must <= CrawlDate
	$('#crawl-date').on('date_changed', function() {
		var crawlDate = $('#crawl-date').time();

		$('#sync-entry-date').datepicker('option', 'maxDate', crawlDate).change();

	});
	
	$("#enable-edit-date").click(function(e) {
		// save the latest value 
		lastCrawlDateValue = $('#crawl-date').val();
		lastSyncEntryDateValue = $('#sync-entry-date').val();
		$('#crawl-date, #sync-entry-date').prop('disabled', false);
		$('#enable-edit-date').hide();
		$('#cancel-edit-date').show();

		restrictDateTime();
		setDefaultDateTime();
		
		
		$('#date-changed').val(1);
		
		e.preventDefault();
	});
	
	$("#cancel-edit-date").click(function() {
		$('#crawl-date, #sync-entry-date').prop('disabled', true);
		$('#enable-edit-date').show();
		$('#cancel-edit-date').hide();

		if ($('#crawl-date-original').length > 0) {
			$('#crawl-date').val($('#crawl-date-original').val());

			me.dapps.ui.enhanced.formatDateTime($('#crawl-date')[0], false, 'dapps-ui-datetimepicker');
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
	
	// handle when from Confirm Edit Page -> Edit page. If not have this function, crawlDate and syncEntryDate will be disabled.
	if ($('#cancel-edit-date').is(":visible")) {
		//$('#crawl-date, #sync-entry-date').prop('disabled', false);
		$("#enable-edit-date").click();
	}

	// If action = view || confirmAdd || confirmEdit || edit ==> crawlDate is disabled => set actualValue, not set defaultValue
	if ($('#crawl-date').is(':disabled')) {

	} else {
		setDefaultDateTime();
		restrictDateTime();
	}

	$('#crawl-date, #sync-entry-date').change(function() {
		$('#date-changed').val(1);
	});
	
});