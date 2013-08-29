package com.puma.common;

import java.io.Serializable;

import org.slf4j.Logger;

import com.puma.exception.BusinessException;

public class ResponseMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String WARNING = "warning";
	private String status;
	private Object message;
	
	public ResponseMessage()
	{
	}
	
	public static final ResponseMessage handleException(BusinessHandler bh, Logger logger)
	{
	    ResponseMessage rm = new ResponseMessage();
	    try
	    {
	        rm.setMessage(bh.handle());
	        rm.setStatus(SUCCESS);
	    }
	    catch(BusinessException be)
	    {
	        rm.setMessage(be.getCode());
	        rm.setStatus(WARNING);
	        if(logger != null && logger.isDebugEnabled())
	            logger.debug(be.getMessage());
	    }
	    catch(Exception e)
	    {
	        rm.setMessage(e.getMessage());
	        rm.setStatus(ERROR);
	        StringBuffer sb = (new StringBuffer(e.toString())).append("\n");
	        StackTraceElement astacktraceelement[];
	        int j = (astacktraceelement = e.getStackTrace()).length;
	        for(int i = 0; i < j; i++)
	        {
	            StackTraceElement ste = astacktraceelement[i];
	            sb.append("\tat ").append(ste).append("\n");
	        }
	
	        if(logger != null)
	            logger.error(sb.toString());
	        else
	            e.printStackTrace();
	    }
	    return rm;
	}
	
	public String getStatus()
	{
	    return status;
	}
	
	public void setStatus(String status)
	{
	    this.status = status;
	}
	
	public Object getMessage()
	{
	    return message;
	}
	
	public void setMessage(Object message)
	{
	    this.message = message;
	}
	
	public String toString()
	{
	    StringBuffer sb = new StringBuffer("{");
	    sb.append("status:\"").append(status != null ? status.replaceAll("\\\"", "\\\\\\\"").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r") : "").append("\",message:\"").append(message != null ? message.toString().replaceAll("\\\"", "\\\\\\\"").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r") : "").append("\"}");
	    return sb.toString();
	}

}
