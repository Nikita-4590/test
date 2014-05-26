function showHistory(accountType, accountId) {
	if (isUnset(me.dapps.global['account.history_box'])) {
		me.dapps.global['account.history_box'] = new me.dapps.box({
			auto_hide : false,
			width : 800,
			title : '変更履歴一覧',
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
					text : 'OK',
					action : function(targetBox) {
						targetBox.close();
					}
				} ]
			}
		});
	}
	
	var mask =  $('<div id="mask" />');
	
	$('body').append(mask);

	me.dapps.global['account.history_box'].showFromUrl({
		url : me.dapps.global['url.history_box'],
		method : 'post',
		data : {
			account_type : accountType,
			account_id : accountId
		},
		callback : function(box) {
			// targetBox.main.find('#delete-account-form').validator();
			me.dapps.ui.enhanced.scanLocaleTexts();
			initHistoryTable(box.main);
			
			$('#mask').remove();
		},
		error : function(box, e) {

			if (isUnset(me.dapps.global['account.history_error_box'])) {
				me.dapps.global['account.history_error_box'] = new me.dapps.box({
					auto_hide : false,
					close_button : false,
					button : {
						align : 'right',
						list : [ {
							text : 'OK',
							action : function(errorBox) {
								if (errorBox._error.status == 403) {
									location.href = me.dapps.global['url.context'] + "/";
								} else {
									errorBox._parent.close();
									errorBox.close();
								}
							}
						} ]
					}
				});
			}

			var messageId = 'ERR251';

			if (e.status == 403) {
				messageId = 'ERR250';
			}

			message = me.dapps.ui.enhanced.locale.text(messageId);
			me.dapps.global['account.history_error_box']._error = e;
			me.dapps.global['account.history_error_box']._parent = box;
			me.dapps.global['account.history_error_box'].show(message);

		}
	});
}

function initHistoryTable(source) {
	var headers = source.find('#tbl-history-header th');

	var columns = source.find('#tbl-history-body tr:first td');
	var tableWidth = 0;

	columns.each(function(index) {
		var paddingLeft = parseFloat($(this).css('padding-left'));
		var paddingRight = parseFloat($(this).css('padding-right'));

		var borderLeftWidth = parseFloat($(this).css('border-left-width'));
		var borderRightWidth = parseFloat($(this).css('border-right-width'));

		var borderOffset = borderLeftWidth + borderRightWidth;
		var diff = paddingLeft + paddingRight + borderOffset;

		var headerWidth = $(headers[index]).outerWidth();

		// $(this).width(headerWidth - diff);

		var desireWidth = headerWidth - diff;

		$.each($('#tbl-history-body .history-basic-row'), function() {
			$($(this).find('td')[index]).css({
				'width' : desireWidth,
				'max-width' : desireWidth,
				'min-width' : desireWidth
			});
		});

		tableWidth += headerWidth;
	});

	source.find('#tbl-history-body').width(tableWidth);

	// add accordion
	source.find('#tbl-history-body tbody .accordion-trigger').click(function() {
		var basicRow = $(this).parent('.history-basic-row');
		var detailRow = basicRow.next('.history-detail-row');

		detailRow.toggle();
		$(this).toggleClass('hide');
	});

}