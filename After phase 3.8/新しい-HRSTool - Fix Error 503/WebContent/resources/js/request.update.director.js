function confirmUpdateDirector(requestId) {

	var select_update_director = $('#select-update-director').val();

	if ($.trim(select_update_director) == '') {
		if (isUnset(me.dapps.global['request.update_director_warning_box'])) {
			me.dapps.global['request.update_director_warning_box'] = new me.dapps.box({
				auto_hide : true,
				title : '!!! 警告  !!!',
				type : 'ERROR',
				close_button : false,
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
		var messageId = me.dapps.global['message.update_director_warning'];

		var localMessage = me.dapps.ui.enhanced.locale.text(messageId);

		me.dapps.global['request.update_director_warning_box'].show(localMessage);
	} else {

		if (isUnset(me.dapps.global['request.update_director_confirm_box'])) {

			me.dapps.global['request.update_director_confirm_box'] = new me.dapps.box({
				auto_hide : false,
				title : '変更しますか？',
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
						text : 'はい',
						action : function(targetBox) {
							targetBox.main.find('#update-director-form').submit();
						}
					} ]
				}
			});
			// fix bug #2879
			me.dapps.global['request.update_director_confirm_box'].submitAgent = {
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
				// /// When isSubmitting = false-> allowed user to submit and
				// cover mask
				injectSubmit : function() {
					if (theForm == null || !theForm.is('form')) {
						return;
					}
					theForm.submit(function(event) {
						if (isSubmitting) {
							event.preventDefault();
						}// if data is submitting oldData, not allow to
							// continue to submit
						else {
							if (!event.isDefaultPrevented()) {// validate
																// success
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
	}

	var messageBox = new me.dapps.box({
		auto_hide : false,
		title : '!!! 警告  !!!',
		type : 'ERROR',
		close_button : false,
		button : {
			align : 'right',
			list : [ {
				text : 'OK',
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
			} ]
		}
	});
	me.dapps.global['request.update_director_confirm_box'].showFromUrl({
		url : me.dapps.global['url.confirm_update_director'],
		method : 'post',
		data : {
			relation_request_id : requestId,
			current_director_id : $('#current-director').val(),
			new_director_id : $('#select-update-director').val()
		},
		callback : function(targetBox) {
			targetBox.main.find('#update-director-form').validator();

			var theAgent = me.dapps.global['request.update_director_confirm_box'].submitAgent;
			theAgent.setSubmitForm(targetBox.main.find('#update-director-form'));
			theAgent.injectSubmit();

			targetBox.main.find('#update-director-form').ajaxForm({
				dataType : 'json',
				success : function(response) {
					if (response.success) {
						ajaxPostFormSubmit();
					} else {
						message = me.dapps.ui.enhanced.locale.text(response.message_id);
						messageBox._response = response;
						messageBox.show(message);
					}
				},
				error : function(e, err) {
					messageBox._response = null;
					var messageId = me.dapps.global['message.update.director.general'];

					if (e.status == 403) {
						messageId = me.dapps.global['message.update.director.forbidden'];
					} else if (e.status == 404) {
						messageId = me.dapps.global['message.update.director.not_found'];
					}

					message = me.dapps.ui.enhanced.locale.text(messageId);
					messageBox._error = e;
					targetBox._parent = null;
					messageBox.show(message);
				},
				complete : function() {
					me.dapps.global['request.update_director_confirm_box'].submitAgent.endSubmit();
				}
			});

		},
		error : function(box, e) {
			var messageId = me.dapps.global['message.update.director.general'];

			if (e.status == 403) {
				messageId = me.dapps.global['message.update.director.forbidden'];
			} else if (e.status == 404) {
				messageId = me.dapps.global['message.update.director.not_found'];
			}

			message = me.dapps.ui.enhanced.locale.text(messageId);

			messageBox._error = e;
			messageBox._parent = box;
			messageBox.show(message);
		}
	});
}

