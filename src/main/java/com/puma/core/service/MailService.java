package com.puma.core.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.puma.common.Email;

@Service("mailService")  
public class MailService {

	@Autowired
	private JavaMailSender mailSender;//注入Spring封装的javamail，Spring的xml中已让框架装配   
	
	@Autowired
	TaskExecutor taskExecutor;//注入Spring封装的异步执行器  private Log log = LogFactory.getLog(getClass());  
	
	private StringBuffer message = new StringBuffer();    
	
	public void sendMail(Email email) throws MessagingException, IOException {    
	   if(email.getAddress() == null || email.getAddress().length == 0)    {     
	        this.message.append("没有收件人");    
	        return;    
	      }    
	   if(email.getAddress().length > 5){//收件人大于5封时，采用异步发送   
		   sendMailByAsynchronousMode(email);    
		   this.message.append("收件人过多，正在采用异步方式发送...<br/>");  
	  }else{    
		  sendMailBySynchronizationMode(email);   
	  	  this.message.append("正在同步方式发送邮件...<br/>");   
	 }   
	}    
	/**    
	* 异步发送   
	* @see com.zhangjihao.service.MailService#sendMailByAsynchronousMode(com.zhangjihao.bean.Email)   
	 */  
	public void sendMailByAsynchronousMode(final Email email){  
		taskExecutor.execute(new Runnable(){    
			 public void run(){      
				try {      
				 sendMailBySynchronizationMode(email);   
				   } catch (Exception e) {    
				     e.printStackTrace();
				 }    
			 }    
	});   
	}   
	  
	 /**    
	* 同步发送   
	* @throws IOException    
	* @see com.zhangjihao.service.MailServiceMode#sendMail(com.zhangjihao.bean.Email)   
	*/   
	  
	public void sendMailBySynchronizationMode(Email email) throws MessagingException, IOException {   
	 MimeMessage mime = mailSender.createMimeMessage();  
	 MimeMessageHelper helper = new MimeMessageHelper(mime, true, "utf-8");    
		helper.setFrom("boyuan2001cn@163.com");//发件人    
		helper.setTo(email.getAddress());//收件人    
		helper.setBcc("boyuan2000cn@163.com");//暗送    
		if(StringUtils.hasLength(email.getCc())){    
		 String cc[] = email.getCc().split(";");   
		  helper.setCc(cc);//抄送   
		 }    
		//helper.setReplyTo("cs@chinaptp.com");//回复到   
		helper.setSubject(email.getSubject());//邮件主题    
		helper.setText(email.getContent(), true);//true表示设定html格式    
		  //内嵌资源，这种功能很少用，因为大部分资源都在网上，只需在邮件正文中给个URL就足够了.    
		//helper.addInline("logo", new ClassPathResource("logo.gif"));   
		//处理附件    
		for(MultipartFile file : email.getAttachment()){   
			if(file == null || file.isEmpty()){     
			  continue;    
		 }     
			String fileName = file.getOriginalFilename();     
			try {     
			 fileName = new String(fileName.getBytes("utf-8"),"ISO-8859-1");   
			  } catch (Exception e) {  
			}   
			helper.addAttachment(fileName, new ByteArrayResource(file.getBytes()));   
		}    
		mailSender.send(mime);  
	 }  
	 public StringBuffer getMessage() {  return message; }  
	 public void setMessage(StringBuffer message) {  this.message = message; }
}
