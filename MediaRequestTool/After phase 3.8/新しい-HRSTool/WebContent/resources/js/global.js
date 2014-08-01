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
	if(isUnset(url)) {
		url = window.location.pathname;
	}
	window.location.href = url;
};