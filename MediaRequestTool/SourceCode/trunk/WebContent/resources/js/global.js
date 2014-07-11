function showFlashMessage(id) {
	var message = me.dapps.ui.enhanced.locale.text(id);
	
	if (isSet(me.dapps.box)) {
		if (isUnset(me.dapps.global['global.splash_message_box'])) {
			me.dapps.global['global.splash_message_box'] = new me.dapps.box({
				close_button: false,
				button : {
					align : 'right',
					list : [ {
						text : 'OK',
						action : function(targetBox) {
							targetBox.close();
						}
					} ]
				}
			});
		}

		me.dapps.global['global.splash_message_box'].show(message);
	} else {
		alert(message);
	}
}

function ajaxPostFormSubmit(url) {
	var form = undefined;
	var value = $('#stored_httprequestid_input').val();
	if(isSet(url)) {
		form = $('<form method="POST" action=' + '"' + url + '"' + '/>');
	} else {
		var url = window.location.pathname;
		form = $('<form method="POST" action=' + '"' + url + '"' + '/>');
	}
	var input = $('<input id="flow_id" name="flow_id" value="' + value + '"' + '>');
	form.append(input);
	form.submit();
};