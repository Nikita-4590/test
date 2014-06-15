package com.hrs.mediarequesttool.pojos;

public class CommentProperty {

	private String label;
	private Object oldValue;
	private Object newValue;
	private boolean isDiff;

	public CommentProperty(String label) {
		this.label = label;
		isDiff = false;
		oldValue = null;
		newValue = null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
		checkDiff();
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
		checkDiff();
	}

	private void checkDiff() {
		if (oldValue == null && newValue == null) {
			isDiff = false;
		} else if (oldValue == null || newValue == null) {
			isDiff = true;
		} else {
			isDiff = !oldValue.equals(newValue);
		}
	}
	
	public boolean isDiff() {
		return isDiff;
	}

}
