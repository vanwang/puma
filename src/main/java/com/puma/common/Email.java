package com.puma.common;

import java.io.Serializable;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class Email implements Serializable {  
	 private static final long serialVersionUID = 9063903350324510652L;    
	  
	/**用户组：可以按用户组来批量发送邮件**/   
	//private UserGroups userGroups;  
	 /**发件人**/  
	 private String sender;
	 /**收件人**/  
	 private String addressee;    
	/**抄送给**/   
	private String cc;    
	/**邮件主题**/  
	 private String subject;   
	 /**邮件内容**/  
	 private String content;    
	/**附件**/   
	private MultipartFile[] attachment = new MultipartFile[0];    
	  
	//////////////////////////解析邮件地址//////////////////////////////  
	public String[] getAddress() {   
		 if(!StringUtils.hasLength(this.addressee)) {    
			 return null;    
		 }
		 addressee = addressee.trim();    
		 addressee.replaceAll("；", ";");   
		 addressee.replaceAll(" ", ";");   
		 addressee.replaceAll(",", ";");   
		 addressee.replaceAll("，", ";");    
		 addressee.replaceAll("|", ";");    
		return addressee.split(";");
	}

	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MultipartFile[] getAttachment() {
		return attachment;
	}

	public void setAttachment(MultipartFile[] attachment) {
		this.attachment = attachment;
	}
	
}  