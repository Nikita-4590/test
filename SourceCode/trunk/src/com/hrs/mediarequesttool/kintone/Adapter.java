package com.hrs.mediarequesttool.kintone;

import com.hrs.mediarequesttool.kintone.data.Record;
import com.hrs.mediarequesttool.kintone.exception.KintoneException;
import com.hrs.mediarequesttool.pojos.RelationRequest;

class Adapter {
  static public final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  static public final String DATE_PATTERN = "yyyy-MM-dd";
  
  
  public Record parse(RelationRequest request, boolean isUkerukun) throws KintoneException {
    Record record = new Record();
    
    // TODO set common fields
    
    
    if (isUkerukun) {
      // TODO set ukerukun fields
    } else {
      // TODO set other-media fields
    }

    return record;
  }
  
  private String correctDate(String date) throws KintoneException {
    // TODO convert date to yyyy-MM-dd'T'HH:mm:ss'Z' and GMT+0
    return date;
  }
  
  private String correctCompanyID(String companyID) throws KintoneException {
    if (companyID != null) {
      int length = companyID.length();
      
      if (length <= 8) {
        int extraLength = 8 - length;
        
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < extraLength; i++) {
          builder.append("0");
        }
        
        builder.append(companyID);
        
        return builder.toString();
      }
    }
    
    throw new KintoneException();
  }
}
