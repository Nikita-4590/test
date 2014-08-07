if (isSet($)) {
	// inject to jQuery
	$.fn.extend({
		validator : function(config) {
			var validator = new me.dapps.validator(config, this);

			return validator;
		}
	});
}

me.dapps.validator = function(config, target) {
	this.target = $(target);

	if (this.target.is('form')) {

		this.config = config == null ? new Object : config;

		this.default_config = {
			use_html_markup : true,
			show_validate_message : true,
			static_inputs : true,
			pre_submit : undefined
		};

		for (key in this.default_config) {

			if (isUnset(this.config[key])) {
				this.config[key] = this.default_config[key];
			}
		}

		var thisValidator = this;

		// watch submit event
		this.target.submit(function(e) {
			if (thisValidator.execute()) {
				if (isFunction(thisValidator.config.pre_submit)) {
					thisValidator.config.pre_submit(e, thisValidator);
				}
			} else {
				e.preventDefault();
			}
		});

		// gather all need-validate inputs on load
		if (this.config.static_inputs) {
			this.gatherInputs();
		}

		// prepare a message box for informing error
		if (isSet(me.dapps.box)) {
			this.box = new me.dapps.box({
				auto_hide : true,
				close_button : false,
				button : {
					align : 'right',
					list : [ {
						text : 'OK',
						action : function(targetBox) {
							targetBox.close();
							targetBox.validator.warning(targetBox.validator.currentValidateInput.DOM);
						}
					} ]
				}
			});

			// create circular reference
			this.box.validator = this;
		}

	} else {
		return null;
	}
};
me.dapps.validator.prototype.gatherInputs = function() {
	thisValidator = this;
	this.inputs = new Array;

	if (this.config.use_html_markup) {
		var inputs = this.target.find('[dapps-validate-rule]').not(':disabled');

		$.each(inputs, function() {
			var input = new Object;

			input.validate_rule = parseJSON(this.getAttribute('dapps-validate-rule'));
			input.validate_message = this.getAttribute('dapps-validate-message');
			input.validate_message_id = this.getAttribute('dapps-validate-message-id');
			input.validate_message_param = this.getAttribute('dapps-validate-message-param');
			input.DOM = this;

			thisValidator.inputs.push(input);
		});

	} else {

	}
};
me.dapps.validator.prototype.execute = function() {
	if (this.config.static_inputs) {

	} else {
		this.gatherInputs();
	}

	console.log(this.inputs);

	var result = true;

	// begin validate
	for ( var i = 0; i < this.inputs.length; i++) {
		if (!this.validateInput(this.inputs[i])) {
			result = false;
			break;
		}
	}

	return result;
};
me.dapps.validator.prototype.validateInput = function(input) {
	try {
		var validateResult = true;

		if (isSet(input.DOM)) {
			var value = $.trim(input.DOM.value);

			if (isSet(input.validate_rule)) {
				if (isSet(input.validate_rule.required) && input.validate_rule.required) {
					if (value.length == 0)
						validateResult = false;
				}

				if (validateResult && value.length > 0 && isSet(input.validate_rule.format)) {
					switch (input.validate_rule.format) {
					case 'email':
						var regex = /^([a-zA-Z0-9\!\#\$\%\&\'\*\+\-\/\=\?\^\_\`\{\|\}\~\.])+@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|([a-zA-Z0-9]+((\-[a-zA-Z0-9]+)*)\.)+[a-zA-Z]{2,})$/;

						validateResult = regex.test(value);
						break;
					}
				}
			}
		}

		if (validateResult) {
			this.unwarning(input.DOM);
		} else {
			if (this.config.show_validate_message) {
				this.showMessage(input);
			}
		}

		return validateResult;
	} catch (e) {
		console.log(e);
		return false;
	}
};
me.dapps.validator.prototype.showMessage = function(input) {
	var message = null;

	if (isSet(input.validate_message_id) && exists('me.dapps.ui.enhanced.locale')) {
		message = me.dapps.ui.enhanced.locale.text(input.validate_message_id);

		if (isSet(input.validate_message_param)) {
			message = message.replace(/\{0\}/g, input.validate_message_param);
		}
	} else if (isSet(input.validate_message)) {
		message = input.validate_message;
	}

	if (isSet(message)) {
		if (isSet(this.box)) {
			this.currentValidateInput = input;
			this.box.show(message);
		} else {
			alert(message);
			this.warning(input.DOM);
		}
	}
};
me.dapps.validator.prototype.unwarning = function(html) {
	$(html).removeClass('dapps-validator-focus');
};
me.dapps.validator.prototype.warning = function(html) {

	this.target.find('.dapps-validator-focus').removeClass('dapps-validator-focus');

	$(html).addClass('dapps-validator-focus').focus();
};