function confirmDestroy(requestId) {

	if (isUnset(me.dapps.global['request.destroy_confirm_box'])) {

		me.dapps.global['request.destroy_confirm_box'] = new me.dapps.box({
			auto_hide : false,
			title : 'この依頼を取り消しますか？',
			close_button : false,
			loading_text : '読み込み中。。。',
			button : {
				align : 'right',
				list : [ {
					text : 'キャンセル',
					loading : true,
					action : function(targetBox) {
						targetBox.close();
					}
				}, {
					text : 'いいえ',
					action : function(targetBox) {
						targetBox.close();
					}
				}, {
					text : '取り消す',
					action : function(targetBox) {
						targetBox.main.find('#destroy-request-form').submit();
					}
				} ]
			}
		});
		// fix bug #2879
		me.dapps.global['request.destroy_confirm_box'].submitAgent = {
			isSubmitting : false,
			theForm : null,// form
			theMask : null,// mask
			theBoxMain : null,// Confirm delete dialog box
			// public functions
			setSubmitForm : function(theform) {
				// default config
				isSubmitting = false;
				theForm = theMask = theBoxMain = null;
				// ///
				theForm = theform;
				theBoxMain = theForm.parent().parent();
			},

			// ///Submit process, When isSubmitting = true -> not allowed to
			// submit
			// /// When isSubmitting = false-> allowed user to submit and cover
			// mask
			injectSubmit : function() {
				if (theForm == null || !theForm.is('form')) {
					return;
				}
				theForm.submit(function(event) {
					if (isSubmitting) {
						event.preventDefault();
					}// if data is submitting oldData, not allow to continue
						// to submit
					else {
						if (!event.isDefaultPrevented()) {// validate success
							isSubmitting = true;
							if (theMask == null) {
								theMask = $('<div class="mask" />');
								theMask.zIndex(theBoxMain.zIndex());
							}
							theMask.insertAfter(theBoxMain);
						}
					}
				});
			},
			endSubmit : function() {
				isSubmitting = false;
				theMask.remove();
			}
		};
	}

	var messageBox = new me.dapps.box({
		auto_hide : false,
		title: '!!! 警告  !!!',
		type: 'ERROR',
		close_button : false,
		button : {
			align : 'right',
			list : [ {
				text : 'OK',
				action : function(targetBox) {
					if (isSet(targetBox._response) && targetBox._response.message_id == "ERR251") {
						location.reload();
					} else if (isSet(targetBox._error) && targetBox._error.status == 403) {
						location.href = me.dapps.global['url.context'] + "/"; // redirect to login page
					} else if (isSet(targetBox._error) && targetBox._error.status == 404) {
						location.reload();
					} else {
						if (isSet(targetBox._parent)) {
							targetBox._parent.close();
						}
						targetBox.close();
					}
				}
			} ]
		}
	});

	// $('body').append($('<div class="mask" />'));

	me.dapps.global['request.destroy_confirm_box'].showFromUrl({
		url : me.dapps.global['url.confirm_destroy'],
		method : 'post',
		data : {
			relation_request_id : requestId
		},
		callback : function(targetBox) {
			targetBox.main.find('#destroy-request-form').validator();

			var theAgent = me.dapps.global['request.destroy_confirm_box'].submitAgent;
			theAgent.setSubmitForm(targetBox.main.find('#destroy-request-form'));
			theAgent.injectSubmit();

			targetBox.main.find('#destroy-request-form').ajaxForm({
				dataType : 'json',
				success : function(response) {
					if (response.success) {
						window.open("", "_self").close();
					} else {
						message = me.dapps.ui.enhanced.locale.text(response.message_id);
						messageBox._response = response;
						messageBox.show(message);
					}
				},
				error : function(e, err) {
					messageBox._response = null;
					var messageId = me.dapps.global['message.destroy.general'];

					if (e.status == 403) {
						messageId = me.dapps.global['message.destroy.forbidden'];
					} else if (e.status == 404) {
						messageId = me.dapps.global['message.destroy.not_found'];
					}

					message = me.dapps.ui.enhanced.locale.text(messageId);
					messageBox._error = e;
					targetBox._parent = null;
					messageBox.show(message);
				},
				complete : function() {
					me.dapps.global['request.destroy_confirm_box'].submitAgent.endSubmit();
				}
			});

		},
		error : function(box, e) {
			var messageId = me.dapps.global['message.destroy.general'];

			if (e.status == 403) {
				messageId = me.dapps.global['message.destroy.forbidden'];
			} else if (e.status == 404) {
				messageId = me.dapps.global['message.destroy.not_found'];
			}

			message = me.dapps.ui.enhanced.locale.text(messageId);

			messageBox._error = e;
			messageBox._parent = box;
			messageBox.show(message);
		}
	});

}
