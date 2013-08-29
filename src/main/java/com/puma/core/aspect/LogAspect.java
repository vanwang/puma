package com.puma.core.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.puma.core.annotation.LogIt;
import com.puma.core.domain.Log;
import com.puma.core.domain.Member;
import com.puma.core.security.SecurityUtils;
import com.puma.core.service.LogService;

@Aspect //该注解标示该类为切面类 
@Component //注入依赖
public class LogAspect {

	@Autowired
	private LogService logService;
	
	//@@AfterReturning("execution (* com.puma.core.controller.*.*(..)) && @annotation(lg)")  
	//标注该方法体为后置通知，当目标方法执行成功后执行该方法体  
	@AfterReturning("execution (* *.*(..)) && @annotation(lg)")  
    public void addLogSuccess(JoinPoint jp, LogIt lg){  
        /*Object[] parames = jp.getArgs();//获取目标方法体参数  
        String params = parseParames(parames); //解析目标方法体的参数  
        String className = jp.getTarget().getClass().toString();//获取目标类名  
        className = className.substring(className.indexOf("com"));  
        String signature = jp.getSignature().toString();//获取目标方法签名  
        String methodName = signature.substring(signature.lastIndexOf(".")+1, signature.indexOf("("));  
        String modelName = getModelName(className); //根据类名获取所属的模块  
        System.out.println(lg.message()+lg.logLevel()+lg.logType());*/
        Member member = SecurityUtils.getAuthedMember();
        if(member != null){
        	Log log = new Log();
            log.setLevel(lg.logLevel());
            log.setType(lg.logType());
            log.setMember(member);
            log.setMname(member.getName());
            log.setMemail(member.getEmail());
            log.setMessage(lg.message());
            log.setStatus(1);
            logService.save(log);
        }
    }  
  
    //标注该方法体为异常通知，当目标方法出现异常时，执行该方法体  
    @AfterThrowing(pointcut="execution (* com.puma.core.controller.*.*(..)) && @annotation(lg)", throwing="ex")  
    public void addLog(JoinPoint jp, LogIt lg, Exception ex){  
    	 Member member = SecurityUtils.getAuthedMember();
         if(member != null){
        	 Log log = new Log();
             log.setLevel(lg.logLevel());
             log.setType(lg.logType());
             log.setMember(member);
             log.setMname(member.getName());
             log.setMemail(member.getEmail());
             log.setMessage(lg.message());
             log.setStatus(0);
             logService.save(log);
         }
    }  
}
