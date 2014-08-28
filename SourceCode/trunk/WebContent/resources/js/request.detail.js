
$(me.dapps).bind('load', function() {
	var curentMemo = $('#hrs-memo').val();
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
		title : 'Result',
		close_button : false,
		loading_text : '読み込み中。。。',
		button : {
			align : 'right',
			list : [ {
				text : 'OK',
				loading : true,
				action : function(targetBox) {
					if (isSet(targetBox._response) && targetBox._response.message_id == "ERR201") {
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
	$('#update-memo').click(function(e) {
		e.preventDefault();
		
		$.ajax({
			url : me.dapps.global['url.update_memo'],
			dataType : 'json',
			method : 'post',
			data : {
				relation_request_id : $('#request-id').text(),
				current_memo : curentMemo,
				updated_at : $('#updated-at').text(),
				new_memo : $('#hrs-memo').val()
			},
			success : function(response) {
				if (!response.success) {
					messageBox._response = response;
					messageBox.show("Du lieu da bi thay doi, Click OK de load lai!!!");
				} else {
					messageBox._response = response;
					messageBox.show("Da update thanh cong!!!");
				}
			}
		});
	});
});