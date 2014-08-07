package com.hrs.mediarequesttool.kintone.exception;

public class KintoneException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -8887119069663420681L;
  
  
  public KintoneException() {
    super();
  } 
  
  public KintoneException(Exception e) {
    super(e);
  }
}
