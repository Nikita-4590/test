package com.hrs.mediarequesttool.kintone;

import com.hrs.mediarequesttool.kintone.data.Record;
import com.hrs.mediarequesttool.kintone.data.RecordElement;
import com.hrs.mediarequesttool.kintone.exception.KintoneException;
import com.hrs.mediarequesttool.pojos.RelationRequest;

class Adapter {
  static public final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  static public final String DATE_PATTERN = "yyyy-MM-dd";
  
  
  public Record parse(RelationRequest request, boolean isUkerukun) throws KintoneException {
    Record record = new Record();
    
    // TODO set common fields
    record.setCompanyName(RecordElement.create(request.getCompany_name()));
    record.setCompanyID(RecordElement.create(correctCompanyID(request.getCompany_id())));
    record.setCrawlDate(RecordElement.create(request.getCrawl_date())); // TODO
    record.setCrawlDateExtra(RecordElement.create(request.getCrawl_date())); // TODO
    record.setAssignUser(RecordElement.create(request.getAssign_user_name()));
    
    
    if (isUkerukun) {
      // TODO set ukerukun fields
    	record.setMediaType(RecordElement.create("うける君連携"));
    	record.setLoginIdForUkerukun(RecordElement.create(request.getLogin_id_1()));
    	record.setPasswordForUkerukun(RecordElement.create(request.getLogin_password_1()));
    } else {
      // TODO set other-media fields
    	record.setMediaType(RecordElement.create("多媒体連携"));
    	record.setMediaNameForOtherMedia(RecordElement.create(request.getMedia_name()));
    	record.setUrlForOtherMedia(RecordElement.create(request.getUrl()));
    	record.setLoginID1ForOtherMedia(RecordElement.create(request.getLogin_id_1()));
    	record.setLoginID2ForOtherMedia(RecordElement.create(request.getLogin_id_2()));
    	record.setPassword1ForOtherMedia(RecordElement.create(request.getLogin_password_1())); // TODO
    	record.setPassword2ForOtherMedia(RecordElement.create(request.getLogin_password_2())); // TODO
    }

    return record;
  }
  
  //private String correctDate(String date) throws KintoneException {
    // TODO convert date to yyyy-MM-dd'T'HH:mm:ss'Z' and GMT+0
    //return date;
  //}
  
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
