package com.hrs.mediarequesttool.kintone.data;

public class RecordElement {
  public static RecordElement create(String value) {
    RecordElement element = new RecordElement();
    element.setValue(value);
    return element;
  }
  
  public RecordElement() {
  }

  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
