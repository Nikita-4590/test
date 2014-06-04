
$(me.dapps).bind('load', function() {

	// create datetimepicker
	me.dapps.ui.enhanced.scanDatetimePickers();
	
	if ($('#crawl-date').length > 0) {
		$('#crawl-date').val($('#crawl-date').val());
	} else {
		//restrictDateTime();
	}
	
	// set restrict DateTime, CrawlDate must >= tomorrow
	function restrictDateTime() {
		var tomorrow = new Date();
		tomorrow.setDate(tomorrow.getDate() + 1);
		$('#crawl-date').datetimepicker('option', 'minDate', tomorrow).trigger('change');
	}
});