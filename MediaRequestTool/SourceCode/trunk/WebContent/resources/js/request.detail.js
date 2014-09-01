
$(me.dapps).bind('load', function() {
	var currentMemo = $('#hrs-memo').val().trim();
	// create datetimepicker
	me.dapps.ui.enhanced.scanDatetimePickers();
	
	var crawlDate = $('#crawl-date');
	var today = new Date();
	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);
	// set default date is tomorrow
	crawlDate.datetimepicker('setDate', tomorrow).change();
	// set min date is today
	$('#crawl-date').datetimepicker('option', 'minDate', today);
	
	$("#enable-change-director").click(function(e) {
		
		$('#show-more').show();
		$('#enable-change-director').hide();
		$('#cancel-change-director').show();
		e.preventDefault();
	});
	
	$("#cancel-change-director").click(function(e) {
		
		$('#show-more').hide();
		$('#cancel-change-director').hide();
		$('#enable-change-director').show();
		e.preventDefault();
	});
	
	$('#back-to-list').click(function(e) {
		e.preventDefault();
		var url = $('#back-to-list').attr('href');
		ajaxPostFormSubmit(url);
	});
	
	var messageBox = new me.dapps.box({
		auto_hide : false,
		title : '!!! 警告  !!!',
		type : 'ERROR',
		close_button : false,
		loading_text : '読み込み中。。。',
		button : {
			align : 'right',
			list : [ {
				text : 'OK',
				loading : true,
				action : function(targetBox) {
					if (isSet(targetBox._response) && targetBox._response.message_id == "ERR301") {
						ajaxPostFormSubmit();
					} else if (isSet(targetBox._error) && targetBox._error.status == 403) {
						location.href = me.dapps.global['url.context'] + "/"; // redirect to login page
					} else if (isSet(targetBox._error) && targetBox._error.status == 404) {
						ajaxPostFormSubmit();
					} else {
						if (isSet(targetBox._parent)) {
							targetBox._parent.close();
						}
						targetBox.close();
					}
				}
			}]
		}
	});
	
	var messageBoxInfo = new me.dapps.box({
		auto_hide : false,
		close_button : false,
		loading_text : '読み込み中。。。',
		button : {
			align : 'right',
			list : [ {
				text : 'OK',
				loading : true,
				action : function(targetBox) {
					if (isSet(targetBox._parent)) {
						targetBox._parent.close();
					}
					targetBox.close();
				}
			}]
		}
	});
	
	$('#update-memo').click(function(e) {
		e.preventDefault();
		currentMemo = currentMemo.trim();
		var newMemo = $('#hrs-memo').val().trim();
		if (currentMemo != newMemo) {
			$.ajax({
				url : me.dapps.global['url.update_memo'],
				dataType : 'json',
				method : 'post',
				data : {
					relation_request_id : $('#request-id').text(),
					current_memo : currentMemo,
					updated_at : $('#updated-at').text(),
					new_memo : newMemo
				},
				success : function(response) {
					if (response.success) {
						currentMemo = $('#hrs-memo').val();
					}
					
					message = me.dapps.ui.enhanced.locale.text(response.message_id);
					
					if (response.message_id == "INF300") {
						messageBoxInfo._response = response;
						messageBoxInfo.show(message);
					} else {
						messageBox._response = response;
						messageBox.show(message);
					}
				},
				error : function(e, err) {
					messageBox._response = null;
					var messageId = me.dapps.global['message.update.memo.general'];

					if (e.status == 403) {
						messageId = me.dapps.global['message.update.memo.forbidden'];
					} else if (e.status == 404) {
						messageId = me.dapps.global['message.update.memo.not_found'];
					}

					message = me.dapps.ui.enhanced.locale.text(messageId);
					messageBox._error = e;
					messageBox._parent = null;
					messageBox.show(message);
				}
			});
		}
	});
});