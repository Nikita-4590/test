$(me.dapps).bind('load', function() {
	// create box
	var messageBox = new me.dapps.box({
		auto_hide : false,
		close_button : false,
		button : {
			align : 'right',
			list : [ {
				text : 'OK',
				action : function(targetBox) {
					if (isSet(targetBox._response) && isSet(targetBox._response.url)) {
						location.href = me.dapps.global['url.context'] + targetBox._response.url;
					} else {
						if (isSet(targetBox._error && targetBox._error.status == 403)) {
							location.href = me.dapps.global['url.context'] + "/";
						} else {
							targetBox.close();
						}
					}
				}
			} ]
		}
	});

	// make ajax submit
	$('#ukerukun-form').on('post_validate', function(e, originalEvent) {
		if (isSet(me.dapps.global['form.ukerukun.ajax']) && me.dapps.global['form.ukerukun.ajax']) {

			originalEvent.preventDefault();
			
			$('body').append($('<div class="mask" />'));

			$(this).ajaxSubmit({
				dataType : 'json',
				success : function(response) {
					message = me.dapps.ui.enhanced.locale.text(response.message_id);
					messageBox._response = response;
					messageBox.show(message);
				},
				error : function(e) {
					messageBox._response = null;

					var messageId = me.dapps.global['message.bad_request'];

					if (e.status == 403) {
						messageId = me.dapps.global['message.session_expired'];
					}
					
					message = me.dapps.ui.enhanced.locale.text(messageId);
					messageBox._error = e;
					messageBox.show(message);
				}, complete: function () {
				    $('.mask').remove();
			    }
			});
		}
	});

	$("#back-button").click(function() {
		me.dapps.global['form.ukerukun.ajax'] = false;
		var url = me.dapps.global['url.back_from_confirm'];
		$('#ukerukun-form').attr('action', url);
		$('#ukerukun-form').submit();
	});

	me.dapps.global['form.ukerukun.ajax'] = true;
});