// inject jquery api
if (isSet($)) {
	// inject to jQuery
	$.fn.extend({
		time : function() {
			if (this.is('[dapps-ui-datetimepicker]')) {
				// parse date-time
				var config = parseJSON(this.attr('dapps-ui-datetimepicker'));
				var text = this.val();

				var elements = text.split(config.output.separator);
				var formats = config.output.format.split(config.output.separator);

				var date = $.datepicker.parseDate(formats[config.output.date], elements[config.output.date]);
				var time = $.datepicker.parseTime(formats[config.output.time], elements[config.output.time]);

				date.setHours(time.hour);
				date.setMinutes(time.minute);
				date.setSeconds(time.second);

				return date;
			} else if (this.is('[dapps-ui-datepicker]')) {
				// parse date
				var config = parseJSON(this.attr('dapps-ui-datepicker'));

				var text = this.val();
				return $.datepicker.parseDate(config.output.format, text);
			} else {
				return null;
			}
		}
	});
}

me.dapps.ui = new Object;
me.dapps.ui.enhanced = new Object;
me.dapps.ui.enhanced.formatDateTime = function(dom, dateOnly, configAttribute) {
	var config = parseJSON(dom.getAttribute(configAttribute));

	try {
		// try parse date time to right format
		if (isSet(config) && $.trim(dom.value).length > 0) {
			dom.value = $.trim(dom.value);

			if (dateOnly) {
				var date = $.datepicker.parseDate(config.input.format, dom.value);
				var formattedText = $.datepicker.formatDate(config.output.format, date);
				dom.value = formattedText;
			} else {
				var input = dom.value.split(config.input.separator);
				var input_format = config.input.format.split(config.input.separator);

				var date = $.datepicker.parseDate(input_format[config.input.date], input[config.input.date]);

				var time = $.datepicker.parseTime(input_format[config.input.time], input[config.input.time]);

				var output_format = config.output.format.split(config.output.separator);

				// format
				var formattedText = new Array;
				formattedText[config.input.date] = $.datepicker.formatDate(output_format[config.input.date], date);
				formattedText[config.input.time] = $.datepicker.formatTime(output_format[config.input.time], time);

				dom.value = formattedText.join(config.output.separator);
			}
		} else {
			$(dom).datepicker('setDate', new Date());
		}
	} catch (e) {
	}
};
me.dapps.ui.enhanced.scanDatetimePickers = function(config) {
	var inputs = $('[dapps-ui-datetimepicker], [dapps-ui-datepicker]');

	$.each(inputs, function(index, input) {
		var dateOnly = $(input).is('[dapps-ui-datepicker]');
		var configAttribute = dateOnly ? 'dapps-ui-datepicker' : 'dapps-ui-datetimepicker';

		// clone
		var isInitialized = input.getAttribute('dapps-ui-datepicker-initialized');

		if (isUnset(isInitialized)) {
			if (isUnset(input.id) || input.id.length == 0) {
				input.id = 'datepicker-' + (new Date().getTime());
			}

			var valueId = input.id + '-dapps-value';

			var hiddenInput = $('<input type="hidden" />');
			hiddenInput.attr('id', valueId).attr('name', input.name);
			hiddenInput.insertAfter(this);

			input.name = configAttribute + '-' + input.name;

			$(input).attr('dapps-ui-datepicker-initialized', true);

			$(input).data('value', valueId);
		}

		var initConfig = {
			showOtherMonths : true,
			selectOtherMonths : true
		};

		if (dateOnly) {
			$(input).datepicker(initConfig);
		} else {
			$(input).datetimepicker(initConfig);
		}

		me.dapps.ui.enhanced.formatDateTime(input, dateOnly, configAttribute);

		$(input).change(function() {
			try {
				this.value = $.trim(this.value);
				var hiddenInputId = $(this).data('value');
				var config = parseJSON(this.getAttribute(configAttribute));

				var dateOnly = $(this).is('[dapps-ui-datepicker]');

				if (dateOnly) {
					var date = $(this).time(); // datepicker('getDate');
					if (isSet(config) && this.value.length > 0) {
						var formattedText = $.datepicker.formatDate(config.input.format, date);

						$('#' + hiddenInputId).val(formattedText);
					}
				} else {
					var date = $(this).time(); // datetimepicker('getDate');

					// try parse date time to
					// right format
					if (isSet(config) && this.value.length > 0) {
						var input_format = config.input.format.split(config.input.separator);

						var formattedText = new Array;

						var time = new Object;
						time.hour = date.getHours();
						time.minute = date.getMinutes();
						time.second = date.getSeconds();
						time.millisec = date.getMilliseconds();

						formattedText[config.output.date] = $.datepicker.formatDate(input_format[config.output.date], date);
						formattedText[config.output.time] = $.datepicker.formatTime(input_format[config.output.time], time);
						$('#' + hiddenInputId).val(formattedText.join(config.output.separator));
					}
				}
			} catch (e) {
			}
		}).change();

		// make input readonly
		$(input).prop('readonly', true);
	});
};
/**
 * 
 * Locale (Loading multi-language)
 * 
 */
me.dapps.ui.enhanced.locale = new Object;
me.dapps.ui.enhanced.scanLocaleTexts = function() {
	var localeTextPlaces = $('dapps-locale-text');

	$.each(localeTextPlaces, function() {
		var textId = this.innerHTML;
		if (isSet(me.dapps.ui.enhanced.locale[textId])) {
			this.outerHTML = me.dapps.ui.enhanced.locale.text(textId);
		} else {
			this.outerHTML = '';
		}
	});
};
me.dapps.ui.enhanced.locale.text = function(id) {
	var prefix = me.dapps.ui.enhanced.locale._DEBUG ? '[' + id + '] ' : '';

	var message = (isSet(me.dapps.ui.enhanced.locale[id])) ? me.dapps.ui.enhanced.locale[id] : '';
	
	message = message.replace('"', '<br />"');

	return prefix + message;
};