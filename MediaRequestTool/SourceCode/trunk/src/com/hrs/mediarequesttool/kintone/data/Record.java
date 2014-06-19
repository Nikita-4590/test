package com.hrs.mediarequesttool.kintone.data;

import com.google.gson.annotations.SerializedName;

public class Record {
  // common fields - begin
  @SerializedName("文字列_1行_")
  private RecordElement companyName;

  @SerializedName("文字列__1行__0")
  private RecordElement companyID;

  @SerializedName("ドロップダウン_1")
  private RecordElement mediaType;

  @SerializedName("日時")
  private RecordElement crawlDate;

  @SerializedName("日時_0")
  private RecordElement crawlDateExtra;

  @SerializedName("ドロップダウン")
  private RecordElement assignUser;
  
  @SerializedName("文字列__複数行__1")
  private RecordElement commentForOtherMedia;
  // common fields - end

  // ukerukun fields - begin
  @SerializedName("文字列__複数行_")
  private RecordElement commentForUkerukun;

  @SerializedName("文字列__1行__9")
  private RecordElement passwordForUkerukun;

  @SerializedName("文字列__1行__1")
  private RecordElement loginIdForUkerukun;
  // ukerukun fields - end

  // other-media fields - begin

  @SerializedName("文字列__1行__4")
  private RecordElement mediaNameForOtherMedia;

  @SerializedName("文字列__1行__5")
  private RecordElement urlForOtherMedia;

  @SerializedName("文字列__1行__6")
  private RecordElement loginID1ForOtherMedia;

  @SerializedName("文字列__複数行__0")
  private RecordElement loginID2ForOtherMedia;

  @SerializedName("文字列__1行__7")
  private RecordElement password1ForOtherMedia;

  @SerializedName("文字列__1行__8")
  private RecordElement password2ForOtherMedia;

  // other-media fields - end

  public RecordElement getCompanyName() {
    return companyName;
  }

  public void setCompanyName(RecordElement companyName) {
    this.companyName = companyName;
  }

  public RecordElement getCompanyID() {
    return companyID;
  }

  public void setCompanyID(RecordElement companyID) {
    this.companyID = companyID;
  }

  public RecordElement getMediaType() {
    return mediaType;
  }

  public void setMediaType(RecordElement mediaType) {
    this.mediaType = mediaType;
  }

  public RecordElement getCrawlDate() {
    return crawlDate;
  }

  public void setCrawlDate(RecordElement crawlDate) {
    this.crawlDate = crawlDate;
  }

  public RecordElement getCrawlDateExtra() {
    return crawlDateExtra;
  }

  public void setCrawlDateExtra(RecordElement crawlDateExtra) {
    this.crawlDateExtra = crawlDateExtra;
  }

  public RecordElement getAssignUser() {
    return assignUser;
  }

  public void setAssignUser(RecordElement assignUser) {
    this.assignUser = assignUser;
  }

  public RecordElement getCommentForUkerukun() {
    return commentForUkerukun;
  }

  public void setCommentForUkerukun(RecordElement commentForUkerukun) {
    this.commentForUkerukun = commentForUkerukun;
  }

  public RecordElement getPasswordForUkerukun() {
    return passwordForUkerukun;
  }

  public void setPasswordForUkerukun(RecordElement passwordForUkerukun) {
    this.passwordForUkerukun = passwordForUkerukun;
  }

  public RecordElement getLoginIdForUkerukun() {
    return loginIdForUkerukun;
  }

  public void setLoginIdForUkerukun(RecordElement loginIdForUkerukun) {
    this.loginIdForUkerukun = loginIdForUkerukun;
  }

  public RecordElement getCommentForOtherMedia() {
    return commentForOtherMedia;
  }

  public void setCommentForOtherMedia(RecordElement commentForOtherMedia) {
    this.commentForOtherMedia = commentForOtherMedia;
  }

  public RecordElement getMediaNameForOtherMedia() {
    return mediaNameForOtherMedia;
  }

  public void setMediaNameForOtherMedia(RecordElement mediaNameForOtherMedia) {
    this.mediaNameForOtherMedia = mediaNameForOtherMedia;
  }

  public RecordElement getUrlForOtherMedia() {
    return urlForOtherMedia;
  }

  public void setUrlForOtherMedia(RecordElement urlForOtherMedia) {
    this.urlForOtherMedia = urlForOtherMedia;
  }

  public RecordElement getLoginID1ForOtherMedia() {
    return loginID1ForOtherMedia;
  }

  public void setLoginID1ForOtherMedia(RecordElement loginID1ForOtherMedia) {
    this.loginID1ForOtherMedia = loginID1ForOtherMedia;
  }

  public RecordElement getLoginID2ForOtherMedia() {
    return loginID2ForOtherMedia;
  }

  public void setLoginID2ForOtherMedia(RecordElement loginID2ForOtherMedia) {
    this.loginID2ForOtherMedia = loginID2ForOtherMedia;
  }

  public RecordElement getPassword1ForOtherMedia() {
    return password1ForOtherMedia;
  }

  public void setPassword1ForOtherMedia(RecordElement password1ForOtherMedia) {
    this.password1ForOtherMedia = password1ForOtherMedia;
  }

  public RecordElement getPassword2ForOtherMedia() {
    return password2ForOtherMedia;
  }

  public void setPassword2ForOtherMedia(RecordElement password2ForOtherMedia) {
    this.password2ForOtherMedia = password2ForOtherMedia;
  }
}