
$(me.dapps).bind('load', function() {

	// create datetimepicker
	me.dapps.ui.enhanced.scanDatetimePickers();
	
	var crawlDate = $('#crawl-date');
	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);
	crawlDate.datetimepicker('setDate', tomorrow).change();
	$('#crawl-date').datetimepicker('option', 'minDate', tomorrow);
	
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
		e.preventDefault();// can suy nghi them
	});
	
	$('#close-button').click(function() {
		var url = me.dapps.global['url.back_to_list'];
		ajaxPostFormSubmit(url, $('#stored_httprequestid_input').val());
	});
	function ajaxPostFormSubmit(url, value) {
		var form = $('<form method="POST" action=' + '"' + url + '"' + '/>');
		var input = $('<input id="flow_id" name="flow_id" value="' + value + '"' + '>');
		form.append(input);
		form.submit();
	};
});