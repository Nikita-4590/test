
$(me.dapps).bind('load', function() {

	// create datetimepicker
	me.dapps.ui.enhanced.scanDatetimePickers();
	
	var crawlDate = $('#crawl-date');
	var today = new Date();
	crawlDate.datetimepicker('setDate', today).change();
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
});