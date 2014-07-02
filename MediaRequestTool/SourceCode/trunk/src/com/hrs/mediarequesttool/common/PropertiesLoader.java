package com.hrs.mediarequesttool.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Configuration
public class PropertiesLoader extends SpringBeanAutowiringSupport {
	public static PropertiesLoader instance = new PropertiesLoader();

	@Value("${mail.host}")
	private String mailHost;

	@Value("${mail.port}")
	private String mailPort;

	@Value("${mail.address}")
	private String mailAddress;

	@Value("${mail.password}")
	private String mailPassword;

	@Value("${mail.toaddress}")
	private String toAddress;

	@Value("${mail.ccaddress}")
	private String ccAddress;

	@Value("${mail.bccaddress}")
	private String bccAddress;
	
	@Value("${format.datetime.input}")
	private String inputDateFormat;

	@Value("${format.datetime.input.alter}")
	private String alterInputDateFormater;

	@Value("${format.datetime.output}")
	private String outputDateFormat;

	@Value("${paging.size}")
	private String pageSize;
	
	@Value("${pgcrypto.passwd}")
	private String pgcryptoPasswd;
	
	@Value("${kintone.username}")
	private String kintoneUsername;
	
	@Value("${kintone.password}")
	private String kintonePassword;
	
	@Value("${kintone.url}")
	private String kintoneUrl;
	
	@Value("${kintone.host}")
	private String kintoneHost;
	
	@Value("${kintone.app}")
	private String kintoneApplicationID;
	
	@Value("${kintone.ukerukuntype}")
	private String kintoneUkerukunType;
	
	@Value("${kintone.tabaitaitype}")
	private String kintoneTabaitaiType;
	
	@Value("${kintone.passwordkey}")
	private String kintonePasswordKey;

	public int getPageSize() {
		try {
			return Integer.parseInt(pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return 10;
		}
	}

	public String getInputDateFormat() {
		return inputDateFormat;
	}

	public String getAlterInputDateFormater() {
		return alterInputDateFormater;
	}

	public String getOutputDateFormat() {
		return outputDateFormat;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMailPort() {
		return mailPort;
	}

	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public String getToAddress() {
		return toAddress;
	}
	
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getCcAddress() {
		return ccAddress;
	}
	
	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getBccAddress() {
		return bccAddress;
	}
	
	public void setBccAddress(String bccAddress) {
		this.bccAddress = bccAddress;
	}
	
	public String getPgcryptoPasswd() {
		return pgcryptoPasswd;
	}

	public void setPgcryptoPasswd(String pgcryptoPasswd) {
		this.pgcryptoPasswd = pgcryptoPasswd;
	}

  public String getKintoneUsername() {
    return kintoneUsername;
  }

  public String getKintonePassword() {
    return kintonePassword;
  }

  public String getKintoneUrl() {
    return kintoneUrl;
  }

  public String getKintoneHost() {
    return kintoneHost;
  }

  public String getKintoneApplicationID() {
    return kintoneApplicationID;
  }
  
  public String getUkerukunType() {
	return kintoneUkerukunType;
  }
  
  public String getTabaitaiType() {
	return kintoneTabaitaiType;
  }
  
  public String getPasswordKey() {
	return kintonePasswordKey;
  }
}
