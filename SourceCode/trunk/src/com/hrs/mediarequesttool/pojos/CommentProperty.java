package com.hrs.mediarequesttool.pojos;

public class CommentProperty {

	private String label;
	private Object value;

	public CommentProperty(String label) {
		this.label = label;
		value = null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
